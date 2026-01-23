package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manages game sound effects and music.
 * It handles loading, playing, and disposing of audio resources.
 */
public class SoundController {
    private final Music backgroundMusic;
    private final Music stepping;
    private final Music mainMenu;
    private final Music backgroundStory;

    private final Sound enemyDie;
    private final Sound enemyHit;
    private final Sound keyPickup;
    private final Sound playerDash;
    private final Sound playerDie;
    private final Sound playerHeal;
    private final Sound playerHit;
    private final Sound touristRun;
    private final Sound uiButtonClick;
    private final Sound uiHoverOver;
    private final Sound win;

    /**
     * Creates a new SoundController and loads all audio files.
     */
    public SoundController() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/background_music.mp3"));
        stepping = Gdx.audio.newMusic(Gdx.files.internal("Sounds/stepping.mp3"));
        mainMenu = Gdx.audio.newMusic(Gdx.files.internal("Sounds/mainMenuBackground.mp3"));
        backgroundStory = Gdx.audio.newMusic(Gdx.files.internal("Sounds/backgroundStory.mp3"));

        enemyDie = Gdx.audio.newSound(Gdx.files.internal("Sounds/enemyDie.mp3"));
        enemyHit = Gdx.audio.newSound(Gdx.files.internal("Sounds/enemyHit.mp3"));
        keyPickup = Gdx.audio.newSound(Gdx.files.internal("Sounds/keyPickup.mp3"));
        playerDash = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerDash.mp3"));
        playerDie = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerDie.mp3"));
        playerHeal = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerHeal.mp3"));
        playerHit = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerHit.mp3"));
        touristRun = Gdx.audio.newSound(Gdx.files.internal("Sounds/touristRun.mp3"));
        uiButtonClick = Gdx.audio.newSound(Gdx.files.internal("Sounds/UI_buttonClick.mp3"));
        uiHoverOver = Gdx.audio.newSound(Gdx.files.internal("Sounds/UI_hoverOver.mp3"));
        win = Gdx.audio.newSound(Gdx.files.internal("Sounds/win.mp3"));
    }

    /**
     * Plays the background music.
     * @param volume The volume level (0.0 to 1.0).
     */
    public void playBackgroundMusic(float volume) {
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volume);
        backgroundMusic.play();
    }

    /**
     * Stops the background music.
     */
    public void stopBackgroundMusic() {
        backgroundMusic.pause();
    }

    /**
     * Plays the background story music.
     * @param volume The volume level.
     */
    public void playBackgrounStorydMusic(float volume) {
        backgroundStory.setLooping(true);
        backgroundStory.setVolume(volume);
        backgroundStory.play();
    }

    /**
     * Stops the background story music.
     */
    public void stopBackgroundStoryMusic() {
        backgroundStory.pause();
    }


    /**
     * Plays the main menu music.
     * @param volume The volume level.
     */
    public void playMainMenuMusic(float volume) {
        mainMenu.setLooping(true);
        mainMenu.setVolume(volume);
        mainMenu.play();
    }

    /**
     * Stops the main menu music.
     */
    public void stopMainMenuMusic() {
        mainMenu.pause();
    }

    /**
     * Plays the enemy death sound.
     * @param volume The volume level.
     */
    public void playEnemyDieSound(float volume) {
        enemyDie.play(volume);
    }

    /**
     * Plays the enemy hit sound.
     * @param volume The volume level.
     */
    public void playEnemyHitSound(float volume) {
        enemyHit.play(volume);
    }

    /**
     * Plays the key pickup sound.
     * @param volume The volume level.
     */
    public void playKeyPickupSound(float volume) {
        keyPickup.play(volume);
    }

    /**
     * Plays the player dash sound.
     * @param volume The volume level.
     */
    public void playPlayerDashSound(float volume) {
        playerDash.play(volume);
    }

    /**
     * Plays the player death sound.
     * @param volume The volume level.
     */
    public void playPlayerDieSound(float volume) {
        playerDie.play(volume);
    }

    /**
     * Plays the player heal sound.
     * @param volume The volume level.
     */
    public void playPlayerHealSound(float volume) {
        playerHeal.play(volume);
    }

    /**
     * Plays the player hit sound.
     * @param volume The volume level.
     */
    public void playPlayerHitSound(float volume) {
        playerHit.play(volume);
    }

    /**
     * Plays the stepping sound (looped).
     * @param volume The volume level.
     */
    public void playSteppingSound(float volume) {
        stepping.setVolume(volume);
        stepping.setLooping(true);
        stepping.play();
    }

    /**
     * Stops the stepping sound.
     */
    public void stopSteppingSound() {
        stepping.pause();
    }

    /**
     * Plays the tourist run sound.
     * @param volume The volume level.
     */
    public void playTouristRunSound(float volume) {
        touristRun.play(volume);
    }

    /**
     * Plays the UI button click sound.
     * @param volume The volume level.
     */
    public void playUI_buttonClickSound(float volume) {
        uiButtonClick.play(volume);
    }

    /**
     * Plays the UI hover over sound.
     * @param volume The volume level.
     */
    public void playUI_hoverOverSound(float volume) {
        uiHoverOver.play(volume);
    }

    /**
     * Plays the win sound.
     * @param volume The volume level.
     */
    public void playWinSound(float volume) {
        win.play(volume);
    }

    /**
     * Disposes of all audio resources to prevent memory leaks.
     */
    public void dispose() {
        backgroundMusic.dispose();
        stepping.dispose();
        mainMenu.dispose();
        backgroundStory.dispose();

        enemyDie.dispose();
        enemyHit.dispose();
        keyPickup.dispose();
        playerDash.dispose();
        playerDie.dispose();
        playerHeal.dispose();
        playerHit.dispose();
        touristRun.dispose();
        uiButtonClick.dispose();
        uiHoverOver.dispose();
        win.dispose();
    }


}
