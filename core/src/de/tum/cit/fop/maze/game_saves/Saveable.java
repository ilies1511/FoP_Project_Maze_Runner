package de.tum.cit.fop.maze.game_saves;

public interface Saveable<T extends Memento> {
    T getMemento();
    void loadFromMemento(T memento);
}
