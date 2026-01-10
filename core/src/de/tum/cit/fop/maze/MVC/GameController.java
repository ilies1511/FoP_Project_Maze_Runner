package de.tum.cit.fop.maze.MVC;
import de.tum.cit.fop.maze.MVC.CharacterWASD;

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
import de.tum.cit.fop.maze.effects.Flicker;
import de.tum.cit.fop.maze.effects.GodMode;
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
import com.badlogic.gdx.graphics.g2d.Animation;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Bouncer;

public class GameController {
	private final MazeRunnerGame game;
	private final Player player;
	private final HUD hud;
	private GameField gameField;
	private final SoundController soundController;
	private final SoundController gameScreenSoundController;
	private final GameScreen gameScreen;
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
                if(waveManager.getScore() == 1000){ //TODO Nikita change value
                    gameScreen.getAchievementUI().pointsAchievement();
                }
			}
			playerDie();
			return;
		}
		if (waveManager != null) {
			waveManager.update(delta);
		}
        if(player.hasKeys()){
            hud.rotateArrow();
        }
		player.tickEffects(delta);
		handleInput(delta);
		updateGameObjects(delta);
		updatePlayerPosition(delta);
	}

	private void handleInput(float delta) {

        if (player.isAttacking()) {
            player.updateAnimation(delta);

            Animation<?> currentAnim = player.getDrawable().getCurrentAnimation();
            if (currentAnim.isAnimationFinished(player.getDrawable().getAnimationTime())) {
                player.setAttacking(false); // Attack finished
                if (currentAnim == game.getCharacterAttackUpAnimation()) {
                    player.changeAnimation(AnimationType.CHARACTER_UP);
                } else if (currentAnim == game.getCharacterAttackLeftAnimation()) {
                    player.changeAnimation(AnimationType.CHARACTER_LEFT);
                } else if (currentAnim == game.getCharacterAttackRightAnimation()) {
                    player.changeAnimation(AnimationType.CHARACTER_RIGHT);
                } else {
                    // fallback
                    player.changeAnimation(AnimationType.CHARACTER_DOWN);
                }            } else {
                return;
            }
        }

		boolean isMoving = false;
		soundController.stopSteppingSound();
        handleInvisStatic();

		// Dash
		if ((Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) && player.isCanDash()) {
			this.player.addOrReapplyEffect(new Dash(player));
		}

        //WASD
        if (CharacterWASD.yAxis(player, isMoving, game)){
            isMoving = true;
        }
        if (CharacterWASD.xAxis(player, isMoving, game)){
            isMoving = true;
        }

		// Attack Logic
        //TODO Attack animation + attack animation in INVIS
		if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
            player.setAttacking(true);
            player.endEffect(Invis.class); // attacking breaks invisibility
            Animation<?> currentAnim = player.getDrawable().getCurrentAnimation();

            if (currentAnim == game.getCharacterUpAnimation() || currentAnim == game.getCharacterInvisUpAnimation()) {
                player.changeAnimation(AnimationType.CHARACTER_ATTACK_UP);
            } else if (currentAnim == game.getCharacterLeftAnimation() || currentAnim == game.getCharacterInvisLeftAnimation()) {
                player.changeAnimation(AnimationType.CHARACTER_ATTACK_LEFT);
            } else if (currentAnim == game.getCharacterRightAnimation() || currentAnim == game.getCharacterInvisRightAnimation()) {
                player.changeAnimation(AnimationType.CHARACTER_ATTACK_RIGHT);
            } else {
                // idle
                player.changeAnimation(AnimationType.CHARACTER_ATTACK_DOWN);
            }
			soundController.playPlayerHitSound(1);
			System.out.println("Attacking!");

			ListIterator<GameObject> iter = gameField.getGameObjects().listIterator();
			while (iter.hasNext()) {
				GameObject obj = iter.next();
				if (obj instanceof Enemy) {
					Enemy enemy = (Enemy) obj;
					if (player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position) < 1.5f) {
						iter.remove();
                        soundController.playEnemyDieSound(1f);
						Collectable drop = enemy.onDeath(player);
						iter.add(drop);
						System.out.println("Enemy Killed!");
                        FleeingEnemy.scaredTourists(gameField, enemy.getPosition());
                    }
				}
			}
            return;
		}

		// Emotes
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if(player.hasEffect(Invis.class)){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_VICTORY_POSE);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
            }

			soundController.playWinSound(0.2f);
			isMoving = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            if(player.hasEffect(Invis.class)){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LOSER_POSE);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
            }

			soundController.playPlayerDieSound(0.2f);
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
            if (obj instanceof Enemy) {
                if (obj.getVelocity().len2() > 0.01f) { //Move
                    obj.updateAnimation(delta);
                } else { //Idle
                    obj.setAnimationTime(0f);
                }
            } else {
                obj.updateAnimation(delta);
            }
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
                    if (!this.player.hasEffect(Flicker.class) && ((Dawn) obj).checkPlayerCollision(this.player)) {
                        this.player.addOrReapplyEffect(new Flicker(this.player));
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
		} else enemyGo.getVelocity().set(0, 0);

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
				if (!this.player.hasEffect(Flicker.class) && !player.hasEffect(Invis.class) && !player.hasEffect(GodMode.class)) {
					System.out.println("YOU WERE DAMAGED!");
					player.getDamageOrHeal(1);
					this.player.addOrReapplyEffect(new Flicker(this.player));
					soundController.playPlayerHitSound(1);
				}
			}
            if (enemy instanceof Bouncer) {
                ((Bouncer) enemy).flee();
            }
		}
	}

	private void handleExitLogic(Exit exit) {
		if (player.getPlayerGameObject().position.dst(exit.position) < 1.5f) {
			if (exit.isLocked()) {
				if (player.hasKeys()) {
					if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getInteractKey())) {
						exit.open();
						gameField.setTile((int) exit.position.x, (int) exit.position.y, TileType.EMPTY);
						System.out.println("Door opened!");
                        hud.deleteArrow();
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
					switch (game.getGameMode()) {
						case STORY_MODE:
							gameScreen.loadNextStoryLevel();
							break;
						case ENDLESS_MODE:
							this.player.setHasKeys(false);
							this.player.setCurrentKeys(0);
							hud.updatePlayerKeys();
							this.startNextLevel();
							break;
						case LEVELS_MODE:
						default:
							soundController.dispose();
							if(gameScreen.getSoundController() != null)
								gameScreen.getSoundController().dispose();
							game.goToGameWonScreen();
							break;
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

    private void handleInvisStatic(){

        if(Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAbilityKey())&&player.isCanInvis()){
            gameScreen.getAchievementUI().invisAchievement();

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
            this.player.addOrReapplyEffect(new Invis(player, 5f));
        }
        if(!player.hasEffect(Invis.class)){
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