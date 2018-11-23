package no.ntnu.imt3281.ludo.logic;


/**
 * NoRoomForMorePlayersException is an exception for when there isn't any more room for players
 */
public class NoRoomForMorePlayersException extends RuntimeException {

    /**
     * Returns the error message
     *
     * @return message about exception
     */
    @Override
    public String getMessage() {
        return "There is already the maximum of 4 players in the game.";
    }
}
