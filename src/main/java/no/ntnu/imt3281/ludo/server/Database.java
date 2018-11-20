package no.ntnu.imt3281.ludo.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Database {

    private Connection dbCon;

    public Database(){
        this.dbCon = connectDB();
    }

    protected void closeDatabase(){
        try {
            dbCon.close();
        }catch (SQLException e){
            //handle exception.. or don't
        }
    }

    private Connection connectDB(){
        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB")) {
            System.out.println("DATABASE: Database connected");
            return connect;
        } catch (SQLException e) {
            System.out.println("DATABASE: Cannot connect to database, creating..");
            return setupDB();
        }
    }

    private Connection setupDB(){
        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB;create=true")) {
            System.out.println("DATABASE: Database created");
            String sql = "CREATE TABLE USERS\n" +
                    "    (Name VARCHAR(64) NOT NULL PRIMARY KEY ,\n" +
                    "    Password VARCHAR(512) NOT NULL,\n" +
                    "    Cookie VARCHAR(512) NOT NULL,\n" +
                    "    Wins INTEGER)";
            Statement stmnt = connect.createStatement();
            stmnt.execute(sql);
            System.out.println("DATABASE: USERS Table created");

            //insert mock data
           /* String sql1 = "INSERT INTO USERS VALUES (1,'Johan Aanesen', 0), (2, 'Brede', 0)";
            Statement stmnt1 = connect.createStatement();
            int rows = stmnt1.executeUpdate(sql1);

            System.out.println("DATABASE: Rows inserted: "+rows);*/

            return connect;
        } catch (SQLException e1) {
            System.out.println("DATABASE: Something is wrong with the database: "+e1.getMessage());
            return null;
        }
    }

    protected boolean registerUser(String username, String pass){
        String hashPass = "";
        String cookie = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("brede".getBytes());

            byte[] hashedPassword = md.digest(pass.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< hashedPassword.length ;i++){
                sb.append(Integer.toString((hashedPassword[i] & 0xff) + 0x100, 16).substring(1));
            }

            hashPass = sb.toString();

            byte[] cookieHash = md.digest((""+username+pass).getBytes(StandardCharsets.UTF_8));

            StringBuilder sb2 = new StringBuilder();
            for(int i=0; i< cookieHash.length ;i++){
                sb2.append(Integer.toString((cookieHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            cookie = sb2.toString();
        }catch (NoSuchAlgorithmException e){
            //fu
            return false;
        }

        if(hashPass!=""&&cookie!=""){
            try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB")) {
                //insert mock data
                String sql1 = "INSERT INTO USERS (Name, Password, Cookie, Wins) VALUES (?,?,?,?)";
                PreparedStatement stmnt1 = connect.prepareStatement(sql1);
                stmnt1.setString(1, username);
                stmnt1.setString(2, hashPass);
                stmnt1.setString(3, cookie);
                stmnt1.setInt(4, 0);
                int rows = stmnt1.executeUpdate();

                System.out.println("DATABASE: Rows inserted: " + rows);
            }catch (SQLException e){
                //shit bricks
                System.out.println(e.getMessage());
                return false;
            }
        }


        return true;
    }

    protected String loginUser(String username, String pass){

        String hashPass;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("brede".getBytes());

            byte[] hashedPassword = md.digest(pass.getBytes(StandardCharsets.UTF_8));


            StringBuilder sb = new StringBuilder();
            for(int i=0; i< hashedPassword.length ;i++){
                sb.append(Integer.toString((hashedPassword[i] & 0xff) + 0x100, 16).substring(1));
            }

            hashPass = sb.toString();

        }catch (NoSuchAlgorithmException e){
            //fu
            return null;
        }


        try (Connection connect = DriverManager.getConnection("jdbc:derby:LudoDB")) {
            String sql2 = "SELECT NAME, COOKIE FROM USERS WHERE Name LIKE (?) AND Password LIKE (?)";
            PreparedStatement stmnt2 = connect.prepareStatement(sql2);
            stmnt2.setString(1, username);
            stmnt2.setString(2, hashPass);
            ResultSet res = stmnt2.executeQuery();

            if (res.next()){
                return res.getString("Cookie");
            }


        }catch (SQLException e){
            //rip
            System.out.println(e.getMessage());
        }


        return null;
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
