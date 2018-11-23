package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Ludo is the logic to the ludo game
 */
public class Ludo {

    // Indexes to all the players
    protected static final int RED = 0;
    protected static final int BLUE = 1;
    protected static final int YELLOW = 2;
    protected static final int GREEN = 3;

    // Keep track of the activePlayer
    private int activePlayer = 0;

    // Keep track of lastThrow
    private int lastThrow = 0;

    // Keep track of Ludo status
    private String status;

    // List for all Players
    private List<Player> players = new ArrayList<>();

    // List for all DiceListeners
    private List<DiceListener> diceListeners = new ArrayList<>();

    // List for all PieceListeners
    private List<PieceListener> pieceListeners = new ArrayList<>();

    // List for all PlayerListeners
    private List<PlayerListener> playerListeners = new ArrayList<>();

    /**
     * Constructor without parameters to Ludo
     */
    public Ludo() {
        this.status = "Created";
    }

    /**
     * Constructor with Parameters to Ludo, adds players to players list
     *
     * @param player1 name of player1
     * @param player2 name of player2
     * @param player3 name of player3
     * @param player4 name of player4
     * @throws NotEnoughPlayersException if there isn't enough players
     */
    public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {

        players.add(RED, new Player(player1, RED));
        players.add(BLUE, new Player(player2, BLUE));
        players.add(YELLOW, new Player(player3, YELLOW));
        players.add(GREEN, new Player(player4, GREEN));

        // Minimum two players is needed
        if (this.nrOfPlayers() < 2) {
            throw new NotEnoughPlayersException();
        } else {
            this.status = "Initiated";
        }

    }

    /**
     * Returns number of players in game
     *
     * @return playerCount of players
     */
    public int nrOfPlayers() {
        int playerCount = 0;
        for (Player player : players) {
            if (player.getName() != null) {
                playerCount++;
            }
        }

        return playerCount;
    }

    /**
     * Returns the status of the game
     *
     * @return status to Ludo
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns number of active players
     *
     * @return playerCount of active players
     */
    public int activePlayers() {
        int playerCount = 0;
        for (Player player : players) {
            if (player.getName() != null && player.getState()) {
                playerCount++;
            }
        }

        return playerCount;
    }

    /**
     * Returns the current active player index
     *
     * @return activePlayer player that is active
     */
    public int activePlayer() {
        return this.activePlayer;
    }

    /**
     * Gets the player's name from the color/index
     *
     * @param playerColor index to the player
     * @return name of player or checks if player is inactive or null
     */
    public String getPlayerName(int playerColor) {
        if (nrOfPlayers() - 1 >= playerColor) {
            if (players.get(playerColor).getState()) {
                return players.get(playerColor).getName();
            } else {
                return "Inactive: " + players.get(playerColor).getName();
            }
        }
        return null;
    }

    /**
     * Adds the player to the players list
     *
     * @param name of the player
     */
    public void addPlayer(String name) {
        if (nrOfPlayers() < 4) {
            players.add(new Player(name, nrOfPlayers() + 1));
            this.status = "Initiated";
        } else {
            throw new NoRoomForMorePlayersException();
        }
    }

    /**
     * This function removes the player from the game logic and throws
     * PlayerEvent that user has LEFTGAME
     *
     * @param name to the player
     */
    public void removePlayer(String name) {
        for (Player player : players) {
            if (player.getName() != null && player.getName().equals(name)) {
                player.setState(false);
                for (Piece piece : player.getPieces()) {
                    piece.setPosition(0);
                }

                // PLAYERLISTENER : Trigger playerEvent for player that LEFTGAME
                for (PlayerListener listener : playerListeners) {
                    PlayerEvent event = new PlayerEvent(this, player.getColour(), PlayerEvent.LEFTGAME);
                    listener.playerStateChanged(event);
                }

                if (player.getName().equals(players.get(activePlayer).getName())) {
                    this.setNextActivePlayer();
                }
            }
        }
    }

