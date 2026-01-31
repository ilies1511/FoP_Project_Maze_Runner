package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.Drawing.AnimationType;

/**
 * Memento storing Tourist enemy state.
 */
public class TouristMemento extends GameObjectMemento {
    public TouristMemento() {
        super();
    }

    public TouristMemento(float positionX, float positionY, float velocityX, float velocityY,
                          float speed, AnimationType animationType) {
        super(positionX, positionY, velocityX, velocityY, speed, animationType, "Tourist");
    }
}
