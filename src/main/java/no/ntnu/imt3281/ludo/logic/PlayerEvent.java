package no.ntnu.imt3281.ludo.logic;

public class PlayerEvent {
    protected static final int WAITING = 42;
    protected static final int PLAYING = 69;
    protected static final int WON = 420;
    protected static final int LEFTGAME = 1337;

    private Ludo ludo;
    private int color;
    private int status;

    public PlayerEvent(Ludo ludo, int color, int status){
        this.ludo = ludo;
        this.color = color;
        if(status == WAITING || status == PLAYING || status == WON || status == LEFTGAME){
            this.status = status;
        }
        // TODO : remove this (debugging purpose)
        String player;
        switch (this.color){
            case 0: player = "RED"; break;
            case 1: player = "BLUE"; break;
            case 2: player = "YELLOW"; break;
            case 3: player = "GREEN"; break;
            default: player = "N/A"; break;
        }

        String stringStatus;
        switch (this.status){
            case WAITING: stringStatus = "WAITING"; break;
            case PLAYING: stringStatus = "PLAYING"; break;
            case WON: stringStatus = "WON"; break;
            case LEFTGAME: stringStatus = "LEFTGAME"; break;
            default: stringStatus = "N/A"; break;
        }

        System.out.printf("Player %s's status is %s%n", player, stringStatus);
        // TODO : ends here
    }

    /**
     * https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method
     */
    @Override
    public boolean equals(Object obj) {
        // Return true if object equals to this object
        if (obj == this)
            return true;

        // Return false if obj is not an instance of DiceEvent
        if (!(obj instanceof PlayerEvent))
            return false;

        // Cast to PlayerEvent object and check if all values equals to obj
        PlayerEvent event = (PlayerEvent) obj;
        return (event.ludo == ludo) && (event.color == color) && (event.status == status);
    }
}
