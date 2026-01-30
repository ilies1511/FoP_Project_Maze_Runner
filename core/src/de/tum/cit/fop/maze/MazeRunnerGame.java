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
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.UI.BackgroundStoryScreen;
import de.tum.cit.fop.maze.UI.GameLostScreen;
import de.tum.cit.fop.maze.UI.GameWonScreen;
import de.tum.cit.fop.maze.UI.MenuScreen;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.util.Arrays;
import java.util.List;


/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    /** Array of file paths for story mode levels. */
    public static final String[] STORY_LEVELS = {
            "story_mode/berghain_main_big.properties", // 00 ---> 1.
            "story_mode/06.properties", // Berhain tags --> Dance Area 2.
            "story_mode/02.properties", // DanceFloor aber scheizze
            "story_mode/04.properties", // geil --> black floor + full beton
            "story_mode/01.properties", // --> Pflaster boden
            "story_mode/03.properties", // Aehlicher Aufbau zu 01
            "story_mode/berghain_darkroom.properties", // geil
    };
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
    private Animation<TextureRegion> junkieDown, junkieUp, junkieLeft, junkieRight;
    // Maze Textures
    private Animation<TextureRegion> junkieAttackDown, junkieAttackUp, junkieAttackLeft, junkieAttackRight;
    private TextureRegion emptyTile1;
    private TextureRegion emptyTile2;
    private TextureRegion emptyTile3;
    private TextureRegion emptyTile4;
    private TextureRegion emptyTile5;
    private TextureRegion emptyTile6;
    private TextureRegion emptyTile7;
    private TextureRegion danceFloorTile;
    private TextureRegion wallTile1;
    private TextureRegion wallTile2;
    private TextureRegion wallTile3;
    private TextureRegion wallTile4;
    private TextureRegion wallTile5;
    private TextureRegion wallTile6;
    private TextureRegion wallTile7;
    private TextureRegion wallTile8;
    private TextureRegion buildingTile;
    private TextureRegion wallHigh1;
    private TextureRegion wallHigh2;
    private TextureRegion wallHigh3;
    private TextureRegion wallHigh4;
    private TextureRegion decor1;
    private TextureRegion decor2;
    private TextureRegion decor3;
    private TextureRegion decor4;
    private TextureRegion decor5;
    private TextureRegion decor6;
    private TextureRegion decor7;
    private TextureRegion decor8;
    private TextureRegion decor9;
    private TextureRegion decor10;
    private TextureRegion casketTile;
    private TextureRegion keyTexture;
    private TextureRegion trapTexture;
    //UI
    private Texture spriteSheet;
    private Texture assetsSheet;
    private Texture achievementsSheet;
    private TextureRegion arrow;
    private TextureRegion woodenSign;
    private TextureRegion heart;
    private TextureRegion trophy;
    private TextureRegion startAchievement;
    private TextureRegion endAchievement;
    private TextureRegion firstBloodAchievement;
    private TextureRegion upgradesAchievement;
    private TextureRegion killsAchievement;
    private TextureRegion invisAchievement;
    private TextureRegion consoleAchievement;
    private TextureRegion pointsAchievement;
    // Status effects
    private TextureRegion bloodEffect;
    private TextureRegion poisonEffect;
    private TextureRegion freezeEffect;
    private TextureRegion sunscreenEffect;
    private TextureRegion godModeEffect;
    // Key
    private TextureRegion key;
    private TextureRegion keyOutline;
    // Collectables
    private Animation<TextureRegion> keyShineAnimation;
    private Animation<TextureRegion> pillAnimation;
    private Animation<TextureRegion> vampiricHeartAnimation;
    private Animation<TextureRegion> exitPortalAnimation;
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
    private GameMode gameMode;

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
     * Gets the game info object.
     * @return The GameInfo.
     */
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
        this.gameInfo.initializeAchievements();

        spriteBatch = new SpriteBatch(); // Create SpriteBatch

        spriteSheet = new Texture(Gdx.files.internal("UI.png"));
        assetsSheet = new Texture(Gdx.files.internal("Game_Assets+.png"));
        achievementsSheet = new Texture(Gdx.files.internal("achievements.png"));
        startAchievement = new TextureRegion(achievementsSheet, 11, 135, 103, 122);
        endAchievement = new TextureRegion(achievementsSheet, 145, 125, 105, 125);
        firstBloodAchievement = new TextureRegion(achievementsSheet, 295, 145, 55, 80);
        upgradesAchievement = new TextureRegion(achievementsSheet, 405, 135, 100, 100);
        killsAchievement = new TextureRegion(achievementsSheet, 11, 285, 100, 100);
        invisAchievement = new TextureRegion(achievementsSheet, 165, 280, 105, 105);
        consoleAchievement = new TextureRegion(achievementsSheet, 280, 280, 90, 90);
        pointsAchievement = new TextureRegion(achievementsSheet, 415, 280, 80, 90);
        woodenSign = new TextureRegion(spriteSheet, 0, 128, 128, 48);
        Texture heartTexture = new Texture(Gdx.files.internal("heart.png"));
        heart = new TextureRegion(heartTexture);
        keyOutline = new TextureRegion(assetsSheet, 800, 50, 168, 96);
        key = new TextureRegion(assetsSheet, 800, 139, 168, 90);
        Texture arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
        arrow = new TextureRegion(arrowTexture);
        Texture trophyTexture = new Texture(Gdx.files.internal("trophy.png"));
        trophy = new TextureRegion(trophyTexture);

        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Dracula

        this.loadCharacterInvisAnimation();

        this.loadPillAnimation();

        this.loadTouristAnimation();
        this.loadBouncerAnimation();
        this.loadJunkieAnimation();
        this.loadJunkieAttcackAnimation();

        this.loadKeyShineAnimation();
        this.loadGarlicTrapAnimation();
        this.loadFreezeTrapAnimation();
        this.loadVampiricHeartAnimation();
        this.loadDawnThinAnimation();
        this.loadDawnWideAnimation();
        this.loadStatusEffectsTextures();

        this.loadObjectTextures();
        this.loadMazeTiles();
        goToMenu(); // Navigate to the menu screen
    }

    private void loadObjectTextures() {
        if (Gdx.files.internal("objects.png").exists()) {
            Texture objectsSheet = new Texture(Gdx.files.internal("objects.png"));
            TextureRegion[][] tmp = TextureRegion.split(objectsSheet, 16, 16);

            this.keyTexture = tmp[0][0];
            this.trapTexture = tmp[0][1];

            if (tmp[0].length > 2) {
                this.exitClosedTexture = tmp[0][2];
            } else {
                this.exitClosedTexture = tmp[0][0]; // Fallback to Key if missing
            }
            if (tmp[0].length > 3) {
                this.exitOpenTexture = tmp[0][3];
            } else {
                this.exitOpenTexture = tmp[0][1]; // Fallback to Trap if missing
            }

        } else {
            Gdx.app.error("Assets", "objects.png not found! Using character texture as fallback.");
            this.keyTexture = characterDownAnimation.getKeyFrame(0);
            this.trapTexture = characterDownAnimation.getKeyFrame(0);
        }
    }

    /**
     * Gets the texture for the key.
     * @return The key texture region.
     */
    public TextureRegion getKeyTexture() {
        return keyTexture;
    }

    /**
     * Gets the texture for the trap.
     * @return The trap texture region.
     */
    public TextureRegion getTrapTexture() {
        return trapTexture;
    }

    /**
     * Disposes all active screens.
     */
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
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
    }


    /**
     * Switches to the background story screen.
     */
    public void goToBackgroundStory() {
        disposeAllScreens();
        this.setScreen(new BackgroundStoryScreen(this)); // Set the current screen to GameScreen
    }

    /**
     * Switches to the game lost screen.
     */
    public void goToGameLostScreen(int score) {
        disposeAllScreens();
        this.setScreen(new GameLostScreen(this, score)); // Set the current screen to GameScreen
    }


    /**
     * Switches to the game won screen.
     */
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
        int startY = 4 * frameHeight;


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
    private void loadPillAnimation(){
        TextureRegion pill = new TextureRegion(assetsSheet, 265, 53, 133, 133);
        TextureRegion[] frames = new TextureRegion[]{ pill };
        this.pillAnimation = new Animation<TextureRegion>(0.05f, frames);
        this.pillAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadPortalAnimation() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("exit_portal.atlas"));

        this.exitPortalAnimation = new Animation<>(0.08f, atlas.findRegions("exit_portal"), Animation.PlayMode.LOOP);
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
        this.dawnThinAnimation = new Animation<>(1f, dawnThinTextureRegion);
    }

    private void loadDawnWideAnimation() {
        Texture dawnWideTexture = new Texture(Gdx.files.internal("dawn_wide.png"));

        TextureRegion dawnWideTextureRegion = new TextureRegion(dawnWideTexture);
        this.dawnWideAnimation = new Animation<>(1f, dawnWideTextureRegion);
    }

    private void loadTouristAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("tourist_walk.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        touristUp = new Animation<>(0.075f, getFrames(tmp[0], 1, 8));
        touristLeft = new Animation<>(0.1f, getFrames(tmp[1], 1, 8));
        touristDown = new Animation<>(0.075f, getFrames(tmp[2], 1, 8));
        touristRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 8));
    }

    private void loadBouncerAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("bouncer.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        bouncerUp = new Animation<>(0.075f, getFrames(tmp[0], 1, 8));
        bouncerLeft = new Animation<>(0.1f, getFrames(tmp[1], 1, 8));
        bouncerDown = new Animation<>(0.075f, getFrames(tmp[2], 1, 8));
        bouncerRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 8));
    }

    private void loadJunkieAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("junkie.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        junkieUp = new Animation<>(0.075f, getFrames(tmp[0], 1, 8));
        junkieLeft = new Animation<>(0.1f, getFrames(tmp[1], 1, 8));
        junkieDown = new Animation<>(0.075f, getFrames(tmp[2], 1, 8));
        junkieRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 8));
    }

    private void loadJunkieAttcackAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("junkie_att.png"));
        int frameWidth = 64;
        int frameHeight = 64;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        junkieAttackUp = new Animation<>(0.075f, getFrames(tmp[0], 1, 12));
        junkieAttackLeft = new Animation<>(0.1f, getFrames(tmp[1], 1, 12));
        junkieAttackDown = new Animation<>(0.075f, getFrames(tmp[2], 1, 12));
        junkieAttackRight = new Animation<>(0.1f, getFrames(tmp[3], 1, 12));
    }

    private void loadMazeTiles() {
        try {
            TextureRegion wallTexture1 = new TextureRegion(assetsSheet, 22, 382, 120, 110);
            TextureRegion wallHighTexture1 = new TextureRegion(assetsSheet, 156, 380, 120, 110);
            TextureRegion wallTexture2 = new TextureRegion(assetsSheet, 293, 382, 120, 110);
            TextureRegion wallHighTexture2 = new TextureRegion(assetsSheet, 432, 382, 120, 110);
            TextureRegion wallTexture3 = new TextureRegion(assetsSheet, 563, 382, 120, 110);
            TextureRegion buildingTexture = new TextureRegion(assetsSheet, 694, 382, 120, 110);
            TextureRegion wallTexture4 = new TextureRegion(assetsSheet, 824, 382, 120, 110);
            TextureRegion wallHighTexture4 = new TextureRegion(assetsSheet, 958, 382, 120, 110);
            TextureRegion wallTexture5 = new TextureRegion(assetsSheet, 18, 533, 120, 110);
            TextureRegion wallTexture6 = new TextureRegion(assetsSheet, 157, 533, 120, 110);
            TextureRegion wallTexture7 = new TextureRegion(assetsSheet, 293, 533, 120, 110);
            TextureRegion wallTexture8 = new TextureRegion(assetsSheet, 427, 533, 120, 110);

            TextureRegion emptyTexture1 = new TextureRegion(assetsSheet, 20, 832, 120, 120);
            TextureRegion emptyTexture2 = new TextureRegion(assetsSheet, 157, 832, 120, 120);
            TextureRegion emptyTexture3 = new TextureRegion(assetsSheet, 293, 832, 120, 120);
            TextureRegion emptyTexture4 = new TextureRegion(assetsSheet, 427, 832, 120, 120);
            TextureRegion emptyTexture5 = new TextureRegion(assetsSheet, 563, 832, 120, 120);
            TextureRegion emptyTexture6 = new TextureRegion(assetsSheet, 693, 832, 120, 120);
            TextureRegion emptyTexture7 = new TextureRegion(assetsSheet, 824, 832, 120, 120);
            TextureRegion danceFloorTexture = new TextureRegion(assetsSheet, 824, 533, 120, 120);

            TextureRegion decorTexture1 = new TextureRegion(assetsSheet, 410, 670, 140, 140);
            TextureRegion decorTexture2 = new TextureRegion(assetsSheet, 585, 670, 140, 140);
            TextureRegion decorTexture3 = new TextureRegion(assetsSheet, 775, 715, 140, 90);
            TextureRegion decorTexture4 = new TextureRegion(assetsSheet, 950, 670, 140, 140);
            TextureRegion decorTexture5 = new TextureRegion(assetsSheet, 18, 1000, 140, 90);
            TextureRegion decorTexture6 = new TextureRegion(assetsSheet, 280, 980, 140, 110);
            TextureRegion decorTexture7 = new TextureRegion(assetsSheet, 445, 980, 127, 140);
            TextureRegion decorTexture8 = new TextureRegion(assetsSheet, 565, 950, 137, 157);
            TextureRegion decorTexture9 = new TextureRegion(assetsSheet, 695, 950, 140, 140);
            TextureRegion decorTexture10 = new TextureRegion(assetsSheet, 855, 955, 110, 135);

            TextureRegion exitTexture = new TextureRegion(assetsSheet, 22, 686, 113, 119);
            TextureRegion wallHighTexture3 = new TextureRegion(assetsSheet, 281, 685, 120, 110);

            TextureRegion casketTexture = new TextureRegion(assetsSheet, 800, 210, 240, 147);


            this.wallTile3 = wallTexture3;
            this.wallTile1 = wallHighTexture1;
            this.wallTile2 = wallTexture2;
            this.wallTile4 = wallTexture4;
            this.wallTile5 = wallTexture5;
            this.wallTile6 = wallTexture6;
            this.wallTile7 = wallTexture7;
            this.wallTile8 = wallTexture8;

            this.danceFloorTile = danceFloorTexture;
            this.emptyTile1 = emptyTexture1;
            this.emptyTile2 = emptyTexture2;
            this.emptyTile3 = emptyTexture3;
            this.emptyTile4 = emptyTexture4;
            this.emptyTile5 = emptyTexture5;
            this.emptyTile6 = emptyTexture6;
            this.emptyTile7 = emptyTexture7;

            this.decor1 = decorTexture1;
            this.decor2 = decorTexture2;
            this.decor3 = decorTexture3;
            this.decor4 = decorTexture4;
            this.decor5 = decorTexture5;
            this.decor6 = decorTexture6;
            this.decor7 = decorTexture7;
            this.decor8 = decorTexture8;
            this.decor9 = decorTexture9;
            this.decor10 = decorTexture10;

            this.casketTile = casketTexture;

            this.buildingTile = buildingTexture;

            this.wallHigh1 = wallTexture1;
            this.wallHigh2 = wallHighTexture2;
            this.wallHigh3 = wallHighTexture3;
            this.wallHigh4 = wallHighTexture4;

            this.exitClosedTexture = exitTexture;
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
        this.godModeEffect = effectsAtlas.findRegion("cloudy_sun");
    }

    /**
     * Gets the arrow texture.
     * @return The arrow texture region.
     */
    public TextureRegion getArrow() {
        return arrow;
    }

    // Getter methods

    /**
     * Gets the tourist walking down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getTouristDown() {
        return touristDown;
    }

    /**
     * Gets the tourist walking up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getTouristUp() {
        return touristUp;
    }

    /**
     * Gets the tourist walking left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getTouristLeft() {
        return touristLeft;
    }

    /**
     * Gets the tourist walking right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getTouristRight() {
        return touristRight;
    }


    /**
     * Gets the UI skin.
     * @return The skin.
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Gets the sprite batch.
     * @return The sprite batch.
     */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /**
     * Gets the character walking down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    /**
     * Gets the character walking up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    /**
     * Gets the character walking left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    /**
     * Gets the character walking right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    /**
     * Gets the character jumping down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterDownJumpAnimation() {
        return characterDownJumpAnimation;
    }

    /**
     * Gets the character jumping up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterUpJumpAnimation() {
        return characterUpJumpAnimation;
    }

    /**
     * Gets the character jumping left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterLeftJumpAnimation() {
        return characterLeftJumpAnimation;
    }

    /**
     * Gets the character jumping right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterRightJumpAnimation() {
        return characterRightJumpAnimation;
    }

    /**
     * Gets the character sad down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterDownSad() {
        return characterDownSad;
    }

    /**
     * Gets the character sad up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterUpSad() {
        return characterUpSad;
    }

    /**
     * Gets the character sad left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterLeftSad() {
        return characterLeftSad;
    }

    /**
     * Gets the exit portal animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getExitPortalAnimation() {
        return exitPortalAnimation;
    }

    /**
     * Gets the character sad right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterRightSad() {
        return characterRightSad;
    }

    /**
     * Gets the character win down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterDownWin() {
        return characterDownWin;
    }

    /**
     * Gets the character win up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterUpWin() {
        return characterUpWin;
    }

    /**
     * Gets the character win left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterLeftWin() {
        return characterLeftWin;
    }

    /**
     * Gets the character win right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterRightWin() {
        return characterRightWin;
    }

    /**
     * Gets the bouncer walking down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getBouncerDown() {
        return bouncerDown;
    }

    /**
     * Gets the bouncer walking up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getBouncerUp() {
        return bouncerUp;
    }

    /**
     * Gets the bouncer walking left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getBouncerLeft() {
        return bouncerLeft;
    }

    /**
     * Gets the bouncer walking right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getBouncerRight() {
        return bouncerRight;
    }

    /**
     * Gets the junkie walking down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieDown() {
        return junkieDown;
    }

    /**
     * Gets the junkie walking up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieUp() {
        return junkieUp;
    }

    /**
     * Gets the junkie walking left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieLeft() {
        return junkieLeft;
    }

    /**
     * Gets the junkie walking right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieRight() {
        return junkieRight;
    }

    /**
     * Gets the junkie attack down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieAttackDown() {
        return junkieAttackDown;
    }

    /**
     * Gets the junkie attack up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieAttackUp() {
        return junkieAttackUp;
    }

    /**
     * Gets the junkie attack left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieAttackLeft() {
        return junkieAttackLeft;
    }

    /**
     * Gets the junkie attack right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getJunkieAttackRight() {
        return junkieAttackRight;
    }

    // Collectable animation Getters

    /**
     * Gets the key shine animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getKeyShineAnimation() {
        return this.keyShineAnimation;
    }

    /**
     * Gets the vampiric heart animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getVampiricHeartAnimation() {
        return this.vampiricHeartAnimation;
    }

    // Traps animation Getters

    /**
     * Gets the garlic trap animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getGarlicTrapAnimation() {
        return this.garlicTrapAnimation;
    }

    /**
     * Gets the freeze trap animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getFreezeTrapAnimation() {
        return this.freezeTrapAnimation;
    }

    // Dawn animation Getters

    /**
     * Gets the dawn thin animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getDawnThinAnimation() {
        return this.dawnThinAnimation;
    }

    /**
     * Gets the dawn wide animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getDawnWideAnimation() {
        return this.dawnWideAnimation;
    }

    // Maze Tile Getters

    /**
     * Gets the wall tile 3 texture.
     * @return The texture region.
     */
    public TextureRegion getWallTile3() {
        return wallTile3;
    }

    /**
     * Gets the empty tile 1 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile1() {
        return emptyTile1;
    }

    /**
     * Gets the empty tile 2 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile2() {
        return emptyTile2;
    }

    /**
     * Gets the empty tile 3 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile3() {
        return emptyTile3;
    }

    /**
     * Gets the empty tile 5 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile5() {
        return emptyTile5;
    }

    /**
     * Gets the empty tile 6 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile6() {
        return emptyTile6;
    }

    /**
     * Gets the empty tile 7 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile7() {
        return emptyTile7;
    }

    /**
     * Gets the dance floor tile texture.
     * @return The texture region.
     */
    public TextureRegion getDanceFloorTile() {
        return danceFloorTile;
    }

    /**
     * Gets the wall tile 1 texture.
     * @return The texture region.
     */
    public TextureRegion getWallTile1() {
        return wallTile1;
    }

    /**
     * Gets the wall tile 2 texture.
     * @return The texture region.
     */
    public TextureRegion getWallTile2() {
        return wallTile2;
    }

    /**
     * Gets the wall tile 4 texture.
     * @return The texture region.
     */
    public TextureRegion getWallTile4() {
        return wallTile4;
    }

    /**
     * Gets the wall high 5 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh5() {
        return wallTile5;
    }

    /**
     * Gets the wall high 6 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh6() {
        return wallTile6;
    }

    /**
     * Gets the wall high 7 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh7() {
        return wallTile7;
    }

    /**
     * Gets the wall tile 8 texture.
     * @return The texture region.
     */
    public TextureRegion getWallTile8() {
        return wallTile8;
    }

    /**
     * Gets the empty tile 4 texture.
     * @return The texture region.
     */
    public TextureRegion getEmptyTile4() {
        return emptyTile4;
    }

    /**
     * Gets the decor 1 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor1() {
        return decor1;
    }

    /**
     * Gets the decor 2 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor2() {
        return decor2;
    }

    /**
     * Gets the decor 3 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor3() {
        return decor3;
    }

    /**
     * Gets the decor 4 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor4() {
        return decor4;
    }

    /**
     * Gets the decor 5 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor5() {
        return decor5;
    }

    /**
     * Gets the decor 6 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor6() {
        return decor6;
    }

    /**
     * Gets the decor 7 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor7() {
        return decor7;
    }

    /**
     * Gets the decor 8 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor8() {
        return decor8;
    }

    /**
     * Gets the decor 9 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor9() {
        return decor9;
    }

    /**
     * Gets the decor 10 texture.
     * @return The texture region.
     */
    public TextureRegion getDecor10() {
        return decor10;
    }

    /**
     * Gets the casket tile texture.
     * @return The texture region.
     */
    public TextureRegion getCasketTile() {
        return casketTile;
    }

    /**
     * Gets the main sprite sheet.
     * @return The texture.
     */
    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    /**
     * Gets the wooden sign texture.
     * @return The texture region.
     */
    public TextureRegion getWoodenSign() {
        return woodenSign;
    }

    // Status effect getters

    /**
     * Gets the poison effect texture.
     * @return The texture region.
     */
    public TextureRegion getPoisonEffect() {
        return poisonEffect;
    }

    /**
     * Gets the blood effect texture.
     * @return The texture region.
     */
    public TextureRegion getBloodEffect() {
        return bloodEffect;
    }

    /**
     * Gets the freeze effect texture.
     * @return The texture region.
     */
    public TextureRegion getFreezeEffect() {
        return freezeEffect;
    }

    /**
     * Gets the sunscreen effect texture.
     * @return The texture region.
     */
    public TextureRegion getSunscreenEffect() {
        return sunscreenEffect;
    }

    /**
     * Gets the god mode effect texture.
     * @return The texture region.
     */
    public TextureRegion getGodModeEffect() {
        return godModeEffect;
    }

    /**
     * Gets the closed exit texture.
     * @return The texture region.
     */
    public TextureRegion getExitClosedTexture() {
        return exitClosedTexture;
    }

    /**
     * Gets the open exit texture.
     * @return The texture region.
     */
    public TextureRegion getExitOpenTexture() {
        return exitOpenTexture;
    }

    /**
     * Gets the key texture.
     * @return The texture region.
     */
    public TextureRegion getKey() {
        return key;
    }

    /**
     * Gets the key outline texture.
     * @return The texture region.
     */
    public TextureRegion getKeyOutline() {
        return keyOutline;
    }

    /**
     * Gets the character attack down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterAttackDownAnimation() {
        return characterAttackDownAnimation;
    }

    /**
     * Gets the character attack up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterAttackUpAnimation() {
        return characterAttackUpAnimation;
    }

    /**
     * Gets the character attack left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterAttackLeftAnimation() {
        return characterAttackLeftAnimation;
    }

    /**
     * Gets the character attack right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterAttackRightAnimation() {
        return characterAttackRightAnimation;
    }

    /**
     * Gets the character invisible down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisDownAnimation() {
        return characterInvisDownAnimation;
    }

    /**
     * Gets the character invisible up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisUpAnimation() {
        return characterInvisUpAnimation;
    }

    /**
     * Gets the character invisible left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisLeftAnimation() {
        return characterInvisLeftAnimation;
    }

    /**
     * Gets the character invisible right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisRightAnimation() {
        return characterInvisRightAnimation;
    }

    /**
     * Gets the character invisible jump down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisDownJumpAnimation() {
        return characterInvisDownJumpAnimation;
    }

    /**
     * Gets the character invisible jump up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisUpJumpAnimation() {
        return characterInvisUpJumpAnimation;
    }

    /**
     * Gets the character invisible jump left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisLeftJumpAnimation() {
        return characterInvisLeftJumpAnimation;
    }

    /**
     * Gets the character invisible jump right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisRightJumpAnimation() {
        return characterInvisRightJumpAnimation;
    }

    /**
     * Gets the character invisible sad down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisDownSad() {
        return characterInvisDownSad;
    }

    /**
     * Gets the character invisible sad up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisUpSad() {
        return characterInvisUpSad;
    }

    /**
     * Gets the character invisible sad left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisLeftSad() {
        return characterInvisLeftSad;
    }

    /**
     * Gets the character invisible sad right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisRightSad() {
        return characterInvisRightSad;
    }

    /**
     * Gets the character invisible win down animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisDownWin() {
        return characterInvisDownWin;
    }

    /**
     * Gets the character invisible win up animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisUpWin() {
        return characterInvisUpWin;
    }

    /**
     * Gets the character invisible win left animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisLeftWin() {
        return characterInvisLeftWin;
    }

    /**
     * Gets the character invisible win right animation.
     * @return The animation.
     */
    public Animation<TextureRegion> getCharacterInvisRightWin() {
        return characterInvisRightWin;
    }

    /**
     * Gets the current game mode.
     * @return The game mode.
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Sets the game mode.
     * @param Mode The new game mode.
     */
    public void setGameMode(GameMode Mode) {
        this.gameMode = Mode;
    }

    /**
     * Gets the heart texture.
     * @return The texture region.
     */
    public TextureRegion getHeart() {
        return heart;
    }

    /**
     * Gets the trophy texture.
     * @return The texture region.
     */
    public TextureRegion getTrophy() {
        return trophy;
    }

    /**
     * Gets the points achievement icon.
     * @return The texture region.
     */
    public TextureRegion getPointsAchievement() {
        return pointsAchievement;
    }

    /**
     * Gets the console achievement icon.
     * @return The texture region.
     */
    public TextureRegion getConsoleAchievement() {
        return consoleAchievement;
    }

    /**
     * Gets the invisibility achievement icon.
     * @return The texture region.
     */
    public TextureRegion getInvisAchievement() {
        return invisAchievement;
    }

    /**
     * Gets the kills achievement icon.
     * @return The texture region.
     */
    public TextureRegion getKillsAchievement() {
        return killsAchievement;
    }

    /**
     * Gets the upgrades achievement icon.
     * @return The texture region.
     */
    public TextureRegion getUpgradesAchievement() {
        return upgradesAchievement;
    }

    /**
     * Gets the first blood achievement icon.
     * @return The texture region.
     */
    public TextureRegion getFirstBloodAchievement() {
        return firstBloodAchievement;
    }

    /**
     * Gets the end achievement icon.
     * @return The texture region.
     */
    public TextureRegion getEndAchievement() {
        return endAchievement;
    }

    /**
     * Gets the start achievement icon.
     * @return The texture region.
     */
    public TextureRegion getStartAchievement() {
        return startAchievement;
    }

    /**
     * Gets the building tile texture.
     * @return The texture region.
     */
    public TextureRegion getBuildingTile() {
        return buildingTile;
    }

    /**
     * Gets the wall high 1 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh1() {
        return wallHigh1;
    }

    /**
     * Gets the wall high 2 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh2() {
        return wallHigh2;
    }

    /**
     * Gets the wall high 4 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh4() {
        return wallHigh4;
    }

    /**
     * Gets the wall high 3 texture.
     * @return The texture region.
     */
    public TextureRegion getWallHigh3() {
        return wallHigh3;
    }

    /**
     * Gets the pillAnimation.
     * @return The texture region.
     */
    public Animation<TextureRegion> getPillAnimation() {
        return pillAnimation;
    }
}
