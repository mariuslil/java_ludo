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

    private String status;

    private List<Player> players = new ArrayList<>();

    public Ludo() {
        this.status = "Created";
    }

    public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {

        players.add(RED, new Player(player1));
        players.add(BLUE, new Player(player2));
        players.add(YELLOW, new Player(player3));
        players.add(GREEN, new Player(player4));

        if (this.nrOfPlayers() < 2) {
            throw new NotEnoughPlayersException();
        } else {
            this.status = "Initiated";
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

    public String getStatus() {
        return status;
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
            this.status = "Initiated";
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

    public int throwDice(int number) {
        this.lastThrow = number;
        this.status = "Started";
        Player player = players.get(activePlayer);

        if(number != 6){
            player.setSixersRow(0);
        }else{
            player.setSixersRow(player.getSixersRow()+1);
        }

        if(player.getSixersRow() == 3){
            this.setNextActivePlayer();
            return number;
        }

        if (player.inStartingPosition()) {
            player.setThrowAttempts(player.getThrowAttempts() + 1);
            if (number != 6 && player.getThrowAttempts() == 3) {
                this.setNextActivePlayer();
                player.setThrowAttempts(0);
                return number;
            }
        }

        for (Piece piece : player.getPieces()) {

            if(piece.getPosition() < 53 && piece.getPosition() > 0){
                return number;
            }

            //If piece is at pos over 52 but the thrown dice won't make it 59
            //end of turn
            if(piece.getPosition() > 52 && piece.getPosition()+number != 59 && piece.getPosition() != 59){
                this.setNextActivePlayer();
                return number;
            }
        }

        return number;
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
            if (this.lastThrow == 6 && players.get(player).inStartingPosition() && from == 0) {
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

            if(to == 59){
                boolean finished = players.get(player).pieceFinished();
                if(finished){
                    this.status = "Finished";
                }
            }

            return true;
        } else {
            //doesn't work
            return false;
        }

    }

    public int getWinner() {
        for (int i = 0; i < nrOfPlayers(); i++) {
            if(players.get(i).isFinished()) {
                return i;
            }
        }

        return -1;
    }
}
