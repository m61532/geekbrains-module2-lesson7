package ru.geekbrains.lesson7;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Controller {
    public HBox chatTextFieldWithButtons;
    public TextField chatTextField;
    public TextArea chatTextArea;
    public ChatClient client;
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public HBox loginBox;

    public Controller() {
        client = new ChatClient(this);
    }

    public void sendMessage(ActionEvent actionEvent) {
        Object source = (Button) actionEvent.getSource();
        final String messageToSend = chatTextField.getText().trim();
        if (messageToSend.isEmpty()) {
            return;
        }
        chatTextField.clear();
        //chatTextArea.appendText("You: " + messageToSend + "\n");
        chatTextField.requestFocus();
        client.sendMessage(messageToSend);
    }

    public void addMessage(String message) {
        chatTextArea.appendText(message + "\n");
    }

    public void clearTextField() {
        chatTextField.clear();
    }

    public void bthAuthClick(ActionEvent actionEvent) {
        System.out.println("/auth " + loginField.getText() + " " + passwordField.getText());
        client.openConnection();
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());
    }

    public void setAuth(boolean success){
        loginBox.setVisible(!success);
        chatTextField.setVisible(success);
        chatTextArea.setVisible(success);
        chatTextFieldWithButtons.setVisible(success);
    }
}
