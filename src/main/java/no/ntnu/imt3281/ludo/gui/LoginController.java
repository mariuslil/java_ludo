package no.ntnu.imt3281.ludo.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    private LudoController ludoController;

    public LoginController(LudoController ludoController){
        this.ludoController = ludoController;
    }

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField registerUsername;

    @FXML
    private PasswordField registerPassword;

    @FXML
    void login(ActionEvent event) {

        String username = this.loginUsername.getText();
        String password = this.loginPassword.getText();

        if(username!=null || password!=null){
            ludoController.loginUser(username, password);
        }
    }

    @FXML
    void register(ActionEvent event) {

        String username = this.registerUsername.getText();
        String password = this.registerPassword.getText();

        if(username!=null || password!=null){
            ludoController.registerUser(username, password);
        }
    }
}
