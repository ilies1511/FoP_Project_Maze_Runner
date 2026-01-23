package de.tum.cit.fop.maze.game_saves.mementos;

public class TouristMemento extends GameObjectMemento {
    public TouristMemento() {
        super();
    }

    public TouristMemento(float positionX, float positionY, float velocityX, float velocityY,
                          float speed, String animationType) {
        super(positionX, positionY, velocityX, velocityY, speed, animationType, "Tourist");
    }
}
