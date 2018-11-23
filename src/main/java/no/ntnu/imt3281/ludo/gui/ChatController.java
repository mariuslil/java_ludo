package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.logging.Logger;

/**
 *
 */
public class ChatController {

    private static final Logger LOGGER = Logger.getLogger("Chat Controller");

    private String chatName;
    private LudoController ludoController;

    private ObservableList<String> playerList = FXCollections.observableArrayList();

    /**
     * ChatController
     * Constructor
     * @param ludoController LudoController to talk to parent class
     * @param chatName this custom chat's title
     */
    public ChatController(LudoController ludoController, String chatName) {
        this.ludoController = ludoController;
        this.chatName = chatName;
    }

    @FXML
    private void initialize() {
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

    /**
     * setTextInChat
     * adds text to chat
     * @param user user who sent the text
     * @param message text to be added
     */
    public void setTextInChat(String user, String message) {
        Platform.runLater(()->{
            String completeMessage = String.format("%s: %s%n", user, message);
            chatArea.setText(chatArea.getText() + completeMessage);
        });
    }

    /**
     * addPlayer
     * adds player to chat list
     * @param userName name of player to be added
     */
    public void addPlayer(String userName){
        LOGGER.info("CHATCONTROLLER:"+this.chatName.toUpperCase()+": adding player "+userName);
        Platform.runLater(()->playerList.add(userName));
    }

    /**
     * removePlayer
     * removes player from chat list
     * @param userName name of player to be removed
     */
    public void removePlayer(String userName){
        Platform.runLater(()->{
            if(playerList.contains(userName)) {
                playerList.remove(userName);
            }
        });
    }
}

