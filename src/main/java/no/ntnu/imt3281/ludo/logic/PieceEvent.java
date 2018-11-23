package no.ntnu.imt3281.ludo.logic;

/**
 * PieceEvent is triggered when a piece is moved
 */
public class PieceEvent {
    private Ludo ludo;
    private int color;
    private int pieceNr;
    private int fromPos;
    private int toPos;

    /**
     * Constructor for PieceEvent
     *
     * @param ludo    object to piece
     * @param color   of the player
     * @param pieceNr to the active piece
     * @param fromPos of the piece
     * @param toPos   of the piece
     */
    public PieceEvent(Ludo ludo, int color, int pieceNr, int fromPos, int toPos) {
        this.ludo = ludo;
        this.color = color;
        this.pieceNr = pieceNr;
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    /**
     * Checks if the Object obj is equal to this object
     * Code inspired by:
     * https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method
     *
     * @param obj to be compared
     * @return true if the object is equal to this object
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

    /**
     * Returns the hashCode to this PieceEvent
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        /* ... */
        return 69;
    }

    /**
     * Returns the color of the piece
     *
     * @return color of the piece
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns the piece number
     *
     * @return pieceNr of the piece
     */
    public int getPieceNr() {
        return pieceNr;
    }

    /**
     * Returns the from-position to the piece
     *
     * @return fromPos of the piece
     */
    public int getFromPos() {
        return fromPos;
    }

    /**
     * Returns the to-position to the piece
     *
     * @return toPos of the piece
     */
    public int getToPos() {
        return toPos;
    }

}
