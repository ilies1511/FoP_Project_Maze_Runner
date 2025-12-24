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
import de.tum.cit.fop.maze.objects.GameObjectTemp;

import java.util.ArrayList;
import java.util.List;


public class GameField {

	// BEGIN -- Attributes
	private final int width;
	private final int height;
	private final TileType[] grid;
	private final List<GameObjectTemp> gameObjectTemps;
	private Vector2 playerStartPos;
	// END -- Attributes

	// BEGIN -- Constructor
	public GameField(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new TileType[width * height];
		this.gameObjectTemps = new ArrayList<>();
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

	public List<GameObjectTemp> getGameObjects() {
		return gameObjectTemps;
	}

	public Vector2 getPlayerStartPos() {
		return playerStartPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	// END -- Getters

	// BEGIN -- Setters
	public void setTile(int x, int y, TileType tile) {
		if (isValid(x, y)) {
			grid[y * width + x] = tile;
		}
	}

	public void setPlayerStartPos(Vector2 pos) {
		this.playerStartPos = pos;
	}
	// END -- Setters

	// BEGIN -- Methods
	// TODO: 22.12.25 iziane
	public void addGameObject(GameObjectTemp obj) {
		this.gameObjectTemps.add(obj);
	}

	private boolean isValid(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	public boolean isInsideWall(float x, float y, Vector2 size) {
		int minX = (int) x;
		int maxX = (int) (x + size.x - 0.001f);
		int minY = (int) y;
		int maxY = (int) (y + size.y - 0.001f);

		for (int row = minY; row <= maxY; row++) {
			for (int col = minX; col <= maxX; col++) {
				if (this.getTile(col, row) == TileType.WALL) {
					return true;
				}
			}
		}
		return false;
	}
	// END -- Methods
}