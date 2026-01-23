package de.tum.cit.fop.maze.game_saves;

/**
 * Interface for objects that can be saved and loaded using the Memento pattern.
 * @param <T> The type of memento used for saving state
 */
public interface Saveable<T extends Memento> {
    /**
     * Creates a memento capturing the current state of this object.
     * @return A memento containing the object's state
     */
    T getMemento();

    /**
     * Restores the object's state from a memento.
     * @param memento The memento containing the state to restore
     */
    void loadFromMemento(T memento);
}
