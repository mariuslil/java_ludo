package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.ImageView;


public class GameBoardController {


    private String gameHash;
    private LudoController ludoController;
    private Ludo ludo;
    protected final int home = 216;
    protected final int offset = 48;
    protected Circle[][] pieces = new Circle[4][4];
    private int diceNr;
    protected int[][] numberBoard0 = new int[][]{
            {0,0,0,0,0,0,50,51,52,0,0,0,0,0,0},
            {0,0,0,0,0,0,49,54,53,0,0,0,0,0,0},
            {0,0,0,0,0,0,48,55,2,0,0,0,0,0,0},
            {0,0,0,0,0,0,47,56,3,0,0,0,0,0,0},
            {0,0,0,0,0,0,46,57,4,0,0,0,0,0,0},
            {0,0,0,0,0,0,45,58,5,0,0,0,0,0,0},
        {39,40,41,42,43,44,0,59,0,6,7,8,9,10,11},
            {38,0,0,0,0,0,0,0,0,0,0,0,0,0,12},
        {37,36,35,34,33,32,0,0,0,18,17,16,15,14,13},
            {0,0,0,0,0,0,31,0,19,0,0,0,0,0,0},
            {0,0,0,0,0,0,30,0,20,0,0,0,0,0,0},
            {0,0,0,0,0,0,29,0,21,0,0,0,0,0,0},
            {0,0,0,0,0,0,28,0,22,0,0,0,0,0,0},
            {0,0,0,0,0,0,27,0,23,0,0,0,0,0,0},
            {0,0,0,0,0,0,26,25,24,0,0,0,0,0,0}};

    protected int[][] numberBoard1 = new int[][]{
            {0,0,0,0,0,0,37,38,39,0,0,0,0,0,0},
            {0,0,0,0,0,0,36,0,40,0,0,0,0,0,0},
            {0,0,0,0,0,0,35,0,41,0,0,0,0,0,0},
            {0,0,0,0,0,0,34,0,42,0,0,0,0,0,0},
            {0,0,0,0,0,0,33,0,43,0,0,0,0,0,0},
            {0,0,0,0,0,0,32,0,44,0,0,0,0,0,0},
        {26,27,28,29,30,31,0,0,0,45,46,47,48,49,50},
        {25,0,0,0,0,0,0,0,59,58,57,56,55,54,51},
        {24,23,22,21,20,19,0,0,0,5,4,3,2,53,52},
            {0,0,0,0,0,0,18,0,6,0,0,0,0,0,0},
            {0,0,0,0,0,0,17,0,7,0,0,0,0,0,0},
            {0,0,0,0,0,0,16,0,8,0,0,0,0,0,0},
            {0,0,0,0,0,0,15,0,9,0,0,0,0,0,0},
            {0,0,0,0,0,0,14,0,10,0,0,0,0,0,0},
            {0,0,0,0,0,0,13,12,11,0,0,0,0,0,0}};

    protected int[][] numberBoard2 = new int[][]{
            {0,0,0,0,0,0,24,25,26,0,0,0,0,0,0},
            {0,0,0,0,0,0,23,0,27,0,0,0,0,0,0},
            {0,0,0,0,0,0,22,0,28,0,0,0,0,0,0},
            {0,0,0,0,0,0,21,0,29,0,0,0,0,0,0},
            {0,0,0,0,0,0,20,0,30,0,0,0,0,0,0},
            {0,0,0,0,0,0,19,0,31,0,0,0,0,0,0},
            {13,14,15,16,17,18,0,0,0,32,33,34,35,36,37},
            {12,0,0,0,0,0,0,0,0,0,0,0,0,0,38},
            {11,10,9,8,7,6,0,59,0,44,43,42,41,40,39},
            {0,0,0,0,0,0,5,58,45,0,0,0,0,0,0},
            {0,0,0,0,0,0,4,57,46,0,0,0,0,0,0},
            {0,0,0,0,0,0,3,56,47,0,0,0,0,0,0},
            {0,0,0,0,0,0,2,55,48,0,0,0,0,0,0},
            {0,0,0,0,0,0,53,54,49,0,0,0,0,0,0},
            {0,0,0,0,0,0,52,51,50,0,0,0,0,0,0}};

    protected int[][] numberBoard3 = new int[][]{
            {0,0,0,0,0,0,11,12,13,0,0,0,0,0,0},
            {0,0,0,0,0,0,10,0,14,0,0,0,0,0,0},
            {0,0,0,0,0,0,9,0,15,0,0,0,0,0,0},
            {0,0,0,0,0,0,8,0,16,0,0,0,0,0,0},
            {0,0,0,0,0,0,7,0,17,0,0,0,0,0,0},
            {0,0,0,0,0,0,6,0,18,0,0,0,0,0,0},
        {52,53,2,3,4,5,0,0,0,19,20,21,22,23,24},
        {51,54,55,56,57,58,59,0,0,0,0,0,0,0,25},
        {50,59,58,47,46,45,0,0,0,31,30,29,28,27,26},
            {0,0,0,0,0,0,44,0,32,0,0,0,0,0,0},
            {0,0,0,0,0,0,43,0,33,0,0,0,0,0,0},
            {0,0,0,0,0,0,42,0,34,0,0,0,0,0,0},
            {0,0,0,0,0,0,41,0,35,0,0,0,0,0,0},
            {0,0,0,0,0,0,40,0,36,0,0,0,0,0,0},
            {0,0,0,0,0,0,39,38,37,0,0,0,0,0,0}};


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
    void movePiece(int color, int x, int y){
        int from = 0;

        if(x == -216 || x == 216 || y == -216 || y == 216){
            from = 0;
        }

        else{
            int xPos = (x+350)/offset;
            int yPos = (y+350)/offset;

            switch (color) {
                case 0: from = numberBoard0[yPos][xPos]; break;
                case 1: from = numberBoard1[yPos][xPos]; break;
                case 2: from = numberBoard2[yPos][xPos]; break;
                case 3: from = numberBoard3[yPos][xPos]; break;
            }

            System.out.println("MOVE PIECE FOR: "+color+" X:"+xPos+" Y:"+yPos+" FROM: "+from);
        }

        ludoController.sendMovePieceRequest(this.gameHash, from, from+this.diceNr);
    }