    /**
     * Gets the player's piece's position
     *
     * @param player index of player
     * @param piece  index of player's piece
     * @return position to the piece
     */
    public int getPosition(int player, int piece) {
        return players.get(player)
                .getPieces()
                .get(piece)
                .getPosition();
    }

    /**
     * throwDice checks how many moves, if any at all, that the player can make.
     * This also controls if the user should throw the dice again. Dicelisteners and Playerlisteners
     * is also thrown in this function
     *
     * @param number random number between 1-6
     * @return number of steps the player actually can move
     */
    public int throwDice(int number) {
        this.lastThrow = number;
        this.status = "Started";
        Player player = players.get(activePlayer);

        // DICELISTENER : throw DiceEvent to diceListeners
        for (DiceListener listener : diceListeners) {
            DiceEvent diceEvent = new DiceEvent(this, this.activePlayer, number);
            listener.diceThrown(diceEvent);
        }


        //check if number thrown was a 6
        if (number != 6) {
            player.setSixersRow(0);
        } else if (!player.inStartingPosition()) { //count up the sixers row if not in starting pos
            player.setSixersRow(player.getSixersRow() + 1);
        }

        if (player.getSixersRow() == 3) { //three 6's in a row, next turn
            // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
            for (PlayerListener listener : playerListeners) {
                PlayerEvent event = new PlayerEvent(this, this.activePlayer, PlayerEvent.WAITING);
                listener.playerStateChanged(event);
            }
            this.setNextActivePlayer();
            return number;
        }

        if (player.inStartingPosition()) { //starting position have 3 throws to get a six
            player.setThrowAttempts(player.getThrowAttempts() + 1);
            if (number != 6 && player.getThrowAttempts() == 3) {
                // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
                for (PlayerListener listener : playerListeners) {
                    PlayerEvent event = new PlayerEvent(this, this.activePlayer, PlayerEvent.WAITING);
                    listener.playerStateChanged(event);
                }
                this.setNextActivePlayer();
                player.setThrowAttempts(0);
                return number;
            } else {
                return number;
            }
        }

        boolean nextTurn = false;
        int piecesInPlay = player.piecesInPlay();
        int blockedPieces = 0;
        int notMakingItInPieces = 0;

        for (Piece piece : player.getPieces()) {
            if (piece.isInPlay()) {
                //count blocked pieces
                if (towersBlocksOpponents(player, piece.position, number)) {
                    blockedPieces++;
                }

                //If piece is at pos over 52 but the thrown dice won't make it 59
                //end of turn
                if (piece.getPosition() > 52 && piece.getPosition() + number != 59 && piece.getPosition() != 59) {
                    notMakingItInPieces++;
                }
            }

        }

        //if all active pieces are blocked, end of turn
        if (blockedPieces == piecesInPlay) {
            nextTurn = true;
            //if all pieces are at endplay, but none can get in, end of turn
        } else if (notMakingItInPieces == piecesInPlay) {
            nextTurn = true;
            //if blocked pieces and notmakingitinpieces are all the pieces in play, end of turn
        } else if ((notMakingItInPieces + blockedPieces) == piecesInPlay) {
            nextTurn = true;
        }

        //set next turn
        if (nextTurn) {
            // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
            for (PlayerListener listener : playerListeners) {
                PlayerEvent event = new PlayerEvent(this, this.activePlayer, PlayerEvent.WAITING);
                listener.playerStateChanged(event);
            }
            this.setNextActivePlayer();
        }

        return number;
    }

    /**
     * Generates a random number between 1 and 6, to simulate a dice
     *
     * @return nr a random number between 1-6
     */
    public int throwDice() {
        //create new random number between 1 and 6
        int nr = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        //throw the number
        throwDice(nr);
        //return number
        return nr;
    }

    /**
     * Adds a PieceListener
     *
     * @param listener added to a list
     */
    public void addPieceListener(PieceListener listener) {
        // Add listener til diceListeners list for moved piece
        this.pieceListeners.add(listener);
    }

