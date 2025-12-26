package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.Iterator;

public class GameController {
	private final MazeRunnerGame game;
	private final Player player;
	private final HUD hud;
	private final GameField gameField;
	private final SoundController soundController;

	private float flickerTimer = 0f;

	public GameController(MazeRunnerGame game, Player player, HUD hud, GameField gameField) {
		this.game = game;
		this.player = player;
		this.hud = hud;
		this.gameField = gameField;
		this.soundController = new SoundController();
	}

	public void update(float delta) {
		player.tickEffects(delta);
		handleInput(delta);
		updateGameObjects(delta);
		updatePlayerPosition(delta);
	}

	private void handleInput(float delta) {
		boolean isMoving = false;
		soundController.stopSteppingSound();

		// Dash
		if ((Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) && player.isCanDash()) {
			this.player.addOrReapplyEffect(new Dash(player));
		}

		// Y-Axis
		float speed = player.getPlayerGameObject().getSpeed();
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
			player.getPlayerGameObject().velocity.y = speed;
			player.changeAnimation(AnimationType.CHARACTER_UP);
			isMoving = true;
		}
		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
			player.getPlayerGameObject().velocity.y = -speed;
			player.changeAnimation(AnimationType.CHARACTER_DOWN);
			isMoving = true;
		}
		else {
			player.getPlayerGameObject().velocity.y = 0f;
		}

		// X-Axis
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
			player.getPlayerGameObject().velocity.x = -speed;
			player.changeAnimation(AnimationType.CHARACTER_LEFT);
			isMoving = true;
		}
		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
			player.getPlayerGameObject().velocity.x = speed;
			player.changeAnimation(AnimationType.CHARACTER_RIGHT);
			isMoving = true;
		}
		else {
			player.getPlayerGameObject().velocity.x = 0f;
		}

		// Attack Logic
		if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
			SoundController.playPlayerHitSound(1);
			System.out.println("Attacking!");

			Iterator<GameObject> iter = gameField.getGameObjects().iterator();
			while (iter.hasNext()) {
				GameObject obj = iter.next();
				if (obj instanceof Enemy) {
					Enemy enemy = (Enemy) obj;
					if (player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position) < 1.5f) {
						iter.remove();
						hud.updatePlayerXP(15);
						System.out.println("Enemy Killed!");
					}
				}
			}
		}

		// Emotes
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
			SoundController.playWinSound(1);
			isMoving = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
			SoundController.playPlayerDieSound(1);
			isMoving = true;
		}

		// Animation Updates
		if (isMoving) {
			player.updateAnimation(delta);
			soundController.playSteppingSound(1);
		}
		else {
			player.setAnimationTime(0f);
		}
	}

	private void updateGameObjects(float delta) {
		Iterator<GameObject> iter = gameField.getGameObjects().iterator();
		while (iter.hasNext()) {
			GameObject obj = iter.next();
			obj.updateAnimation(delta);

			if (obj instanceof Enemy) {
				handleEnemyLogic((Enemy) obj, delta, iter);
			}
            else if (obj instanceof Collectable) {
                if (((Collectable) obj).checkPlayerCollision(this.player, this.hud)) {
                    iter.remove();
                }
            }
			else if (obj instanceof Exit) {
				handleExitLogic((Exit) obj);
			}
		}
	}

	private void handleEnemyLogic(Enemy enemy, float delta, Iterator<GameObject> iter) {
		GameObject enemyGo = enemy.getEnemyEntity();
		enemy.updateBehavior(delta, player.getPlayerGameObject().position, gameField);

		Vector2 nextPos = enemyGo.nextPosition(delta);
		if (!gameField.isInsideWall(nextPos.x, nextPos.y, enemyGo.getSize())) {
			enemyGo.move(delta);
		}

		if (GameObject.entitiesOverlap(player.getPlayerGameObject(), enemyGo)) {
			if (enemy instanceof FleeingEnemy) {
				Vector2 pushDir = new Vector2(player.getPlayerGameObject().position).sub(enemyGo.position).nor();
				Vector2 pushAmount = pushDir.scl(0.2f);
				float newX = player.getPlayerGameObject().position.x + pushAmount.x;
				float newY = player.getPlayerGameObject().position.y + pushAmount.y;

				if (!gameField.isInsideWall(newX, newY, player.getPlayerGameObject().getSize())) {
					player.getPlayerGameObject().position.set(newX, newY);
				}
			}
			else {
				if (flickerTimer <= 0 && !player.isCanInvis()) {
					System.out.println("YOU DIED!");
                    player.getDamageOrHeal(1);
					hud.renderPlayerHP();
					flickerTimer = 3f;
					SoundController.playPlayerHitSound(1);
				}
			}
		}
	}

	private void handleExitLogic(Exit exit) {
		if (player.getPlayerGameObject().position.dst(exit.position) < 1.5f) {
			if (exit.isLocked()) {
				if (player.hasKey()) {
					if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
						exit.open();
						gameField.setTile((int) exit.position.x, (int) exit.position.y, TileType.EMPTY);
						System.out.println("Door opened!");
					}
					else {
						// System.out.println("Press O to open");
					}
				}
				else {
					// System.out.println("Locked. Find the key first.");
				}
			}
			else {
				if (GameObject.entitiesOverlap(player.getPlayerGameObject(), exit)) {
					System.out.println("You escaped! Level Finished.");
					SoundController.playWinSound(1);
					// game.goToMenu();
				}
			}
		}
	}

	private void updatePlayerPosition(float delta) {
		float velocityX = player.getPlayerGameObject().velocity.x;
		float velocityY = player.getPlayerGameObject().velocity.y;
		float maxSpeed = player.getPlayerGameObject().getSpeed();

		float diagonalMultiplier = 1.0f;
		float currentSpeedSqr = velocityX * velocityX + velocityY * velocityY;

		if (currentSpeedSqr > (maxSpeed * maxSpeed) + 0.001f) {
			float currentSpeed = (float) Math.sqrt(currentSpeedSqr);
			diagonalMultiplier = maxSpeed / currentSpeed;
		}

		float moveAmountX = velocityX * diagonalMultiplier * delta;
		float nextX = player.getPlayerGameObject().position.x + moveAmountX;
		float currentY = player.getPlayerGameObject().position.y;

		if (!gameField.isInsideWall(nextX, currentY, player.getPlayerGameObject().getSize())) {
			player.getPlayerGameObject().position.x = nextX;
		}

		float currentX = player.getPlayerGameObject().position.x;
		float moveAmountY = velocityY * diagonalMultiplier * delta;
		float nextY = player.getPlayerGameObject().position.y + moveAmountY;

		if (!gameField.isInsideWall(currentX, nextY, player.getPlayerGameObject().getSize())) {
			player.getPlayerGameObject().position.y = nextY;
		}
	}

	public boolean isPlayerVisible(float delta) {
		if (flickerTimer > 0f) {
			flickerTimer -= delta;
		}
		return ((flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f));
	}
//	public boolean flicker(float delta) {
//		if (flickerTimer > 0f) {
//			flickerTimer -= delta;
//		}
//		//player visible if timer is 0 or if flickering
//		return ((flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f));
//	}
}