package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected String name;
    protected boolean state; //active or inactive
    protected List<Piece> pieces = new ArrayList<>();
    protected int throwAttempts = 0;
    protected int colour;
    protected int sixersRow = 0;
    protected int piecesFinished = 0;

    public Player(String name, int colour){
        this.name = name;
        this.state = true;
        this.colour = colour;
        pieces.add(new Piece());
        pieces.add(new Piece());
        pieces.add(new Piece());
        pieces.add(new Piece());
    }

    public String getName() {
        return name;
    }

    public boolean getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public int getPiece(int position){
        for (int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).getPosition() == position){
                return i;
            }
        }

        return 69;
    }

    public boolean inStartingPosition(){
        for (Piece piece: pieces) {
            if(piece.getPosition() != 0){
                return false;
            }
        }

        return true;
    }


    public int getThrowAttempts() {
        return throwAttempts;
    }

    public void setThrowAttempts(int throwAttempts) {
        this.throwAttempts = throwAttempts;
    }


    public void setColour (int colour) {this.colour = colour;}

    public int getColour () {return colour;}

    public int getSixersRow() {
        return sixersRow;
    }

    public void setSixersRow(int sixersRow) {
        this.sixersRow = sixersRow;
    }

    public boolean pieceFinished(){
        piecesFinished++;
        if(piecesFinished == 4){
            return true;
        }
        return false;
    }

    public boolean isFinished(){
        return (piecesFinished == 4);
    }

}
