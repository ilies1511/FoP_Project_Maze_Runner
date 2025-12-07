package de.tum.cit.fop.maze;

public class Player {
    private int health;

    public Player() {
        this.health = 3; //player has x hps
    }

    public int getDamage(int damage){
        if(health - damage < 0){
            health = 0;
            return health;
        }
        health -= damage;
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
