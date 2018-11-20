package no.ntnu.imt3281.ludo.gui;

import java.io.IOException;
import java.util.ResourceBundle;

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
	private WaitDialogController waitDialogController;

	@FXML
	private void initialize(){

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
		loader.setController(loginController);										//set controller to this custom controller
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
		client.requestNewGame();
    }

    @FXML
	public void startNewGame(String gameHash) {
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
	}

	@FXML
	public void startWaitForGame() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitDialog.fxml"));
		loader.setResources(ResourceBundle.getBundle("no.ntnu.imt3281.I18N.i18n"));

		WaitDialogController controller = new WaitDialogController();
		loader.setController(controller);
		this.waitDialogController = controller;

		Parent parent = loader.load();

		Scene scene = new Scene(parent, 600, 400);
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.showAndWait();


	}

    public void loginUser(String username, String password){
		System.out.println("CONTROLLER: User: "+username+" is logging in");

		if(username!=null && password!=null){
			client.connect("LOGIN:", username, password);
		}

		//TODO: close login dialog
	}

	public void registerUser(String username, String password){
		System.out.println("CONTROLLER: User "+username+" is registering");

		if(username!=null && password!=null) {
			client.connect("REGISTER:", username, password);
		}

		//TODO: close login dialog
	}

	public void updateWaitDialog(String update){
		if(waitDialogController!=null){
			waitDialogController.updateTextArea(update);
		}
	}
}
