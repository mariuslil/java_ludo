package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import no.ntnu.imt3281.ludo.logic.Ludo;
import javafx.scene.shape.Circle;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class GameBoardController {

    private String gameHash;
    private LudoController ludoController;
    private Ludo ludo;
    protected final int home = 216;
    protected final int offset = 48;

    public GameBoardController(String gameHash, LudoController ludoController){
        this.gameHash = gameHash;
        this.ludoController = ludoController;
    }

    @FXML
    private void initialize(){
        //TODO: make the loops run acording to number of players
        Color[]colors=new Color[4];
        colors[0]=Color.RED;colors[1]=Color.BLUE;colors[2]=Color.YELLOW;colors[3]=Color.GREEN;
        Circle[][]pieces=new Circle[4][4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                pieces[i][j]=new Circle(20, colors[i]);
                pieces[i][j].setStroke(Color.BLACK);
                pieces[i][j].setStrokeWidth(2);
                board.getChildren().add(pieces[i][j]);
                board.setAlignment(pieces[i][j],Pos.CENTER);
                switch (i){
                    case 0:
                        switch (j){
                            case 0:
                                pieces[i][j].setTranslateX(home);
                                pieces[i][j].setTranslateY(-home-offset);
                                break;
                            case 1:
                                pieces[i][j].setTranslateX(home+offset);
                                pieces[i][j].setTranslateY(-home);
                                break;
                            case 2:
                                pieces[i][j].setTranslateX(home);
                                pieces[i][j].setTranslateY(-home+offset);
                                break;
                            case 3:
                                pieces[i][j].setTranslateX(home-offset);
                                pieces[i][j].setTranslateY(-home);
                                break;
                        }
                        break;
                    case 1:
                        switch (j){
                            case 0:
                                pieces[i][j].setTranslateX(home);
                                pieces[i][j].setTranslateY(home-offset);
                                break;
                            case 1:
                                pieces[i][j].setTranslateX(home+offset);
                                pieces[i][j].setTranslateY(home);
                                break;
                            case 2:
                                pieces[i][j].setTranslateX(home);
                                pieces[i][j].setTranslateY(home+offset);
                                break;
                            case 3:
                                pieces[i][j].setTranslateX(home-offset);
                                pieces[i][j].setTranslateY(home);
                                break;
                        }
                        break;
                    case 2:
                        switch (j){
                            case 0:
                                pieces[i][j].setTranslateX(-home);
                                pieces[i][j].setTranslateY(home-offset);
                                break;
                            case 1:
                                pieces[i][j].setTranslateX(-home+offset);
                                pieces[i][j].setTranslateY(home);
                                break;
                            case 2:
                                pieces[i][j].setTranslateX(-home);
                                pieces[i][j].setTranslateY(home+offset);
                                break;
                            case 3:
                                pieces[i][j].setTranslateX(-home-offset);
                                pieces[i][j].setTranslateY(home);
                                break;
                        }
                        break;
                    case 3:
                        switch (j){
                            case 0:
                                pieces[i][j].setTranslateX(-home);
                                pieces[i][j].setTranslateY(-home-offset);
                                break;
                            case 1:
                                pieces[i][j].setTranslateX(-home+offset);
                                pieces[i][j].setTranslateY(-home);
                                break;
                            case 2:
                                pieces[i][j].setTranslateX(-home);
                                pieces[i][j].setTranslateY(-home+offset);
                                break;
                            case 3:
                                pieces[i][j].setTranslateX(-home-offset);
                                pieces[i][j].setTranslateY(-home);
                                break;
                        }
                        break;
                }
            }
        }
    }

    @FXML
    private StackPane gameArea;

    @FXML
    private StackPane board;

    @FXML
    private ImageView player1Active;

    @FXML
    private Label player1Name;

    @FXML
    private ImageView player2Active;

    @FXML
    private Label player2Name;

    @FXML
    private ImageView player3Active;

    @FXML
    private Label player3Name;

    @FXML
    private ImageView player4Active;

    @FXML
    private Label player4Name;

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