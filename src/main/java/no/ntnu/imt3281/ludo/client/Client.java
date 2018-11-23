package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.gui.LudoController;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * This is the main class for the client.
 * Note, change this to extend other classes if desired.
 */
public class Client {

    private final Logger LOGGER = Logger.getLogger("Chat client");

	protected String name = "";
	protected List<String> messages = new ArrayList<>();
	protected List<String> activeGames = new ArrayList<>();
	protected List<String> activeChats = new ArrayList<>();
	private boolean connected = false;
	private boolean loggedIn = false;
	private boolean lookingForGame = false;
	private int port = 1234;
	private Connection connection;
	ExecutorService executor = Executors.newFixedThreadPool(1);

	//variable used for testing
	public boolean test = false; //TEMP VAR TO SEE IF DICE EVENT WORKS
    //variable used for testing
	public String test2 = "";

	private String cookie;

	private LudoController ludoController = null;

    /**
     * empty constructor
     */
	public Client(){

	}

    /**
     * Client constructor
     * Takes a ludocontroller to control the GUI
     * @param ludoController LudoController
     */
	public Client(LudoController ludoController){
		this.ludoController = ludoController;

		//this.cookie = readCookie();


		if(this.cookie != null){
			try {
				LOGGER.info("CLIENT: "+this.cookie+" Connecting to server.");
				connection = new Connection();			// Connect to the server
				connected = true; //connected but not logged in
				connection.send("SESSION:"+this.cookie); 	// Send cookie
				
				executor.execute(()->listen());			// Starts a new thread, listening to messages from the server
			} catch (UnknownHostException e) {
				// Ignored, means no connection (should have informed the user.)
                LOGGER.info(e.getMessage());
			} catch (IOException e2) {
				// Ignored, means no connection (should have informed the user.)
                LOGGER.info(e2.getMessage()+"");
			}
		}

		this.activeChats.add("Global");
	}

    /**
     * Connect
     * Connects user to server
     * @param type LOGIN|REGISTER|SESSION
     * @param username username
     * @param password password
     */
	public void connect(String type, String username, String password) {
		if (connected) {				// Currently connected, disconnect from server
			connected = false;
			connection.close();			// This will send a message to the server notifying the server about this client leaving
			loggedIn = false;
		} else {
			try {
                LOGGER.info("CLIENT: "+username+" Connecting to server.");
				connection = new Connection();			// Connect to the server
				connected = true; //connected but not logged in
				connection.send(type+username+"§"+password); 	// Send username&password
				this.name = username;
				executor.execute(()->listen());			// Starts a new thread, listening to messages from the server
			} catch (UnknownHostException e) {
                // Ignored, means no connection (should have informed the user.)
                LOGGER.info(e.getMessage());
            } catch (IOException e2) {
                // Ignored, means no connection (should have informed the user.)
                LOGGER.info(e2.getMessage()+"");
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
							String newUser = tmp.replace("JOIN:", "");
                            LOGGER.info("CLIENT:"+name.toUpperCase()+":LOGGED_ON: "+newUser);

							// Send message in Global chat that a new user has logged in and if anyone has logged in before him
							// then show them too
							if(ludoController != null){
								ludoController.addPlayerToChat("Global", newUser);
								ludoController.setMessageInGlobalTextBox("JOINED", newUser);
							}
							//COOKIE//
						}else if(tmp != null && tmp.startsWith("COOKIE:")){
							//todo: STORE COOKIE LOCALLY
                            LOGGER.info("CLIENT:"+name.toUpperCase()+":COOKIE_RECEIVED: "+tmp.replace("COOKIE:", ""));

							String[] payload = tmp.replace("COOKIE:","").split("§");
							if(payload.length==2){
								this.name = payload[0];
								this.cookie = payload[1]; //set cookie
								saveCookie(this.cookie);
								this.loggedIn = true; //Client is logged in :)
								if(ludoController!=null) {
									ludoController.removeOpenDialog();
								}
							}

							//GLOBAL MESSAGE//
						}else if(tmp != null && tmp.startsWith("GLOBALMSG:")){
                            LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_GLOBAL_MESSAGE: "+tmp.replace("GLOBALMSG:",""));
							messages.add(tmp);

							// This is so the sendMessageToClient test won't fail
							if(ludoController != null){
								// Show message in GUI
								String message = tmp.replace("GLOBALMSG:", "");
								String[] messageInfo = message.split("§");

								// 0: userName, 1: message
								if(messageInfo.length == 2){
									ludoController.setMessageInGlobalTextBox(messageInfo[0], messageInfo[1]);
								}
							}

							//GAME MESSAGE//
						}else if(tmp != null && tmp.startsWith("GAMEMSG:")){
							String message = tmp.replace("GAMEMSG:", "");
							String[] messageInfo = message.split("§");
							if(activeGames.size() > 0 && activeGames.contains(messageInfo[0])){
                                LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_CHAT_MESSAGE: "+tmp.replace("GAMEMSG:", ""));
								messages.add(tmp);

								if(ludoController != null){
									// 0: gameID, 1: userName, 2: message
									if(messageInfo.length == 3) {
										ludoController.setMessageInLocalTextBox(messageInfo[0], messageInfo[1], messageInfo[2]);
									}
								}
							}


							//EVENT//
						} else if(tmp != null && tmp.startsWith("EVENT:")){
							//todo: handle event
							String event = tmp.replace("EVENT:", ""); //remove EVENT: from string

								//DICE//
							if(event.startsWith("DICE:")){ //dice event
                                LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_DICE_EVENT: "+event.replace("DICE:", ""));

								this.test = true;
								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 4){
									ludoController.receiveDiceEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]));
								}

