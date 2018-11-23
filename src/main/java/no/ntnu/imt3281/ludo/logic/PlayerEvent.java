package no.ntnu.imt3281.ludo.logic;

/**
 * PlayerEvent is triggered when a player is waiting, playing, has won or left the game
 */
public class PlayerEvent {
    protected static final int WAITING = 100;
    protected static final int PLAYING = 200;
    protected static final int WON = 300;
    protected static final int LEFTGAME = 400;

    private Ludo ludo;
    private int color;
    private int status;

    /**
     * Constructor for the PlayerEvent
     *
     * @param ludo   object to the event
     * @param color  to the player
     * @param status of the player
     */
    public PlayerEvent(Ludo ludo, int color, int status) {
        this.ludo = ludo;
        this.color = color;
        if (status == WAITING || status == PLAYING || status == WON || status == LEFTGAME) {
            this.status = status;
        }
    }

    /**
     * Checks if the object is equal to this object
     * Code inspired by:
     * https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method
     *
     * @param obj is the object to be compared
     * @return true if the object is equal
     */
    @Override
    public boolean equals(Object obj) {
        // Return true if object equals to this object
        if (obj == this)
            return true;

        // Return false if obj is not an instance of DiceEvent
        if (!(obj instanceof PlayerEvent))
            return false;

        // Cast to PlayerEvent object and check if all values equals to obj
        PlayerEvent event = (PlayerEvent) obj;
        return (event.ludo == ludo) && (event.color == color) && (event.status == status);
    }

    /**
     * Returns the hashCode
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        /* ... */
        return 69;
    }

    /**
     * Returns the color to the player
     *
     * @return color to the player
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns The status to the player
     *
     * @return status of the player
     */
    public int getStatus() {
        return status;
    }

}
