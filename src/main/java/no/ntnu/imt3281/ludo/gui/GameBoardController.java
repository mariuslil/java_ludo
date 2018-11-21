package no.ntnu.imt3281.ludo.gui;

/**
 * Sample Skeleton for 'GameBoard.fxml' Controller Class
 */

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import no.ntnu.imt3281.ludo.logic.Ludo;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GameBoardController {

    private String gameHash;
    private LudoController ludoController;

    public GameBoardController(String gameHash, LudoController ludoController){
        this.gameHash = gameHash;
        this.ludoController = ludoController;
    }

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
    void speak(ActionEvent event) {

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
}