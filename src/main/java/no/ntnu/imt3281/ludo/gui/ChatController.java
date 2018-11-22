package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private String chatName;
    private LudoController ludoController;
    private ObservableList<String> playerList = FXCollections.observableArrayList();

    public ChatController(LudoController ludoController, String chatName) {
        this.ludoController = ludoController;
        this.chatName = chatName;
    }

    @FXML
    private void initialize(){
        playerArea.setItems(playerList);
    }

    @FXML
    private TextArea chatArea;

    @FXML
    private ListView<String> playerArea;

    @FXML
    private TextField textToSay;

    @FXML
    private Button sendTextButton;

    @FXML
    void sendMessageToServer(ActionEvent event){
        String message = textToSay.getText();
        textToSay.clear();
        ludoController.sendMessageToServer(this.chatName, message);
    }

    public void setTextInChat(String user, String message) {
        Platform.runLater(()->{
            String completeMessage = String.format("%s: %s%n", user, message);
            chatArea.setText(chatArea.getText() + completeMessage);
        });
    }

    public void addPlayer(String userName){
        Platform.runLater(()->{
            playerList.add(userName);
            playerArea.setItems(playerList);
            playerArea.refresh();
        });
    }

    public void removePlayer(String userName){
        Platform.runLater(()->{
            playerList.remove(userName);
            playerArea.setItems(playerList);
            playerArea.refresh();
        });
    }
}

