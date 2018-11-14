package no.ntnu.imt3281.ludo.logic;


public class DiceEvent {
    private Ludo ludo;
    private int color;
    private int diceNr;

    // TODO : finish this event class
    public DiceEvent(Ludo ludo, int color, int diceNr){
        this.ludo = ludo;
        this.color = color;
        this.diceNr = diceNr;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