    protected void runDiceEvent(int color, int diceNr){
        this.diceNr = diceNr;
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
            int i = 0;
            switch (color) {
                case 0:
                    player1.setVisible(false);
                    while (i < 4){
                        pieces[color][i].setVisible(false);
                }
                    break;
                case 1:
                    player2.setVisible(false);
                    while (i < 4){
                        pieces[color][i].setVisible(false);
                    }
                    break;
                case 2:
                    player3.setVisible(false);
                    while (i < 4){
                        pieces[color][i].setVisible(false);
                    }
                    break;
                case 3:
                    player4.setVisible(false);
                    while (i < 4){
                        pieces[color][i].setVisible(false);
                    }
                    break;
            }
        }else if(status == 300){ //won
            String player = "";
            switch (color){
                case 0: player = "Player 1"; break;
                case 1: player = "Player 2"; break;
                case 2: player = "Player 3"; break;
                case 3: player = "Player 4"; break;
            }
            ludoController.PopUp("Victory Royale", "Congratulations "+player+" on wining the game");
        }else if(status == 200){ //playing
            switch (color) {
                case 0: player1Active.setVisible(true); break;
                case 1: player2Active.setVisible(true); break;
                case 2: player3Active.setVisible(true); break;
                case 3: player4Active.setVisible(true); break;
            }
        }else if(status == 100){ //waiting
            switch (color) {
                case 0: player1Active.setVisible(false); break;
                case 1: player2Active.setVisible(false); break;
                case 2: player3Active.setVisible(false); break;
                case 3: player4Active.setVisible(false); break;
            }
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
        System.out.println("RUN PIECE EVENT FOR: "+color+" MED BRIKKE: "+pieceNr+" FRA: "+fromPos+" TIL: "+toPos);


        if(pieceNr == 69){
            pieceNr = 0;
        }

        if(fromPos == 53){
            toPos = toPos-fromPos;
        }

        if (fromPos == 0) {
            switch (color) {
                case 0:
                        pieces[color][pieceNr].setTranslateX(offset);
                        pieces[color][pieceNr].setTranslateY(-offset * 6);
                    break;
                case 1:
                        pieces[color][pieceNr].setTranslateX(offset * 6);
                        pieces[color][pieceNr].setTranslateY(offset);
                    break;
                case 2:
                        pieces[color][pieceNr].setTranslateX(-offset);
                        pieces[color][pieceNr].setTranslateY(offset * 6);
                    break;
                case 3:
                        pieces[color][pieceNr].setTranslateX(-offset * 6);
                        pieces[color][pieceNr].setTranslateY(-offset);
                    break;
            }
        }

        for(int i = fromPos; i <= toPos; i++) { // For every step
            if (toPos > 15 && toPos < 21 || toPos > 25 && toPos < 29 || toPos > 33 && toPos < 40){
                // Move down
                pieces[color][pieceNr].setTranslateY(pieces[color][pieceNr].getTranslateY()+offset);
                if(i+1 == 21) {
                    pieces[color][pieceNr].setTranslateY(pieces[color][pieceNr].getTranslateY()+offset);
                }
            }
            if (toPos > 20 && toPos < 27 || toPos > 53 && toPos < 60 || toPos > 64 && toPos < 68) {
                // Move right
                pieces[color][pieceNr].setTranslateX(pieces[color][pieceNr].getTranslateX()+offset);
                if(i+1 == 60) {
                    pieces[color][pieceNr].setTranslateX(pieces[color][pieceNr].getTranslateX()+offset);
                }
            }
            if (toPos > 28 && toPos < 34 || toPos > 39 && toPos < 42 || toPos > 46 && toPos < 53) {
                // Move left
                pieces[color][pieceNr].setTranslateX(pieces[color][pieceNr].getTranslateX()-offset);
                if(i+1 == 34) {
                    pieces[color][pieceNr].setTranslateX(pieces[color][pieceNr].getTranslateX()-offset);
                }
            }
            if(toPos > 41 && toPos < 47 || toPos > 52 && toPos < 55 || toPos > 59 && toPos < 66){
                // Move up
                pieces[color][pieceNr].setTranslateY(pieces[color][pieceNr].getTranslateY()-offset);
                if(i+1 == 47) {
                    pieces[color][pieceNr].setTranslateY(pieces[color][pieceNr].getTranslateY()-offset);
                }
            }
        }

    }


    public void setTextInChat(String user, String message) {
        Platform.runLater(() -> {
            String completeMessage = String.format("%s: %s%n", user, message);
            chatArea.setText(chatArea.getText() + completeMessage);
        });
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
           placePiece(color, j, colors);
        }
    }

    private void placePiece(int color, int j, Color[] colors){
        pieces[color][j]=new Circle(20, colors[color]);
        pieces[color][j].setStroke(Color.BLACK);
        pieces[color][j].setStrokeWidth(2);
        board.getChildren().add(pieces[color][j]);
        board.setAlignment(pieces[color][j],Pos.CENTER);
        moveHome(pieces,color,j);
        pieces[color][j].setOnMouseClicked(click -> {

                movePiece(color, (int)pieces[color][j].getTranslateX(), (int)pieces[color][j].getTranslateY());

        });
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