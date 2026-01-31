package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.Drawing.AnimationType;

/**
 * Memento storing Heart collectable state.
 */
public class HeartMemento extends GameObjectMemento {
    public HeartMemento() {
        super();
    }

    public HeartMemento(float positionX, float positionY) {
        super(positionX, positionY, 0, 0, 0, AnimationType.VAMPIRIC_HEART, "Heart");
    }
}
