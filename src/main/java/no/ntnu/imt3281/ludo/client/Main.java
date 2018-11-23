package no.ntnu.imt3281.ludo.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Client Main
 */
public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger("Client Main");

    /**
     * Start
     * Desc: start the javafx program
     * @param primaryStage primary window for javafx program
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n");

            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("../gui/Ludo.fxml"), bundle);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(bundle.getString("clientludo.title"));
            primaryStage.show();
        } catch(IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    /**
     * Main
     * @param args cmd line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
