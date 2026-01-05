package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.Gameplay.MazeGenerator;
import de.tum.cit.fop.maze.Gameplay.WaveManager;
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.MVC.MapRenderer;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
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
    private final Stage devStage;
	private final HUD hud;
	private final PausedOverlay pausedScreen;
	private final ProgressionOverlay progressionOverlay;
	private final DevConsole devConsole;
	private final SoundController soundController;
	private GameField gameField;
	private boolean isPaused = false;
	private boolean isInProgression = false;
	private boolean isInDevConsole = false;
	private final MapRenderer mapRenderer;
	private final GameController gameController;
	private int currentStoryLevelIndex = 0;
    private Viewport viewport;

    private VfxManager vfxManager;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     */
	private void init_mode() {
		GameMode gameMode =  game.getGameMode();

		if (gameMode == GameMode.LEVELS_MODE) {
			this.gameField = MazeParser.loadMap(this.game.getGameInfo().getSelectedLevel(), this.game);
			addTestObjects();
		}
		else if (gameMode == GameMode.ENDLESS_MODE) {
			this.gameField = MazeGenerator.generateMaze(15, 15, game);
		}
		else if (gameMode == GameMode.STORY_MODE) {
			this.gameField = MazeParser.loadMap(MazeRunnerGame.STORY_LEVELS[0], this.game);
		}
		if (this.gameField == null) {
			System.err.println("CRITICAL: Map failed to load for mode " + gameMode);
			this.gameField = new GameField(15, 15);
		}
		else {
			System.out.println("Map loaded successfully for " + gameMode);
			System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
		}
	}

	public GameScreen(MazeRunnerGame game) {
		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
		VignettingEffect vignette = new VignettingEffect(false);
		vignette.setIntensity(1.2f);
		BloomEffect bloom = new BloomEffect();
		bloom.setBloomIntensity(2.2f);
		bloom.setBlurPasses(6);
		bloom.setThreshold(0.7f);
		FilmGrainEffect filmGrainEffect = new FilmGrainEffect();
		filmGrainEffect.setNoiseAmount(0.3f);
		vfxManager.addEffect(filmGrainEffect);
		vfxManager.addEffect(bloom);
		vfxManager.addEffect(vignette);
        soundController = new SoundController();
        soundController.playBackgroundMusic(0.5f);
		this.game = game;
		Drawable.initializeTextureMap(game);
		init_mode();
		this.player = new Player(this); //create an instance of the player
		// Create and configure the camera for the game view
		this.camera = new Camera();

        viewport = new FitViewport(2000, 1200); // TODO maybe add camera in the brackets
		stage = new Stage(viewport); // Create a stage for UI elements
        devStage = new Stage(viewport);

        this.hud = new HUD(player, stage, game, this);

        progressionOverlay = new ProgressionOverlay(stage, game, this, player, hud);
        devConsole = new DevConsole(game, this, devStage, player, hud);

        pausedScreen = new PausedOverlay(stage, game, this);
		this.mapRenderer = new MapRenderer(game);
		this.gameController = new GameController(game, player, hud, gameField, soundController, this);

		if (game.getGameMode() == GameMode.ENDLESS_MODE) {
			WaveManager waveManager = new WaveManager(gameField, System.currentTimeMillis());
			this.gameController.setWaveManager(waveManager);
		}
	}

	private void handleGlobalInput() {
		if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getConsoleKey()) && !isInDevConsole) {
			devConsole.showConsole();
			isInDevConsole = true;
		}
		else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getConsoleKey()) && isInDevConsole) {
			devConsole.hideConsole();
			isInDevConsole = false;
		}
	}

	// Screen interface methods with necessary functionality
	@Override
	public void render(float delta) {
        this.camera.update(delta, this.player.position, this.gameField.getWidth(), this.gameField.getHeight());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        devStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

		handleGlobalInput();

		if (!isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
				pausedScreen.showPausedMenu();
			}
			else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getProgressionTreeKey()) && !isInDevConsole) {
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
			else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getProgressionTreeKey()) && isInProgression) {
				progressionOverlay.hideProgressionMenu();
				isInProgression = false;
			}
		}


		game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);
        mapRenderer.render(gameField, player, delta);
        game.getSpriteBatch().setShader(null);

//		mapRenderer.render(gameController.getGameField, player, delta, playerHidden); // TODO: iziane 29.12



        stage.draw();

        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
        devStage.getViewport().apply();
        devStage.draw();

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
        vfxManager.resize(width, height);
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

	public void setGameField(GameField newGameField) {
		this.gameField = newGameField;
	}

	public void loadNextStoryLevel() {
		currentStoryLevelIndex++;
		if (currentStoryLevelIndex >= MazeRunnerGame.STORY_LEVELS.length) {
			soundController.playWinSound(1f);
			game.goToGameWonScreen();
			return;
		}
		String nextMapPath = MazeRunnerGame.STORY_LEVELS[currentStoryLevelIndex];
		System.out.println("Loading Level: " + nextMapPath);
		GameField newMap = MazeParser.loadMap(nextMapPath, game);

		this.gameField = newMap;
		gameController.setGameField(newMap);

		if (newMap != null) {
			Vector2 start = gameField.getPlayerStartPos();
			if (start != null) {
				System.out.println("Player Start Position at: " + gameField.getPlayerStartPos());
				player.setPosition(gameField.getPlayerStartPos());
			}
			else {
				Vector2 fallBack = gameField.getTileTypeCoords(newMap, TileType.EMPTY);
				if (fallBack != null)
					player.setPosition(fallBack);
				else
					player.setPosition(new Vector2(1, 1));
			}
		}
		else {
			System.err.println("CRITICAL: Map failed to load");
		}
//		player.setCurrentKeys(0);
//		player.setHasKeys(false);
//		hud.renderHUD();
	}

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
