package no.ntnu.imt3281.ludo.client;

import no.ntnu.imt3281.ludo.server.Server;
import org.junit.Before;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class ClientTest {


    @Test
    public void connectingToServer() {
        int port = 1234;
        Server server = new Server(port);

        Client client1 = new Client(port);
        Client client2 = new Client(port);

        client1.connect("Johan");
        client2.connect("Brede");

        assertTrue(server.playerExistInServer("Johan"));
        assertTrue(server.playerExistInServer("Brede"));


    }

    @Test
    public void sendMessageToClient(){
        int port = 1235;
        Server server = new Server(port);

        Client client1 = new Client(port);
        Client client2 = new Client(port);

        client1.connect("Johan");
        client2.connect("Brede");

        String message = "Testing123";

        client1.sendText(message);

        try{
            sleep(500);
        }catch (InterruptedException e){

        }

        assertEquals("MSG:"+message+"&§&Johan", client2.messages.get(0));
    }
}