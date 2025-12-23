package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.UI.MenuScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.util.Arrays;
import java.util.List;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
	// Screens IMPORTANT: add screens to disposeAllScreens when creating a new one
	private MenuScreen menuScreen;
	private GameScreen gameScreen;
	private SettingsScreen settingsScreen;

	// Sprite Batch for rendering
	private SpriteBatch spriteBatch;

	// UI Skin
	private Skin skin;

	// Walking Animations
	private Animation<TextureRegion> characterDownAnimation, characterUpAnimation, characterLeftAnimation, characterRightAnimation;
	// Jumping Animations
	private Animation<TextureRegion> characterDownJumpAnimation, characterUpJumpAnimation, characterLeftJumpAnimation, characterRightJumpAnimation;
	// Sad or Damage Animations
	private Animation<TextureRegion> characterDownSad, characterUpSad, characterLeftSad, characterRightSad;
	// Happy/Win/PowerUp Animations
	private Animation<TextureRegion> characterDownWin, characterUpWin, characterLeftWin, characterRightWin;

	// Maze Textures
	private TextureRegion wallTile;
	private TextureRegion emptyTile;


	//TODO (Connect Parser to Game Assets): - iziane Texture Regions for our Objects
	private TextureRegion keyTexture;
	private TextureRegion trapTexture;


	//UI
	private Texture spriteSheet;
	private Texture effectsSheet;
	private TextureRegion woodenSign;
	private TextureRegion bloodEffect;
	private TextureRegion poisonEffect;

	// GameInfo with all the useful variables
	private final GameInfo gameInfo;

	public GameInfo getGameInfo() {
		return this.gameInfo;
	}

	/**
	 * Constructor for MazeRunnerGame.
	 *
	 * @param fileChooser The file chooser for the game, typically used in desktop environment.
	 */
	public MazeRunnerGame(NativeFileChooser fileChooser) {
		super();
		this.gameInfo = new GameInfo();
	}

	/**
	 * Called when the game is created. Initializes the SpriteBatch and Skin.
	 */
	@Override
	public void create() {
		spriteBatch = new SpriteBatch(); // Create SpriteBatch

		effectsSheet = new Texture(Gdx.files.internal("statusEffects.png"));
		spriteSheet = new Texture(Gdx.files.internal("UI.png"));
		woodenSign = new TextureRegion(spriteSheet, 0, 128, 128, 48);
		bloodEffect = new TextureRegion(effectsSheet, 128, 0, 32, 32);
		poisonEffect = new TextureRegion(effectsSheet, 0, 0, 32, 32);


		skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
		this.loadCharacterAnimation(); // Load character animation
		this.loadMazeTiles();

		// Play some background music
		// Background sound
		Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();


		// TODO: iziane
		this.loadObjectTextures();
		goToMenu(); // Navigate to the menu screen
	}


	// TODO (Connect Parser to Game Assets): iziane
	// TBD with Nikita
	private void loadObjectTextures() {
		if (Gdx.files.internal("objects.png").exists()) {
			Texture objectsSheet = new Texture(Gdx.files.internal("objects.png"));
			TextureRegion[][] tmp = TextureRegion.split(objectsSheet, 16, 16);

			// TODO: iziane - for now dummy stuff
			this.keyTexture = tmp[0][0];
			this.trapTexture = tmp[0][1];
//			this.enemyTexture = tmp[0][2]; // TODO: iziane - TBD with Saad
		}
		else {
			Gdx.app.error("Assets", "objects.png not found! Using character texture as fallback.");
			this.keyTexture = characterDownAnimation.getKeyFrame(0);
			this.trapTexture = characterDownAnimation.getKeyFrame(0);
//			this.enemyTexture = characterDownAnimation.getKeyFrame(0);
		}
	}

	public TextureRegion getKeyTexture() {
		return keyTexture;
	}

	public TextureRegion getTrapTexture() {
		return trapTexture;
	}

