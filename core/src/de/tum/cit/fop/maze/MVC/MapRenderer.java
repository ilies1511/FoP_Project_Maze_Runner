package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

public class MapRenderer {
	private final MazeRunnerGame game;
	private final SpriteBatch batch;

	public MapRenderer(MazeRunnerGame game) {
		this.game = game;
		this.batch = game.getSpriteBatch();
	}

	private boolean isWall(TileType t) {
		return t == TileType.WALL
				|| t == TileType.WALL_1
				|| t == TileType.WALL_2
				|| t == TileType.WALL_3
				|| t == TileType.WALL_4
				|| t == TileType.WALL_5
				|| t == TileType.WALL_6
				|| t == TileType.WALL_7
				|| t == TileType.WALL_8
				|| t == TileType.WALL_HIGH_1
				|| t == TileType.WALL_HIGH_2
				|| t == TileType.WALL_HIGH_3
				|| t == TileType.WALL_HIGH_4;
	}

	private boolean isWallAt(GameField f, int x, int y) {
		if (x < 0 || y < 0 || x >= f.getWidth() || y >= f.getHeight()) {
			return false;
		}
		return isWall(f.getTile(x, y));
	}

	private TextureRegion getWallTop(TileType t) {
		return game.getBuildingTile();
	}

	private TextureRegion getWallFront(TileType t) {
		return switch (t) {
			case WALL_1 -> game.getWallHigh1();
			case WALL_2 -> game.getWallHigh2();
			case WALL_3 -> game.getWallHigh3();
			case WALL_4, WALL -> game.getWallHigh4();
			default -> game.getWallHigh1();
		};
	}

	private void drawWalls2_5D(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);
				if (!isWall(tile)) {
					continue;
				}
				boolean wallBelow = isWallAt(gameField, x, y - 1);
				TextureRegion wallToDraw = wallBelow ? getWallTop(tile) : getWallFront(tile);
				batch.draw(wallToDraw, x, y, 1, 1);
			}
		}
	}

	private void drawFloors(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);

				TextureRegion floor = game.getEmptyTile1();

				switch (tile) {
					case EMPTY_1 -> floor = game.getEmptyTile1();
					case EMPTY_2 -> floor = game.getEmptyTile2();
					case EMPTY_3 -> floor = game.getEmptyTile3();
					case EMPTY, EMPTY_4 -> floor = game.getEmptyTile4();
					case EMPTY_5 -> floor = game.getEmptyTile5();
					case EMPTY_6 -> floor = game.getEmptyTile6();
					case EMPTY_7 -> floor = game.getEmptyTile7();
					case DANCE_FLOOR -> floor = game.getDanceFloorTile();
					default -> {
					}
				}
				batch.draw(floor, x, y, 1, 1);
			}
		}
	}

	public void render(GameField gameField, Player player, float delta) {
		batch.begin();

		drawFloors(gameField);
		drawWalls2_5D(gameField);
		gameField.getGameObjects().sort((o1, o2) -> Float.compare(o2.position.y, o1.position.y));
		for (GameObject obj : gameField.getGameObjects()) {
			obj.animate(batch);
		}
		player.animate(batch);
		batch.end();
	}
}
