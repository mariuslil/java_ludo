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

    private Ludo ludo;
    private LudoController ludoController;

    public GameBoardController(String gameHash, LudoController ludoController){
        ludo = new Ludo(gameHash);
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
        ludo.throwDice();
    }

    protected void runDiceEvent(int color, int diceNr){
        if(ludo.activePlayer() == color){
            ludo.throwDice(diceNr);
        }
    }

    protected void runPlayerEvent(int color, int status){
        if(status == 400){ //left
            ludo.removePlayer(ludo.getPlayerName(color));
        }else if(status == 300){ //won
            //TODO: suspend shit because player won
            //TODO: update db of winner
        }else if(status == 200){ //playing
            //todo: this
        }else if(status == 100){ //waiting
            //todo: this
        }
    }

    protected void runPieceEvent(int color, int pieceNr, int fromPos, int toPos){
        if(ludo.activePlayer() == color){
            ludo.movePiece(color, fromPos, toPos);
        }
    }
}