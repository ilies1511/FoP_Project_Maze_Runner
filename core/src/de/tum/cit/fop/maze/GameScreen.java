package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.MVC.MapRenderer;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.UI.DevConsole;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.UI.PausedOverlay;
import de.tum.cit.fop.maze.UI.ProgressionOverlay;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.GameObjectTemp;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

	private final MazeRunnerGame game;
	private final Camera camera;
	private final BitmapFont font;
	private final Player player;
	private final Stage stage;
	private final GameObject playerGameObject;
	private final HUD hud;
	private final PausedOverlay pausedScreen;
	private final ProgressionOverlay progressionOverlay;
	private final DevConsole devConsole;
	private final SoundController soundController;
	private float flickerTimer = 0f;
	private GameField gameField;
	//	private ArrayList<Enemy> enemies;
	//	private ArrayList<Collectable> collectables;
	private boolean isPaused = false;
	private boolean isInProgression = false;
	private boolean isInDevConsole = false;
	private final MapRenderer mapRenderer;
	private final GameController gameController;


//  For testing

	/**
	 * Constructor for GameScreen. Sets up the camera and font.
	 *
	 */

	public GameScreen(MazeRunnerGame game) {
		this.game = game;

		Drawable.initializeTextureMap(game);

		this.gameField = MazeParser.loadMap("level-1.properties", this.game);
		if (this.gameField == null) {
			System.err.println("Map failed to load! Check file path.");
			this.gameField = new GameField(5, 5);
		}
		else {
			System.out.println("Map loaded successfully!");
			System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
		}

		addTestObjects();

		this.player = new Player(this); //create an instance of the player
		this.playerGameObject = this.player.getPlayerGameObject();

		// Create and configure the camera for the game view
		this.camera = new Camera(this);

		// Get the font from the game's skin
		font = game.getSkin().getFont("vampire-font");

		Viewport viewport = new FitViewport(2000, 1200); // TODO maybe add camera in the brackets
		stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

		this.hud = new HUD(player, stage, game, this);
		pausedScreen = new PausedOverlay(stage, game, this);
		progressionOverlay = new ProgressionOverlay(stage, game, this, player, hud);
		devConsole = new DevConsole(game, this, stage, player, hud);
		soundController = new SoundController();
//        shapeRenderer = new ShapeRenderer();
		this.mapRenderer = new MapRenderer(game);
		this.gameController = new GameController(game, player, hud, gameField);
	}


//	private void updateGameObjects(float delta) {
//		Iterator<GameObject> iter = gameField.getGameObjects().iterator();
//		while (iter.hasNext()) {
//			GameObject obj = iter.next();
//
//			obj.updateAnimation(delta);
//
//			if (obj instanceof Enemy) {
//				handleEnemyLogic((Enemy) obj, delta, iter);
//			}
//			else if (obj instanceof Key) {
//				if (GameObject.entitiesOverlap(playerGameObject, obj)) {
//					player.collectKey();
//					// hud.setHasKey(true); // TODO: Uncomment when HUD ready
//					SoundController.playKeyPickupSound(1);
//					iter.remove();
//				}
//			}
//			else if (obj instanceof Exit) {
//				handleExitLogic((Exit) obj);
//			}
//		}
//	}

	private void handleGlobalInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE) && !isInDevConsole) {
			devConsole.showConsole();
			isInDevConsole = true;
		}
		else if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE) && isInDevConsole) {
			devConsole.hideConsole();
			isInDevConsole = false;
		}
	}

	// Screen interface methods with necessary functionality
	@Override
	public void render(float delta) {
		handleGlobalInput();

		if (!isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				pausedScreen.showPausedMenu();
			}
			else if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !isInDevConsole) {
				progressionOverlay.showProgressionMenu();
				isPaused = true;
				isInProgression = true;
			}
			ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

			gameController.update(delta);
////			// Tick all the effects
//			this.player.tickEffects(delta);
//			handleInput(delta);
//			updateGameObjects(delta);
//			updatePlayerPosition(delta);
			if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomInKey()))
				camera.zoom_in();
			if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomOutKey()))
				camera.zoom_out();
			this.camera.update(delta);
		}

		else if (isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isInProgression) {
				pausedScreen.hidePausedMenu();
			}
			else if (Gdx.input.isKeyJustPressed(Input.Keys.I) && isInProgression) {
				progressionOverlay.hideProgressionMenu();
				isInProgression = false;
			}
		}
		game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);
		boolean playerHidden = !gameController.isPlayerVisible(delta);
		mapRenderer.render(gameField, player, delta, playerHidden);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	public GameField getGameField() {
		return gameField;
	}

