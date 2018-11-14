package no.ntnu.imt3281.ludo.logic;


public class DiceEvent {
    private Ludo ludo;
    private int color;
    private int diceNr;

    /**
     * @param ludo Ludo
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
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DiceEvent)) {
            return false;
        }
        DiceEvent event = (DiceEvent) obj;
        return (event.ludo == ludo) && (event.color == color) && (event.diceNr == diceNr);
    }
}
