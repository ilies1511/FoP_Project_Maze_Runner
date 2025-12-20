package de.tum.cit.fop.maze;

public class Player {
    private int health;
    private boolean isPoisoned;
    private boolean isBuffed;
    private float poisonDuration;
    private float buffDuration;

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

    public float getBuffed(float buffDuration){
        isBuffed = true;
        this.buffDuration = buffDuration;
        return buffDuration;
    }

    public float getPoisoned(float poisonDuration){
        isPoisoned = true;
        this.poisonDuration = poisonDuration;
        return poisonDuration;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public float getPoisonDuration() {
        return poisonDuration;
    }

    public void setPoisonDuration(float poisonDuration) {
        this.poisonDuration = poisonDuration;
    }

    public boolean isBuffed() {
        return isBuffed;
    }

    public void setBuffed(boolean buffed) {
        isBuffed = buffed;
    }

    public float getBuffDuration() {
        return buffDuration;
    }

    public void setBuffDuration(float buffDuration) {
        this.buffDuration = buffDuration;
    }
}
