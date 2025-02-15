package pfa.java.pfa2025java.controllers.medicin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessagesController {

    public AnchorPane messageRoot;
    public Button sendButton;
    @FXML private ListView<String> userList;
    @FXML private ListView<String> messageList;
    @FXML private TextField messageField;



}