package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.game_saves.Memento;

/**
 * Memento storing basic game object state including position, velocity, and animation.
 */
public class GameObjectMemento implements Memento {
    public float positionX;
    public float positionY;
    public float velocityX;
    public float velocityY;
    public float speed;
    public String animationType;
    public String objectType; // To identify the type when deserializing

    public GameObjectMemento() {
        // For JSON deserialization
    }

    public GameObjectMemento(float positionX, float positionY, float velocityX, float velocityY,
                             float speed, String animationType, String objectType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.speed = speed;
        this.animationType = animationType;
        this.objectType = objectType;
    }
}
