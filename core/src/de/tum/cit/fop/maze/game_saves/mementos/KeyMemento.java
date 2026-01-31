package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.Drawing.AnimationType;

/**
 * Memento storing Key collectable state.
 */
public class KeyMemento extends GameObjectMemento {
    public KeyMemento() {
        super();
    }

    public KeyMemento(float positionX, float positionY) {
        super(positionX, positionY, 0, 0, 0, AnimationType.KEY_SHINE_ANIMATION, "Key");
    }
}