								//PIECE//
							}else if(event.startsWith("PLAYER:")){ //player event
								LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_PIECE_EVENT: "+event.replace("PIECE:", ""));

								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 4){
									ludoController.receivePlayerEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]));
								}

								//PLAYER//
							}else if(event.startsWith("PIECE:")){ //PIECE event
								LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_PLAYER_EVENT: "+event.replace("PLAYER:", ""));

								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 6){
									ludoController.receivePieceEvent(payload[1], Integer.parseInt(payload[2]),  Integer.parseInt(payload[3]), Integer.parseInt(payload[4]), Integer.parseInt(payload[5]));
								}

								//JOIN//
							}else if(event.startsWith("JOIN:")){ //join game event
								LOGGER.info("CLIENT:"+name.toUpperCase()+":RECEIVED_JOIN_EVENT: "+event.replace("PLAYER:", ""));
								String[] payload = tmp.split("§");
								if(ludoController!=null && payload.length == 4){
									ludoController.receiveJoinEvent(payload[1], payload[2],  Integer.parseInt(payload[3]));
									// Send message in local game chat that a user has joined
									ludoController.setMessageInLocalTextBox(payload[1], "JOINED", payload[2]);
								}
							}

							//DISCONNECTED//
						}else if(tmp != null && tmp.startsWith("DISCONNECTED:")){ // Disconnected from application
							//todo: handle disconnect
							String discUser = tmp.replace("DISCONNECTED:", "");

							if(ludoController != null){
								// Send message to GLOBAL that user is leaving
								ludoController.setMessageInGlobalTextBox("LEFT", discUser);
								for(String chat: activeChats){
									ludoController.removeUserFromAllChats(chat, discUser);
								}
							}

							//LOGINERROR//
						}else if(tmp != null && tmp.startsWith("LOGINERROR:")){
							LOGGER.info("CLIENT:"+name.toUpperCase()+":LOGINERROR: "+tmp.replace("LOGINERROR:", ""));
							this.connected = false;
							connection.close();
							this.loggedIn = false;
							LOGGER.info("CLIENT:"+name.toUpperCase()+": Disconnected from server.");

							//NEWGAME//
						}else if(tmp != null && tmp.startsWith("STARTGAME:")){
							LOGGER.info("CLIENT:"+name.toUpperCase()+":STARTGAME: "+tmp.replace("STARTGAME:", ""));
							String game = tmp.replace("STARTGAME:","");
							this.test2 = game;
							if(this.lookingForGame){
								activeGames.add(game);
								if(ludoController!=null) {
									ludoController.startNewGame(game);
									ludoController.removeOpenDialog();
								}
							}
							//RANDOMGAMEREQUESTUPDATE//
						}else if(tmp != null && tmp.startsWith("RANDOMGAMEREQUESTUPDATE:")){
							String update = tmp.replace("RANDOMGAMEREQUESTUPDATE:", "");
							if(ludoController!=null){
								ludoController.updateWaitDialog(update);
							}
							//PING//
						}else if(tmp != null && tmp.equals("PING")){
							sendPing();

							//CHATJOIN//
						}else if(tmp != null && tmp.startsWith("CHATJOIN:")){
							String[] payload = tmp.split("§");
							if(payload.length == 3 && ludoController!=null){
								if(payload[2].equals(this.getName())){
									ludoController.createChat(payload[1]);
									activeChats.add(payload[1]);
								}
								ludoController.addPlayerToChat(payload[1], payload[2]);
								ludoController.sendMessageToChat(payload[1],payload[2]," joined the chatroom.");
							}
							//CHATLEFT//
						}else if(tmp != null && tmp.startsWith("CHATLEFT:")){
							String[] payload = tmp.split("§");
							if(payload.length == 3 && ludoController!=null){

								ludoController.removePlayerFromChat(payload[1], payload[2]);
								ludoController.sendMessageToChat(payload[1], payload[2], " left the chatroom.");
							}
							//CHATMESSAGE//
						}else if(tmp != null && tmp.startsWith("CHATMESSAGE:")){
							String[] payload = tmp.split("§");
							if(payload.length == 4 && ludoController!=null){
								ludoController.sendMessageToChat(payload[1], payload[2], payload[3]);
							}
							//ROOMLIST//
						}else if(tmp != null && tmp.startsWith("ROOMLIST:")){
							String payload = tmp.replace("ROOMLIST:","");
							if(!payload.equals("") && ludoController!=null){
								LOGGER.info("CLINT:RECEIVED_ROOMLIST:ROOM: "+payload);
								ludoController.updateRoomList(payload);
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

	private void sendPing(){
		if (connected && loggedIn){
			try{
				connection.send("PING");
			}catch (IOException e){
			    LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * sendDiceEvent
     * sends dice event to server
     * @param gameHash Unique game identifier
     */
	public void sendDiceEvent(String gameHash){
		if (connected && loggedIn){
			try{
				connection.send("EVENT:DICE:§"+gameHash);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * sendMoveEvent
     * sends move event to server
     * @param gameHash Unique game identifier
     * @param from from position
     * @param to to position
     */
	public void sendMoveEvent(String gameHash, int from, int to){
		if (connected && loggedIn){
			try{
				connection.send("EVENT:MOVE:§"+gameHash);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * sendGLOBALText
     * send message to global chat
     * @param message message
     */
	public void sendGLOBALText(String message){
		if (connected  && loggedIn){
			try{
				connection.send("GLOBALMSG:"+message);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * sendLOCALText
     * send local text to gamechat
     * @param message message
     * @param gameHash game id
     */
	public void sendLOCALText(String message, String gameHash){
		if(connected && loggedIn){
			try{
				if(activeGames.size() > 0){
					connection.send("GAMEMSG:"+gameHash+ "§" + message);
				}

			} catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * requestNewGame
     * requests new game from server and initializes a waitForGameDialog
     */
	public void requestNewGame(){
		if (connected  && loggedIn){
			try{
				connection.send("JOINRANDOMGAME");
				this.lookingForGame = true;
				if(ludoController!=null){
					ludoController.startWaitForGame();
				}
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}

	}

    /**
     * requestCreateChat
     * requests that a chat should be created form server
     * @param newChatName the created chat should be named newChatName
     */
	public void requestCreateChat(String newChatName){
		if (connected  && loggedIn){
			try{
				connection.send("CHATCREATE:"+newChatName);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * requestJoinChat
     * requests server to join a specific chat
     * @param joinChatName chatroom name
     */
	public void requestJoinChat(String joinChatName){
		if (connected  && loggedIn){
			try{
				connection.send("CHATJOIN:"+joinChatName);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * requestLeaveChat
     * requests server to leave a chat room
     * @param leaveChatName chatroom name
     */
	public void requestLeaveChat(String leaveChatName){
		if (connected  && loggedIn){
			try{
				connection.send("CHATLEAVE:"+leaveChatName);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * requestSendChatMessage
     * requests server to send chat message
     * @param chatName chatroom name
     * @param message message
     */
	public void requestSendChatMessage(String chatName, String message){
		if (connected  && loggedIn){
			try{
				connection.send("CHATMESSAGE:"+chatName+"§"+message);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * leaveGame
     * leavesgame, makes server notify the rest
     * @param gameHash game id
     */
	public void leaveGame(String gameHash){
		if (connected && loggedIn){
			try{
				connection.send("LEAVEGAME:"+gameHash);
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

    /**
     * requestRoomList
     * request roomlist form server
     */
	public void requestRoomList(){
		if (connected && loggedIn){
			try{
				connection.send("ROOMLIST");
			}catch (IOException e){
                LOGGER.info(e.getMessage());
				connection.close();
			}
		}
	}

	private void saveCookie(String cookie){
		if(cookie != null)
		try{
			List<String> lines = new ArrayList<>();
			lines.add(cookie);
			Path file = Paths.get("cookie.txt");
			Files.write(file, lines, Charset.forName("UTF-8"));
		}catch (IOException e){
			LOGGER.info(e.getMessage());
			connection.close();
		}
	}

	private String readCookie() {

		try {
			byte[] encoded = Files.readAllBytes(Paths.get("cookie.txt"));
			String cookie = new String(encoded, Charset.forName("UTF-8"));
			LOGGER.info("COOKIE: "+cookie);
			return cookie;
		}catch (IOException e){
			LOGGER.info(e.getMessage());
		}

		return null;
	}

    /**
     * isLoggedIn
     * @return true|false
     */
	public boolean isLoggedIn() {
		return loggedIn;
	}

    /**
     * getName
     * @return name
     */
	public String getName() {
		return name;
	}

    /**
     * closeConnection
     * cloeses connection
     */
	public void closeConnection(){
		connection.close();
	}

    /**
     * Connection class
     */
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
			socket = new Socket("localhost", port);
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
