package no.ntnu.imt3281.ludo.logic;

/**
 * Title:   BadNumberException
 * Desc:    Custom exception class
 */
public class NotEnoughPlayersException extends RuntimeException {

    @Override
    public String getMessage(){
        return "There needs to be atleast 2 players to play Ludo.";
    }

}