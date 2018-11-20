package no.ntnu.imt3281.ludo.logic;

public class PieceListener {
    private PieceEvent event;

    public void pieceMoved(PieceEvent event) {
        this.event = event;
    }
}
