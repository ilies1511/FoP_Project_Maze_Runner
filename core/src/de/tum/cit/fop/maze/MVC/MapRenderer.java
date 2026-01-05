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

	private void drawDecor(TextureRegion decor, int x, int y) {
		batch.draw(game.getEmptyTile4(), x, y, 1, 1);
		batch.draw(decor, x, y, 1, 1);
	}

//	private void drawGameField(GameField gameField) {
//		for (int y = 0; y < gameField.getHeight(); y++) {
//			for (int x = 0; x < gameField.getWidth(); x++) {
//				TileType tile = gameField.getTile(x, y);
//				batch.draw(game.getEmptyTile4(), x, y, 1, 1);
//				if (tile == TileType.WALL) {
//					batch.draw(game.getWallTile3(), x, y, 1, 1);
//				}
//			}
//		}
//	}

	private void drawGameField(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);

				batch.draw(game.getEmptyTile4(), x, y, 1, 1); // Fallback just in Case

				switch (tile) {
					case WALL:
						batch.draw(game.getWallTile3(), x, y, 1, 1); // TODO: iziane 04.01 --> default
						break;
					case WALL_1:
						batch.draw(game.getWallTile1(), x, y, 1, 1);
						break;
					case WALL_2:
						batch.draw(game.getWallTile2(), x, y, 1, 1);
						break;
					case WALL_3:
						batch.draw(game.getWallTile3(), x, y, 1, 1);
						break;
					case WALL_4:
						batch.draw(game.getWallTile4(), x, y, 1, 1);
						break;
					case WALL_5:
						batch.draw(game.getWallTile5(), x, y, 1, 1);
						break;
					case WALL_6:
						batch.draw(game.getWallTile6(), x, y, 1, 1);
						break;
					case WALL_7:
						batch.draw(game.getWallTile7(), x, y, 1, 1);
						break;
					case WALL_8:
						batch.draw(game.getWallTile8(), x, y, 1, 1);
						break;

					case EMPTY:
						batch.draw(game.getEmptyTile4(), x, y, 1, 1);
						break; // Default
					case EMPTY_1:
						batch.draw(game.getEmptyTile1(), x, y, 1, 1);
						break;
					case EMPTY_2:
						batch.draw(game.getEmptyTile2(), x, y, 1, 1);
						break;
					case EMPTY_3:
						batch.draw(game.getEmptyTile3(), x, y, 1, 1);
						break;
					case EMPTY_4:
						batch.draw(game.getEmptyTile4(), x, y, 1, 1);
						break;
					case EMPTY_5:
						batch.draw(game.getEmptyTile5(), x, y, 1, 1);
						break;
					case EMPTY_6:
						batch.draw(game.getEmptyTile6(), x, y, 1, 1);
						break;
					case EMPTY_7:
						batch.draw(game.getEmptyTile7(), x, y, 1, 1);
						break;
					case DANCE_FLOOR:
						batch.draw(game.getDanceFloorTile(), x, y, 1, 1);
						break;
					// TODO: iziane DBD with Nikita
//					case DECOR_1:
//						drawDecor(game.getDecor1(), x, y);
//						break;
//					case DECOR_2:
//						drawDecor(game.getDecor2(), x, y);
//						break;
//					case DECOR_3:
//						drawDecor(game.getDecor3(), x, y);
//						break;
//					case DECOR_4:
//						drawDecor(game.getDecor4(), x, y);
//						break;
//					case DECOR_5:
//						drawDecor(game.getDecor5(), x, y);
//						break;
//					case DECOR_6:
//						drawDecor(game.getDecor6(), x, y);
//						break;
//					case DECOR_7:
//						drawDecor(game.getDecor7(), x, y);
//						break;
//					case DECOR_8:
//						drawDecor(game.getDecor8(), x, y);
//						break;
//					case DECOR_9:
//						drawDecor(game.getDecor9(), x, y);
//						break;
//					case DECOR_10:
//						drawDecor(game.getDecor10(), x, y);
//						break;
//					case DECOR_11:
//						drawDecor(game.getDecor11(), x, y);
//						break;
//					case EXIT:
//						batch.draw(game.getEmptyTile4(), x, y, 1, 1);
//						break;

					default:
						batch.draw(game.getEmptyTile4(), x, y, 1, 1);
						break;
				}
			}
		}
	}


	public void render(GameField gameField, Player player, float delta) {
		batch.begin();

		drawGameField(gameField);

		gameField.getGameObjects().sort((o1, o2) -> Float.compare(o2.position.y, o1.position.y));
		for (GameObject obj : gameField.getGameObjects()) {
			obj.animate(batch);
		}
		player.animate(batch);
		batch.end();
	}
}