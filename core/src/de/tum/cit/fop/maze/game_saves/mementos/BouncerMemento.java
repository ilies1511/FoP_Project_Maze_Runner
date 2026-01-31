package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.Drawing.AnimationType;

/**
 * Memento storing Bouncer enemy state including remaining lives.
 */
public class BouncerMemento extends GameObjectMemento {
    public int lives;

    public BouncerMemento() {
        super();
    }

    public BouncerMemento(float positionX, float positionY, float velocityX, float velocityY,
                          float speed, AnimationType animationType, int lives) {
        super(positionX, positionY, velocityX, velocityY, speed, animationType, "Bouncer");
        this.lives = lives;
    }
}
