package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.gui.LudoController;
import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.PieceEvent;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;

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
	protected List<String> activeGames = new ArrayList<>();
	private boolean connected = false;
	private boolean loggedIn = false;
	private boolean lookingForGame = false;
	private int PORT = 1234;
	private Connection connection;
	ExecutorService executor = Executors.newFixedThreadPool(1);

	public boolean test = false; //TEMP VAR TO SEE IF DICE EVENT WORKS
	public String test2 = "";

	private String cookie = "";

	private LudoController ludoController = null;

	public Client(){

	}

	public Client(LudoController ludoController){
		this.ludoController = ludoController;
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
							//JOIN//
						if(tmp != null && tmp.startsWith("JOIN:")){
							//todo: handle join
							System.out.println("CLIENT:"+name.toUpperCase()+":LOGGED_ON: "+tmp.replace("JOIN:", ""));

							//COOKIE//
						}else if(tmp != null && tmp.startsWith("COOKIE:")){
							//todo: STORE COOKIE LOCALLY
							System.out.println("CLIENT:"+name.toUpperCase()+":COOKIE_RECEIVED: "+tmp.replace("COOKIE:", ""));
							this.cookie = tmp.replace("COOKIE:",""); //set cookie
							this.loggedIn = true; //Client is logged in :)
							if(ludoController!=null) {
								ludoController.removeOpenDialog();
							}

							//GLOBAL MESSAGE//
						}else if(tmp != null && tmp.startsWith("GLOBALMSG:")){
							// TODO : Handle message
							System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_MESSAGE: "+tmp.replace("GLOBALMSG:",""));
							messages.add(tmp);

							// This is so the sendMessageToClient test won't fail
							if(ludoController != null){
								// Show message in GUI
								String message = tmp.replace("GLOBALMSG:", "");
								String[] messageInfo = message.split("§");
								if(messageInfo.length == 2){
									ludoController.setMessageInGlobalTextBox(messageInfo[0], messageInfo[1]);
								}
							}

							//GAME MESSAGE//
						}else if(tmp != null && tmp.startsWith("GAMEMSG:")){
							// TODO : Handle local message
							System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_MESSAGE: "+tmp.replace("GAMEMSG:", ""));
							messages.add(tmp);

							//EVENT//
						} else if(tmp != null && tmp.startsWith("EVENT:")){
							//todo: handle event
							String event = tmp.replace("EVENT:", ""); //remove EVENT: from string

								//DICE//
							if(event.startsWith("DICE:")){ //dice event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_DICE_EVENT: "+event.replace("DICE:", ""));

								this.test = true;
								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 4){
									ludoController.receiveDiceEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]));
								}

								//PIECE//
							}else if(event.startsWith("PIECE:")){ //piece event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_PIECE_EVENT: "+event.replace("PIECE:", ""));

								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 4){
									ludoController.receivePlayerEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]));
								}

								//PLAYER//
							}else if(event.startsWith("PLAYER:")){ //player event
								System.out.println("CLIENT:"+name.toUpperCase()+":RECEIVED_PLAYER_EVENT: "+event.replace("PLAYER:", ""));

								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 6){
									ludoController.receivePieceEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]), Integer.parseInt(payload[4]), Integer.parseInt(payload[5]));
								}
							}

							//DISCONNECTED//
						}else if(tmp != null && tmp.startsWith("DISCONNECTED:")){
							//todo: handle disconnect
							//remove disconnected user from thing

							//LOGINERROR//
						}else if(tmp != null && tmp.startsWith("LOGINERROR:")){
							System.out.println("CLIENT:"+name.toUpperCase()+":LOGINERROR: "+tmp.replace("LOGINERROR:", ""));
							this.connected = false;
							connection.close();
							this.loggedIn = false;
							System.out.println("CLIENT:"+name.toUpperCase()+": Disconnected from server.");

							//NEWGAME//
						}else if(tmp != null && tmp.startsWith("STARTGAME:")){
							System.out.println("CLIENT:"+name.toUpperCase()+":STARTGAME: "+tmp.replace("STARTGAME:", ""));
							String game = tmp.replace("STARTGAME:","");
							this.test2 = game;
							if(this.lookingForGame){
								activeGames.add(game);
								if(ludoController!=null) {
									ludoController.startNewGame(game);
									ludoController.removeOpenDialog();
								}
							}
						}else if(tmp != null && tmp.startsWith("RANDOMGAMEREQUESTUPDATE:")){
							String update = tmp.replace("RANDOMGAMEREQUESTUPDATE:", "");
							if(ludoController!=null){
								ludoController.updateWaitDialog(update);
							}
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

	public void sendDiceEvent(String gameHash){
		if (connected && loggedIn){
			try{
				connection.send("EVENT:DICE:"+gameHash);
			}catch (IOException e){
				connection.close();
			}
		}
	}


	public void sendGLOBALText(String message){
		if (connected  && loggedIn){
			try{
				connection.send("GLOBALMSG:"+message);
			}catch (IOException e){
				connection.close();
			}
		}
	}

	public void sendLOCALText(String message, String ludoID){
		if(connected && loggedIn){
			try{
				connection.send("GAMEMSG:"+message);
			} catch (IOException e){
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

	public void requestNewGame(){
		if (connected  && loggedIn){
			try{
				connection.send("JOINRANDOMGAME");
				this.lookingForGame = true;
				if(ludoController!=null){
					ludoController.startWaitForGame();
				}
			}catch (IOException e){
				connection.close();
			}
		}

	}

	public void request(String request){
		if (connected  && loggedIn){
			try{
				connection.send(request);
			}catch (IOException e){
				connection.close();
			}
		}
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getName() {
		return name;
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
