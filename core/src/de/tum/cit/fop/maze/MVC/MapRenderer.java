package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	private void drawGameField(GameField gameField) {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tile = gameField.getTile(x, y);
				batch.draw(game.getEmptyTile(), x, y, 1, 1);
				if (tile == TileType.WALL) {
					batch.draw(game.getWallTile(), x, y, 1, 1);
				}
			}
		}
	}

	public void render(GameField gameField, Player player, float delta, boolean playerHidden) {
		batch.begin();

		drawGameField(gameField);

		gameField.getGameObjects().sort((o1, o2) -> Float.compare(o2.position.y, o1.position.y));
		for (GameObject obj : gameField.getGameObjects()) {
			obj.animate(batch);
		}
		if (!playerHidden) {
			player.animate(batch);
		}
		batch.end();
	}
}