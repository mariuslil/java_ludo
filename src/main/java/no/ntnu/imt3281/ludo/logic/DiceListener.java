package no.ntnu.imt3281.ludo.logic;


public class DiceListener {
    private DiceEvent event;

    public void diceThrown(DiceEvent event) {
        this.event = event;
    }
}
