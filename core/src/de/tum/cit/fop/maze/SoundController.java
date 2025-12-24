package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Music backgroundMusic;
    private Music stepping;
//TODO Enemy, key, playerDie, playerHeal, win, touristRun  sounds

        public SoundController(){
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/PressFuse-FrenchFuse.mp3"));
             stepping= Gdx.audio.newMusic(Gdx.files.internal("Sounds/stepping.mp3"));
        }

        public void playBackgroundMusic(float volume){
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();
        }
        public void stopBackgroundMusic(){
            backgroundMusic.pause();
        }

        public static void playEnemyDieSound(float volume){
            Sound enemyDie= Gdx.audio.newSound(Gdx.files.internal("Sounds/enemyDie.mp3"));
            enemyDie.play(volume);
        }
        public static void playEnemyHitSound(float volume){
            Sound enemyHit= Gdx.audio.newSound(Gdx.files.internal("Sounds/enemyHit.mp3"));
            enemyHit.play(volume);
        }
    public static void playKeyPickupSound(float volume){
        Sound keyPickup= Gdx.audio.newSound(Gdx.files.internal("Sounds/keyPickup.mp3"));
        keyPickup.play(volume);
    }
    public static void playPlayerDashSound(float volume){
        Sound playerDash= Gdx.audio.newSound(Gdx.files.internal("Sounds/playerDash.mp3"));
        playerDash.play(volume);
    }
    public static void playPlayerDieSound(float volume){
        Sound playerDie= Gdx.audio.newSound(Gdx.files.internal("Sounds/playerDie.mp3"));
        playerDie.play(volume);
    }
    public static void playPlayerHealSound(float volume){
        Sound playerHeal= Gdx.audio.newSound(Gdx.files.internal("Sounds/playerHeal.mp3"));
        playerHeal.play(volume);
    }
    public static void playPlayerHitSound(float volume){
        Sound playerHit= Gdx.audio.newSound(Gdx.files.internal("Sounds/playerHit.mp3"));
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
    public static void playTouristRunSound(float volume){
        Sound touristRun= Gdx.audio.newSound(Gdx.files.internal("Sounds/touristRun.mp3"));
        touristRun.play(volume);
    }
    public static void playUI_buttonClickSound(float volume){
        Sound UI_buttonClick= Gdx.audio.newSound(Gdx.files.internal("Sounds/UI_buttonClick.mp3"));
        UI_buttonClick.play(volume);
    }
    public static void playUI_hoverOverSound(float volume){
        Sound UI_hoverOver= Gdx.audio.newSound(Gdx.files.internal("Sounds/UI_hoverOver.mp3"));
        UI_hoverOver.play(volume);
    }
    public static void playWinSound(float volume){
        Sound win= Gdx.audio.newSound(Gdx.files.internal("Sounds/win.mp3"));
        win.play(volume);
    }



}
