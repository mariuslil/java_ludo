package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class ProfileInformationController {

    private LudoController ludoController;
    private String userName;

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

        if(!userNameChange.getText().isEmpty()){ // If the textfield is not empty

            if(!userNameChange.getText().equals(this.userName)){ // Only change userName if the name is changed
                // TODO : Actually change userName
                // db
                // client
                // server

                // Save username for here
                this.userName = userNameChange.getText();

                // TODO : Give confirmation that userName is changed

            }
        }
    }

    public void setProfileData(String userName, int nrOfGames, int nrOfChats){

        Platform.runLater(() ->{
            // Initialize
            userNameChange.setText("");
            numberOfGames.setText("");
            numberOfChats.setText("");

            // Fill in profile information
            if(userName != null && !userName.isEmpty()){
                this.userName = userName;
                userNameChange.setText(this.userName);
            }

            numberOfGames.setText(nrOfGames+"");
            numberOfChats.setText(nrOfChats+"");
        });

    }
}
