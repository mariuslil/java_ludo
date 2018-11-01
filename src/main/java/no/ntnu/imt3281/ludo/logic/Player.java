package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected String name;
    protected boolean state; //active or inactive
    protected List<Piece> pieces = new ArrayList<>();

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

}
