package de.tum.cit.fop.maze.game_saves.mementos;

/**
 * Memento storing FreezeTrap state.
 */
public class FreezeTrapMemento extends GameObjectMemento {
    public FreezeTrapMemento() {
        super();
    }

    public FreezeTrapMemento(float positionX, float positionY) {
        super(positionX, positionY, 0, 0, 0, "FREEZE_TRAP", "FreezeTrap");
    }
}