//	public TextureRegion getEnemyTexture() {
//		return enemyTexture;
//	}

	public void disposeAllScreens() {
		List<Screen> allScreens = Arrays.asList(this.gameScreen, this.settingsScreen, this.menuScreen);
		for (Screen screen : allScreens) {
			if (screen != null) {
				screen.dispose();
				screen = null;
			}
		}
	}

	/**
	 * Switches to the menu screen.
	 */
	public void goToMenu() {
		disposeAllScreens();
		this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
	}

	/**
	 * Switches to the game screen.
	 */
	public void goToGame() {
		disposeAllScreens();
		this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
	}

	/**
	 * Switches to the settings screen.
	 */
	public void goToSettings() {
		disposeAllScreens();
		this.setScreen(new SettingsScreen(this)); // Set the current screen to GameScreen
	}

	/**
	 * Loads the character animation from the character.png file.
	 */
	private void loadCharacterAnimation() {
		Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

		int frameWidth = 16;
		int frameHeight = 32;

		//split up character.png to frames and add what each of them does
//
		// Split the big image into a grid
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

		// ROW 0: DOWN
		characterDownAnimation = new Animation<>(0.1f, getFrames(tmp[0], 0, 4)); // Walk 0-3
		characterDownJumpAnimation = new Animation<>(0.1f, getFrames(tmp[0], 4, 3)); // Jump 4-6
		characterDownSad = new Animation<>(0.2f, getFrames(tmp[0], 7, 3)); // Sad 7-9
		characterDownWin = new Animation<>(0.1f, getFrames(tmp[0], 10, 1)); // Win 10

		// ROW 1: RIGHT
		characterRightAnimation = new Animation<>(0.1f, getFrames(tmp[1], 0, 4));
		characterRightJumpAnimation = new Animation<>(0.1f, getFrames(tmp[1], 4, 3));
		characterRightSad = new Animation<>(0.2f, getFrames(tmp[1], 7, 3));
		characterRightWin = new Animation<>(0.1f, getFrames(tmp[1], 10, 1));

		// ROW 2: UP
		characterUpAnimation = new Animation<>(0.1f, getFrames(tmp[2], 0, 4));
		characterUpJumpAnimation = new Animation<>(0.1f, getFrames(tmp[2], 4, 3));
		characterUpSad = new Animation<>(0.2f, getFrames(tmp[2], 7, 3));
		characterUpWin = new Animation<>(0.1f, getFrames(tmp[2], 10, 1));

		// ROW 3: LEFT
		characterLeftAnimation = new Animation<>(0.1f, getFrames(tmp[3], 0, 4));
		characterLeftJumpAnimation = new Animation<>(0.1f, getFrames(tmp[3], 4, 3));
		characterLeftSad = new Animation<>(0.2f, getFrames(tmp[3], 7, 3));
		characterLeftWin = new Animation<>(0.1f, getFrames(tmp[3], 10, 1));

		// int animationFrames = 4;

		// libGDX internal Array instead of ArrayList because of performance
		//Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

		// Add all frames to the animation
		// for (int col = 0; col < animationFrames; col++) {
		//      walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
		//   }

		//  characterDownAnimation = new Animation<>(0.1f, walkFrames);
	}

	private void loadMazeTiles() {
		try {
			Texture wallTexture = new Texture(Gdx.files.internal("maze_wall.png"));
			Texture emptyTexture = new Texture(Gdx.files.internal("maze_empty.png"));

			this.wallTile = new TextureRegion(wallTexture);
			this.emptyTile = new TextureRegion(emptyTexture);
		} catch (Exception e) {
			System.err.println("Error loading maze tiles: " + e.getMessage());
			System.err.println("Make sure maze_wall.png and maze_empty.png exist in the assets folder");
		}
	}

	private Array<TextureRegion> getFrames(TextureRegion[] row, int start, int count) {
		Array<TextureRegion> frames = new Array<>();
		for (int i = start; i < start + count; i++) {
			frames.add(row[i]);
		}
		return frames;
	}

	/**
	 * Cleans up resources when the game is disposed.
	 */
	@Override
	public void dispose() {
		getScreen().hide(); // Hide the current screen
		getScreen().dispose(); // Dispose the current screen
		spriteBatch.dispose(); // Dispose the spriteBatch
		skin.dispose(); // Dispose the skin
	}

	// Getter methods
	public Skin getSkin() {
		return skin;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public Animation<TextureRegion> getCharacterDownAnimation() {
		return characterDownAnimation;
	}

	public Animation<TextureRegion> getCharacterUpAnimation() {
		return characterUpAnimation;
	}

	public Animation<TextureRegion> getCharacterLeftAnimation() {
		return characterLeftAnimation;
	}

	public Animation<TextureRegion> getCharacterRightAnimation() {
		return characterRightAnimation;
	}

	public Animation<TextureRegion> getCharacterDownJumpAnimation() {
		return characterDownJumpAnimation;
	}

	public Animation<TextureRegion> getCharacterUpJumpAnimation() {
		return characterUpJumpAnimation;
	}

	public Animation<TextureRegion> getCharacterLeftJumpAnimation() {
		return characterLeftJumpAnimation;
	}

	public Animation<TextureRegion> getCharacterRightJumpAnimation() {
		return characterRightJumpAnimation;
	}

	public Animation<TextureRegion> getCharacterDownSad() {
		return characterDownSad;
	}

	public Animation<TextureRegion> getCharacterUpSad() {
		return characterUpSad;
	}

	public Animation<TextureRegion> getCharacterLeftSad() {
		return characterLeftSad;
	}

	public Animation<TextureRegion> getCharacterRightSad() {
		return characterRightSad;
	}

	public Animation<TextureRegion> getCharacterDownWin() {
		return characterDownWin;
	}

	public Animation<TextureRegion> getCharacterUpWin() {
		return characterUpWin;
	}

	public Animation<TextureRegion> getCharacterLeftWin() {
		return characterLeftWin;
	}

	public Animation<TextureRegion> getCharacterRightWin() {
		return characterRightWin;
	}

	// Maze Tile Getters
	public TextureRegion getWallTile() {
		return wallTile;
	}

	public TextureRegion getEmptyTile() {
		return emptyTile;
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public TextureRegion getWoodenSign() {
		return woodenSign;
	}

	public TextureRegion getPoisonEffect() {
		return poisonEffect;
	}

	public TextureRegion getBloodEffect() {
		return bloodEffect;
	}
}
