package de.tum.cit.fop.maze.Gameplay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {

	// BEGIN -- Attributes
	private static final int DEFAULT_WIDTH = 15;
	private static final int DEFAULT_HEIGHT = 15;
	// END -- Attributes

	// BEGIN -- Constructor
	// END -- Constructor

	// BEGIN -- Getters
	// END -- Getters

	// BEGIN -- Setters
	// END -- Setters

	// BEGIN -- Methods
	private static void placeKeys(int width, int height, GameField gameField) {
		Random random = new Random();

		int keysToPlace = 1;
		while (keysToPlace > 0) {
			int rx = random.nextInt(width);
			int ry = random.nextInt(height);
			if (gameField.getTile(rx, ry) == TileType.EMPTY) {
				gameField.addGameObject(new Key(new Vector2(rx, ry)));
				keysToPlace--;
			}
		}
	}

	private static void setExit(int width, int height, GameField gameField, MazeRunnerGame game) {
		Vector2 exitCoords = new Vector2();

		exitCoords.x = width - 2;
		exitCoords.y = height - 2;
//		float exitX = width - 2;
//		float exitY = height - 2;

		while (gameField.getTile((int) exitCoords.x, (int) exitCoords.y) == TileType.WALL) {
			exitCoords.x--;
			if (exitCoords.x < 1) {
				exitCoords.x = width - 2;
				exitCoords.y--;
			}
		}
		gameField.setTile((int) exitCoords.x, (int) exitCoords.y, TileType.EXIT);
		gameField.addGameObject(new Exit(exitCoords.x, exitCoords.y, game));
	}

	private static ArrayList<Vector2> getUnvisitedNeighbors(Vector2 current, GameField field, int w, int h) {
		ArrayList<Vector2> neighbors = new ArrayList<>();
		int[][] dirs = {{0, 2}, {0, -2}, {2, 0}, {-2, 0}};

		for (int[] dir : dirs) {
			int nx = (int) current.x + dir[0];
			int ny = (int) current.y + dir[1];

			if (nx > 0 && nx < w - 1 && ny > 0 && ny < h - 1) {
				if (field.getTile(nx, ny) == TileType.WALL) {
					neighbors.add(new Vector2(nx, ny));
				}
			}
		}
		return neighbors;
	}

	private static void carvePath(int width, int height, GameField gameField) {
		Random random = new Random();
		Stack<Vector2> stack = new Stack<>();
		Vector2 start = new Vector2(1, 1);
		gameField.setTile(1, 1, TileType.EMPTY);
		stack.push(start);

		while (!stack.isEmpty()) {
			Vector2 current = stack.peek();
			ArrayList<Vector2> neighbors = getUnvisitedNeighbors(current, gameField, width, height);

			if (!neighbors.isEmpty()) {
				Vector2 next = neighbors.get(random.nextInt(neighbors.size()));

				int wallX = (int) (current.x + next.x) / 2;
				int wallY = (int) (current.y + next.y) / 2;
				gameField.setTile(wallX, wallY, TileType.EMPTY);
				gameField.setTile((int) next.x, (int) next.y, TileType.EMPTY);
				stack.push(next);
			}
			else {
				stack.pop();
			}
		}
	}

	private static void putWalls(int width, int height, GameField gameField) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				gameField.setTile(x, y, TileType.WALL);
			}
		}
	}

	public static GameField generateMaze(int width, int height, MazeRunnerGame game) {
		GameField gameField = new GameField(width, height);

		if (width % 2 == 0) {
			width++;
		}
		if (height % 2 == 0) {
			height++;
		}
		putWalls(width, height, gameField);
		carvePath(width, height, gameField);
		gameField.setTile(1, 1, TileType.ENTRY_POINT);
		setExit(width, height, gameField, game);
		placeKeys(width, height, gameField);
		return gameField;
	}
	// END -- Methods
}