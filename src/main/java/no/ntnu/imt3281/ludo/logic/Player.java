package no.ntnu.imt3281.ludo.logic;

public class Player {
    protected String name;
    protected boolean state; //active or inactive

    public Player(String name){
        this.name = name;
        this.state = true;
    }

    public String getName() {
        return name;
    }

    public boolean getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
