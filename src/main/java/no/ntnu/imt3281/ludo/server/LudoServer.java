package no.ntnu.imt3281.ludo.server;

import no.ntnu.imt3281.ludo.logic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class LudoServer {

    protected Server server;
    protected final ConcurrentHashMap<String, Ludo> games = new ConcurrentHashMap<>(); //games by gamehash
    protected final ConcurrentHashMap<String, List<String>> players = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Integer> gameSize = new ConcurrentHashMap<>();


    protected LudoServer(Server server){
        this.server = server;
    }

    protected void newGame(String gameHash, int size){
        games.put(gameHash, new Ludo());
        players.put(gameHash, new ArrayList<>());
        gameSize.put(gameHash, size);
        System.out.println("LUDOSERVER: New game created with hash: "+gameHash);

        games.get(gameHash).addDiceListener(new DiceListener(gameHash){
            @Override
            public void diceThrown(DiceEvent event){
                try {
                    System.out.println("LUDOSERVER: Color: " +event.getColor()+" threw a "+event.getDiceNr());
                    server.events.put("EVENT:DICE:§"+this.getGameHash()+"§"+event.getColor()+"§"+event.getDiceNr());
                }catch (InterruptedException e){
                    System.out.println("DICEEVENT:ERROR: "+e.getMessage());
                }
            }
        });
        System.out.println("LUDOSERVER: Created DiceListener for game: "+gameHash);

        games.get(gameHash).addPlayerListener(new PlayerListener(gameHash){
            @Override
            public void playerStateChanged(PlayerEvent event){
                try {
                    server.events.put("EVENT:PLAYER:§"+this.getGameHash()+"§"+event.getColor()+"§"+event.getStatus());
                }catch (InterruptedException e){
                    System.out.println("PLAYEREVENT:ERROR: "+e.getMessage());
                }
            }
        });
        System.out.println("LUDOSERVER: Created PlayerListener for game: "+gameHash);

        games.get(gameHash).addPieceListener(new PieceListener(gameHash){
            @Override
            public void pieceMoved(PieceEvent event){
                try {
                    server.events.put("EVENT:PIECE:§"+this.getGameHash()+"§"+event.getColor()+"§"+event.getPieceNr()+"§"+event.getFromPos()+"§"+event.getToPos());
                }catch (InterruptedException e){
                    System.out.println("PIECEEVENT:ERROR: "+e.getMessage());
                }
            }
        });
        System.out.println("LUDOSERVER: Created PieceListener for game: "+gameHash);
    }

    protected void addPlayerToGame(String gameHash, String player){

        System.out.println("LUDOSERVER: Player "+player+" joined game: "+gameHash);


        players.get(gameHash).add(player);
        games.get(gameHash).addPlayer(player);

        if (players.get(gameHash).size() == gameSize.get(gameHash)) {
            int i = 0;
            for (String client:players.get(gameHash)) {
                try {
                    server.events.put("EVENT:JOIN:§" + gameHash + "§" + client + "§" + players.get(gameHash).indexOf(client));
                }catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }


        }

    }

    protected void removeUserFromGame(String gameHash, String player){
        players.get(gameHash).remove(player);
        games.get(gameHash).removePlayer(player);
    }

    protected void throwDice(String gameHash, String player){
        if(games.get(gameHash).getPlayerName(games.get(gameHash).activePlayer()).equals(player)){ //only active player can issue this command
            games.get(gameHash).throwDice(); //throw dice
        }else{
            System.out.println("LUDOSERVER: A not active player tried to issue DICE event");
        }
    }

    protected void movePiece(String gameHash, String player, int from, int to){
        if(games.get(gameHash).getPlayerName(games.get(gameHash).activePlayer()).equals(player)){ //only active player can issue this command
            games.get(gameHash).movePiece(games.get(gameHash).activePlayer(), from, to);
        }
    }

}
