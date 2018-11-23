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
import java.util.concurrent.ConcurrentHashMap;


/**
 * LudoController class, for controlling Ludo.fxml
 */
public class LudoController {

    private Client client = new Client(this);
    private final ConcurrentHashMap<String, GameBoardController> gameControllers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ChatController> chatControllers = new ConcurrentHashMap<>();
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n");
    private ChatRoomsDialog chatRoomsDialog;

    @FXML
    private WaitDialogController waitDialogController;

    @FXML
    private Stage openDialog;

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
    private void initialize() {

        tabbedPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        chatTab.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));

        ChatController chatController = new ChatController(this, resourceBundle.getString("ludo.globalchat"));
        chatControllers.put("Global", chatController);

        loader.setController(chatController);
        loader.setResources(resourceBundle);


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
    void ListRooms(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatRoomsDialog.fxml"));

        this.chatRoomsDialog = new ChatRoomsDialog(this); //create controller that points to this controller
        loader.setController(chatRoomsDialog);                                        //set controller to this custom controller
        loader.setResources(resourceBundle);

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 400);
        this.openDialog = new Stage();
        this.openDialog.initModality(Modality.APPLICATION_MODAL);
        this.openDialog.setTitle(resourceBundle.getString("ludo.listrooms"));
        this.openDialog.setScene(scene);
        this.openDialog.showAndWait();
    }

    /**
     * Sends a room request to the server through the client
     */
    public void sendRoomRequestToServer() {
        client.requestRoomList();
    }

    /**
     * Updates the room list by adding a new room
     *
     * @param roomName to update
     */
    public void updateRoomList(String roomName) {
        this.chatRoomsDialog.addRoom(roomName);
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
        client.closeConnection();
        System.exit(0);
    }

    @FXML
    void connectToServer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));

        LoginController loginController = new LoginController(this); //create controller that points to this controller
        loader.setController(loginController);                                        //set controller to this custom controller
        loader.setResources(resourceBundle);

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 340);
        this.openDialog = new Stage();
        this.openDialog.setTitle(resourceBundle.getString("ludo.logintitle"));
        this.openDialog.initModality(Modality.APPLICATION_MODAL);
        this.openDialog.setScene(scene);
        this.openDialog.showAndWait();
    }

    @FXML
    void requestJoinChat(String chatName) {
        client.requestJoinChat(chatName);
    }

    /**
     * Creates the chat dialog
     *
     * @param chatName name of the new chat
     */
    @FXML
    public void createChat(String chatName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));

        ChatController chatController = new ChatController(this, chatName);
        chatControllers.put(chatName, chatController);

        loader.setController(chatController);
        loader.setResources(resourceBundle);

        try {
            AnchorPane chat = loader.load();
            Tab tab = new Tab(chatName);
            tab.setClosable(true);
            tab.setOnCloseRequest(close -> {
                if (close.getEventType().equals(Tab.TAB_CLOSE_REQUEST_EVENT)) {
                    client.requestLeaveChat(chatName);
                }
            });
            tab.setContent(chat);
            Platform.runLater(() -> chatTab.getTabs().add(tab));
        } catch (IOException el) {
            el.printStackTrace();
        }
    }

    /**
     * Sends a request to create a chat through the client
     *
     * @param chatName of new chatRoom
     */
    @FXML
    public void requestCreateChat(String chatName) {
        client.requestCreateChat(chatName);
    }

    /**
     * Adds a player to a chat
     *
     * @param chatName of the chat the player wants to join
     * @param playerName is the name of the player
     */
    @FXML
    public void addPlayerToChat(String chatName, String playerName) {
        this.chatControllers.get(chatName).addPlayer(playerName);
    }

    /**
     * Sends a message to a chat
     *
     * @param chatName of the chat
     * @param userName of the player
     * @param message is message to be sent
     */
    @FXML
    public void sendMessageToChat(String chatName, String userName, String message) {
        this.chatControllers.get(chatName).setTextInChat(userName, message);
    }

    /**
     * TODO : description here
     *
     * @param chatName
     * @param message
     */
    @FXML
    public void sendMessageToServer(String chatName, String message) {
        this.client.requestSendChatMessage(chatName, message);
    }

    /**
     * Removes the player from the chat
     *
     * @param chatName of the chat to remove the player from
     * @param userName of the player to be removed
     */
    @FXML
    public void removePlayerFromChat(String chatName, String userName) {
        this.chatControllers.get(chatName).removePlayer(userName);
    }

    /**
     * Sends a request to join a new random game through the client
     *
     * @param e auto-created
     */
    @FXML
    public void joinRandomGame(ActionEvent e) {
        client.requestNewGame();
    }

    /**
     * Starts a new game
     *
     * @param gameHash is the gamehash the game is going to have
     */
    @FXML
    public void startNewGame(String gameHash) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameBoard.fxml"));
        loader.setResources(resourceBundle);

        GameBoardController gameBoardController = new GameBoardController(gameHash, this);
        loader.setController(gameBoardController);

        gameControllers.put(gameHash, gameBoardController);

        Platform.runLater(() -> {
            try {
                AnchorPane gameBoard = loader.load();
                Tab tab = new Tab(gameHash);
                tab.setClosable(true);
                tab.setOnCloseRequest(close -> {
                    if (close.getEventType().equals(Tab.TAB_CLOSE_REQUEST_EVENT)) {
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


    /**
     * Starts the WaitDialog dialog and is open until a game is found.
     * It's to show the queue.
     *
     * @throws IOException if it can't load loader
     */
    @FXML
    public void startWaitForGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitDialog.fxml"));
        loader.setResources(resourceBundle);

        WaitDialogController controller = new WaitDialogController();
        loader.setController(controller);
        this.waitDialogController = controller;

        Parent parent = loader.load();

        Scene scene = new Scene(parent, 600, 400);
        this.openDialog = new Stage();
        this.openDialog.setTitle(resourceBundle.getString("ludo.waitforgame"));
        this.openDialog.initModality(Modality.APPLICATION_MODAL);
        this.openDialog.setScene(scene);
        this.openDialog.showAndWait();
    }

    /**
     * Sends a server message to login a user through the client
     *
     * @param username to log in with
     * @param password to log in with
     */
    public void loginUser(String username, String password) {
        System.out.println("CONTROLLER: User: " + username + " is logging in");

        if (username != null && password != null) {
            client.connect("LOGIN:", username, password);
        }
    }

    /**
     * Sends a server message to register a user through the client
     *
     * @param username to register with
     * @param password to register with
     */
    public void registerUser(String username, String password) {
        System.out.println("CONTROLLER: User " + username + " is registering");

        if (username != null && password != null) {
            client.connect("REGISTER:", username, password);
        }

    }

    /**
     * Updates the waitDialog
     *
     * @param update update text
     */
    @FXML
    public void updateWaitDialog(String update) {
        if (waitDialogController != null) {
            Platform.runLater(() -> waitDialogController.updateTextArea(update));
        }
    }

    /**
     * Removes/closes the open dialog
     */
    @FXML
    public void removeOpenDialog() {
        if (openDialog != null) {
            Platform.runLater(() -> openDialog.close());
        }
    }

    /**
     * Sets a message in the global chat
     *
     * @param sender is the name of the user
     * @param message is the actual message
     */
    @FXML
    public void setMessageInGlobalTextBox(String sender, String message) {
        chatControllers.get("Global").setTextInChat(sender, message);
    }

    /**
     * Sets a message in a chat
     *
     * @param gameID gamehash of the chat to set message in
     * @param sender is the name of the user
     * @param message is the actual message
     */
    @FXML
    public void setMessageInLocalTextBox(String gameID, String sender, String message) {
        gameControllers.get(gameID).setTextInChat(sender, message);
    }

    /**
     * TODO : description here
     *
     * @param gameHash
     * @param color
     * @param diceNr
     */
    @FXML
    public void receiveDiceEvent(String gameHash, int color, int diceNr) {
        gameControllers.get(gameHash).runDiceEvent(color, diceNr);
    }

    /**
     * TODO : description here
     *
     * @param gameHash
     * @param color
     * @param status
     */
    @FXML
    public void receivePlayerEvent(String gameHash, int color, int status) {
        gameControllers.get(gameHash).runPlayerEvent(color, status);
    }

    /**
     * TODO : description here
     *
     * @param gameHash
     * @param color
     * @param pieceNr
     * @param fromPos
     * @param toPos
     */
    @FXML
    public void receivePieceEvent(String gameHash, int color, int pieceNr, int fromPos, int toPos) {
        gameControllers.get(gameHash).runPieceEvent(color, pieceNr, fromPos, toPos);
    }

    /**
     * TODO : description here
     *
     * @param gameHash
     * @param username
     * @param color
     */
    @FXML
    public void receiveJoinEvent(String gameHash, String username, int color) {
        gameControllers.get(gameHash).runJoinEvent(username, color);
    }

    /**
     * Sends a diceevent through the client
     *
     * @param gameHash is to know wich game it came from
     */
    public void sendDiceThrowRequest(String gameHash) {
        client.sendDiceEvent(gameHash);
    }

    /**
     * Sends a pieceevent through the client
     *
     * @param gameHash of the game
     * @param from position to the piece
     * @param to position to the piece
     */
    public void sendMovePieceRequest(String gameHash, int from, int to) {
        client.sendMoveEvent(gameHash, from, to);
    }


    /**
     * Sends a messaeg to a chat through the client
     *
     * @param message to be sent
     * @param gameHash to the chat
     */
    public void sendMessageFromLocal(String message, String gameHash) {
        if (message != null && !message.isEmpty() && !message.contains("§")) {
            client.sendLOCALText(message, gameHash);
        }
    }

    /**
     * Removes a user from all chats
     *
     * @param chatName to the game
     * @param userName to the player
     */
    public void removeUserFromAllChats(String chatName, String userName) {
        chatControllers.get(chatName).removePlayer(userName);
    }
}
