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
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;


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
    private Animation<TextureRegion> characterInvisDownAnimation, characterInvisUpAnimation, characterInvisLeftAnimation, characterInvisRightAnimation;
    private Animation<TextureRegion> characterAttackDownAnimation, characterAttackUpAnimation, characterAttackLeftAnimation, characterAttackRightAnimation;
    // Jumping Animations
    private Animation<TextureRegion> characterInvisDownJumpAnimation, characterInvisUpJumpAnimation, characterInvisLeftJumpAnimation, characterInvisRightJumpAnimation;
    // Sad or Damage Animations
    private Animation<TextureRegion> characterInvisDownSad, characterInvisUpSad, characterInvisLeftSad, characterInvisRightSad;
    // Happy/Win/PowerUp Animations
    private Animation<TextureRegion> characterInvisDownWin, characterInvisUpWin, characterInvisLeftWin, characterInvisRightWin;
    //Enemies
    //Enemies
    private Animation<TextureRegion> touristDown, touristUp, touristLeft, touristRight;
    private Animation<TextureRegion> bouncerDown, bouncerUp, bouncerLeft, bouncerRight;
    // Maze Textures
    private TextureRegion wallTile;
    private TextureRegion emptyTile;
    //TODO (Connect Parser to Game Assets): - iziane Texture Regions for our Objects
    private TextureRegion keyTexture;
    private TextureRegion trapTexture;
    //UI
    private Texture spriteSheet;
    private TextureRegion woodenSign;
    // Status effects
    private TextureRegion bloodEffect;
    private TextureRegion poisonEffect;
    private TextureRegion freezeEffect;
    private TextureRegion sunscreenEffect;
    // Key
    private TextureRegion key;
    private TextureRegion keyOutline;
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
    // Dawn Animations
    private Animation<TextureRegion> dawnThinAnimation;
    private Animation<TextureRegion> dawnWideAnimation;
    private TextureRegion shadowTexture;


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
        this.Shadow();

        spriteSheet = new Texture(Gdx.files.internal("UI.png"));
        Texture assetsSheet = new Texture(Gdx.files.internal("Game_Assets+.png"));
        woodenSign = new TextureRegion(spriteSheet, 0, 128, 128, 48);
        keyOutline = new TextureRegion(assetsSheet, 800,50,168,96);
        key = new TextureRegion(assetsSheet, 800,139,168,90);


        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Dracula

        this.loadCharacterInvisAnimation();

        this.loadTouristAnimation();
        this.loadBouncerAnimation();
        this.loadMazeTiles();
        this.loadKeyShineAnimation();
        this.loadGarlicTrapAnimation();
        this.loadFreezeTrapAnimation();
        this.loadVampiricHeartAnimation();
        this.loadDawnThinAnimation();
        this.loadDawnWideAnimation();
        this.loadStatusEffectsTextures();


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


    public void Shadow(){
        Texture shadowSheet = new Texture(Gdx.files.internal("lpc_shadow.png"));
        TextureRegion[][] tmp = TextureRegion.split(shadowSheet, 64, 64);
        TextureRegion genericShadow = tmp[10][0];
        Enemy.setShadowTexture(genericShadow);
    }

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


	public void goToGameWonScreen() {
		disposeAllScreens();
		this.setScreen(new GameWonScreen(this));
	}

    /**
     * Loads the character animation from the dracula.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("dracula.png"));
        int frameWidth = 16;
        int frameHeight = 32;
        int attackWidth = 32;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        charAnimation(tmp);
        int startY = 4*frameHeight;


        //Attack
        characterAttackDownAnimation = createAnimation(walkSheet, 0, startY, attackWidth, frameHeight, 4);
        characterAttackUpAnimation = createAnimation(walkSheet, 0, startY + frameHeight, attackWidth, frameHeight, 4);
        characterAttackRightAnimation = createAnimation(walkSheet, 0, startY + (2 * frameHeight), attackWidth, frameHeight, 4);
        characterAttackLeftAnimation = createAnimation(walkSheet, 0, startY + (3 * frameHeight), attackWidth, frameHeight, 4);
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int startX, int startY, int frameWidth, int frameHeight, int frameCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(texture, startX + (i * frameWidth), startY, frameWidth, frameHeight));
        }
        return new Animation<>(0.1f, frames);
    }
    private void loadCharacterInvisAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("DraculaInvis.png"));
        int frameWidth = 16;
        int frameHeight = 32;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        // Call your helper to set up the animations
        charInvisAnimation(tmp);
    }
    private void charInvisAnimation(TextureRegion[][] tmp) {
        // ROW 0: DOWN
        characterInvisDownAnimation = new Animation<>(0.1f, getFrames(tmp[0], 0, 4)); // Walk 0-3
        characterInvisDownJumpAnimation = new Animation<>(0.1f, getFrames(tmp[0], 4, 3)); // Jump 4-6
        characterInvisDownSad = new Animation<>(0.2f, getFrames(tmp[0], 7, 3)); // Sad 7-9
        characterInvisDownWin = new Animation<>(0.1f, getFrames(tmp[0], 10, 1)); // Win 10

        // ROW 1: RIGHT
        characterInvisRightAnimation = new Animation<>(0.1f, getFrames(tmp[1], 0, 4));
        characterInvisRightJumpAnimation = new Animation<>(0.1f, getFrames(tmp[1], 4, 3));
        characterInvisRightSad = new Animation<>(0.2f, getFrames(tmp[1], 7, 3));
        characterInvisRightWin = new Animation<>(0.1f, getFrames(tmp[1], 10, 1));

        // ROW 2: UP
        characterInvisUpAnimation = new Animation<>(0.1f, getFrames(tmp[2], 0, 4));
        characterInvisUpJumpAnimation = new Animation<>(0.1f, getFrames(tmp[2], 4, 3));
        characterInvisUpSad = new Animation<>(0.2f, getFrames(tmp[2], 7, 3));
        characterInvisUpWin = new Animation<>(0.1f, getFrames(tmp[2], 10, 1));

        // ROW 3: LEFT
        characterInvisLeftAnimation = new Animation<>(0.1f, getFrames(tmp[3], 0, 4));
        characterInvisLeftJumpAnimation = new Animation<>(0.1f, getFrames(tmp[3], 4, 3));
        characterInvisLeftSad = new Animation<>(0.2f, getFrames(tmp[3], 7, 3));
        characterInvisLeftWin = new Animation<>(0.1f, getFrames(tmp[3], 10, 1));

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
        Texture freezeTrapTexture = new Texture(Gdx.files.internal("freeze_trap.png"));

        TextureRegion freezeTrapTextureRegion = new TextureRegion(freezeTrapTexture);
        this.freezeTrapAnimation = new Animation<>(1f, freezeTrapTextureRegion);
    }

    private void loadDawnThinAnimation() {
        Texture dawnThinTexture = new Texture(Gdx.files.internal("dawn_thin.png"));

        TextureRegion dawnThinTextureRegion = new TextureRegion(dawnThinTexture);
        this.dawnThinAnimation = new Animation<>(1f,  dawnThinTextureRegion);
    }

    private void loadDawnWideAnimation() {
        Texture dawnWideTexture = new Texture(Gdx.files.internal("dawn_wide.png"));

        TextureRegion dawnWideTextureRegion = new TextureRegion(dawnWideTexture);
        this.dawnWideAnimation = new Animation<>(1f,  dawnWideTextureRegion);
    }

    private void loadTouristAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("tourist_walk.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        touristUp    = new Animation<>(0.075f, getFrames(tmp[0], 1, 8));
        touristLeft  = new Animation<>(0.1f, getFrames(tmp[1], 1, 8));
        touristDown  = new Animation<>(0.075f, getFrames(tmp[2], 1, 8));
        touristRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 8));
    }

    private void loadBouncerAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("bouncer.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        bouncerUp    = new Animation<>(0.075f, getFrames(tmp[0], 1, 8));
        bouncerLeft  = new Animation<>(0.1f, getFrames(tmp[1], 1, 8));
        bouncerDown  = new Animation<>(0.075f, getFrames(tmp[2], 1, 8));
        bouncerRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 8));
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

    private void loadStatusEffectsTextures() {
        TextureAtlas effectsAtlas = new TextureAtlas(Gdx.files.internal("statusEffects.atlas"));
        this.bloodEffect = effectsAtlas.findRegion("blood");
        this.poisonEffect = effectsAtlas.findRegion("poison");
        this.freezeEffect = effectsAtlas.findRegion("snowflake");
        this.sunscreenEffect = effectsAtlas.findRegion("cloudy_sun");
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

    public Animation<TextureRegion> getBouncerDown() { return bouncerDown; }
    public Animation<TextureRegion> getBouncerUp() { return bouncerUp; }
    public Animation<TextureRegion> getBouncerLeft() { return bouncerLeft; }
    public Animation<TextureRegion> getBouncerRight() { return bouncerRight; }

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

    // Dawn animation Getters
    public Animation<TextureRegion> getDawnThinAnimation() {
        return this.dawnThinAnimation;
    }

    public Animation<TextureRegion> getDawnWideAnimation() {
        return this.dawnWideAnimation;
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

    // Status effect getters
    public TextureRegion getPoisonEffect() {
        return poisonEffect;
    }

    public TextureRegion getBloodEffect() {
        return bloodEffect;
    }

    public TextureRegion getFreezeEffect() {
        return freezeEffect;
    }

    public TextureRegion getSunscreenEffect() {
        return sunscreenEffect;
    }

	public TextureRegion getExitClosedTexture() {
		return exitClosedTexture;
	}

	public TextureRegion getExitOpenTexture() {
		return exitOpenTexture;
	}

    public TextureRegion getKey() {
        return key;
    }

    public TextureRegion getKeyOutline() {
        return keyOutline;
    }
    public Animation<TextureRegion> getCharacterAttackDownAnimation() {
        return characterAttackDownAnimation;
    }

    public Animation<TextureRegion> getCharacterAttackUpAnimation() {
        return characterAttackUpAnimation;
    }

    public Animation<TextureRegion> getCharacterAttackLeftAnimation() {
        return characterAttackLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterAttackRightAnimation() {
        return characterAttackRightAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisDownAnimation() {
        return characterInvisDownAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisUpAnimation() {
        return characterInvisUpAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisLeftAnimation() {
        return characterInvisLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisRightAnimation() {
        return characterInvisRightAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisDownJumpAnimation() {
        return characterInvisDownJumpAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisUpJumpAnimation() {
        return characterInvisUpJumpAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisLeftJumpAnimation() {
        return characterInvisLeftJumpAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisRightJumpAnimation() {
        return characterInvisRightJumpAnimation;
    }

    public Animation<TextureRegion> getCharacterInvisDownSad() {
        return characterInvisDownSad;
    }

    public Animation<TextureRegion> getCharacterInvisUpSad() {
        return characterInvisUpSad;
    }

    public Animation<TextureRegion> getCharacterInvisLeftSad() {
        return characterInvisLeftSad;
    }

    public Animation<TextureRegion> getCharacterInvisRightSad() {
        return characterInvisRightSad;
    }

    public Animation<TextureRegion> getCharacterInvisDownWin() {
        return characterInvisDownWin;
    }

    public Animation<TextureRegion> getCharacterInvisUpWin() {
        return characterInvisUpWin;
    }

    public Animation<TextureRegion> getCharacterInvisLeftWin() {
        return characterInvisLeftWin;
    }

    public Animation<TextureRegion> getCharacterInvisRightWin() {
        return characterInvisRightWin;
    }
}
