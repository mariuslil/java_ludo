package no.ntnu.imt3281.ludo.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

                // TODO : make actual check
                boolean nameIsChangedSuccesfully = true;
                if(nameIsChangedSuccesfully){
                    // Confirm name is changed
                    userNameChange.setStyle("-fx-control-inner-background: #66b266");
                } else {
                    // Couldn't change name or it already exists
                    userNameChange.setStyle("-fx-control-inner-background: #ff6666");
                }
            } else {
                // Name is the same as before
                userNameChange.setStyle("-fx-control-inner-background: #ff6666");
            }
        } else {
            // Name is empty or null
            userNameChange.setStyle("-fx-control-inner-background: #ff6666");
        }
    }

    public void setProfileData(String userName, int nrOfGames, int nrOfChats){

        Platform.runLater(() ->{
            userNameChange.setStyle("-fx-control-inner-background: #FFFFFF");
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
