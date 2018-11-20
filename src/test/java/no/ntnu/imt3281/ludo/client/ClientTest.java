package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void sendDiceEvent(){
        DiceEvent diceEvent = new DiceEvent(new Ludo(), 1, 6); //mock diceEvent

        client1.sendDiceEvent(diceEvent);
        try{
            sleep(500); //wait 500ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        assertTrue(client2.test);

        System.out.println("TEST: sendDiceEvent complete");
        //TODO: assert event came through correctly, in the mean time check log :)
    }
}