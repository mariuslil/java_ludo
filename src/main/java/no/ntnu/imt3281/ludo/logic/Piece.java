package no.ntnu.imt3281.ludo.logic;

public class Piece {
    protected int position;
    protected int towerPos;
    protected boolean inPlay;

    public Piece(){
        this.position = 0;
        this.inPlay = false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTower (int pos) { this.towerPos = pos;}

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }
}
