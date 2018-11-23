package no.ntnu.imt3281.ludo.logic;

/**
 * PlayerListener is listening for the PlayerEvent
 */
public class PlayerListener {
    private PlayerEvent event;
    private String gameHash;

    /**
     * Constructor for PlayerListener
     *
     * @param gameHash for the game
     */
    public PlayerListener(String gameHash) {
        this.gameHash = gameHash;
    }

    /**
     * This is adding the event to the listener
     *
     * @param event to the player
     */
    public void playerStateChanged(PlayerEvent event) {
        this.event = event;
    }

    /**
     * Returns the gamehash
     *
     * @return gamehash to the game
     */
    public String getGameHash() {
        return gameHash;
    }
}
