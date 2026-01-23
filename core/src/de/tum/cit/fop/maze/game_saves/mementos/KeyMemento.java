package de.tum.cit.fop.maze.game_saves.mementos;

/**
 * Memento storing Key collectable state.
 */
public class KeyMemento extends GameObjectMemento {
    public KeyMemento() {
        super();
    }

    public KeyMemento(float positionX, float positionY) {
        super(positionX, positionY, 0, 0, 0, "KEY_SHINE_ANIMATION", "Key");
    }
}
