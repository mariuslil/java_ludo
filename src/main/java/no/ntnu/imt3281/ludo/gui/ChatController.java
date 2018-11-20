package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private List<String> messages = new ArrayList<>();
    private LudoController ludoController;

    public ChatController(LudoController ludoController){
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
        if(message != null && !message.isEmpty()){
            textToSay.clear();
            ludoController.sendMessageFromGlobal(message);
        }
    }

    public void setTextInChat(String user, String message){
        // TODO : add all messages and not one by one :/
        String completeMessage = String.format("%s said: %s%n", user, message);
        messages.add(completeMessage);
        textToSay.clear();
        chatArea.setText(completeMessage);
    }

}

