package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ludo {

    protected static final int RED = 0;
    protected static final int BLUE = 1;
    protected static final int YELLOW = 2;
    protected static final int GREEN = 3;

    private int activePlayer = 0;

    private List<Player> players = new ArrayList<>();

    public Ludo(){

    }

    public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {

        players.add(RED, new Player(player1));
        players.add(BLUE, new Player(player2));
        players.add(YELLOW, new Player(player3));
        players.add(GREEN, new Player(player4));

        if(this.nrOfPlayers() < 2){
            throw new NotEnoughPlayersException();
        }

    }

    public int nrOfPlayers(){
        int playerCount = 0;
        for (Player player: players) {
            if(player.getName() != null){
                playerCount++;
            }
        }

        return playerCount;
    }

    public int activePlayers(){
        int playerCount = 0;
        for (Player player: players) {
            if(player.getName() != null && player.getState()){
                playerCount++;
            }
        }

        return playerCount;
    }

    public int activePlayer(){
        return this.activePlayer;
    }

    public String getPlayerName(int playerColor){
        if(nrOfPlayers()-1 >= playerColor){
            if(players.get(playerColor).getState()){
                return players.get(playerColor).getName();
            }else{
                return "Inactive: " + players.get(playerColor).getName();
            }

        }

        return null;
    }

    public void addPlayer(String name) {
        if(nrOfPlayers() < 4) {
            players.add(new Player(name));
        }else{
            throw new NoRoomForMorePlayersException();
        }
    }

    public void removePlayer(String name){
        for (Player player: players) {
            if(player.getName().equals(name)){
                player.setState(false);
            }
        }
    }

    public int getPosition(int player, int piece){
        //dette ble jo jævelig stygt da men
        //eventuelt: players.get(player).pieces.get(piece).position;
        return players.get(player)
                .getPieces()
                .get(piece)
                .getPosition();
    }

    public void throwDice(int number){
        if(getPosition(activePlayer(), 0) == 0){

        }
    }

    public int throwDice(){
        //create new random number between 1 and 6
        int nr = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        //throw the number
        throwDice(nr);
        //return number
        return nr;
    }

/*    public boolean movePiece(int player, int from, int to){


    }*/
}
