package no.ntnu.imt3281.ludo.gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import no.ntnu.imt3281.ludo.logic.Ludo;
import javafx.scene.shape.Circle;

public class GameBoardController {

    private Ludo ludo;
    protected final int home = 216;
    protected final int offset = 48;

    public GameBoardController(String gameHash){
        ludo = new Ludo();
    }

    @FXML
    private void initialize(){

        Color[] colors = new Color[4];
        colors[0] = Color.RED; colors[1] = Color.BLUE; colors[2] = Color.YELLOW; colors[3] = Color.GREEN;
        Circle[][] pieces = new Circle[ludo.nrOfPlayers()][4];
        for (int i = 1; i <= ludo.nrOfPlayers(); i++){
            for (int j = 1; j <= 4; j++){
                pieces[i][j] = new Circle(23, colors[i]);
                board.getChildren().add(i*10+j, pieces[i][j]);
                board.getChildren().get(1*10+j).setTranslateX(-home);
                board.getChildren().get(1*10+j).setTranslateY(home);
            }
        }
    }

    @FXML
    private StackPane gameArea;

    @FXML
    private Pane board;

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

    }
}