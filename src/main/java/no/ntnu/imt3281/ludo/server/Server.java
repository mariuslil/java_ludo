package no.ntnu.imt3281.ludo.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * This is the main class for the server. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Server extends Application {

    private Connection dbCon = null;
    private Database database = null;
    private int PORT = 1234;

    private final Logger logger = Logger.getLogger("Chat server");
    private boolean shutdown = false;

    private final LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();


    /**
     * Starts the server, creates the serversocket and the threads
     * listening for incomming connections, messages from clients
     * and the thread that sends messages to all clients.
     */
    public Server() {
        ExecutorService executor = Executors.newCachedThreadPool();
        //executor.execute(()->connectionListenerThread());
        executor.execute(()->connectionListenerThread());	// This thread listens for connections from clients
        executor.execute(()->messageSenderThread());					// This thread listens for messages from clients
        //executor.execute(()->messageListenerThread());					// This thread waits for messages to send to clients, then sends the message(s) to all clients.
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.database = new Database();

        try {
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("./Server.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            //connect/create db
            dbCon = database.connectDB();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

	public static void main(String[] args) {
        launch(args);
        System.exit(0);
	}

    private void connectionListenerThread() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!shutdown) {		// Run until server stopping
                Socket s= null;
                try {
                    s = serverSocket.accept();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while getting client connection: "+PORT, e);
                    System.exit(0);
                }
                try {
                    addPlayer(s);	// Method that gets username and adds client to hashmap
                } catch (IOException e) {
                    logger.log(Level.FINER, "Unable to establish connection with client", e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to listen to port: "+PORT, e);
            System.exit(0);
        }
    }

    private void messageSenderThread() {
        while (!shutdown) {
            try {
                final String message = messages.take();		// Blocks until a message is available
                players.forEachValue(100, player->player.write(message));	// Do in parallel if more than 100 clients
            } catch (InterruptedException e) {
                logger.log(Level.INFO, "Error fetching message from que", e);
                Thread.currentThread().interrupt();
            }
            // Clients that have IO error was marked inactive
            players.forEachValue(100, player-> {	// Remove clients that could not receive message
                if (!player.active&&players.remove(player.getName())!=null) {	// Client is inactive and still existed in the hashmap
                    //clientRemoved(player);
                }
            });
        }
    }

    private void addPlayer(Socket s) throws IOException {
        final Player player = new Player(s);
        players.forEachKey(100, name-> player.write("JOIN:"+name)); // Let the new client know the name of the existing clients
        players.put(player.getName(), player);
        messages.add("JOIN:" +player.getName());

    }

    class Player {
        private String name;
        private Socket s;
        private BufferedReader input;
        private BufferedWriter output;
        private boolean active = true;

        /**
         * Gets a socket and creates reader/writer objects and reads the name of the client/user
         *
         * @param s the socket from the serversocket
         * @throws IOException if unable to create reader/writer objects or read username.
         */
        public Player (Socket s) throws IOException {
            this.s = s;
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            name = input.readLine();
        }

        /**
         * Non blocking read of the input reader.
         * Checks if data is available and if so reads the data.
         *
         * @return the data read from the client or null if no data was available.
         */
        public String read() {
            try {
                if (input.ready()) {
                    return input.readLine();
                } else {
                    return null;
                }
            } catch (IOException e) {
                active = false;	// If unable to read (IO error) mark as inactive, this will remove the client from the server
                return null;
            }
        }

        public void write(String msg) {
            try {
                output.write(msg);
                output.newLine();	// Must send newline for client to be able to read
                output.flush();		// Nothing is sent without flushing
            } catch (IOException e) {
                active = false; // If unable to write (IO error) mark as inactive, this will remove the client from the server
            }
        }

        public boolean getActive() {
            return active;
        }

        public String getName() {
            return name;
        }

        public Socket getSocket() {
            return s;
        }

        /**
         * Marks this client as inactive and closes the connection.
         */
        public void close() {
            try {
                active = false;
                input.close();
                output.close();
                s.close();
            } catch (IOException e) {
                // This connection will be dropped anyway, nothing much to do about it
            }
        }
    }

}
