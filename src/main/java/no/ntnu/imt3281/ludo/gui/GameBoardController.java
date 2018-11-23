package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


/**
 * GameBoardController class, controller for GameBoard.fxml
 */
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

    @FXML
    void movePiece(){
        int from = 0;   //TEMP make it dynamic with an event or something
        int to = 1;     //same
        ludoController.sendMovePieceRequest(this.gameHash, from, to);
    }

    protected void runDiceEvent(int color, int diceNr){
        //player color threw a diceNr, update GUI
    }

    protected void runPlayerEvent(int color, int status){
        if(status == 400){ //left
            //TODO: remove players name and pieces from the game
            // Send message that user left
            setTextInChat("LEFT", getPlayer(color));
        }else if(status == 300){ //won
            //TODO: Show flashy playername WON! message
        }else if(status == 200){ //playing
            //todo: Activate player color
        }else if(status == 100){ //waiting
            //todo: Deactivate player color
        }
    }

    private String getPlayer(int color){
        String name;
        switch (color){
            case 0: name = player1Name.getText(); break;
            case 1: name = player2Name.getText(); break;
            case 2: name = player3Name.getText();break;
            case 3: name = player4Name.getText();break;
            default: name = "" ; break;
        }
        if(name == null || name.isEmpty()){
            return "";
        }
        return name;
    }

    protected void runPieceEvent(int color, int pieceNr, int fromPos, int toPos){
        //TODO: move player color's pieceNr from Pos to Pos
    }


    public void setTextInChat(String user, String message) {
        Platform.runLater(() -> {
            String completeMessage = String.format("%s: %s%n", user, message);
            chatArea.setText(chatArea.getText() + completeMessage);
        });
    }

    protected void runJoinEvent(String username, int color){
        //TODO: USERNAME joins as player COLOR
    }
}