//	private void handleInput(float delta) {
//		boolean isMoving = false;
//		soundController.stopSteppingSound();
//
//		// Dash
//		if ((Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) && player.isCanDash()) {
//			this.player.addOrReapplyEffect(new Dash(player));
//		}
//
//		// Y-Axis
//		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
//			player.getPlayerGameObject().velocity.y = player.getPlayerGameObject().getSpeed();
//			this.player.changeAnimation(AnimationType.CHARACTER_UP);
//			isMoving = true;
//		}
//		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
//			player.getPlayerGameObject().velocity.y = -player.getPlayerGameObject().getSpeed();
//			this.player.changeAnimation(AnimationType.CHARACTER_DOWN);
//			isMoving = true;
//		}
//		else {
//			player.getPlayerGameObject().velocity.y = 0f;
//		}
//
//		// X-Axis
//		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
//			player.getPlayerGameObject().velocity.x = -player.getPlayerGameObject().getSpeed();
//			this.player.changeAnimation(AnimationType.CHARACTER_LEFT);
//			isMoving = true;
//		}
//		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
//			player.getPlayerGameObject().velocity.x = player.getPlayerGameObject().getSpeed();
//			this.player.changeAnimation(AnimationType.CHARACTER_RIGHT);
//			isMoving = true;
//		}
//		else {
//			player.getPlayerGameObject().velocity.x = 0f;
//		}
//
//		if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
//			SoundController.playPlayerHitSound(1);
//			System.out.println("Attacking!");
//
//			Iterator<GameObject> iter = gameField.getGameObjects().iterator();
//			while (iter.hasNext()) {
//				GameObject obj = iter.next();
//
//				if (obj instanceof Enemy) {
//					Enemy enemy = (Enemy) obj;
//
//					float distance = player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position);
//
//					if (distance < 1.5f) {
//						iter.remove();
//						hud.updatePlayerXP(15);
//						System.out.println("Enemy Killed!");
//					}
//				}
//			}
//		}
//
//		// Zoom Controls
//		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomInKey())) {
//			this.camera.zoom_in();
//		}
//
//		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomOutKey())) {
//			this.camera.zoom_out();
//		}
//
//		// Victory pose (Testing)
//		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
//			this.player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
//			SoundController.playWinSound(1);
//			isMoving = true;
//		}
//
//		// Loser pose (Testing)
//		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
//			this.player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
//			SoundController.playPlayerDieSound(1);
//			isMoving = true;
//		}
//
//		// Animation Timer Update
//		if (isMoving) {
//			this.player.updateAnimation(delta);
//			soundController.playSteppingSound(1);
//		}
//		else {
//			this.player.setAnimationTime(0f);
//		}
//	}

	private void addTestObjects() {
		gameField.addGameObject(new FleeingEnemy(new Vector2(7f, 7f)));
		gameField.addGameObject(new Key(new Vector2(5f, 5f)));
	}

	public boolean flicker(float delta) {
		if (flickerTimer > 0f) {
			flickerTimer -= delta;
		}
		//player visible if timer is 0 or if flickering
		return ((flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f));
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean paused) {
		isPaused = paused;
	}

//	private void handleExitLogic(Exit exit) {
//		if (player.getPlayerGameObject().position.dst(exit.position) < 1.5f) {
//
//			if (exit.isLocked()) {
//				if (player.hasKey()) {
//					if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
//						exit.open();
//						gameField.setTile((int) exit.position.x, (int) exit.position.y, TileType.EMPTY);
//						System.out.println("Door opened!");
//					}
//					else {
//						// System.out.println("Press O to open"); // Optional hint
//					}
//				}
//				else {
//					// System.out.println("Locked. Find the key first.");
//				}
//			}
//			else {
//				if (GameObject.entitiesOverlap(playerGameObject, exit)) {
//					System.out.println("You escaped! Level Finished.");
//					SoundController.playWinSound(1);
//					// game.goToMenu();
//				}
//			}
//		}
//	}

//	private void handleEnemyLogic(Enemy enemy, float delta, Iterator<GameObject> iter) {
//		GameObject enemyGo = enemy.getEnemyEntity();
//
//		enemy.updateBehavior(delta, playerGameObject.position, gameField);
//
//		Vector2 nextPos = enemyGo.nextPosition(delta);
//		if (!gameField.isInsideWall(nextPos.x, nextPos.y, enemyGo.getSize())) {
//			enemyGo.move(delta);
//		}
//
//		if (GameObject.entitiesOverlap(playerGameObject, enemyGo)) {
//			if (enemy instanceof FleeingEnemy) {
//				Vector2 pushDir = new Vector2(playerGameObject.position).sub(enemyGo.position).nor();
//				Vector2 pushAmount = pushDir.scl(0.2f);
//
//				float newX = playerGameObject.position.x + pushAmount.x;
//				float newY = playerGameObject.position.y + pushAmount.y;
//
//				if (!gameField.isInsideWall(newX, newY, playerGameObject.getSize())) {
//					playerGameObject.position.set(newX, newY);
//				}
//			}
//			else {
//				if (flickerTimer <= 0 && !player.isCanInvis()) {
//					System.out.println("YOU DIED!");
//					hud.updatePlayerHP(1);
//					flickerTimer = 3f;
//					SoundController.playPlayerHitSound(1);
//				}
//			}
//		}
//	}

//	private void updatePlayerPosition(float delta) {
//		float velocityX = player.getPlayerGameObject().velocity.x;
//		float velocityY = player.getPlayerGameObject().velocity.y;
//		float maxSpeed = player.getPlayerGameObject().getSpeed();
//
//		float diagonalMultiplier = 1.0f;
//		float currentSpeedSqr = velocityX * velocityX + velocityY * velocityY;
//
//		if (currentSpeedSqr > (maxSpeed * maxSpeed) + 0.001f) {
//			float currentSpeed = (float) Math.sqrt(currentSpeedSqr);
//			diagonalMultiplier = maxSpeed / currentSpeed;
//		}
//
//		float moveAmountX = velocityX * diagonalMultiplier * delta;
//		float nextX = player.getPlayerGameObject().position.x + moveAmountX;
//		float currentY = player.getPlayerGameObject().position.y;
//
//		if (!this.gameField.isInsideWall(nextX, currentY, this.player.getPlayerGameObject().getSize())) {
//			this.player.getPlayerGameObject().position.x = nextX;
//		}
//
//		float currentX = player.getPlayerGameObject().position.x;
//		float moveAmountY = velocityY * diagonalMultiplier * delta;
//		float nextY = player.getPlayerGameObject().position.y + moveAmountY;
//
//		if (!this.gameField.isInsideWall(currentX, nextY, this.player.getPlayerGameObject().getSize())) {
//			player.getPlayerGameObject().position.y = nextY;
//		}
//	}
}

