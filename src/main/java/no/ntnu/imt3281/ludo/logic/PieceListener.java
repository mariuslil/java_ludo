package no.ntnu.imt3281.ludo.logic;

public class PieceListener {

    private PieceEvent event;
    private String gameHash;

    public PieceListener(String gameHash){
        this.gameHash = gameHash;
    }

    public void pieceMoved(PieceEvent event) {
        this.event = event;
    }

    public String getGameHash() {
        return gameHash;
    }
}
