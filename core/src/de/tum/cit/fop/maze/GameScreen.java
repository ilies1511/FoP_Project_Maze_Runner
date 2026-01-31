package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.FilmGrainEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.Gameplay.MazeGenerator;
import de.tum.cit.fop.maze.Gameplay.WaveManager;
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.MVC.MapRenderer;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.UI.*;
import de.tum.cit.fop.maze.game_saves.SaveManager;
import de.tum.cit.fop.maze.game_saves.mementos.GameFieldMemento;
import de.tum.cit.fop.maze.game_saves.mementos.GameScreenMemento;
import de.tum.cit.fop.maze.game_saves.mementos.PlayerMemento;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Bouncer;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Junkie;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Tourist;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.traps.TrapCreator;

import java.util.function.Function;

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
    private final MapRenderer mapRenderer;
    private final GameController gameController;
    private GameField gameField;
    private boolean isPaused = false;
    private boolean isInProgression = false;
    private boolean isInDevConsole = false;
    private int currentStoryLevelIndex = 0;
    private final Viewport viewport;
    private final AchievementUI achievementUI;

    private final VfxManager vfxManager;

    /**
     * Constructor for GameScreen. Initializes the game world, UI, and effects.
     * @param game The main game class.
     */
    public GameScreen(MazeRunnerGame game) {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        VignettingEffect vignette = new VignettingEffect(false);
        vignette.setIntensity(1.2f);
        BloomEffect bloom = new BloomEffect();
        bloom.setBloomIntensity(1.5f);
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
        viewport = new FitViewport(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
        stage = new Stage(viewport); // Create a stage for UI elements
        devStage = new Stage(viewport);
        achievementUI = new AchievementUI(stage, game, soundController);
        achievementUI.startAchievement();
        this.player = new Player(this); //create an instance of the player
        // Create and configure the camera for the game view
        this.camera = new Camera();


        this.hud = new HUD(player, stage, game, this);

        progressionOverlay = new ProgressionOverlay(game, stage, this);
        devConsole = new DevConsole(game, this, hud, devStage, player);

        pausedScreen = new PausedOverlay(game, stage, this);
        this.mapRenderer = new MapRenderer(game);
        this.gameController = new GameController(game, player, hud, gameField, soundController, this);

        if (game.getGameMode() == GameMode.ENDLESS_MODE) {
            WaveManager waveManager = new WaveManager(gameField, System.currentTimeMillis());
            this.gameController.setWaveManager(waveManager);
        }

    }

    /**
     * Constructor for loading a saved game state.
     * @param game The main game class.
     * @param memento The memento containing saved state.
     */
    public GameScreen(MazeRunnerGame game, GameScreenMemento memento) {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        VignettingEffect vignette = new VignettingEffect(false);
        vignette.setIntensity(1.2f);
        BloomEffect bloom = new BloomEffect();
        bloom.setBloomIntensity(1.5f);
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

        // Restore game field
        this.gameField = SaveManager.restoreGameField(memento.gameField);

        viewport = new FitViewport(game.getGameInfo().getSettings().getResolutionWidth(),
                game.getGameInfo().getSettings().getResolutionHeight());
        stage = new Stage(viewport);
        devStage = new Stage(viewport);
        achievementUI = new AchievementUI(stage, game, soundController);
        achievementUI.startAchievement();

        this.player = new Player(this);

        // Restore player state
        this.player.loadFromMemento(memento.player);

        this.camera = new Camera();
        this.hud = new HUD(player, stage, game, this);

        progressionOverlay = new ProgressionOverlay(game, stage, this);
        devConsole = new DevConsole(game, this, hud, devStage, player);
        pausedScreen = new PausedOverlay(game, stage, this);
        this.mapRenderer = new MapRenderer(game);
        this.gameController = new GameController(game, player, hud, gameField, soundController, this);

        // Restore story level index
        this.currentStoryLevelIndex = memento.currentStoryLevelIndex;

        // Restore game mode
        try {
            GameMode mode = GameMode.valueOf(memento.gameMode);
            game.setGameMode(mode);

            if (mode == GameMode.ENDLESS_MODE) {
                WaveManager waveManager = new WaveManager(gameField, System.currentTimeMillis());
                this.gameController.setWaveManager(waveManager);
            }
        } catch (Exception e) {
            System.err.println("Failed to restore game mode: " + e.getMessage());
        }

        System.out.println("Game loaded successfully!");
    }

    /**
     * Initializes the game mode (Levels, Endless, Story).
     * Loads appropriate maps or generates mazes.
     */
    private void init_mode() {
        GameMode gameMode = game.getGameMode();

        if (gameMode == GameMode.LEVELS_MODE) {
            this.gameField = MazeParser.loadMap(this.game.getGameInfo().getSelectedLevel(), this.game);
        } else if (gameMode == GameMode.ENDLESS_MODE) {
            this.gameField = MazeGenerator.generateMaze(15, 15, game);
        } else if (gameMode == GameMode.STORY_MODE) {
            this.gameField = MazeParser.loadMap(MazeRunnerGame.STORY_LEVELS[0], this.game);
	        if (this.gameField != null) {
				spawnObjects(1, pos -> new Key(pos));
		        spawnObjects(5, pos -> TrapCreator.createRandomTrap(pos));
		        spawnRandomEnemies(35);
	        }
        }
        if (this.gameField == null) {
            System.err.println("CRITICAL: Map failed to load for mode " + gameMode);
            this.gameField = new GameField(15, 15);
        } else {
            System.out.println("Map loaded successfully for " + gameMode);
            System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
        }
    }

    private void handleGlobalInput() {
        if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getConsoleKey()) && !isInDevConsole) {
            devConsole.showConsole();
            isInDevConsole = true;
        } else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getConsoleKey()) && isInDevConsole) {
            devConsole.hideConsole();
            isInDevConsole = false;
        }
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {


        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

        handleGlobalInput();

        if (!isPaused) {
            this.camera.update(delta, this.player.position, this.gameField.getWidth(), this.gameField.getHeight());

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                pausedScreen.showPausedMenu();
            } else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getProgressionTreeKey()) && !isInDevConsole) {
                progressionOverlay.showProgressionMenu();
                isPaused = true;
                isInProgression = true;
            }

            if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomInKey()))
                camera.zoom_in();
            if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomOutKey()))
                camera.zoom_out();

            gameController.update(delta);
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isInProgression) {
                pausedScreen.hidePausedMenu();
            } else if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getProgressionTreeKey()) && isInProgression) {
                progressionOverlay.hideProgressionMenu();
                isInProgression = false;
            }
        }


        game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);

        mapRenderer.render(gameField, player, delta);
        game.getSpriteBatch().setShader(null);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        devStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();

        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
        devStage.getViewport().apply();
        devStage.draw();

    }

    /**
     * Gets the current game field.
     * @return The game field.
     */
    public GameField getGameField() {
        return gameField;
    }

    /**
     * Sets a new game field.
     * @param newGameField The new game field to set.
     */
    public void setGameField(GameField newGameField) {
        this.gameField = newGameField;
    }

    private void addTestObjects() {
        gameField.addGameObject(new Bouncer(new Vector2(4f, 9f)));
        gameField.addGameObject(new Bouncer(new Vector2(9f, 3f)));
        gameField.addGameObject(new Key(new Vector2(5f, 5f)));
    }

    /**
     * Gets the player entity.
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
        vfxManager.resize(width, height);
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

    /**
     * Checks if the game is paused.
     * @return True if paused.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Sets the pause state of the game.
     * @param paused True to pause, false to unpause.
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Gets the sound controller.
     * @return The sound controller.
     */
    public SoundController getSoundController() {
        return soundController;
    }

    /**
     * Gets the HUD.
     * @return The HUD.
     */
    public HUD getHUD() {
        return hud;
    }

    /**
     * Gets the game controller.
     * @return The game controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Loads the next level in story mode.
     * Triggers win screen if all levels are completed.
     */
    public void loadNextStoryLevel() {
        currentStoryLevelIndex++;
        if (currentStoryLevelIndex >= MazeRunnerGame.STORY_LEVELS.length) {
            game.goToGameWonScreen();
            soundController.dispose();
            return;
        }
        String nextMapPath = MazeRunnerGame.STORY_LEVELS[currentStoryLevelIndex];
        System.out.println("Loading Level: " + nextMapPath);
        GameField newMap = MazeParser.loadMap(nextMapPath, game);

        this.gameField = newMap;
        gameController.setGameField(newMap);

	    if (newMap != null) {
		    int enemyCount = 40 + (currentStoryLevelIndex * 2);
		    spawnObjects(1, pos -> new Key(pos));
		    spawnObjects(5, pos -> TrapCreator.createRandomTrap(pos));
		    spawnRandomEnemies(enemyCount);
	    }

        if (newMap != null) {
            Vector2 start = gameField.getPlayerStartPos();
            if (start != null) {
                System.out.println("Player Start Position at: " + gameField.getPlayerStartPos());
                player.setPosition(gameField.getPlayerStartPos());
            } else {
                Vector2 fallBack = gameField.getTileTypeCoords(newMap, TileType.EMPTY);
                if (fallBack != null)
                    player.setPosition(fallBack);
                else
                    player.setPosition(new Vector2(1, 1));
            }
        } else {
            System.err.println("CRITICAL: Map failed to load");
        }
        player.setCurrentKeys(0);
        player.setHasKeys(false);

        player.setRequiredKeys(newMap.getRequiredKeys());


        hud.updatePlayerKeys();
    }

    /**
     * Gets the camera.
     * @return The camera.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Gets the viewport.
     * @return The viewport.
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Gets the achievement UI.
     * @return The achievement UI.
     */
    public AchievementUI getAchievementUI() {
        return achievementUI;
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        try {
            GameFieldMemento gameFieldMemento = SaveManager.createGameFieldMemento(this.gameField);
            PlayerMemento playerMemento = this.player.getMemento();

            String selectedLevel = game.getGameInfo().getSelectedLevel();

            GameScreenMemento memento = new GameScreenMemento(
                    gameFieldMemento,
                    playerMemento,
                    game.getGameMode().name(),
                    this.currentStoryLevelIndex,
                    selectedLevel
            );

            SaveManager.saveGame(memento, game.getGameMode());
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }


	private boolean isSafeToPlace(GameField gameField, TileType tile) {
		return tile != TileType.WALL &&
				tile != TileType.WALL_1 &&
				tile != TileType.WALL_2 &&
				tile != TileType.WALL_3 &&
				tile != TileType.WALL_4 &&
				tile != TileType.WALL_5 &&
				tile != TileType.WALL_6 &&
				tile != TileType.WALL_7 &&
				tile != TileType.WALL_8 &&
				tile != TileType.EXIT &&
				tile != TileType.DECOR_1 &&
				tile != TileType.DECOR_2 &&
				tile != TileType.DECOR_3 &&
				tile != TileType.DECOR_4 &&
				tile != TileType.DECOR_5 &&
				tile != TileType.DECOR_6 &&
				tile != TileType.DECOR_7 &&
				tile != TileType.DECOR_8 &&
				tile != TileType.DECOR_9 &&
				tile != TileType.DECOR_10 &&
				tile != TileType.CASKET &&
				tile != TileType.ENTRY_POINT;
	}

	/**
	 * Randomly spawns a specific number of enemies on valid floor tiles.
	 * @param count The number of enemies to spawn.
	 */
	private void spawnRandomEnemies(int count) {
		java.util.Random random = new java.util.Random();
		int spawned = 0;
		int attempts = 0;

		while (spawned < count && attempts < 1000) {
			int x = random.nextInt(gameField.getWidth());
			int y = random.nextInt(gameField.getHeight());

			TileType tile = gameField.getTile(x, y);

			if (isSafeToPlace(gameField, tile)) {
				Vector2 pos = new Vector2(x, y);
				int roll = MathUtils.random(99);

				if (roll < 10) {
					gameField.addGameObject(new Bouncer(pos));
				} else if (roll < 50) {
					gameField.addGameObject(new Tourist(pos));
				} else {
					gameField.addGameObject(new Junkie(pos));
				}
				spawned++;
			}
			attempts++;
		}
	}

	/**
	 * Generic method to spawn GameObjects on empty tiles safely.
	 * @param count Number of objects to spawn
	 * @param objectFactory Lambda to create the object
	 */
	private void spawnObjects(int count, Function<Vector2, GameObject> objectFactory) {
		for (int i = 0; i < count; i++) {
			Vector2 position = gameField.getRandomEmptySpawnTile();

			if (position != null) {
				GameObject obj = objectFactory.apply(position);
				gameField.addGameObject(obj);
			} else {
				break;
			}
		}
	}
}
