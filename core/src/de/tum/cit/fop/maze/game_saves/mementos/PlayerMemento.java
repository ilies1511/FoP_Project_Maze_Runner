package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.game_saves.Memento;

public class PlayerMemento implements Memento {
    public int currentHealth;
    public int maxHealth;
    public int xp;
    public boolean canDash;
    public boolean canInvis;
    public boolean hasKeys;
    public int currentKeys;
    public int requiredKeys;
    public int kills;
    public float positionX;
    public float positionY;
    public float velocityX;
    public float velocityY;
    public float speed;
    public String animationType;

    public PlayerMemento() {
        // Required for JSON deserialization
    }

    public PlayerMemento(int currentHealth, int maxHealth, int xp, boolean canDash, 
                        boolean canInvis, boolean hasKeys, int currentKeys, int requiredKeys,
                        int kills, float positionX, float positionY, float velocityX, 
                        float velocityY, float speed, String animationType) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.xp = xp;
        this.canDash = canDash;
        this.canInvis = canInvis;
        this.hasKeys = hasKeys;
        this.currentKeys = currentKeys;
        this.requiredKeys = requiredKeys;
        this.kills = kills;
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.speed = speed;
        this.animationType = animationType;
    }
}
