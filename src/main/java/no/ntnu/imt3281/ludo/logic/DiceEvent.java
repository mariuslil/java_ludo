package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.List;

public class DiceEvent {
    private Ludo ludo;
    private int color;
    private int diceNr;
    private List<DiceListener> diceListeners;

    // TODO : finish this event class
    public DiceEvent(Ludo ludo, int color, int diceNr){
        this.ludo = ludo;
        this.color = color;
        this.diceNr = diceNr;
        this.diceListeners = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
