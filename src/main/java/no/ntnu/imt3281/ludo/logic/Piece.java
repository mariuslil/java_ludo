package no.ntnu.imt3281.ludo.logic;

/**
 * Piece is a class for a players game piece
 */
public class Piece {
    protected int position;
    protected int towerPos;
    protected boolean inPlay;

    /**
     * Constructor for Piece
     */
    public Piece() {
        this.position = 0;
        this.inPlay = false;
    }

    /**
     * Returns the position to the piece
     *
     * @return position of piece
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position for the piece
     *
     * @param position of the piece
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets a tower
     *
     * @param pos to the tower
     */
    public void setTower(int pos) {
        this.towerPos = pos;
    }

    /**
     * Returns true if this piece is in play
     *
     * @return inPlay of piece
     */
    public boolean isInPlay() {
        return inPlay;
    }

    /**
     * Sets this piece in play
     *
     * @param inPlay of piece
     */
    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }
}
