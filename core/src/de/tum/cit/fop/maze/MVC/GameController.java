package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.Gameplay.MazeGenerator;
import de.tum.cit.fop.maze.Gameplay.WaveManager;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.Utils.ScoreManager;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.Dawn;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.traps.Trap;

import java.util.Iterator;
import java.util.ListIterator;

public class GameController {
	private final MazeRunnerGame game;
	private final Player player;
	private final HUD hud;
	private GameField gameField;
	private final SoundController soundController;
	private final SoundController gameScreenSoundController;
	private GameScreen gameScreen;
	private WaveManager waveManager = null;

	private float flickerTimer = 0f;

	public GameController(MazeRunnerGame game, Player player, HUD hud, GameField gameField, SoundController gameScreenSoundController, GameScreen gameScreen) {
		this.game = game;
		this.player = player;
		this.hud = hud;
		this.gameField = gameField;
		this.gameScreenSoundController = gameScreenSoundController;
		this.soundController = new SoundController();
		this.gameScreen = gameScreen;
	}

	public void update(float delta) {
		if (player.getCurrentHealth() <= 0) {
			if (waveManager != null) {
				ScoreManager.getInstance().addScore(waveManager.getScore());
			}
			playerDie();
			return;
		}
		if (waveManager != null) {
			waveManager.update(delta);
		}
		player.tickEffects(delta);
		handleInput(delta);
		updateGameObjects(delta);
		updatePlayerPosition(delta);
	}

	private void handleInput(float delta) {
		boolean isMoving = false;
		soundController.stopSteppingSound();
        handleInvisStatic();

		// Dash
		if ((Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) && player.isCanDash()) {
			this.player.addOrReapplyEffect(new Dash(player));
		}

		// Y-Axis
		float speed = player.getPlayerGameObject().getSpeed();
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
			player.getPlayerGameObject().velocity.y = speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_UP);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_UP);
            }

			isMoving = true;
		}
		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
			player.getPlayerGameObject().velocity.y = -speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_DOWN);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_DOWN);
            }

			isMoving = true;
		}
		else {
			player.getPlayerGameObject().velocity.y = 0f;
		}

		// X-Axis
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
			player.getPlayerGameObject().velocity.x = -speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LEFT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_LEFT);
            }

			isMoving = true;
		}
		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
			player.getPlayerGameObject().velocity.x = speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_RIGHT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_RIGHT);
            }

			isMoving = true;
		}
		else {
			player.getPlayerGameObject().velocity.x = 0f;
		}

		// Attack Logic
        //TODO Attack animation + attack animation in INVIS
		if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
			soundController.playPlayerHitSound(1);
			System.out.println("Attacking!");

			ListIterator<GameObject> iter = gameField.getGameObjects().listIterator();
			while (iter.hasNext()) {
				GameObject obj = iter.next();
				if (obj instanceof Enemy) {
					Enemy enemy = (Enemy) obj;
					if (player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position) < 1.5f) {
						iter.remove();
						Collectable drop = enemy.onDeath(player);
						iter.add(drop);
						System.out.println("Enemy Killed!");
					}
				}
			}
		}

		// Emotes
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_VICTORY_POSE);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
            }

			soundController.playWinSound(1);
			isMoving = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LOSER_POSE);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
            }

			soundController.playPlayerDieSound(1);
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
        // spawn new Dawn if enough time left
        Dawn.spawnNewDawn(delta, this.gameField, this.player.position, this.gameField.getGameObjects());

        // update game objects
        ListIterator<GameObject> iter = gameField.getGameObjects().listIterator();
		while (iter.hasNext()) {
			GameObject obj = iter.next();
			obj.updateAnimation(delta);

			if (obj instanceof Enemy) {
				handleEnemyLogic((Enemy) obj, delta, iter);
			}
			else if (obj instanceof Collectable) {
				if (((Collectable) obj).checkPlayerCollision(this.player, this.hud)) {
					iter.remove();
					// hud.setHasKey(true); // TODO: Uncomment when HUD ready --> Nikita
				}
			}
			else if (obj instanceof Trap) {
				((Trap) obj).checkPlayerCollision(this.player, this.hud);
			} else if (obj instanceof Dawn) {
                if (((Dawn) obj).updateDawn(delta)) {
                    iter.remove();
                } else {
                    if (this.flickerTimer <= 0.0f && ((Dawn) obj).checkPlayerCollision(this.player)) {
                        this.flickerTimer = 3f;
                    }
                }
            }
			else if (obj instanceof Exit) {
				handleExitLogic((Exit) obj);
			}
		}
	}

	private void handleEnemyLogic(Enemy enemy, float delta, Iterator<GameObject> iter) {
		GameObject enemyGo = enemy.getEnemyEntity();
		enemy.updateBehavior(delta, player.getPlayerGameObject().position, gameField, player);

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
				if (flickerTimer <= 0 && !player.isInvis() && !player.isInGodMode()) {
					System.out.println("YOU WERE DAMAGED!");
					player.getDamageOrHeal(1);
					flickerTimer = 3f;
					soundController.playPlayerHitSound(1);
				}
			}
