package no.ntnu.imt3281.ludo.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import no.ntnu.imt3281.ludo.logic.DiceEvent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * This is the main class for the client. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */

public class Client {

	protected String name = "";
	protected List<String> messages = new ArrayList<>();
	private boolean connected = false;
	private boolean loggedIn = false;
	private int PORT = 1234;
	private Connection connection;
	ExecutorService executor = Executors.newFixedThreadPool(1);

	protected boolean test = false; //TEMP VAR TO SEE IF DICE EVENT WORKS

	private String cookie = "";


	public Client(){

	}

	public void connect(String type, String username, String password) {
		if (connected) {				// Currently connected, disconnect from server
			connected = false;
			connection.close();			// This will send a message to the server notifying the server about this client leaving
			loggedIn = false;
		} else {
			try {
				System.out.println("CLIENT: "+username+" Connecting to server.");
				connection = new Connection();			// Connect to the server
				connected = true; //connected but not logged in
				connection.send(type+username+"§"+password); 	// Send username&password
				this.name = username;
				executor.execute(()->listen());			// Starts a new thread, listening to messages from the server
			} catch (UnknownHostException e) {
				// Ignored, means no connection (should have informed the user.)
			} catch (IOException e) {
				// Ignored, means no connection (should have informed the user.)
			}
		}
	}

	// This message will run in a separate thread while the client is connected
	private void listen() {
		while (connected) {		// End when connection is closed
			try {
				if (connection.input.ready()) {		// A line can be read
					String tmp = connection.input.readLine();
					//Platform.runLater(()-> {		// NOTE! IMPORTANT! DO NOT UPDATE THE GUI IN ANY OTHER WAY
						if(tmp.startsWith("JOIN:")){
							//todo: handle join
							System.out.println("CLIENT:"+name.toUpperCase()+":LOGGED_ON: "+tmp.replace("JOIN:", ""));
						}else if(tmp.startsWith("COOKIE:")){
							//todo: STORE COOKIE LOCALLY
							System.out.println("CLIENT:"+name.toUpperCase()+":COOKIE_RECEIVED: "+tmp.replace("COOKIE:", ""));
							this.cookie = tmp.replace("COOKIE:",""); //set cookie
							this.loggedIn = true; //Client is logged in :)
						}else if(tmp.startsWith("MSG:")){
							//todo: handle message
							System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_MESSAGE: "+tmp.replace("MSG:",""));
							messages.add(tmp);
						}else if(tmp.startsWith("EVENT:")){
							//todo: handle event
							String event = tmp.replace("EVENT:", ""); //remove EVENT: from string
							if(event.startsWith("DICE:")){ //dice event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_DICE_EVENT: "+event.replace("DICE:", ""));
								//TODO: handle DICE event
								this.test = true;
							}else if(event.startsWith("PIECE:")){ //piece event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_PIECE_EVENT: "+event.replace("PIECE:", ""));
								//TODO: handle PIECE event
							}else if(event.startsWith("PLAYER:")){ //player event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_PLAYER_EVENT: "+event.replace("PLAYER:", ""));
								//TODO: handle PLAYER event
							}
						}else if(tmp.startsWith("DISCONNECTED:")){
							//todo: handle disconnect
							//remove disconnected user from thing
						}else if(tmp.startsWith("LOGINERROR:")){
							System.out.println("CLIENT:"+name.toUpperCase()+":LOGINERROR: "+tmp.replace("LOGINERROR:", ""));
							this.connected = false;
							connection.close();
							this.loggedIn = false;
							System.out.println("CLIENT:"+name.toUpperCase()+": Disconnected from server.");
						}
					//});
				}
				Thread.sleep(50); 	// Prevent client from using 100% CPU
			} catch (IOException e) {
				// Ignored, should have disconnected
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	protected void sendDiceEvent(DiceEvent diceEvent){
		if (connected && loggedIn){
			try{
				connection.send("EVENT:DICE:§"+diceEvent.getLudoHash()+"§"+diceEvent.getColor()+"§"+diceEvent.getDiceNr());
			}catch (IOException e){
				connection.close();
			}
		}
	}

	protected void sendText(String message){
		if (connected  && loggedIn){
			try{
				connection.send("MSG:"+message);
			}catch (IOException e){
				connection.close();
			}
		}
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	class Connection {
		private final Socket socket;
		private final BufferedReader input;
		private final BufferedWriter output;

		/**
		 * Creates a new connection to the server.
		 *
		 * @throws IOException
		 */
		public Connection() throws IOException {
			socket = new Socket("localhost", PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}

		/**
		 * Sends a message to the server, adds a newline and flushes the bufferedWriter.

		 * @param text the message to send (no newline)
		 * @throws IOException thrown if error during transmission
		 */
		public void send(String text) throws IOException {
			output.write(text);
			output.newLine();
			output.flush();
		}

		/**
		 * Sends a message to the server notifying it about this client
		 * leaving and then closes the connection.
		 */
		public void close() {
			try {
				send("§§BYEBYE§§");
				output.close();
				input.close();
				socket.close();
			} catch (IOException e) {
				// Nothing to do, the connection is closed
			}
		}
	}
}
