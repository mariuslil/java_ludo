package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import no.ntnu.imt3281.ludo.logic.Ludo;

public class GameBoardController {

    private Ludo ludo;

    public GameBoardController(String gameHash){
        ludo = new Ludo();
    }

    @FXML
    private void initialize(){

    }

    @FXML
    private StackPane gameArea;

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