    /**
     * Adds a DiceListener
     *
     * @param listener added to a List
     */
    public void addDiceListener(DiceListener listener) {
        // Add listener to diceListeners list for dice thrown
        this.diceListeners.add(listener);
    }

    /**
     * Adds a Playerlistener
     *
     * @param listener added to a List
     */
    public void addPlayerListener(PlayerListener listener) {
        // Add listener to playerListeners list for players
        this.playerListeners.add(listener);
    }

    /**
     * Sets the next active player and throws PlayerEvent about new player that's playing
     */
    public void setNextActivePlayer() {
        this.activePlayer = getNextActivePlayer();
        // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
        for (PlayerListener listener : playerListeners) {
            PlayerEvent event = new PlayerEvent(this, this.activePlayer, PlayerEvent.PLAYING);
            listener.playerStateChanged(event);
        }
    }

    /**
     * Gets the next active player after current active player
     *
     * @return nextActivePlayer if possible, returns -1 if not
     */
    public int getNextActivePlayer() {
        //first check all players after current player
        for (int i = this.activePlayer + 1; i < nrOfPlayers(); i++) {
            if (players.get(i).getState()) {
                return i;
            }
        }

        //then check all players from the beginning
        for (int i = 0; i < nrOfPlayers(); i++) {
            if (players.get(i).getState()) {
                return i;
            }
        }

        return -1; //should never be returned
    }


    /**
     * Moves the player's piece if possible,
     * this also throws piecelistener and playerlistener for Waiting when done moving.
     *
     * @param player index to the player
     * @param from   position to the piece
     * @param to     position to the piece
     * @return returns true if the piece is at the from position
     */
    public boolean movePiece(int player, int from, int to) {

        int piece = players.get(player).getPiece(from);

        // PIECELISTENER : Throw PieceEvent to PieceListeners
        for (PieceListener listener : pieceListeners) {
            PieceEvent pieceEvent = new PieceEvent(this, player, piece, from, to);
            listener.pieceMoved(pieceEvent);
        }

        if (piece != -1) {
            //last throw was a 6, but player is in starting position
            //end of turn
            if (this.lastThrow == 6 && players.get(player).inStartingPosition() && from == 0) {
                // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
                for (PlayerListener listener : playerListeners) {
                    PlayerEvent event = new PlayerEvent(this, players.get(player).getColour(), PlayerEvent.WAITING);
                    listener.playerStateChanged(event);
                }
                this.setNextActivePlayer();
            }
            //player didn't throw a 6
            //end of turn
            if (this.lastThrow != 6) {
                // PLAYERLISTENER : Trigger playerEvent for player that is WAITING
                for (PlayerListener listener : playerListeners) {
                    PlayerEvent event = new PlayerEvent(this, players.get(player).getColour(), PlayerEvent.WAITING);
                    listener.playerStateChanged(event);
                }
                this.setNextActivePlayer();
            }

            //Resets the tower info when a tower is split
            if (players.get(player).getPieces().get(piece).towerPos != -1) {
                players.get(player)
                        .getPieces()
                        .get(piece)
                        .setTower(-1);

                for (Piece piece2 : players.get(player).pieces) {
                    if (piece2.position == from) {
                        piece2.setTower(-1);
                    }
                }
            }

            //Sets tower info when a tower is made
            for (Piece piece2 : players.get(player).pieces) {
                if (piece2.position == to) {
                    //Both pieces become a tower
                    piece2.setTower(piece2.position);
                    players.get(player)
                            .getPieces()
                            .get(piece)
                            .setTower(to);
                }
            }

            //move piece
            players.get(player)
                    .getPieces()
                    .get(piece)
                    .setPosition(to);

            //set piece to in play
            if (to > 0 && to < 59) {
                players.get(player)
                        .getPieces()
                        .get(piece)
                        .setInPlay(true);
            }

            checkIfAnotherPlayerLiesThere(player, to);

            if (to == 59) {

                if (players.get(player).pieceFinished()) {
                    this.status = "Finished";
                    getWinner();
                }
            }

            return true;
        } else {
            //doesn't work
            return false;
        }

    }

