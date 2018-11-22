package no.ntnu.imt3281.ludo.logic;

public class PlayerListener {
    private PlayerEvent event;
    private String gameHash;

    public PlayerListener(String gameHash){
        this.gameHash = gameHash;
    }

    public void playerStateChanged(PlayerEvent event){
        this.event = event;
    }

    public String getGameHash() {
        return gameHash;
    }
}