//            if (enemy instanceof de.tum.cit.fop.maze.objects.Dynamic.Enemy.Bouncer) {
//                if(!player.isInvis()){
////                    iter.remove();
//                    //
//                }
//
//            }
		}
	}

	private void handleExitLogic(Exit exit) {
		if (player.getPlayerGameObject().position.dst(exit.position) < 1.5f) {
			if (exit.isLocked()) {
				if (player.hasKeys()) {
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
					if (gameScreen.isSurvivalMode()) {
						this.player.setHasKeys(false);
						this.player.setCurrentKeys(0);
						this.gameScreen.getPlayer().setCurrentKeys(0);
						this.gameScreen.getPlayer().setHasKeys(false);
						this.hud.renderHUD();
						this.startNextLevel();
	//						gameScreen.startNextLevel();
					} else {
						soundController.dispose();
						gameScreen.getSoundController().dispose();
						game.goToGameWonScreen();
					}
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

	public SoundController getSoundController() {
		return soundController;
	}

	private void playerDie() {
		player.die();
		soundController.dispose();
		gameScreenSoundController.dispose();
		game.goToGameLostScreen();
	}

	public void setWaveManager(WaveManager waveManager) {
		this.waveManager = waveManager;
	}

	public void setGameField(GameField newMap) {
		this.gameField = newMap;
	}

	public void startNextLevel() {
		GameField newMap = MazeGenerator.generateMaze(19, 19, this.game);
		Vector2 entryPos = gameField.getTileTypeCoords(newMap, TileType.ENTRY_POINT);

		this.gameScreen.setGameField(newMap);
		this.setGameField(newMap);
		if (waveManager != null) {
			waveManager.setGameField(newMap);
		}
		if (entryPos != null) {
			player.getPlayerGameObject().position.set(entryPos.x, entryPos.y);
		}
		if (soundController != null) {
			soundController.playWinSound(1.0f);
		}
	}
	public WaveManager getWaveManager() {
		return waveManager;
	}

	//	public boolean flicker(float delta) {
//		if (flickerTimer > 0f) {
//			flickerTimer -= delta;
//		}
//		//player visible if timer is 0 or if flickering
//		return ((flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f));
//	}
    private void handleInvisStatic(){

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)&&player.isCanInvis()){

            if(player.getDrawable().getCurrentAnimation() == game.getCharacterUpAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_UP);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterDownAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_DOWN);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterLeftAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LEFT);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterRightAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_RIGHT);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterDownWin()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_VICTORY_POSE);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterDownSad()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LOSER_POSE);
            }
            this.player.addOrReapplyEffect(new Invis(player, 5f, game));
        }
        if(!player.isInvis()){
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisUpAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_UP);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisDownAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_DOWN);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisLeftAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_LEFT);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisRightAnimation()){
                player.changeAnimation(AnimationType.CHARACTER_RIGHT);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisDownWin()){
                player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
            }
            if(player.getDrawable().getCurrentAnimation() == game.getCharacterInvisDownSad()){
                player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
            }
        }
    }
}