    /**
     * Checks if a tower blocks an opponent
     *
     * @param playr  index to the player
     * @param from   position to the players piece
     * @param diceNr the number of the dice
     * @return true if a tower is blocking the player
     */
    public boolean towersBlocksOpponents(Player playr, int from, int diceNr) {

        int tPos;
        int pos = userGridToLudoBoardGrid(playr.getColour(), from);
        for (Player player : players) { //Goes through all other players
            for (Piece piece : player.pieces) {//Checks the relevant pieces
                if (player != playr && player.getName() != null &&
                        piece.position != 0 && piece.towerPos != -1) {
                    // If the piece is in a tower
                    tPos = userGridToLudoBoardGrid(player.getColour(), piece.position);
                    for (int i = pos; i <= pos + diceNr; i++) { //Checks all fields the piece would have to move
                        if (tPos == i) { //Returns if a tower blocks the move
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if another players piece is at a position on the board
     *
     * @param player index to the player
     * @param place  position to the players piece
     */
    private void checkIfAnotherPlayerLiesThere(int player, int place) {
        //get our board position
        int playerPos = userGridToLudoBoardGrid(player, place);

        //iterate all players
        for (int i = 0; i < nrOfPlayers(); i++) {
            //not our player
            if (players.get(i).getName() != players.get(player).getName()) {
                int counter = 0;
                //go through all pieces
                for (Piece piece : players.get(i).getPieces()) {
                    //check if board position is the same
                    if (userGridToLudoBoardGrid(i, piece.getPosition()) == playerPos) {
                        counter++;
                    }
                }
                //if there is only 1 piece there, reset it
                if (counter == 1) {
                    int pieceIndex = 0;
                    for (Piece piece : players.get(i).getPieces()) {
                        if (userGridToLudoBoardGrid(i, piece.getPosition()) == playerPos) {

                            // PIECELISTENER : Throw PieceEvent to PieceListeners
                            for (PieceListener listener : pieceListeners) {
                                PieceEvent pieceEvent = new PieceEvent(this, players.get(i).colour, pieceIndex++, piece.getPosition(), 0);
                                listener.pieceMoved(pieceEvent);
                            }

                            // Reset piece
                            piece.setPosition(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a user has won, and if someone has won and ads a listener for this PlayerEvent
     *
     * @return index to the player that has won or -1 if no one has won
     */
    public int getWinner() {
        for (int i = 0; i < nrOfPlayers(); i++) {
            if (players.get(i).isFinished()) {

                // PLAYERLISTENER : Trigger playerEvent for player that WON
                for (PlayerListener listener : playerListeners) {
                    PlayerEvent event = new PlayerEvent(this, players.get(i).getColour(), PlayerEvent.WON);
                    listener.playerStateChanged(event);
                }
                return i;
            }
        }
        return -1;
    }

    /**
     * Converts the players piece on the players grid board to the game board grid
     *
     * @param color    to the player
     * @param position to the players piece
     * @return position on the game board
     */
    protected int userGridToLudoBoardGrid(int color, int position) {
        // Get base for calculating board position
        int boardPos = (position + 15 + (13 * color));

        // If position is zero, multiply color with 4
        if (position == 0) {
            return (color * 4);

        } else if (position > 53 && position < 60) {
            // If position is 54, add position with 14 and then add 6 times color code,
            // because it's 6 special positions for each color
            return position + 14 + (6 * color);

        } else if (boardPos > 0) {
            // if position is not 0 or 54, calculate board position
            return (boardPos < 67) ? boardPos : (boardPos % 67) + 15;

        } else {
            // If failed to calculate, return -1
            return -1;
        }
    }
}