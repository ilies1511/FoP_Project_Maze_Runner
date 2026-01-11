package de.tum.cit.fop.maze.Parser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.*;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.traps.TrapCreator;

import java.io.IOException;
import java.util.Properties;

import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;

/*
	Value | Type
	-------------------
	0     | Wall
	1     | Entry point
	2     | Exit
	3     | Trap (static obstacle)
	4     | Enemy (dynamic obstacle)
	5     | Key
*/

//TODO (Connect Parser to Game Assets) - iziane
public class MazeParser {
	// BEGIN -- Methods
	private static void processTile(GameField field, int x, int y, int id, MazeRunnerGame game) {
		TileType[] allTypes = TileType.values();
		TileType type;

		if (id >= 0 && id < allTypes.length) {
			type = allTypes[id];
		}
		else {
			type = TileType.EMPTY;
		}

		switch (type) {
			case WALL:
				field.setTile(x, y, TileType.WALL);
				break;
			case ENTRY_POINT:
				field.setTile(x, y, TileType.EMPTY);
				field.setPlayerStartPos(new Vector2(x, y));
				break;
			case EXIT:
				field.setTile(x, y, TileType.EXIT);
				field.addGameObject(new Exit(x, y, game));
				break;
			case TRAP:
				field.setTile(x, y, TileType.EMPTY);
				field.addGameObject(TrapCreator.createRandomTrap(new Vector2(x, y)));
				break;
			case ENEMY:
				field.setTile(x, y, TileType.EMPTY);
				field.addGameObject(new Junkie(new Vector2(x, y)));
				break;
			case KEY:
				field.setTile(x, y, TileType.EMPTY);
				field.addGameObject(new Key(new Vector2(x, y)));
				break;
			case EMPTY:
				field.setTile(x, y, TileType.EMPTY);
				break;
			default:
				field.setTile(x, y, type);
				break;
		}
	}


	private static void parseData(Properties properties, GameField gameField, MazeRunnerGame game) {

		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			String[] parts = key.split(",");

			if (parts.length == 2) {
				int x = Integer.parseInt(parts[0].trim());
				int y = Integer.parseInt(parts[1].trim());
				int typeId = Integer.parseInt(value.trim());

				processTile(gameField, x, y, typeId, game);
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

	//	public static GameField loadMap(String filename) {
	public static GameField loadMap(String filename, MazeRunnerGame game) {
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
		parseData(properties, gameField, game);
		return gameField;
	}
	// END -- Methods
}