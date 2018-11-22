package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import java.util.concurrent.ConcurrentHashMap;

public class LudoController {

    private Client client = new Client(this);
    //private ChatController chatController = new ChatController(this);

	private final ConcurrentHashMap<String, GameBoardController> gameControllers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ChatController> chatControllers = new ConcurrentHashMap<>();

	@FXML
	private WaitDialogController waitDialogController;

    @FXML
    private Stage openDialog;

    @FXML
    private void initialize() {

        tabbedPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        chatTab.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));

        ChatController chatController = new ChatController(this, "Global");
        chatControllers.put("Global", chatController);

        loader.setController(chatController);
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));


        try {
            AnchorPane chat = loader.load();
            Tab tab = new Tab("Global");
            tab.setContent(chat);
            tab.setClosable(false);
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
        this.openDialog = new Stage();
        this.openDialog.initModality(Modality.APPLICATION_MODAL);
        this.openDialog.setScene(scene);
        this.openDialog.showAndWait();


        //TODO: close stage when logged In or registered.

    }

    @FXML
    void joinChat(ActionEvent event) {
        //TODO: this
    }

    @FXML
    public void createChat(String chatName){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));

        ChatController chatController = new ChatController(this, chatName);
        chatControllers.put(chatName, chatController);

        loader.setController(chatController);
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        try {
            AnchorPane chat = loader.load();
            Tab tab = new Tab(chatName);
            tab.setClosable(true);
            tab.setContent(chat);
            chatTab.getTabs().add(tab);
        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    @FXML
    public void addPlayerToChat(String chatName, String playerName){
        this.chatControllers.get(chatName).addPlayer(playerName);
    }

    @FXML
    public void sendMessageToChat(String chatName, String userName, String message){
        this.chatControllers.get(chatName).setTextInChat(userName, message);
    }

    @FXML
    public void sendMessageToServer(String chatName, String message){
        this.client.requestSendChatMessage(chatName, message);
    }

    @FXML
    public void removePlayerFromChat(String chatName, String userName){
        this.chatControllers.get(chatName).removePlayer(userName);
    }

    @FXML
    public void joinRandomGame(ActionEvent e) {
        client.requestNewGame();
    }

    @FXML
    public void startNewGame(String gameHash) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
        loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

        GameBoardController gameBoardController = new GameBoardController(gameHash, this);
		loader.setController(gameBoardController);

		gameControllers.put(gameHash, gameBoardController);

		Platform.runLater(()-> {
			try {
				AnchorPane gameBoard = loader.load();
				Tab tab = new Tab(gameHash);
				tab.setClosable(true);
                tab.setOnCloseRequest(close -> {
                    if(close.getEventType().equals(Tab.TAB_CLOSE_REQUEST_EVENT)){
                        client.leaveGame(gameHash);
                    }
                });
                tab.setClosable(true);
				tab.setContent(gameBoard);
				this.tabbedPane.getTabs().add(tab);

            } catch (IOException e1) {
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
    }

    public void registerUser(String username, String password) {
        System.out.println("CONTROLLER: User " + username + " is registering");

        if (username != null && password != null) {
            client.connect("REGISTER:", username, password);
        }

    }

    @FXML
    public void updateWaitDialog(String update) {
        if (waitDialogController != null) {
            Platform.runLater(() -> waitDialogController.updateTextArea(update));
        }
    }

    @FXML
    public void removeOpenDialog(){
        if(openDialog!=null){
            Platform.runLater(()-> openDialog.close());
        }
    }

    @FXML
    public void setMessageInGlobalTextBox(String sender, String message){
        chatControllers.get("Global").setTextInChat(sender, message);
    }

    public void setMessageInLocalTextBox(String gameID, String sender, String message){
        gameControllers.get(gameID).setTextInChat(sender, message);
    }

	@FXML
	public void receiveDiceEvent(String gameHash, int color, int diceNr){
		gameControllers.get(gameHash).runDiceEvent(color, diceNr);
	}

	@FXML
	public void receivePlayerEvent(String gameHash, int color, int status){
		gameControllers.get(gameHash).runPlayerEvent(color, status);
	}

	@FXML
	public void receivePieceEvent(String gameHash, int color, int pieceNr, int fromPos, int toPos){
		gameControllers.get(gameHash).runPieceEvent(color, pieceNr, fromPos, toPos);
	}

    @FXML
    public void receiveJoinEvent(String gameHash, String username, int color){
        gameControllers.get(gameHash).runJoinEvent(username, color);
    }

	public void sendDiceThrowRequest(String gameHash){
		client.sendDiceEvent(gameHash);
	}

    public void sendMovePieceRequest(String gameHash, int from, int to){
        client.sendMoveEvent(gameHash, from, to);
    }

    public void sendMessageFromLocal(String message, String gameHash){
        if(message != null && !message.isEmpty() && !message.contains("§")){
            client.sendLOCALText(message, gameHash);
        }
    }
}
