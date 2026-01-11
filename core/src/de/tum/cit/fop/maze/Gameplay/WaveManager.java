package de.tum.cit.fop.maze.Gameplay;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Tourist;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Junkie;

import java.util.Random;

public class WaveManager {
	// BEGIN -- Attributes
	private GameField gameField;
	private final Random random;

	private float timeSinceLastSpawn = 0;
	private float spawnInterval = 5.0f;
	private final float minSpawnInterval = 1.0f;
	private int difficultyLevel = 1;
	private float totalTime = 0;
	// END -- Attributes

	// BEGIN -- Constructor
	public WaveManager(GameField gameField, long seed) {
		this.gameField = gameField;
		this.random = new Random(seed);
	}
	// END -- Constructor

	// BEGIN -- Getters
	public int getScore() {
		return (int) totalTime * 10;
	}
	// END -- Getters

	// BEGIN -- Setters
	public void setGameField(GameField newMap) {
		this.gameField = newMap;
	}
	// END -- Setters

	// BEGIN -- Methods
	public void update(float delta) {
		timeSinceLastSpawn += delta;
		totalTime += delta;

		if (totalTime > difficultyLevel * 15.0f) {
			difficultyLevel++;
			spawnInterval = Math.max(minSpawnInterval, spawnInterval * 0.9f); // Spawn 10% faster
			System.out.println("Difficulty Increased! Level: " + difficultyLevel + " Spawn Rate: " + spawnInterval);
		}

		if (timeSinceLastSpawn >= spawnInterval) {
			spawnEnemy();
			timeSinceLastSpawn = 0;
		}
	}

	private void spawnEnemy() {
		int x, y;
		int maxAttempts = 100;
		do {
			x = random.nextInt(gameField.getWidth());
			y = random.nextInt(gameField.getHeight());
			maxAttempts--;
		} while (gameField.getTile(x, y) == TileType.WALL && maxAttempts > 0);

		if (maxAttempts > 0) {
			Enemy newEnemy;
			if (difficultyLevel >= 3 && random.nextBoolean()) {
				newEnemy = new Junkie(new Vector2(x, y));
			}
			else {
				newEnemy = new Tourist(new Vector2(x, y));
			}
			gameField.addGameObject(newEnemy);
			System.out.println("Spawned Enemy at " + x + ", " + y);
		}
	}
	// END -- Methods
}