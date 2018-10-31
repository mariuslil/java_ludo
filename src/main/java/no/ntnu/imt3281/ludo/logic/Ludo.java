package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

public class Ludo {

    protected static final int RED = 0;
    protected static final int BLUE = 1;
    protected static final int YELLOW = 2;
    protected static final int GREEN = 3;

    private List<Player> players = new ArrayList<>();

    public Ludo(){

    }

    public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {
        if(player1 != null){
            players.add(RED, new Player(player1));
        }
        if(player2 != null){
            players.add(BLUE, new Player(player2));
        }
        if(player3 != null){
            players.add(YELLOW, new Player(player3));
        }
        if(player4 != null){
            players.add(GREEN, new Player(player4));
        }


        if(this.nrOfPlayers() < 2){
            throw new NotEnoughPlayersException();
        }

    }

    public int nrOfPlayers(){
        return players.size();
    }

    public String getPlayerName(int playerColor){
        if(nrOfPlayers()-1 >= playerColor){
            return players.get(playerColor).getName();
        }

        return null;
    }

}
