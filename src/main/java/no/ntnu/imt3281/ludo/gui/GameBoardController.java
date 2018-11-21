package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


public class GameBoardController {
    private LudoController ludoController;

    public GameBoardController(LudoController ludoController){
        this.ludoController = ludoController;
    }

    @FXML
    private void initialize() {


    }

    @FXML
    private Canvas board;

    @FXML
    private Label player1Name;

    @FXML
    private ImageView player1Active;

    @FXML
    private Label player2Name;

    @FXML
    private ImageView player2Active;

    @FXML
    private Label player3Name;

    @FXML
    private ImageView player3Active;

    @FXML
    private Label player4Name;

    @FXML
    private ImageView player4Active;

    @FXML
    private ImageView diceThrown;

    @FXML
    private Button throwTheDice;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textToSay;

    @FXML
    private Button sendTextButton;

    @FXML
    void sendChatMessage(ActionEvent event) {
        String message = textToSay.getText();
        textToSay.clear();
        ludoController.sendMessageFromLocal(message);
    }

    @FXML
    void throwDice(ActionEvent event) {

    }

    public void setTextInChat(String user, String message) {
        String completeMessage = String.format("%s: %s%n", user, message);
        chatArea.setText(chatArea.getText() + completeMessage);
    }
}