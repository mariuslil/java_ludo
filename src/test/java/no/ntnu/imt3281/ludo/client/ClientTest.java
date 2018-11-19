package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.logic.ListenerAndEvents.DiceEvent;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
        client1.connect("Johan");
        client2.connect("Brede");
    }

    @After
    public void tearDown(){
        this.server.killServer();


    }

    @Test
    public void connectingToServer() {
        assertTrue(server.playerExistInServer("Johan"));
        assertTrue(server.playerExistInServer("Brede"));
    }

    @Test
    public void sendMessageToClient(){

        String message = "Testing123";

        client1.sendText(message);

        try{
            sleep(500); //wait 500ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        assertEquals("MSG:"+message+"§Johan", client2.messages.get(0));
    }

    @Test
    public void testSendDiceEvent(){
        DiceEvent diceEvent = new DiceEvent(new Ludo(), 1, 6); //mock diceEvent

        client1.sendDiceEvent(diceEvent);
        try{
            sleep(500); //wait 500ms to let the message go through the system.
        }catch (InterruptedException e){

        }

        //TODO: assert event came through correctly, in the mean time check log :)
    }
}