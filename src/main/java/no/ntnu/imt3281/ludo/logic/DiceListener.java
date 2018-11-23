package no.ntnu.imt3281.ludo.logic;


/**
 * DiceListener is listening for the DiceEvent, this contains DiceEvent object ang gameHash
 */
public class DiceListener {
    private DiceEvent event;
    private String gameHash;


    /**
     * Constructor for DiceListener
     *
     * @param gameHash gameHash for the game
     */
    public DiceListener(String gameHash) {
        this.gameHash = gameHash;
    }

    /**
     * This function is for adding the event to the listener
     *
     * @param event DiceEvent that the listener is waiting for
     */
    public void diceThrown(DiceEvent event) {
        this.event = event;
    }

    /**
     * Returns the gamehash to DiceListener
     *
     * @return gameHash from this DiceListener
     */
    public String getGameHash() {
        return gameHash;
    }
}
