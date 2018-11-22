package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import no.ntnu.imt3281.ludo.logic.Ludo;
import javafx.scene.shape.Circle;


public class GameBoardController {

    private String gameHash;
    private LudoController ludoController;
    private Ludo ludo;
    protected final int home = 216;
    protected final int offset = 48;
    protected Circle[][] pieces = new Circle[4][4];

    public GameBoardController(String gameHash, LudoController ludoController){
        this.gameHash = gameHash;
        this.ludoController = ludoController;
    }

    @FXML
    private void initialize(){
        //TODO: make the loops run acording to number of players
        player2Active.setVisible(false);
        player3Active.setVisible(false);
        player4Active.setVisible(false);
        player3.setVisible(false);
        player4.setVisible(false);
    }

    @FXML
    private StackPane gameArea;

    @FXML
    private StackPane board;

    @FXML
    private GridPane players;

    @FXML
    private Pane player1;

    @FXML
    private ImageView player1Active;

    @FXML
    private Label player1Name;

    @FXML
    private Pane player2;

    @FXML
    private ImageView player2Active;

    @FXML
    private Label player2Name;

    @FXML
    private Pane player3;

    @FXML
    private ImageView player3Active;

    @FXML
    private Label player3Name;

    @FXML
    private Pane player4;

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
        //TODO: player color threw a diceNr, update GUI
        switch (diceNr){
            case 1:
                diceThrown.setImage(new Image("/images/dice1.png"));
                break;
            case 2:
                diceThrown.setImage(new Image("/images/dice2.png"));
                break;
            case 3:
                diceThrown.setImage(new Image("/images/dice3.png"));
                break;
            case 4:
                diceThrown.setImage(new Image("/images/dice4.png"));
                break;
            case 5:
                diceThrown.setImage(new Image("/images/dice5.png"));
                break;
            case 6:
                diceThrown.setImage(new Image("/images/dice6.png"));
                break;
        }
    }

    protected void runPlayerEvent(int color, int status){
        if(status == 400){ //left
            //TODO: remove players name and pieces from the game
        }else if(status == 300){ //won
            //TODO: Show flashy playername WON! message
        }else if(status == 200){ //playing
            switch (color) {
                case 0: player1Active.setVisible(true); break;
                case 1: player1Active.setVisible(true); break;
                case 2: player1Active.setVisible(true); break;
                case 3: player1Active.setVisible(true); break;
            }
        }else if(status == 100){ //waiting
            switch (color) {
                case 0: player1Active.setVisible(false); break;
                case 1: player1Active.setVisible(false); break;
                case 2: player1Active.setVisible(false); break;
                case 3: player1Active.setVisible(false); break;
            }
        }
    }

    protected void runPieceEvent(int color, int pieceNr, int fromPos, int toPos){
        //TODO: move player color's pieceNr from Pos to Pos
    }

    protected void runJoinEvent(String username, int color){
        Platform.runLater(()->{
            switch (color){
                case 0:
                    player1Name.setText(username);
                    placePieces(color);
                    break;
                case 1:
                    player2Name.setText(username);
                    placePieces(color);
                    break;
                case 2:
                    player3Name.setText(username);
                    player3.setVisible(true);
                    placePieces(color);
                    break;
                case 3:
                    player4Name.setText(username);
                    player4.setVisible(true);
                    placePieces(color);
                    break;
            }
        });
    }

    protected void placePieces(int color) {
        Color[]colors=new Color[4];
        colors[0]=Color.RED;colors[1]=Color.BLUE;colors[2]=Color.YELLOW;colors[3]=Color.GREEN;
        for(int j=0;j<4;j++){
            pieces[color][j]=new Circle(20, colors[color]);
            pieces[color][j].setStroke(Color.BLACK);
            pieces[color][j].setStrokeWidth(2);
            board.getChildren().add(pieces[color][j]);
            board.setAlignment(pieces[color][j],Pos.CENTER);
            moveHome(pieces,color,j);
        }
    }

    public void moveHome(Circle[][] pieces, int color, int piece) {
        switch (color){
            case 0:
                switch (piece){
                    case 0:
                        pieces[color][piece].setTranslateX(home);
                        pieces[color][piece].setTranslateY(-home-offset);
                        break;
                    case 1:
                        pieces[color][piece].setTranslateX(home+offset);
                        pieces[color][piece].setTranslateY(-home);
                        break;
                    case 2:
                        pieces[color][piece].setTranslateX(home);
                        pieces[color][piece].setTranslateY(-home+offset);
                        break;
                    case 3:
                        pieces[color][piece].setTranslateX(home-offset);
                        pieces[color][piece].setTranslateY(-home);
                        break;
                }
                break;
            case 1:
                switch (piece){
                    case 0:
                        pieces[color][piece].setTranslateX(home);
                        pieces[color][piece].setTranslateY(home-offset);
                        break;
                    case 1:
                        pieces[color][piece].setTranslateX(home+offset);
                        pieces[color][piece].setTranslateY(home);
                        break;
                    case 2:
                        pieces[color][piece].setTranslateX(home);
                        pieces[color][piece].setTranslateY(home+offset);
                        break;
                    case 3:
                        pieces[color][piece].setTranslateX(home-offset);
                        pieces[color][piece].setTranslateY(home);
                        break;
                }
                break;
            case 2:
                switch (piece){
                    case 0:
                        pieces[color][piece].setTranslateX(-home);
                        pieces[color][piece].setTranslateY(home-offset);
                        break;
                    case 1:
                        pieces[color][piece].setTranslateX(-home+offset);
                        pieces[color][piece].setTranslateY(home);
                        break;
                    case 2:
                        pieces[color][piece].setTranslateX(-home);
                        pieces[color][piece].setTranslateY(home+offset);
                        break;
                    case 3:
                        pieces[color][piece].setTranslateX(-home-offset);
                        pieces[color][piece].setTranslateY(home);
                        break;
                }
                break;
            case 3:
                switch (piece){
                    case 0:
                        pieces[color][piece].setTranslateX(-home);
                        pieces[color][piece].setTranslateY(-home-offset);
                        break;
                    case 1:
                        pieces[color][piece].setTranslateX(-home+offset);
                        pieces[color][piece].setTranslateY(-home);
                        break;
                    case 2:
                        pieces[color][piece].setTranslateX(-home);
                        pieces[color][piece].setTranslateY(-home+offset);
                        break;
                    case 3:
                        pieces[color][piece].setTranslateX(-home-offset);
                        pieces[color][piece].setTranslateY(-home);
                        break;
                }
                break;
        }
    }
}