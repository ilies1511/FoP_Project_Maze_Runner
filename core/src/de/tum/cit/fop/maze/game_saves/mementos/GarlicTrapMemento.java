package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.Drawing.AnimationType;

/**
 * Memento storing GarlicTrap state.
 */
public class GarlicTrapMemento extends GameObjectMemento {
    public GarlicTrapMemento() {
        super();
    }

    public GarlicTrapMemento(float positionX, float positionY) {
        super(positionX, positionY, 0, 0, 0, AnimationType.GARLIC_TRAP, "GarlicTrap");
    }
}
