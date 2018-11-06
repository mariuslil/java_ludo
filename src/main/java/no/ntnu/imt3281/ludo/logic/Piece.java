package no.ntnu.imt3281.ludo.logic;

public class Piece {
    protected int position;
    protected int towerPos;

    public Piece(){
        this.position = 0;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTower (int pos) { this.towerPos = pos;}
}
