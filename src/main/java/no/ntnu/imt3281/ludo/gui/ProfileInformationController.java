package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileInformationController {

    private LudoController ludoController;

    public ProfileInformationController(LudoController ludoController){
        this.ludoController = ludoController;
    }

    @FXML
    private Label numberOfGames;

    @FXML
    private Label numberOfChats;

    @FXML
    private Button changeButton;

    @FXML
    private TextField userNameChange;

    @FXML
    void onChangeUsernameClick(ActionEvent event) {
        System.out.println(userNameChange.getText());
    }
}
