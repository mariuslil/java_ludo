package no.ntnu.imt3281.ludo.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * This is the main class for the server.
 * **Note, change this to extend other classes if desired.**
 *
 * @author
 */
public class Server {

    private Connection dbCon = null;
    private Database database = null;
    private int PORT = 1234;

    private final Logger logger = Logger.getLogger("Chat server");
    protected boolean shutdown = false;

    private final LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> events = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, LinkedBlockingQueue<String>> games = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<Player> wannaGame = new LinkedBlockingQueue<>();

    ConcurrentHashMap<String, Player> waitingPlayers = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    /**
     * Starts the server, creates the serversocket and the threads
     * listening for incomming connections, messages from clients
     * and the thread that sends messages to all clients.
     */
    public Server() {
        this.database = new Database();
        //ExecutorService executor = Executors.newCachedThreadPool();
        //executor.execute(()->connectionListenerThread());
        executor.execute(() -> connectionListenerThread());    // This thread listens for connections from clients
        executor.execute(() -> playerListenerThread());        // This thread waits for messages to send to clients, then sends the message(s) to all clients.
        executor.execute(() -> messageSenderThread());        // This thread listens for messages from clients
        //EVENTSENDERS
        executor.execute(() -> eventSenderThread());      // This thread sends events to the users

        //GAME THREADS
        executor.execute(() -> assignGameThread()); // This thread will add players to new games a
        executor.execute(() -> pingWaitingPlayers());
    }

    public void killServer() {
        this.shutdown = true;
        executor.shutdown();

        database.closeDatabase();
        try {
            serverSocket.close();
        } catch (IOException e) {
            //
        }
    }


