package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.game_saves.Memento;
import java.util.ArrayList;
import java.util.List;

public class GameFieldMemento implements Memento {
    public int width;
    public int height;
    public String[] grid; // TileType as String array for serialization
    public List<GameObjectMemento> gameObjects;
    public float playerStartPosX;
    public float playerStartPosY;

    public GameFieldMemento() {
        // Required for JSON deserialization
        this.gameObjects = new ArrayList<>();
    }

    public GameFieldMemento(int width, int height, String[] grid, 
                           List<GameObjectMemento> gameObjects, 
                           float playerStartPosX, float playerStartPosY) {
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.gameObjects = gameObjects;
        this.playerStartPosX = playerStartPosX;
        this.playerStartPosY = playerStartPosY;
    }
}
