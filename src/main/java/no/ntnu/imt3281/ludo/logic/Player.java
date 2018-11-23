package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Player is the object for a player
 */
public class Player {
    protected String name;
    protected boolean state; //active or inactive
    protected List<Piece> pieces = new ArrayList<>();
    protected int throwAttempts = 0;
    protected int colour;
    protected int sixersRow = 0;
    protected int piecesFinished = 0;

    /**
     * Constructor for Player
     *
     * @param name   of the player
     * @param colour to the player
     */
    public Player(String name, int colour) {
        this.name = name;
        this.state = true;
        this.colour = colour;
        pieces.add(new Piece());
        pieces.add(new Piece());
        pieces.add(new Piece());
        pieces.add(new Piece());
    }

    /**
     * Returns the player name
     *
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the state if the player is active or not
     *
     * @return state to the player
     */
    public boolean getState() {
        return state;
    }

    /**
     * Sets the name for the player
     *
     * @param name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the state to the player active/inactive
     *
     * @param state to the player
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Returns a list of the pieces to the player
     *
     * @return pieces is a list of all pieces
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * Gets the piece at a position
     *
     * @param position to the piece
     * @return index to the piece
     */
    public int getPiece(int position) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getPosition() == position) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Check if user has atleast one piece in starting position
     *
     * @return true if a piece is in starting position
     */
    public boolean inStartingPosition() {
        for (Piece piece : pieces) {
            if (piece.getPosition() != 0 && piece.getPosition() != 59) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks how many pieces that is still in play
     *
     * @return count of pieces in play
     */
    public int piecesInPlay() {
        int count = 0;
        for (Piece piece : this.pieces) {
            if (piece.isInPlay()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Returns how many throw attempts to the player
     *
     * @return throwAttempts to the player
     */
    public int getThrowAttempts() {
        return throwAttempts;
    }

    /**
     * Sets the player attempts to the player
     *
     * @param throwAttempts to the playerS
     */
    public void setThrowAttempts(int throwAttempts) {
        this.throwAttempts = throwAttempts;
    }


    /**
     * Sets the color to the player
     *
     * @param colour to the player
     */
    public void setColour(int colour) {
        this.colour = colour;
    }

    /**
     * Returns the color to the player
     *
     * @return color to the player
     */
    public int getColour() {
        return colour;
    }

    /**
     * Returns count of how many times a player got diceNr 6 in a row
     *
     * @return sixersRow to the player
     */
    public int getSixersRow() {
        return sixersRow;
    }

    /**
     * Sets the time of how many times a player got diceNr 6 in a row
     *
     * @param sixersRow to the player
     */
    public void setSixersRow(int sixersRow) {
        this.sixersRow = sixersRow;
    }

    /**
     * Returns if all the pieces to the player is finished
     *
     * @return true if all pieces is finished
     */
    public boolean pieceFinished() {
        for (Piece piece : pieces) {
            if (piece.getPosition() == 59) {
                piece.setInPlay(false);
            }
        }
        piecesFinished++;
        if (piecesFinished == 4) {
            return true;
        }
        return false;
    }

    /**
     * Returns if all the pieces is finished
     *
     * @return true if all pieces is finished
     */
    public boolean isFinished() {
        return (piecesFinished == 4);
    }

}