    private void connectionListenerThread() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("SERVER: Serversocket created on Port: " + PORT);
            while (!shutdown) {        // Run until server stopping
                Socket s = null;
                try {
                    s = serverSocket.accept(); //accept incoming connection
                    System.out.println("SERVER: Accepted connection");
                    try {
                        addPlayer(s);    // Method that gets username and adds client to hashmap
                    } catch (IOException e) {
                        logger.log(Level.FINER, "Unable to establish connection with client", e);
                    }
                } catch (IOException e) {
                    //logger.log(Level.SEVERE, "Error while getting client connection: "+PORT, e);
                    //System.exit(0);
                    System.out.println("SERVER: Exception thrown when shutting down serversocket\nSERVER: Because serverSocket is shut down from another thread.");
                    //continue; //jump out of callstack to dodge nullpointers when tossing exception //edit: moved thge addplayer trycatch into this try catch
                }

            }

        } catch (IOException e) {
            System.out.println("SERVER: Unable to listen to port: " + PORT);
            logger.log(Level.SEVERE, "Unable to listen to port: " + PORT, e);
            //System.exit(0);
        }
    }

    private void messageSenderThread() {
        while (!shutdown) {
            try {
                final String message = messages.take();        // Blocks until a message is available
                players.forEachValue(100, player -> player.write(message));    // Do in parallel if more than 100 clients

            } catch (InterruptedException e) {
                logger.log(Level.INFO, "Error fetching message from queue", e);
                Thread.currentThread().interrupt();
            }
            // Clients that have IO error was marked inactive
            players.forEachValue(100, player -> {    // Remove clients that could not receive message
                if (!player.active && players.remove(player.getName()) != null) {    // Client is inactive and still existed in the hashmap
                    //clientRemoved(player);
                    //TODO: this
                }
            });
        }
    }

    private void assignGameThread(){
        LinkedBlockingQueue<String> newGame = new LinkedBlockingQueue<>();

        int ticktock = 0;
        while (!shutdown){

            System.out.println("SERVER: TICKTOCK: "+String.valueOf(ticktock));

            try{
                if(wannaGame.size()>0) {
                    Player player = wannaGame.take(); //this fucker

                    System.out.println("SERVER: Player "+player.getName()+" added to game waiting list.");

                    waitingPlayers.put(player.getName(), player);
                    newGame.add(player.getName());

                    waitingPlayers.forEachValue(100, waitingPlayer -> {
                        waitingPlayer.write("RANDOMGAMEREQUESTUPDATE: "+player.getName().toUpperCase()+" added to queue");
                    });
                    ticktock = 0;
                }
            }catch (InterruptedException e){
                //meep
            }

            if(newGame.size() == 4 || (ticktock > 29 && newGame.size() > 1)){
                String uniqID = "pels"; //TODO: make it uniqueid
                games.put(uniqID, newGame);

                System.out.println("SERVER: Starting game: "+uniqID);

                //TODO: do this more efficiently
                waitingPlayers.forEachValue(100, waitingPlayer -> {
                    for (String playerName:newGame) {
                        if (waitingPlayer.getName().equals(playerName)) {
                            waitingPlayer.write("STARTGAME:"+uniqID);
                            waitingPlayer.activeGames.add(uniqID);
                            waitingPlayers.remove(waitingPlayer.name);
                            System.out.println("SERVER: WAITING PLAYERS: "+waitingPlayers.size());
                        }
                    }
                });



                //waitingPlayers.get(newGame.r)

                for (String name:newGame) {
                    newGame.remove(name); //reset newGame
                }

                ticktock = 0;
            }

            try{
                sleep(1000); //wait one second
            }catch (InterruptedException e){

            }
            if(ticktock>50){ticktock=0;}
            ticktock++;
        }
    }

    private void pingWaitingPlayers(){
        while(!shutdown){
            waitingPlayers.forEachValue(100, waitingPlayer -> {
                waitingPlayer.write("RANDOMGAMEREQUESTUPDATE: SERVER: Waiting for players..");
            });
            try{
                sleep(1000); //wait one second
            }catch (InterruptedException e){

            }
        }
    }

    private void playerListenerThread() {
        while (!shutdown) {
            players.forEachValue(100, player -> {
                String msg = player.read();        // Read message from client
                if (msg != null && msg.equals("§§BYEBYE§§")) {    // Client says goodbye

                    if (players.remove(player.getName()) != null) {
                        //todo: clientRemoved(client);		// Let other clients know this client has left, adds message to textarea
                    }

                } else if (msg != null && msg.startsWith("EVENT:")) {
                    events.add(player.getName() + msg);   // Add event to event queue
                }  else if (msg != null && msg.startsWith("GLOBALMSG:")) {
                    messages.add("GLOBALMSG:" + player.getName() + "§" + msg.replace("GLOBALMSG:", ""));    // Add message to message queue
                } else if (msg != null && msg.startsWith("GAMEMSG:")){
                    // TODO : Get ludo-game id
                    messages.add("GAMEMSG:" + "luduID" + "§" + player.getName() + "§" + msg.replace("GAMEMSG:", ""));
                }else if (msg != null && msg.startsWith("JOINRANDOMGAME")) {
                    wannaGame.add(player);
                } // TODO: THIS IS WHERE YOU WANT TO ADD MORE ENDPOINTS FROM CLIENT
            });
            try {
                Thread.sleep(10);    // Prevent excessive processor usage
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void eventSenderThread() {
        while (!shutdown) {
            try {
                final String event = events.take();        // Blocks until an event is available
                String[] eventParts = event.split("§");
                String[] playerName = eventParts[0].split("EVENT:"); //player who triggered this event
                /*
                 * eventParts[0] = EVENT:DICE: or EVENT:PIECE: or EVENT:PLAYER:
                 * eventParts[1] = GAMEHASH/ID
                 * eventParts[2] = Event information
                 */
                //////////////////MOCK GAME UNTIL I IMPLEMNT IT
                //TODO: THIS^
                LinkedBlockingQueue<String> test = new LinkedBlockingQueue<>();
                test.add("Johan");
                test.add("Brede");
                games.put(eventParts[1], test);
                //////////////////
                for (String player : games.get(eventParts[1])) { //get players from the game
                    String payload = event.replace(playerName[0], ""); //remove name from string
                    if (!player.equals("") && !player.equals(playerName[0])) {
                        System.out.println("SERVER: Sending player " + player + " new event.");
                        players.get(player).write(payload);           //send these players the event
                    }
                }

            } catch (InterruptedException e) {
                logger.log(Level.INFO, "Error fetching message from queue", e);
                Thread.currentThread().interrupt();
            }
        }
    }


    private void addPlayer(Socket s) throws IOException {
        final Player player = new Player(s);
        //players.forEachKey(100, name-> player.write("JOIN:"+name)); // Let the new client know the name of the existing clients

        if (player.connectionString.startsWith("REGISTER:")) {
            registerPlayer(player);
        } else {
            loginPlayer(player);
        }
    }

    private void registerPlayer(Player player) {
        if (player.connectionString.startsWith("REGISTER:")) { //payload should be REGISTER:USERNAME§PASSWORD
            String trim = player.connectionString.replace("REGISTER:", ""); //remove REGISTER:
            String[] namePass = trim.split("§");    //split the remaining string into USERNAME and PASSWORD
            if (namePass.length == 2) {   //make sure both username and password is present
                if (database.userExists(namePass[0])) { //if the username already exists
                    System.out.println("SERVER: Username '" + namePass[0] + "' already exists, trying to login instead.");
                    player.connectionString = player.connectionString.replace("REGISTER:", "LOGIN:"); //change REGISTER to LOGIN
                    loginPlayer(player); //then try to login the person instead.
                } else { //username does not exits, try to register user
                    boolean registered = database.registerUser(namePass[0], namePass[1]);
                    if (registered) { //registered successfull
                        System.out.println("SERVER: User " + namePass[0].toUpperCase() + " registered succesfully.");
                        player.connectionString = player.connectionString.replace("REGISTER:", "LOGIN:"); //change REGISTER to LOGIN
                        loginPlayer(player); //login player after registering
                    }
                }
            }
        }
    }

    private void loginPlayer(Player player) {
        if (player.connectionString.startsWith("LOGIN:")) {//Payload should be LOGIN:USERNAME§PASSWORD

            String trim = player.connectionString.replace("LOGIN:", ""); //remove LOGIN: from string
            String[] namePass = trim.split("§"); //split string into USERNAME and PASSWORD
            System.out.println("SERVER: Logging in user: " + namePass[0]);

            if (namePass.length == 2) { //make sure both username and password is present
                String cookie = database.loginUser(namePass[0], namePass[1]); //login user/fetch cookie from db
                if (cookie != null) { //no cookie, not a valid user
                    System.out.println("SERVER: User " + namePass[0].toUpperCase() + " logged in succesfully.");
                    player.setName(namePass[0]);
                    player.write("COOKIE:" + cookie); //send cookie to client for it to keep

                    //send new player name of all logged in players
                    players.forEachValue(100, player1 -> player.write("JOIN:" + player1.getName()));
                    //add player to logged in players list
                    players.put(player.getName(), player);
                    //send all players that this player has logged in.
                    messages.add("JOIN:" + player.getName());
                } else { //failed to log in
                    System.out.println("SERVER: Failed to login user " + namePass[0].toUpperCase());
                    player.write("LOGINERROR: Failed to login user");
                }
            }
        } else if (player.connectionString.startsWith("SESSION:")) { //login user through a session key instead
            System.out.println("SERVER: client trying to connect through session key.");
            //String trim = player.connectionString.replace("SESSION:", "");
            //TODO: THIS -> login client with only the cookie
        } else {
            System.out.println("SERVER: Something wrong validating user.");
        }
    }

    public boolean playerExistInServer(String username) {
        return (players.get(username) != null);
    }

    public boolean playerInGame(String username, String game) {
        return players.get(username).activeGames.contains(game);
    }

    class Player {
        private String name = "";
        private String connectionString;
        private Socket s;
        private BufferedReader input;
        private BufferedWriter output;
        private boolean active = true;
        private List<String> activeGames = new ArrayList<>();

        /**
         * Gets a socket and creates reader/writer objects and reads the name of the client/user
         *
         * @param s the socket from the serversocket
         * @throws IOException if unable to create reader/writer objects or read username.
         */
        public Player(Socket s) throws IOException {
            this.s = s;
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            connectionString = input.readLine();
        }

        /**
         * Non blocking read of the input reader.
         * Checks if data is available and if so reads the data.
         *
         * @return the data read from the client or null if no data was available.
         */
        private String read() {
            try {
                if (input.ready()) {
                    return input.readLine();
                } else {
                    return null;
                }
            } catch (IOException e) {
                active = false;    // If unable to read (IO error) mark as inactive, this will remove the client from the server
                return null;
            }
        }

        private void write(String msg) {
            try {
                output.write(msg);
                output.newLine();    // Must send newline for client to be able to read
                output.flush();        // Nothing is sent without flushing
            } catch (IOException e) {
                active = false; // If unable to write (IO error) mark as inactive, this will remove the client from the server
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean getActive() {
            return active;
        }

        public String getName() {
            return name;
        }

        public String getConnectionString() {
            return connectionString;
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

        public boolean isInThisGame(String game) {
            for (String activeGame : activeGames) {
                if (activeGame.equals(game)) {
                    return true;
                }
            }
            return false;
        }
    }

}
