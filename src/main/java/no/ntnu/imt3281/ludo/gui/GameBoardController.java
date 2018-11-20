package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class GameBoardController {

    public GameBoardController() {


        /*
         String path = "../../../images/ludo-board.png";
         Image img = new Image(path);

         GraphicsContext gc = board.getGraphicsContext2D();
         gc.drawImage(img, img.getWidth(), img.getHeight());
         */
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

    }
}