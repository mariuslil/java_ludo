package no.ntnu.imt3281.ludo.logic;

public class PlayerListener {
    private PlayerEvent event;

    public void playerStateChanged(PlayerEvent event){
        this.event = event;
    }
}
