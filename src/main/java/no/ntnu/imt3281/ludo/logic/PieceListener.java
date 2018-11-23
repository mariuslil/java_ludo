package no.ntnu.imt3281.ludo.logic;

/**
 * PieceListener is listening for the PieceEvent
 */
public class PieceListener {

    private PieceEvent event;
    private String gameHash;

    /**
     * Constructor for PieceListener
     *
     * @param gameHash for the game
     */
    public PieceListener(String gameHash) {
        this.gameHash = gameHash;
    }

    /**
     * Is called when the piece moved
     *
     * @param event to the Piece
     */
    public void pieceMoved(PieceEvent event) {
        this.event = event;
    }

    /**
     * Returns the gameHash
     *
     * @return gameHash to the PieceListener
     */
    public String getGameHash() {
        return gameHash;
    }
}
