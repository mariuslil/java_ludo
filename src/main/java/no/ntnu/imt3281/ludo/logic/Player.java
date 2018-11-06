package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected String name;
    protected boolean state; //active or inactive
    protected List<Piece> pieces = new ArrayList<>();
    protected int throwAttempts = 0;
    protected int colour;

    public Player(String name){
        this.name = name;
        this.state = true;
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
}
