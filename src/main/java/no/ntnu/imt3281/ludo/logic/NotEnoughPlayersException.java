package no.ntnu.imt3281.ludo.logic;

/**
 * Title:   BadNumberException
 * Desc:    Custom exception class for when there aren't enough players
 */
public class NotEnoughPlayersException extends RuntimeException {

    /**
     * Returns error message
     *
     * @return message about exception
     */
    @Override
    public String getMessage(){
        return "There needs to be atleast 2 players to play Ludo.";
    }

}