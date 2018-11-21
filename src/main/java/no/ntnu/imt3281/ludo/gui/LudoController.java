package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.ntnu.imt3281.ludo.client.Client;

import java.net.URL;

public class LudoController {

    private Client client = new Client(this);
    private ChatController chatController = new ChatController(this);

    @FXML
    private WaitDialogController waitDialogController;

    @FXML
    private Stage openDialog;

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        loader.setController(chatController);
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));


        try {
            AnchorPane chat = loader.load();
            Tab tab = new Tab("Chat: Global");
            tab.setContent(chat);
            chatTab.getTabs().add(tab);
        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem connect;

    @FXML
    private MenuItem close;

    @FXML
    private MenuItem random;

    @FXML
    private MenuItem challenge;

    @FXML
    private MenuItem joinChat;

    @FXML
    private MenuItem listRooms;

    @FXML
    private MenuItem about;

    @FXML
    private TabPane tabbedPane;

    @FXML
    private TabPane chatTab;

    @FXML
    void ListRooms(ActionEvent event) {
        //TODO: this
    }

    @FXML
    void about(ActionEvent event) {
        //TODO: this
    }

    @FXML
    void challengePlayer(ActionEvent event) {
        //TODO: this
    }

    @FXML
    void closeApp(ActionEvent event) {
        //TODO: this
    }

    @FXML
    void connectToServer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));

        LoginController loginController = new LoginController(this); //create controller that points to this controller
        loader.setController(loginController);                                        //set controller to this custom controller
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        Parent parent = loader.load();


        Scene scene = new Scene(parent, 600, 340);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();


        //TODO: close stage when logged In or registered.

    }

    @FXML
    void joinChat(ActionEvent event) {
        //TODO: this
    }

    @FXML
    public void joinRandomGame(ActionEvent e) {
        // TODO : Two different outcomes from pressing button This is Johan's version
        client.requestNewGame();

        // TODO : And this is Marius
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        GameBoardController controller = loader.getController();
        // Use controller to set up communication for this game.
        // Note, a new game tab would be created due to some communication from the server
        // This is here purely to illustrate how a layout is loaded and added to a tab pane.

        try {
            AnchorPane gameBoard = loader.load();
            Tab tab = new Tab("Game");
            tab.setContent(gameBoard);
            tabbedPane.getTabs().add(tab);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        */
    }

    @FXML
    public void startNewGame(String gameHash) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        GameBoardController controller = loader.getController(); //TODO: store this and feed it events
        // Use controller to set up communication for this game.
        // Note, a new game tab would be created due to some communication from the server
        // This is here purely to illustrate how a layout is loaded and added to a tab pane.
        Platform.runLater(() -> {
            try {
                AnchorPane gameBoard = loader.load();
                Tab tab = new Tab(gameHash);
                tab.setContent(gameBoard);
                this.tabbedPane.getTabs().add(tab);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    @FXML
    public void startWaitForGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitDialog.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        WaitDialogController controller = new WaitDialogController();
        loader.setController(controller);
        this.waitDialogController = controller;

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 400);
        this.openDialog = new Stage();
        this.openDialog.initModality(Modality.APPLICATION_MODAL);
        this.openDialog.setScene(scene);
        this.openDialog.showAndWait();


    }

    public void loginUser(String username, String password) {
        System.out.println("CONTROLLER: User: " + username + " is logging in");

        if (username != null && password != null) {
            client.connect("LOGIN:", username, password);
        }

        //TODO: close login dialog
    }

    public void registerUser(String username, String password) {
        System.out.println("CONTROLLER: User " + username + " is registering");

        if (username != null && password != null) {
            client.connect("REGISTER:", username, password);
        }

        //TODO: close login dialog
    }

    @FXML
    public void updateWaitDialog(String update) {
        if (waitDialogController != null) {
            Platform.runLater(() -> waitDialogController.updateTextArea(update));
        }
    }

    @FXML
    public void removeOpenDialog() {
        if (openDialog != null) {
            Platform.runLater(() -> openDialog.close());
        }
    }

    public void setMessageInGlobalTextBox(String sender, String message){
        Platform.runLater(() ->{
            chatController.setTextInChat(sender, message);
        });
    }

    public void sendMessageFromGlobal(String message){
        // TODO : change this so I can get the actual message
        if(message != null && !message.isEmpty()){
            client.sendGLOBALText(message);
        }
    }
}
