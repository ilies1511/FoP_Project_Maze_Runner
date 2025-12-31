package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.Gameplay.WaveManager;
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.MVC.MapRenderer;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.UI.DevConsole;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.UI.PausedOverlay;
import de.tum.cit.fop.maze.UI.ProgressionOverlay;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Bouncer;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Key;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

	private final MazeRunnerGame game;
	private final Camera camera;
	private final Player player;
	private final Stage stage;
	private final HUD hud;
	private final PausedOverlay pausedScreen;
	private final ProgressionOverlay progressionOverlay;
	private final DevConsole devConsole;
	private final SoundController soundController;
	private GameField gameField;
	private boolean isPaused = false;
	private boolean isInProgression = false;
	private boolean isInDevConsole = false;
	private final boolean isSurvivalMode;
	private final MapRenderer mapRenderer;
	private final GameController gameController;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     */

	public GameScreen(MazeRunnerGame game, boolean isSurvivalMode) {
        soundController = new SoundController();
        soundController.playBackgroundMusic(0.5f);
		this.game = game;
		this.isSurvivalMode = isSurvivalMode;

		Drawable.initializeTextureMap(game);


		this.gameField = MazeParser.loadMap(this.game.getGameInfo().getSelectedLevel(), this.game);
		if (this.gameField == null) {
			System.err.println("Map failed to load! Check file path.");
			this.gameField = new GameField(5, 5);
		}
		else {
			System.out.println("Map loaded successfully!");
			System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
		}

		if (!isSurvivalMode) {
			addTestObjects();
		}

		this.player = new Player(this); //create an instance of the player

		// Create and configure the camera for the game view
		this.camera = new Camera();

		Viewport viewport = new FitViewport(2000, 1200); // TODO maybe add camera in the brackets
		stage = new Stage(viewport); // Create a stage for UI elements

        this.hud = new HUD(player, stage, game, this);



        progressionOverlay = new ProgressionOverlay(stage, game, this, player, hud);
        devConsole = new DevConsole(game, this, stage, player, hud);

        pausedScreen = new PausedOverlay(stage, game, this);
		this.mapRenderer = new MapRenderer(game);
		this.gameController = new GameController(game, player, hud, gameField, soundController, this);

		if (isSurvivalMode) {
			WaveManager waveManager = new WaveManager(gameField, System.currentTimeMillis());
			this.gameController.setWaveManager(waveManager);
		}
	}

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
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

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

			if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomInKey()))
				camera.zoom_in();
			if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomOutKey()))
				camera.zoom_out();

			gameController.update(delta);
		}
		else {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isInProgression) {
				pausedScreen.hidePausedMenu();
			}
			else if (Gdx.input.isKeyJustPressed(Input.Keys.I) && isInProgression) {
				progressionOverlay.hideProgressionMenu();
				isInProgression = false;
			}
		}

        this.camera.update(delta, this.player.position, this.gameField.getWidth(), this.gameField.getHeight());
		game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);
//		mapRenderer.render(gameController.getGameField, player, delta, playerHidden); // TODO: iziane 29.12
		mapRenderer.render(gameField, player, delta);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	public GameField getGameField() {
		return gameField;
	}

	private void addTestObjects() {
		gameField.addGameObject(new Bouncer(new Vector2(4f, 9f)));
		gameField.addGameObject(new Bouncer(new Vector2(9f, 3f)));
		gameField.addGameObject(new Key(new Vector2(5f, 5f)));
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
//		if (soundController != null) {
//			soundController.dispose();
//		}
////		if (gameController != null && gameController.getSoundController() != null) {
////			gameController.getSoundController().dispose();
////		}
	}

	@Override
	public void dispose() {
//		stage.dispose();
//		if (soundController != null) {
//			soundController.dispose();
//		}
//		if (gameController != null && gameController.getSoundController() != null) {
//			gameController.getSoundController().dispose();
//		}
	}

	public boolean isPaused() {
		return isPaused;
	}

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public HUD getHUD() {
        return hud;
    }

    public GameController getGameController() {
        return gameController;
    }
    // Additional methods and logic can be added as needed for the game screen

	public boolean isSurvivalMode() {
		return this.isSurvivalMode;
	}

	public void setGameField(GameField newGameField) {
		this.gameField = newGameField;
	}

}
