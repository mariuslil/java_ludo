package no.ntnu.imt3281.ludo.server;

import no.ntnu.imt3281.ludo.logic.Ludo;

import java.util.concurrent.ConcurrentHashMap;

public class LudoServer {

    protected Server server;
    protected final ConcurrentHashMap<String, Ludo> games = new ConcurrentHashMap<>(); //games by gamehash


    protected LudoServer(Server server){
        this.server = server;
    }

    protected void newGame(String gameHash){
        games.put(gameHash, new Ludo());
    }

    protected void addPlayerToGame(String gameHash, String player){
        games.get(gameHash).addPlayer(player);
    }

    protected void throwDice(String gameHash, String player){
        if(games.get(gameHash).getPlayerName(games.get(gameHash).activePlayer()).equals(player)){ //only active player can issue this command
            games.get(gameHash).throwDice(); //throw dice
        }
    }

    protected void movePiece(String gameHash, String player, int from, int to){
        if(games.get(gameHash).getPlayerName(games.get(gameHash).activePlayer()).equals(player)){ //only active player can issue this command
            games.get(gameHash).movePiece(games.get(gameHash).activePlayer(), from, to);
        }
    }

}
