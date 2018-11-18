package no.ntnu.imt3281.ludo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {


    public Database(){
    }

    protected Connection connectDB(){
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

    /*
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
    }*/
}
