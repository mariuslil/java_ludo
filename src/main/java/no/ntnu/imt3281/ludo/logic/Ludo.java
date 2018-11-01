package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ludo {

    protected static final int RED = 0;
    protected static final int BLUE = 1;
    protected static final int YELLOW = 2;
    protected static final int GREEN = 3;

    private int activePlayer = 0;

    private int lastThrow = 0;

    private List<Player> players = new ArrayList<>();

    public Ludo() {

    }

    public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {

        players.add(RED, new Player(player1));
        players.add(BLUE, new Player(player2));
        players.add(YELLOW, new Player(player3));
        players.add(GREEN, new Player(player4));

        if (this.nrOfPlayers() < 2) {
            throw new NotEnoughPlayersException();
        }

    }

    public int nrOfPlayers() {
        int playerCount = 0;
        for (Player player : players) {
            if (player.getName() != null) {
                playerCount++;
            }
        }

        return playerCount;
    }

    public int activePlayers() {
        int playerCount = 0;
        for (Player player : players) {
            if (player.getName() != null && player.getState()) {
                playerCount++;
            }
        }

        return playerCount;
    }

    public int activePlayer() {
        return this.activePlayer;
    }

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

    public void addPlayer(String name) {
        if (nrOfPlayers() < 4) {
            players.add(new Player(name));
        } else {
            throw new NoRoomForMorePlayersException();
        }
    }

    public void removePlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                player.setState(false);
            }
        }
    }

    public int getPosition(int player, int piece) {
        return players.get(player)
                .getPieces()
                .get(piece)
                .getPosition();
    }

    public void throwDice(int number) {
        this.lastThrow = number;
        Player player = players.get(activePlayer);
        if (player.inStartingPosition()) {
            player.setThrowAttempts(player.getThrowAttempts() + 1);
            if (number != 6 && player.getThrowAttempts() == 3) {
                this.setNextActivePlayer();
                player.setThrowAttempts(0);
            }
        }
    }

    public int throwDice() {
        //create new random number between 1 and 6
        int nr = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        //throw the number
        throwDice(nr);
        //return number
        return nr;
    }

    public void setNextActivePlayer() {
        this.activePlayer = getNextActivePlayer();
    }

    public int getNextActivePlayer() {
        if (this.activePlayer == nrOfPlayers() - 1) {
            for (int i = 0; i < nrOfPlayers(); i++) {
                if (players.get(i).getState()) {
                    return i;
                }
            }
        } else {
            for (int i = activePlayer + 1; i < nrOfPlayers(); i++) {
                if (players.get(i).getState()) {
                    return i;
                }
            }
        }

        return 69; //should never be returned
    }


    public boolean movePiece(int player, int from, int to) {

        int piece = players.get(player).getPiece(from);

        if (piece != 69) {
            //last throw was a 6, but player is in starting position
            //end of turn
            if (this.lastThrow == 6 && players.get(player).inStartingPosition()) {
                this.setNextActivePlayer();
            }
            //player didn't throw a 6
            //end of turn
            if (this.lastThrow != 6) {
                this.setNextActivePlayer();
            }

            //move piece
            players.get(player)
                    .getPieces()
                    .get(piece)
                    .setPosition(to);

            return true;
        } else {
            //doesn't work
            return false;
        }

    }

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
