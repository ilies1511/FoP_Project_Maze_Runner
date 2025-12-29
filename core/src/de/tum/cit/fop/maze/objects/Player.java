package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.effects.*;

import java.util.ArrayList;

public class Player extends GameObject {
    private final static float DEFAULT_PLAYER_SPEED = 3f;
    private final static int STARTING_PLAYER_HEALTH = 3;
    private int requiredKeys;

    private final ArrayList<Effect> effects;
    private int currentHealth;
    private int maxHealth;
    private int xp;
    private boolean canDash;
    private boolean canInvis;
    private boolean invis;
   // private boolean isDead;

    private boolean isInGodMode;
	private boolean hasKeys = false;
    private int currentKeys = 0;


    private final GameScreen gameScreen;
    private final SoundController soundController;

    public Player(GameScreen gameScreen) {
        super(gameScreen.getGameField().getPlayerStartPos(), new Vector2(0f, 0f), DEFAULT_PLAYER_SPEED, new Vector2(0.4f, 0.4f), new Vector2(0.4f,
                0.8f), AnimationType.CHARACTER_DOWN);
        canDash = false;
        canInvis = false;
        isInGodMode = false;
        this.xp = 0;
        this.currentHealth = STARTING_PLAYER_HEALTH;
        this.maxHealth = STARTING_PLAYER_HEALTH;
        this.effects = new ArrayList<>();
		this.hasKeys = false;

        this.gameScreen = gameScreen;
		this.requiredKeys = gameScreen.getGameField().getRequiredKeys();
        this.soundController = gameScreen.getSoundController();
    }

    public int receiveXP(int xpReceived) {
        xp += xpReceived;
        gameScreen.getHUD().updatePlayerXP();
        return xp;
    }

    public int getDamageOrHeal(int damage) {
        // negative damage for heal
        if (currentHealth - damage < 0) {
            currentHealth = 0;
        }
        else if (currentHealth - damage > maxHealth) {
            currentHealth = maxHealth;
        }
        else {
            currentHealth -= damage;
        }
        if (this.gameScreen.getHUD() != null) {
            this.gameScreen.getHUD().renderPlayerHP();
        }
        return currentHealth;
    }

    public int increaseMaxHealth(int extraHealth) {
        this.maxHealth += extraHealth;
        this.currentHealth += extraHealth;
        if (this.gameScreen.getHUD() != null) {
            this.gameScreen.getHUD().renderPlayerHP();
        }
        return this.maxHealth;
    }

    private void removeEffect(Class<? extends Effect> effectType) {
        // removes only one Effect (if exists)
        for (int i = 0; i < effects.size(); i++) {
            Effect e = effects.get(i);
            if (e.getClass() == effectType) {
                e.onEnd();
                effects.remove(i);
                break;
            }
        }
        this.gameScreen.getHUD().updatePlayerStatusEffects();
    }

    public void addOrReapplyEffect(Effect effect) {
        if (effect.getClass() == Dash.class && this.isDashInCooldown()) { // do not apply dash if it's in cooldown
            return;
        }
        if (effect.getClass() == Invis.class && this.isInvisInCooldown()) { // do not apply dash if it's in cooldown
            return;
        }

        this.removeEffect(effect.getClass());
        effect.onStart();
        this.effects.add(effect);
        this.gameScreen.getHUD().updatePlayerStatusEffects();
    }

    public void tickEffects(float deltaTime) {
        ArrayList<Class<? extends Effect>> effectsToRemove = new ArrayList<>();
        for (Effect effect : effects) {
            boolean isFinished = effect.onTick(deltaTime);
            if (isFinished) {
                effectsToRemove.add(effect.getClass());
            }
        }
        for (Class<? extends Effect> effectType : effectsToRemove) {
            this.removeEffect(effectType);
        }
    }

//    public void activateInvis(MazeRunnerGame game){
//        if(canInvis){
//            game.loadCharacterInvisAnimation();
//        }
//    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getEffectDuration(Class<? extends Effect> effectType) {
        for (Effect effect : effects) {
            if (effect.getClass() == effectType) {
                return effect.getTimeLeft();
            }
        }
        return 0f;
    }

    public boolean hasEffect(Class<? extends Effect> effectType) {
        for (Effect effect : effects) {
            if (effect.getClass() == effectType) {
                return true;
            }
        }
        return false;
    }

    public GameObject getPlayerGameObject() {
        return (GameObject) this;
    }

	public void collectKey() {
        this.currentKeys ++;
        if(currentKeys == requiredKeys){
            this.hasKeys = true;
        }
        else if(currentKeys > requiredKeys){
            this.currentKeys = requiredKeys;
        }

		System.out.println("Key collected!");
	}

    public void die(){
        soundController.playPlayerDieSound(1f);
    }

	public boolean hasKeys() {
		return hasKeys;
	}

    private boolean isDashInCooldown() {
        return hasEffect(DashCooldown.class);
    }
    private boolean isInvisInCooldown() {
        return hasEffect(InvisCooldown.class);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public boolean isCanDash() {
        return canDash;
    }

    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    public boolean isCanInvis() {
        return canInvis;
    }

    public void setCanInvis(boolean canInvis) {
        this.canInvis = canInvis;
    }

//    public void multiplySpeed(float multiplier) {
//        this.multiplySpeed(multiplier);
//    }

    public boolean isInGodMode() {
        return isInGodMode;
    }

    public void setInGodMode(boolean inGodMode) {
        isInGodMode = inGodMode;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public void setHasKeys(boolean hasKey) {
        this.hasKeys = hasKey;
    }

    public int getCurrentKeys() {
        return currentKeys;
    }

    public int getRequiredKeys() {
        return requiredKeys;
    }

    public boolean isInvis() {
        return invis;
    }

    public void setInvis(boolean invis) {
        this.invis = invis;
    }
}
