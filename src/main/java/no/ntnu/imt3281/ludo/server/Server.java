package no.ntnu.imt3281.ludo.server;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

/**
 * 
 * This is the main class for the server. 
 * **Note, change this to extend other classes if desired.**
 * 
 * @author 
 *
 */
public class Server extends Application {

    private Connection dbCon = null;

    @FXML
    private Text connected;

    @FXML
    private Text connected1;

    @FXML
    private Text connected2;

    @FXML
    private Text connected3;

    @Override
    public void start(Stage primaryStage) throws Exception {


        try {
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("./Server.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            dbCon = connectDB();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

	public static void main(String[] args) {
        launch(args);
        System.exit(0);
	}

    @FXML
    void initialize() {
        connected.setText("");
        connected1.setText("");
        connected2.setText("");
        connected3.setText("");
    }

    private Connection connectDB(){
        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB")) {
            System.out.println("Database connected");
            return connect;
        } catch (SQLException e) {
            System.out.println("Cannot connect to database, will create");
            return setupDB();
        }
    }

    private Connection setupDB(){
        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB;create=true")) {
            System.out.println("Database created");
            String sql = "CREATE TABLE USERS\n" +
                    "    (ID INT PRIMARY KEY,\n" +
                    "    NAME VARCHAR(24) NOT NULL)";
            Statement stmnt = connect.createStatement();
            stmnt.execute(sql);
            System.out.println("User Table created");

            //insert mock data
            String sql1 = "INSERT INTO USERS VALUES (1,'Johan Aanesen'), (2, 'Brede')";
            Statement stmnt1 = connect.createStatement();
            int rows = stmnt1.executeUpdate(sql1);

            System.out.println("Rows inserted: "+rows);

            return connect;
        } catch (SQLException e1) {
            System.out.println("Something is wrong with the database: "+e1.getMessage());
            return null;
        }
    }

    @FXML
    private void getData(){
        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB;create=true")) {
            String sql2 = "SELECT * FROM USERS";
            Statement stmnt2 = connect.createStatement();
            ResultSet res = stmnt2.executeQuery(sql2);
            ResultSetMetaData meta = res.getMetaData();

            System.out.println(meta.getColumnCount());

            if (res.next()) {
                //System.out.println("TEST: "+res.getString(2));
                if (connected != null) {

                    connected.setText("" + res.getString(2));
                }


            }
        }catch (SQLException e){
            //poop
        }
    }
}
