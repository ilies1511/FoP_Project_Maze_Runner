package de.tum.cit.fop.maze.Parser;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the game field grid containing tiles and game objects.
 */
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
    /**
     * Gets the tile type at specified coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @return The tile type, or WALL if out of bounds
     */
    public TileType getTile(int x, int y) {
        if (!isValid(x, y)) {
            return TileType.WALL;
        }
        TileType t = grid[y * width + x];
        return (t == null) ? TileType.EMPTY : t;
    }

    /**
     * Gets all game objects in the field.
     * @return List of game objects
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * Gets the player's starting position.
     * @return Player start position vector
     */
    public Vector2 getPlayerStartPos() {
        return playerStartPos;
    }

    // BEGIN -- Setters
    /**
     * Sets the player's starting position.
     * @param pos New start position
     */
    public void setPlayerStartPos(Vector2 pos) {
        this.playerStartPos = pos;
    }

    /**
     * Gets the field width.
     * @return Width in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the number of keys required to open the exit.
     * @return Number of keys in the field
     */
    public int getRequiredKeys() {
        List<Key> keysInMap = getObjectsByType(Key.class);

        int keys = keysInMap.size();
        if (keys <= 0) {
            return 1;
        }
        return keysInMap.size();
    }

    /**
     * Gets the field height.
     * @return Height in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets all game objects of a specific type.
     * @param type The class type to filter by
     * @param <T> Type parameter extending GameObject
     * @return List of objects matching the type
     */
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

    /**
     * Sets a tile at specified coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @param tile Tile type to set
     */
    public void setTile(int x, int y, TileType tile) {
        if (isValid(x, y)) {
            grid[y * width + x] = tile;
        }
    }
    // END -- Setters

    // BEGIN -- Methods
    
    /**
     * Adds a game object to the field.
     * @param obj The game object to add
     */
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

    /**
     * Checks if a position overlaps with any solid tile.
     * @param x X position
     * @param y Y position
     * @param size Object size for collision checking
     * @return True if inside a wall or solid tile
     */
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