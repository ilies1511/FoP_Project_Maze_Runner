package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.UI.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.util.Arrays;
import java.util.List;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // GameInfo with all the useful variables
    private final GameInfo gameInfo;
    // Screens IMPORTANT: add screens to disposeAllScreens when creating a new one
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private BackgroundStoryScreen backgroundStoryScreen;
    private GameLostScreen gameLostScreen;
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
    //Enemy(Tourist)
    private Animation<TextureRegion> touristDown, touristUp, touristLeft, touristRight;
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
    private TextureRegion freezeEffect;
    // Collectables
    private Animation<TextureRegion> keyShineAnimation;
    private Animation<TextureRegion> vampiricHeartAnimation;
    // Traps
    private Animation<TextureRegion> garlicTrapAnimation;
    private Animation<TextureRegion> freezeTrapAnimation;
	// BEGIN -- Exit
	private TextureRegion exitClosedTexture;
	private TextureRegion exitOpenTexture;
	// END -- Exit



    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.gameInfo = new GameInfo();
    }

    public GameInfo getGameInfo() {
        return this.gameInfo;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        // Initialize settings AFTER creating game (so that loading works)
        this.gameInfo.initializeSettings();

        spriteBatch = new SpriteBatch(); // Create SpriteBatch

        effectsSheet = new Texture(Gdx.files.internal("statusEffects.png"));
        spriteSheet = new Texture(Gdx.files.internal("UI.png"));
        woodenSign = new TextureRegion(spriteSheet, 0, 128, 128, 48);
        bloodEffect = new TextureRegion(effectsSheet, 128, 0, 32, 32);
        poisonEffect = new TextureRegion(effectsSheet, 0, 0, 32, 32);
        freezeEffect = new TextureRegion(effectsSheet, 160, 0, 32, 32);


        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation
        this.loadTouristAnimation();
        this.loadMazeTiles();
        this.loadKeyShineAnimation();
        this.loadGarlicTrapAnimation();
        this.loadFreezeTrapAnimation();
        this.loadVampiricHeartAnimation();


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

			if (tmp[0].length > 2) {
				this.exitClosedTexture = tmp[0][2];
			}
			else {
				this.exitClosedTexture = tmp[0][0]; // Fallback to Key if missing
			}
			if (tmp[0].length > 3) {
				this.exitOpenTexture = tmp[0][3];
			}
			else {
				this.exitOpenTexture = tmp[0][1]; // Fallback to Trap if missing
			}

        } else {
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
        List<Screen> allScreens = Arrays.asList(this.gameScreen, this.backgroundStoryScreen, this.menuScreen, this.gameLostScreen);
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
        this.setScreen(new GameScreen(this, false)); // Set the current screen to GameScreen
    }



    public void goToBackgroundStory(){
        disposeAllScreens();
        this.setScreen(new BackgroundStoryScreen(this)); // Set the current screen to GameScreen
    }

    public void goToGameLostScreen(){
        disposeAllScreens();
        this.setScreen(new GameLostScreen(this)); // Set the current screen to GameScreen
    }

	public void goToLeaderboard() {
		disposeAllScreens();
		this.setScreen(new LeaderboardScreen(this));
	}

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;


        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);
        charAnimation(tmp);
    }

    private void charAnimation(TextureRegion[][] tmp) {
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

    }

    private void loadKeyShineAnimation() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("key_shine.atlas"));

        this.keyShineAnimation = new Animation<>(0.08f, atlas.findRegions("key"), Animation.PlayMode.LOOP);
    }

    private void loadVampiricHeartAnimation() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("vampiric_heart.atlas"));

        this.vampiricHeartAnimation = new Animation<>(0.15f, atlas.findRegions("vampiric_heart"), Animation.PlayMode.LOOP);
    }

    private void loadGarlicTrapAnimation() {
        Texture garlicTrapTexture = new Texture(Gdx.files.internal("garlic_trap.png"));

        TextureRegion garlicTrapTextureRegion = new TextureRegion(garlicTrapTexture);
        this.garlicTrapAnimation = new Animation<>(1f, garlicTrapTextureRegion);
    }

    private void loadFreezeTrapAnimation() {
        Texture freezeTrapAnimation = new Texture(Gdx.files.internal("freeze_trap.png"));

        TextureRegion freezeTrapTextureRegion = new TextureRegion(freezeTrapAnimation);
        this.freezeTrapAnimation = new Animation<>(1f, freezeTrapTextureRegion);
    }

    private void loadTouristAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));
        int frameWidth = 16;
        int frameHeight = 32;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        // Row 0: Down, Row 1: Right, Row 2: Up, Row 3: Left
        touristDown = new Animation<>(0.1f, getFrames(tmp[0], 0, 4));
        touristRight = new Animation<>(0.1f, getFrames(tmp[1], 0, 4));
        touristUp = new Animation<>(0.1f, getFrames(tmp[2], 0, 4));
        touristLeft = new Animation<>(0.1f, getFrames(tmp[3], 0, 4));
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
    public Animation<TextureRegion> getTouristDown() {
        return touristDown;
    }

    public Animation<TextureRegion> getTouristUp() {
        return touristUp;
    }

    public Animation<TextureRegion> getTouristLeft() {
        return touristLeft;
    }

    public Animation<TextureRegion> getTouristRight() {
        return touristRight;
    }


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

    // Collectable animation Getters
    public Animation<TextureRegion> getKeyShineAnimation() {
        return this.keyShineAnimation;
    }

    public Animation<TextureRegion> getVampiricHeartAnimation() {
        return this.vampiricHeartAnimation;
    }

    // Traps animation Getters
    public Animation<TextureRegion> getGarlicTrapAnimation() {
        return this.garlicTrapAnimation;
    }

    public Animation<TextureRegion> getFreezeTrapAnimation() {
        return this.freezeTrapAnimation;
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

    public TextureRegion getFreezeEffect() {
        return freezeEffect;
    }

	public TextureRegion getExitClosedTexture() {
		return exitClosedTexture;
	}

	public TextureRegion getExitOpenTexture() {
		return exitOpenTexture;
	}
}
