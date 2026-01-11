package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Music backgroundMusic;
    private Music stepping;
    private Music mainMenu;
    private Music backgroundStory;

    private Sound enemyDie;
    private Sound enemyHit;
    private Sound keyPickup;
    private Sound playerDash;
    private Sound playerDie;
    private Sound playerHeal;
    private Sound playerHit;
    private Sound touristRun;
    private Sound uiButtonClick;
    private Sound uiHoverOver;
    private Sound win;
//TODO Enemy, key, playerDie, playerHeal, win, touristRun  sounds

        public SoundController(){
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

        public void playBackgroundMusic(float volume){
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();
        }
        public void stopBackgroundMusic(){
            backgroundMusic.pause();
        }

    public void playBackgrounStorydMusic(float volume){
        backgroundStory.setLooping(true);
        backgroundStory.setVolume(volume);
        backgroundStory.play();
    }
    public void stopBackgroundStoryMusic(){
        backgroundStory.pause();
    }


        public void playMainMenuMusic(float volume){
            mainMenu.setLooping(true);
            mainMenu.setVolume(volume);
            mainMenu.play();
        }
        public void stopMainMenuMusic(){
            mainMenu.pause();
        }

        public void playEnemyDieSound(float volume){
            enemyDie.play(volume);
        }
        public void playEnemyHitSound(float volume){
            enemyHit.play(volume);
        }
    public void playKeyPickupSound(float volume){
        keyPickup.play(volume);
    }
    public void playPlayerDashSound(float volume){
        playerDash.play(volume);
    }
    public void playPlayerDieSound(float volume){
        playerDie.play(volume);
    }
    public void playPlayerHealSound(float volume){
        playerHeal.play(volume);
    }
    public  void playPlayerHitSound(float volume){
        playerHit.play(volume);
    }
    public void playSteppingSound(float volume){
        stepping.setVolume(volume);
        stepping.setLooping(true);
        stepping.play();
    }
    public void stopSteppingSound(){
            stepping.pause();
    }
    public void playTouristRunSound(float volume){
        touristRun.play(volume);
    }
    public  void playUI_buttonClickSound(float volume){
        uiButtonClick.play(volume);
    }
    public  void playUI_hoverOverSound(float volume){
        uiHoverOver.play(volume);
    }
    public  void playWinSound(float volume){
        win.play(volume);
    }

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
