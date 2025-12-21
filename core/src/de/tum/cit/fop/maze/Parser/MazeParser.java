package de.tum.cit.fop.maze.Parser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.io.*;
import java.util.Properties;

//public class MazeParser {
//	// BEGIN -- Attributes
//	BufferedReader _mazeFile;
//	Path _path;
//
//	// END -- Attributes
//
//	// BEGIN -- Constructor
//
//	public MazeParser() {
//		try {
//			this._mazeFile =  new BufferedReader(new InputStreamReader(new FileInputStream("level-1.properties"), StandardCharsets.UTF_8));
//		}
//		catch (FileNotFoundException e) {
//			System.out.println("Exception in " + getClass().getName() + ": " + e.getMessage());
//		}
//	}
//
//	// END -- Constructor
//
//	// BEGIN -- Getters
//	// END -- Getters
//
//	// BEGIN -- Setters
//	// END -- Setters
//
//	// BEGIN -- Methods
//
//	// END -- Methods
//}


public class MazeParser {
	// BEGIN -- Methods
	private static void processTile(GameField field, int x, int y, int id) {
		switch (id) {
			case 0:
				field.setTile(x, y, TileType.WALL);
				break;
			case 1:
				field.setTile(x, y, TileType.ENTRY_POINT);
				field.setPlayerStartPos(new Vector2(x, y));
				break;
			case 2:
				field.setTile(x, y, TileType.EXIT); // TODO: iziane - Use Object later
				break;
			case 3:
				// field.addGameObject(new Trap(x, y));  // TODO: iziane - Later, after GameObject is done
				field.setTile(x, y, TileType.EMPTY);
				break;
			case 4:
				// field.addGameObject(new Enemy(x, y));
				field.setTile(x, y, TileType.EMPTY);
				break;
			case 5:
				// field.addGameObject(new Key(x, y));
				field.setTile(x, y, TileType.EMPTY);
				break;
			default:
				field.setTile(x, y, TileType.EMPTY);
				break;
		}
	}

	private static void parseData(Properties properties, GameField gameField) {

		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			String[] parts = key.split(",");

			if (parts.length == 2) {
				int x = Integer.parseInt(parts[0].trim());
				int y = Integer.parseInt(parts[1].trim());
				int typeId = Integer.parseInt(value.trim());

				processTile(gameField, x, y, typeId);
			}
		}
	}

	private static GameField calculateDimension(Properties properties) {
		int maxX = 0;
		int maxY = 0;

		for (String key : properties.stringPropertyNames()) {
			String[] parts = key.split(",");
			if (parts.length == 2) {
				try {
					int x = Integer.parseInt(parts[0].trim());
					int y = Integer.parseInt(parts[1].trim());
					if (x > maxX) {
						maxX = x;
					}
					if (y > maxY) {
						maxY = y;
					}
				} catch (NumberFormatException ignored) {
				}
			}
		}
		return new GameField(maxX + 1, maxY + 1);
	}

	public static GameField loadMap(String filename) {
		Properties properties = new Properties();

		FileHandle mazeFile = Gdx.files.internal("maps/" + filename);

		if (!mazeFile.exists()) {
			Gdx.app.error("MazeParser", "Map file not found: " + mazeFile.path());
			return null;
		}

		try {
			properties.load(mazeFile.read());
		} catch (IOException e) {
			String fileName2 = filename;
			Gdx.app.error("MazeParser", "Could not load map: " + fileName2, e);
			return null;
//			throw new RuntimeException(e);
		}
		GameField gameField = calculateDimension(properties);
		parseData(properties, gameField);
		return gameField;
	}
	// END -- Methods
}