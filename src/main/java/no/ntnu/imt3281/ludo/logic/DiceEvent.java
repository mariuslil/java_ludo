package no.ntnu.imt3281.ludo.logic;


public class DiceEvent {
    private Ludo ludo;
    private int color;
    private int diceNr;

    /**
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
     * https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method
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

    public int getColor() {
        return color;
    }

    public int getDiceNr() {
        return diceNr;
    }

    public int getLudoHash() {
        return ludo.hashCode();
    }
}
