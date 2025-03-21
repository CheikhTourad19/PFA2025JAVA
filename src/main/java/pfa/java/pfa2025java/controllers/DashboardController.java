package pfa.java.pfa2025java.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class DashboardController {
    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://lookerstudio.google.com/reporting/7e28c4ce-f848-4402-b3fb-b082ba635485");

        // Use JavaScript to set the zoom level
        String script = "document.body.style.zoom = '70%';";
        webEngine.executeScript(script);
    }
}
