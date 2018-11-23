package no.ntnu.imt3281.ludo.logic;

/**
 * DiceEvent is triggered when a dice is thrown, this contains the Ludo object, color and diceNr
 */
public class DiceEvent {
    private Ludo ludo;
    private int color;
    private int diceNr;

    /**
     * Constructor for DiceEvent
     *
     * @param ludo   Ludo
     * @param color  int
     * @param diceNr int
     */
    public DiceEvent(Ludo ludo, int color, int diceNr) {
        this.ludo = ludo;
        this.color = color;
        this.diceNr = diceNr;
    }

    /**
     * Checks if the Object that is sent in is equal to this DiceEvent
     * Inspired by this link:
     * https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method
     *
     * @param obj Object to compare with
     * @return true/false if object equals DiceEvent object
     */
    @Override
    public boolean equals(Object obj) {
        // Return true if object equals to this object
        if (obj == this)
            return true;

        // Return false if obj is not an instance of DiceEvent
        if (!(obj instanceof DiceEvent))
            return false;

        // Cast to DiceEvent object and check if all values equals to obj
        DiceEvent event = (DiceEvent) obj;
        return (event.ludo == ludo) && (event.color == color) && (event.diceNr == diceNr);
    }


    /**
     * Returns hash for DiceEvent
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        /* ... */
        return 69;
    }

    /**
     * Returns color from DiceEvent
     *
     * @return color from DiceEvent
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns diceNr from DiceEvent
     *
     * @return diceNr from DiceEvent
     */
    public int getDiceNr() {
        return diceNr;
    }

}
