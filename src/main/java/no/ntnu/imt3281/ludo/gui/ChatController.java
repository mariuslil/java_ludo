package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import no.ntnu.imt3281.ludo.client.Client;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private List<String> messages = new ArrayList<>();
    private LudoController ludoController;
    private Client client;

    public ChatController(LudoController ludoController, Client client) {
        this.ludoController = ludoController;
        this.client = client;
    }

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textToSay;

    @FXML
    private Button sendTextButton;

    @FXML
    void onSendGlobalMessage(ActionEvent event) {
        if(client.isLoggedIn()) {
            String message = textToSay.getText();
            if (message != null && !message.isEmpty()) {
                textToSay.clear();
                ludoController.sendMessageFromGlobal(message);
            }
        }
        else {
            ludoController.warningPopUp("Need to log in to speak in chat");
        }
    }

    public void setTextInChat(String user, String message) {
        String completeMessage = String.format("%s: %s%n", user, message);
        chatArea.setText(chatArea.getText() + completeMessage);
    }

}

