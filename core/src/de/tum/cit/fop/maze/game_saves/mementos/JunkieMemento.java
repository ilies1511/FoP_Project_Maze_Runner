package de.tum.cit.fop.maze.game_saves.mementos;

public class JunkieMemento extends GameObjectMemento {
    public JunkieMemento() {
        super();
    }

    public JunkieMemento(float positionX, float positionY, float velocityX, float velocityY,
                         float speed, String animationType) {
        super(positionX, positionY, velocityX, velocityY, speed, animationType, "Junkie");
    }
}
