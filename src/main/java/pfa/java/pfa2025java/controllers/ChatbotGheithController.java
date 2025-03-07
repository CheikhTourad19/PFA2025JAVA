package pfa.java.pfa2025java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pfa.java.pfa2025java.SwtichScene;
import pfa.java.pfa2025java.UserSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChatbotGheithController {
    @FXML
    private VBox rootContainer;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea responseArea;

    private final String ollamaApiUrl = "http://127.0.0.1:11434/api/generate";


    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> sendRequestToOllama());
        inputField.setOnKeyPressed(this::handleEnterKey);

    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendRequestToOllama();
        }
    }

    private void sendRequestToOllama() {
        String userInput = inputField.getText();
        if (userInput.isEmpty()) {
            responseArea.setText("Veuillez entrer une requête.");
            return;
        }
        // Clear the TextArea and display "En cours..."
        javafx.application.Platform.runLater(() -> {
            responseArea.clear();
            responseArea.setText("En cours...");
        });
        String guidance = "Tu es un assistant médical de E-Medical de l'esprim en Tunisie. Présente-toi toujours en tant qu'assistant de E-Medical. Réponds uniquement aux questions liées à la santé, aux symptômes, aux médicaments, aux traitements ou aux conseils médicaux. Si la question n'est pas médicale, réponds simplement par : 'Je ne peux répondre qu'aux questions médicales.'";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                "{\"model\": \"llama3.2\", \"prompt\": \"" + guidance + userInput + "\"}");
        Request request = new Request.Builder()
                .url(ollamaApiUrl)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> responseArea.setText("Erreur de connexion à l'API Ollama."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    StringBuilder fullResponse = new StringBuilder();
                    String line;

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                        while ((line = reader.readLine()) != null) {
                            System.out.println("Réponse brute de l'API : " + line);

                            try {
                                JSONObject jsonResponse = new JSONObject(line);
                                if (jsonResponse.has("response")) {
                                    String partialResponse = jsonResponse.getString("response");

                                    // Filtrer les segments de raisonnement (balises <think> et </think>)
                                    if (partialResponse.contains("\u003cthink\u003e") || partialResponse.contains("\u003c/think\u003e")) {
                                        continue;
                                    }

                                    // Décoder les caractères Unicode
                                    partialResponse = java.net.URLDecoder.decode(partialResponse, "UTF-8");

                                    // Accumuler la réponse
                                    fullResponse.append(partialResponse);

                                    // ✅ Mise à jour en temps réel du TextArea
                                    String finalResponse = fullResponse.toString().trim();
                                    javafx.application.Platform.runLater(() -> responseArea.setText(finalResponse));
                                }

                                // Si la réponse est complète, arrêter la lecture
                                if (jsonResponse.optBoolean("done", false)) {
                                    break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                javafx.application.Platform.runLater(() -> responseArea.setText("Format de réponse JSON invalide."));
                            }
                        }
                    }
                } else {
                    javafx.application.Platform.runLater(() -> responseArea.setText("Réponse invalide de l'API."));
                }
            }


        });
    }


}
