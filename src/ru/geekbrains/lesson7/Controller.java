package ru.geekbrains.lesson7;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    public TextField chatTextField;
    public TextArea chatTextArea;
    public ChatClient client;

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
}
