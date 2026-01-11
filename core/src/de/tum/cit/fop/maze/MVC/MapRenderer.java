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

	private void drawDecors(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);
				if (!isDecor(tile)) {
					continue;
				}
				TextureRegion decor = getDecorTexture(tile);
				if (decor == null) {
					continue;
				}
				batch.draw(decor, x, y, 1, 1);
			}
		}
	}

	private boolean isDecor(TileType t) {
		return t == TileType.DECOR_1
				|| t == TileType.DECOR_2
				|| t == TileType.DECOR_3
				|| t == TileType.DECOR_4
				|| t == TileType.DECOR_5
				|| t == TileType.DECOR_6
				|| t == TileType.DECOR_7
				|| t == TileType.DECOR_8
				|| t == TileType.DECOR_9
				|| t == TileType.DECOR_10
				|| t == TileType.CASKET;
	}

	private TextureRegion getDecorTexture(TileType t) {
		return switch (t) {
			case DECOR_1 -> game.getDecor1();
			case DECOR_2 -> game.getDecor2();
			case DECOR_3 -> game.getDecor3();
			case DECOR_4 -> game.getDecor4();
			case DECOR_5 -> game.getDecor5();
			case DECOR_6 -> game.getDecor6();
			case DECOR_7 -> game.getDecor7();
			case DECOR_8 -> game.getDecor8();
			case DECOR_9 -> game.getDecor9();
			case DECOR_10 -> game.getDecor10();
			case CASKET -> game.getCasketTile();
			default -> null;
		};
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

	private boolean isSingleWall(GameField f, int x, int y) {
		if (!isWallAt(f, x, y) || y == 0) {
			return false;
		}
//		return !isWallAt(f, x + 1, y)
//				&& !isWallAt(f, x - 1, y)
//				&& !isWallAt(f, x, y - 1)
//				&& !isWallAt(f, x, y + 1);
		return !isWallAt(f, x, y - 1)
				&& !isWallAt(f, x, y + 1);
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
			case WALL, WALL_1 -> game.getWallTile1();
			case WALL_2 -> game.getWallHigh2();
			case WALL_3 -> game.getWallHigh3();
			case WALL_4 -> game.getWallHigh4();
			default -> game.getWallHigh1();
		};
	}

	private TextureRegion getWallFront_Small(TileType t) {
		return switch (t) {
//			case WALL_1 -> game.getWallTile1();
			case WALL, WALL_1 -> game.getWallHigh1();
			case WALL_2 -> game.getWallTile2();
			case WALL_3 -> game.getWallTile3();
			case WALL_4 -> game.getWallTile4();
			default -> game.getWallTile1();
		};
	}

	private void drawWalls2_5D(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TextureRegion wallToDraw;
				TileType tile = gameField.getTile(x, y);

				if (!isWall(tile)) {
					continue;
				}
				if (isSingleWall(gameField, x, y)) {
					wallToDraw = getWallFront_Small(tile);
					batch.draw(wallToDraw, x, y, 1, 1);
					continue;
				}
				boolean wallBelow = isWallAt(gameField, x, y - 1);
				wallToDraw = wallBelow ? getWallTop(tile) : getWallFront(tile);
				batch.draw(wallToDraw, x, y, 1, 1);
			}
		}
	}

	private void drawFloors(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);

				TextureRegion floor = game.getEmptyTile4(); // TODO 11.01 iziane - stick to one default

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
		drawDecors(gameField);

		gameField.getGameObjects().sort((o1, o2) -> Float.compare(o2.position.y, o1.position.y));
		for (GameObject obj : gameField.getGameObjects()) {
			obj.animate(batch);
		}
		player.animate(batch);
		batch.end();
	}
}
