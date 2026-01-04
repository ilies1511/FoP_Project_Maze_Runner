//package de.tum.cit.fop.maze;
//
//import com.badlogic.gdx.math.Vector2;
//
//public class GameField {
//	private final int width;
//	private final int height;
//	private final TileType[] grid;
//	private final Vector2 playerStartPos;
//
//	public TileType getTile(int x, int y) {
//		if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
//			return TileType.WALL; // boundaries are walls to avoid going outside of the map
//		}
//		return this.grid[y * width + x];
//	}
//
//	private void setTile(int x, int y, TileType tile) {
//		if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
//			System.err.println("Trying to set a tile at (" + x + ", " + y + ") out of bounds!");
//			return;
//		}
//		this.grid[y * width + x] = tile;
//	}
//
//	public int getHeight() {
//		return this.height;
//	}
//
//	public int getWidth() {
//		return this.width;
//	}
//
//	public GameField() {
//		this.height = 15;
//		this.width = 20;
//		this.grid = new TileType[this.width * this.height];
//
//		// Visual representation (Top-Down view)
//		String[] layout = {
//				"####################", // Visual Top
//				"#..................#",
//				"#.###.########.###.#",
//				"#.#...#......#...#.#",
//				"#.#.###.####.###.#.#",
//				"#.#.....#........#.#",
//				"#.#######.########.#",
//				"#..................#",
//				"#####.##########.###",
//				"#...#......#.......#",
//				"#.#####.##.#####.###",
//				"#.......##.........#",
//				"#.################.#",
//				"#..................#",
//				"####################"  // Visual Bottom
//		};
//
//		// Iterate through the VISUAL layout rows (i = 0 is Top)
//		for (int i = 0; i < this.height; i++) {
//
//			// FLIP THE Y AXIS (because libgdx is stupid)
//			int y = (this.height - 1) - i;
//
//			for (int x = 0; x < this.width; x++) {
//				char c = layout[i].charAt(x);
//
//				if (c == '#') {
//					this.setTile(x, y, TileType.WALL);
//				} else {
//					this.setTile(x, y, TileType.EMPTY);
//				}
//			}
//		}
//
//		this.playerStartPos = new Vector2(1f, 1f);
//	}
//
//	public Vector2 getPlayerStartPos() {
//		return  this.playerStartPos;
//	}
//
//	public boolean isInsideWall(float x, float y, Vector2 size) {
//		int minX = (int) x;
//		int maxX = (int) (x + size.x - 0.001f);
//		int minY = (int) y;
//		int maxY = (int) (y + size.y - 0.001f);
//
//		for (int row = minY; row <= maxY; row++) {
//			for (int col = minX; col <= maxX; col++) {
//				if (this.getTile(col, row) == TileType.WALL) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//}


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
	// END -- Getters

	// BEGIN -- Setters
	public void setPlayerStartPos(Vector2 pos) {
		this.playerStartPos = pos;
	}

	public void setTile(int x, int y, TileType tile) {
		if (isValid(x, y)) {
			grid[y * width + x] = tile;
		}
	}
	// END -- Setters

	// BEGIN -- Methods
	// TODO: 22.12.25 iziane
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
//				if (tile == TileType.WALL || tile == TileType.EXIT) {
//					return true;
//				}
				if (isSolid(tile))
					return true;
			}
		}
		return false;
	}
}