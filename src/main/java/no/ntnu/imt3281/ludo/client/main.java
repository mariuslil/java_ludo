package no.ntnu.imt3281.ludo.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n");

            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("../gui/Ludo.fxml"), bundle);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
