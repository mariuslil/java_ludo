package no.ntnu.imt3281.ludo.logic;


public class DiceListener {
    private DiceEvent event;
    private String gameHash;


    public DiceListener(String gameHash){
        this.gameHash = gameHash;
    }

    public void diceThrown(DiceEvent event) {
        this.event = event;
    }

    public String getGameHash() {
        return gameHash;
    }
}
