package no.ntnu.imt3281.ludo.logic;

public class PieceEvent {
    private Ludo ludo;
    private int color;
    private int pieceNr;
    private int fromPos;
    private int toPos;

    public PieceEvent(Ludo ludo, int color, int pieceNr, int fromPos, int toPos) {
        this.ludo = ludo;
        this.color = color;
        this.pieceNr = pieceNr;
        this.fromPos = fromPos;
        this.toPos = toPos;
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
        if (!(obj instanceof PieceEvent))
            return false;

        // Cast to PieceEvent object and check if all values equals to obj
        PieceEvent event = (PieceEvent) obj;
        return (event.ludo == ludo) && (event.color == color) && (event.pieceNr == pieceNr) &&
                (event.fromPos == fromPos) && (event.toPos == toPos);
    }
}
