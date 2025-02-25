package pfa.java.pfa2025java.controllers;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;



public class ChatbotController {

    @FXML
    private WebView webView;

    private WebEngine webEngine;

    @FXML
    public void initialize() {
        webEngine = webView.getEngine();

        try {
            String chatbotUrl = getClass().getResource("/pfa/java/pfa2025java/views/chatbot.html").toExternalForm();
            webEngine.load(chatbotUrl);
            System.out.println("✅ Chatbot HTML loaded successfully!");

            // Enable JavaFX to communicate with JavaScript
            webEngine.documentProperty().addListener((observable, oldDoc, newDoc) -> {
                if (newDoc != null) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("java", this);
                }
            });

        } catch (NullPointerException e) {
            System.err.println("❌ Error: chatbot.html not found! Check resource path.");
        }
    }

    public void notifyChatbotStatus(String message) {
        System.out.println("JavaScript Message: " + message);
    }
}
