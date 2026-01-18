package de.tum.cit.fop.maze.game_saves.mementos;

public class ExitMemento extends GameObjectMemento {
    public boolean isLocked;

    public ExitMemento() {
        super();
    }

    public ExitMemento(float positionX, float positionY, boolean isLocked, String animationType) {
        super(positionX, positionY, 0, 0, 0, animationType, "Exit");
        this.isLocked = isLocked;
    }
}
