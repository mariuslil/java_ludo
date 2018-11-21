package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


public class GameBoardController {


    private String gameHash;
    private LudoController ludoController;

    public GameBoardController(String gameHash, LudoController ludoController){
        this.gameHash = gameHash;
        this.ludoController = ludoController;
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
        ludoController.sendMessageFromLocal(message, gameHash);
    }

    @FXML
    void throwDice(ActionEvent event) {
        ludoController.sendDiceThrowRequest(this.gameHash);
    }

    protected void runDiceEvent(int color, int diceNr){
        //player color threw a diceNr, update GUI
    }

    protected void runPlayerEvent(int color, int status){
        if(status == 400){ //left
            //TODO: remove players name and pieces from the game
        }else if(status == 300){ //won
            //TODO: Show flashy playername WON! message
        }else if(status == 200){ //playing
            //todo: Activate player color
        }else if(status == 100){ //waiting
            //todo: Deactivate player color
        }
    }

    protected void runPieceEvent(int color, int pieceNr, int fromPos, int toPos){
        //TODO: move player color's pieceNr from Pos to Pos
    }

    public void setTextInChat(String user, String message) {
        String completeMessage = String.format("%s: %s%n", user, message);
        chatArea.setText(chatArea.getText() + completeMessage);
    }

    /*
    public void setNames(String user1, String user2, String user3, String user4){
        if(user1 != null && !user1.isEmpty()){
            player1Name.setText(user1);
        } else {
            player1Name.setText("No Player");
        }
        if(user2 != null && !user2.isEmpty()){
            player2Name.setText(user2);
        }else {
            player2Name.setText("No Player");
        }
        if(user3 != null && !user3.isEmpty()){
            player3Name.setText(user3);
        }else {
            player3Name.setText("No Player");
        }
        if(user4 != null && !user4.isEmpty()){
            player4Name.setText(user4);
        }else {
            player4Name.setText("No Player");
        }
    }
    */
}