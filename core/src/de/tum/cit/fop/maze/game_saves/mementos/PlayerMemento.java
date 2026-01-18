package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.game_saves.Memento;

public class PlayerMemento implements Memento {
    public int currentHealth;
    public int maxHealth;

    public PlayerMemento(int currentHealth, int maxHealth) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
    }
}
