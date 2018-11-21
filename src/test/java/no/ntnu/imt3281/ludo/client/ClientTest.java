package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class ClientTest {

    Server server;
    Client client1;
    Client client2;


    @Before
    public void init(){
        this.server = new Server();
        this.client1 = new Client();
        this.client2 = new Client();
        client1.connect("REGISTER:", "Johan", "hei");
        client2.connect("REGISTER:", "Brede", "HEI");
        try{
            sleep(500); //wait 500ms to let the system connect
        }catch (InterruptedException e){

        }
    }

    @After
    public void tearDown(){
        this.server.killServer();
    }

    @Test
    public void connectingToServer() {
        assertTrue(server.playerExistInServer("Johan"));
        assertTrue(server.playerExistInServer("Brede"));
        System.out.println("TEST: connectingToServer complete");
    }

    @Test
    public void loggingInToServer(){
        assertTrue(client1.isLoggedIn());
        assertTrue(client2.isLoggedIn());
        System.out.println("TEST: loggingInToServer complete");
    }

    @Test
    public void sendMessageToClient(){

        String message = "Testing123";

        client1.sendGLOBALText(message);

        try{
            sleep(100); //wait 100ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        assertEquals("GLOBALMSG:Johan§" + message, client2.messages.get(0));
        System.out.println("TEST: sendMessageToClient complete");
    }

    /*@Test
    public void sendDiceEvent(){

        client1.sendDiceEvent(server.);

        try{
            sleep(500); //wait 500ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        assertTrue(client2.test);

        System.out.println("TEST: sendDiceEvent complete");
        //TODO: assert event came through correctly, in the mean time check log :)
    }*/

    @Test
    public void checkIfGameStarts(){
        Client client3 = new Client();
        Client client4 = new Client();
        client3.connect("REGISTER:", "Marius", "hei");
        client4.connect("REGISTER:", "Okolloen", "hei");
        try{
            sleep(500); //wait 500ms to let the system connect
        }catch (InterruptedException e){

        }

        client1.requestNewGame();
        client2.requestNewGame();
        client3.requestNewGame();
        client4.requestNewGame();


        try{
            sleep(3000); //wait 100ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        String gameHash = client1.activeGames.get(0); //get gameHash from client1

        assertTrue(server.playerInGame(client1.name, gameHash));
        assertTrue(server.playerInGame(client2.name, gameHash));
        assertTrue(server.playerInGame(client3.name, gameHash));
        assertTrue(server.playerInGame(client4.name, gameHash));
    }
}