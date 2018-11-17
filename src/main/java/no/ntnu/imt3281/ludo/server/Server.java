package no.ntnu.imt3281.ludo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * This is the main class for the server. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Server {

    private static void createDB(){

        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB")) {
            System.out.println("connected");
        } catch (SQLException e) {
            System.out.println("Cannot connect, will create");
            try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB;create=true")) {
                System.out.println("created");
            } catch (SQLException e1) {
                System.out.println("Doesnt work: "+e1.getMessage());
            }
        }



    }

	public static void main(String[] args) {
		createDB();

	}

}
