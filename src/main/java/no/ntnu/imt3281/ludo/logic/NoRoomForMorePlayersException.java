package no.ntnu.imt3281.ludo.logic;

public class NoRoomForMorePlayersException extends RuntimeException {
    @Override
    public String getMessage(){
        return "There is already the maximum of 4 players in the game.";
    }
}
