package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.GameObject;

public class MapRenderer {
	private final MazeRunnerGame game;
	private final SpriteBatch batch;

	public MapRenderer(MazeRunnerGame game) {
		this.game = game;
		this.batch = game.getSpriteBatch();
	}

	public void render(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tileType = gameField.getTile(x, y);

				batch.draw(game.getEmptyTile(), x, y, 1, 1);
				if (tileType == TileType.WALL) {
					batch.draw(game.getWallTile(), x, y, 1, 1);
				}
			}
		}

		for (GameObject obj : gameField.getGameObjects()) {
			obj.draw(batch);
		}
	}
}
