package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.GameObject;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

	private final MazeRunnerGame game;
	private final OrthographicCamera camera;
	private final BitmapFont font;

	private float sinusInput = 0f;
	private float animationTime = 0f;

	private Animation<TextureRegion> currentAnimation;

	private final Player player;


	private final Stage stage;

	private Entity playerEntity;

	private GameField gameField;

	private final HUD hud;
	private final PausedOverlay pausedScreen;

	private boolean isPaused = false;


	//  For testing
	int i = 0;

	/**
	 * Constructor for GameScreen. Sets up the camera and font.
	 *
	 * @param game The main game class, used to access global resources and methods.
	 */
	public GameScreen(MazeRunnerGame game) {
		this.game = game;

		this.player = new Player(); //create an instance of the player

//        this.gameField = new GameField();
		this.gameField = MazeParser.loadMap("level-1.properties", this.game);
		if (this.gameField == null) {
			System.err.println("Map failed to load! Check file path.");
			this.gameField = new GameField(5, 5);
		}
		else {
			System.out.println("Map loaded successfully!");
			System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
		}

		// Use startpos from the GameField
		this.playerEntity = new Entity(this.gameField.getPlayerStartPos(), new Vector2(0f, 0f), 3f, new Vector2(0.4f, 0.4f));

		float viewportWidth = 25f;  // Visible world width
		float viewportHeight = 18.75f; // Visible world height

		// Create and configure the camera for the game view
		camera = new OrthographicCamera();
		camera.setToOrtho(false, viewportWidth, viewportHeight);
		camera.zoom = 0.75f;

		// Start by looking down
		currentAnimation = game.getCharacterDownAnimation();

		// Get the font from the game's skin
		font = game.getSkin().getFont("vampire-font");

		Viewport viewport = new ScreenViewport(); // TODO maybe add camera in the brackets
		stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

		this.hud = new HUD(player, stage, game, this);
		pausedScreen = new PausedOverlay(stage, game, this);
//        shapeRenderer = new ShapeRenderer();

	}


	// Screen interface methods with necessary functionality
	@Override
	public void render(float delta) {
		if (!isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				pausedScreen.showPausedMenu();
			}
			ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

			// Input Handling New Class
			handleInput(delta);

			// Move all the entities
			updateEntitiesPositions(delta);

			// Update camera to move with the player
			moveCamera(delta);
			animationTime += delta;
			i++;
			if (i == 1) {
				player.getPoisoned(5f);
				player.getBuffed(2f);
				hud.updatePlayerStatusEffects();
				hud.updatePlayerHP(player.getDamage(1));

			}
			// Set up and begin drawing with the sprite batch

		}
		else if (isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				pausedScreen.hidePausedMenu();
			}

		}

		game.getSpriteBatch().setProjectionMatrix(camera.combined);

		game.getSpriteBatch().begin(); // Important to call this before drawing anything


		drawGameField();

		//TODO (Connect Parser to Game Assets): iziane
		for (GameObject obj : gameField.getGameObjects()) {
			obj.draw(game.getSpriteBatch());
		}

		game.getSpriteBatch().draw(
				currentAnimation.getKeyFrame(animationTime, true),
				playerEntity.position.x,
				playerEntity.position.y,
				0.4f,
				0.8f
		);

		game.getSpriteBatch().end(); // Important to call this after drawing everything

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
// For Testing


	}

	private void handleInput(float delta) {
		boolean isMoving = false;

		// Speed boost // TODO: Add boost to keybindings, handle it via playerEntity
//        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
//            currentSpeed *= 1.8f;
//        }

		// --- MOVEMENT LOGIC ---

		// Y-Axis
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
			playerEntity.velocity.y = playerEntity.speed;
//            yPlayer += currentSpeed * delta;
			currentAnimation = game.getCharacterUpAnimation();
			isMoving = true;
		}

		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
			playerEntity.velocity.y = -playerEntity.speed;
//            yPlayer -= currentSpeed * delta;
			currentAnimation = game.getCharacterDownAnimation();
			isMoving = true;
		}

		else {
			playerEntity.velocity.y = 0f;
		}

		// X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
		if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
			playerEntity.velocity.x = -playerEntity.speed;
//            xPlayer -= currentSpeed * delta;
			currentAnimation = game.getCharacterLeftAnimation();
			isMoving = true;
		}

		else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
			playerEntity.velocity.x = playerEntity.speed;
//            xPlayer += currentSpeed * delta;
			currentAnimation = game.getCharacterRightAnimation();
			isMoving = true;
		}

		else {
			playerEntity.velocity.x = 0f;
		}

		// Jump (Fix proper logic when final image of dracula is confirmed) // TODO: handle via playerEntity, add to keybindings
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            // Check which direction we are currently facing/moving to pick the right jump
//            if (currentAnimation == game.getCharacterUpAnimation() || currentAnimation == game.getCharacterUpJumpAnimation()) {
//                currentAnimation = game.getCharacterUpJumpAnimation();
//            } else if (currentAnimation == game.getCharacterLeftAnimation() || currentAnimation == game.getCharacterLeftJumpAnimation()) {
//                currentAnimation = game.getCharacterLeftJumpAnimation();
//            } else if (currentAnimation == game.getCharacterRightAnimation() || currentAnimation == game.getCharacterRightJumpAnimation()) {
//                currentAnimation = game.getCharacterRightJumpAnimation();
//            } else {
//                currentAnimation = game.getCharacterDownJumpAnimation();
//            }
//            isMoving = true; // Keep animating while jumping
//        }

		// Victory pose
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			currentAnimation = game.getCharacterDownWin();
			isMoving = true;
		}
		// Loser pose
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			currentAnimation = game.getCharacterDownSad();
			isMoving = true;
		}

		// Animation Timer
		if (isMoving) {
			animationTime += delta;
		}
		else {
			animationTime = 0; // Reset to standing frame when not moving
		}

		// Go to Menu
	}

	private void updateEntitiesPositions(float delta) {
		// X and Y are calculated separately to avoid going through walls diagonally

		float nextX = playerEntity.position.x + playerEntity.velocity.x * delta;
		float currentY = playerEntity.position.y;

		if (!this.gameField.isInsideWall(nextX, currentY, this.playerEntity.getSize())) {
			this.playerEntity.position.x = nextX;
		}

		float currentX = playerEntity.position.x;
		float nextY = playerEntity.position.y + playerEntity.velocity.y * delta;

		if (!this.gameField.isInsideWall(currentX, nextY, this.playerEntity.getSize())) {
			playerEntity.position.y = nextY;
		}
	}

	private void moveCamera(float delta) {
		camera.position.x = playerEntity.position.x;
		camera.position.y = playerEntity.position.y;
		camera.update();
	}

	private void drawGameField() {
		for (int y = 0; y < gameField.getHeight(); y++) {
			for (int x = 0; x < gameField.getWidth(); x++) {
				TileType tileType = gameField.getTile(x, y);
				if (tileType == TileType.WALL) {
					game.getSpriteBatch().draw(
							game.getWallTile(),
							x, y, 1, 1
					);
				}
				else if (tileType == TileType.EMPTY) {
					game.getSpriteBatch().draw(
							game.getEmptyTile(),
							x, y, 1, 1
					);
				}
			}
		}
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
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	public void setPaused(boolean paused) {
		isPaused = paused;
	}

	public boolean isPaused() {
		return isPaused;
	}
	// Additional methods and logic can be added as needed for the game screen
}
