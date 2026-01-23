package de.tum.cit.fop.maze.Parser;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.ArrayList;
import java.util.List;


public class GameField {

    // BEGIN -- Attributes
    private final int width;
    private final int height;
    private final TileType[] grid;
    private final List<GameObject> gameObjects;
    private Vector2 playerStartPos;
    // END -- Attributes

    // BEGIN -- Constructor
    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new TileType[width * height];
        this.gameObjects = new ArrayList<>();
        this.playerStartPos = new Vector2(1, 1);
    }
    // END -- Constructor

    // BEGIN -- Getters
    public TileType getTile(int x, int y) {
        if (!isValid(x, y)) {
            return TileType.WALL;
        }
        TileType t = grid[y * width + x];
        return (t == null) ? TileType.EMPTY : t;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Vector2 getPlayerStartPos() {
        return playerStartPos;
    }

    // BEGIN -- Setters
    public void setPlayerStartPos(Vector2 pos) {
        this.playerStartPos = pos;
    }

    public int getWidth() {
        return width;
    }

    public int getRequiredKeys() {
        List<Key> keysInMap = getObjectsByType(Key.class);

        int keys = keysInMap.size();
        if (keys <= 0) {
            return 1;
        }
        return keysInMap.size();
    }

    public int getHeight() {
        return height;
    }

    public <T extends GameObject> List<T> getObjectsByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (GameObject obj : gameObjects) {
            if (type.isInstance(obj)) {
                result.add(type.cast(obj));
            }
        }
        return result;
    }
    // END -- Getters

    public Vector2 getTileTypeCoords(GameField newMap, TileType TYPE) {
        for (int y = 0; y < newMap.getHeight(); y++) {
            for (int x = 0; x < newMap.getWidth(); x++) {
                if (newMap.getTile(x, y) == TYPE) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }

    public void setTile(int x, int y, TileType tile) {
        if (isValid(x, y)) {
            grid[y * width + x] = tile;
        }
    }
    // END -- Setters

    // BEGIN -- Methods
    
    public void addGameObject(GameObject obj) {
        this.gameObjects.add(obj);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean isSolid(TileType tile) {
        if (tile == TileType.EXIT)
            return true;
        if (tile == TileType.WALL)
            return true;

        switch (tile) {
            case WALL_1:
            case WALL_2:
            case WALL_3:
            case WALL_4:
            case WALL_5:
            case WALL_6:
            case WALL_7:
            case WALL_8:
            case WALL_HIGH_1:
            case WALL_HIGH_2:
            case WALL_HIGH_3:
            case WALL_HIGH_4:
            case DECOR_1:
            case DECOR_2:
            case DECOR_3:
            case DECOR_4:
            case DECOR_5:
            case DECOR_6:
            case DECOR_7:
            case DECOR_8:
            case DECOR_9:
            case DECOR_10:
                return true;
            default:
                return false;
        }
    }

    public boolean isInsideWall(float x, float y, Vector2 size) {
        int minX = (int) x;
        int maxX = (int) (x + size.x - 0.001f);
        int minY = (int) y;
        int maxY = (int) (y + size.y - 0.001f);

        for (int row = minY; row <= maxY; row++) {
            for (int col = minX; col <= maxX; col++) {
                TileType tile = this.getTile(col, row);
                if (isSolid(tile))
                    return true;
            }
        }
        return false;
    }
}