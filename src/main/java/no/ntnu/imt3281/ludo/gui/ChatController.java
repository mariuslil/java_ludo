package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private LudoController ludoController;

    public ChatController(LudoController ludoController) {
        this.ludoController = ludoController;
    }

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textToSay;

    @FXML
    private Button sendTextButton;

    @FXML
    void onSendGlobalMessage(ActionEvent event) {
        String message = textToSay.getText();
        textToSay.clear();
        ludoController.sendMessageFromGlobal(message);
    }

    public void setTextInChat(String user, String message) {
        String completeMessage = String.format("%s: %s%n", user, message);
        chatArea.setText(chatArea.getText() + completeMessage);
    }

}

