package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ResourceBundle;


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
    void speak(ActionEvent event) {

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

    protected void runJoinEvent(String username, int color){
        //TODO: USERNAME joins as player COLOR
    }
}