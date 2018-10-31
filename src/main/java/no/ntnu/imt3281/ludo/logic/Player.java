package no.ntnu.imt3281.ludo.logic;

public class Player {
    protected String name;

    public Player(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
