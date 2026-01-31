package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.UI.AchievementUI;
import de.tum.cit.fop.maze.effects.*;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.PlayerMemento;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main player character with health, abilities, inventory, and effects management.
 */
public class Player extends GameObject implements Saveable<PlayerMemento> {
    private final static float DEFAULT_PLAYER_SPEED = 3f;
    private final static int STARTING_PLAYER_HEALTH = 3;
    private final ArrayList<Effect> effects;
    private final GameScreen gameScreen;
    private final SoundController soundController;
    private final AchievementUI achievementUI;
    private int requiredKeys;
    private int currentHealth;
    private int maxHealth;
    private int xp;
    private boolean canDash;
    private boolean canInvis;
    private boolean isAttacking = false;
    private boolean hasKeys = false;
    private int currentKeys = 0;
    private int kills;


    public Player(GameScreen gameScreen) {
        super(gameScreen.getGameField().getPlayerStartPos(), new Vector2(0f, 0f), DEFAULT_PLAYER_SPEED, new Vector2(0.4f, 0.4f), new Vector2(0.4f,
                0.8f), AnimationType.CHARACTER_DOWN);
        canDash = false;
        canInvis = false;
        this.xp = 0;
        this.currentHealth = STARTING_PLAYER_HEALTH;
        this.maxHealth = STARTING_PLAYER_HEALTH;
        this.effects = new ArrayList<>();
        this.hasKeys = false;

        this.gameScreen = gameScreen;
        this.requiredKeys = gameScreen.getGameField().getRequiredKeys();
        this.soundController = gameScreen.getSoundController();
        achievementUI = gameScreen.getAchievementUI();


    }

    /**
     * Adds experience points to the player.
     * @param xpReceived Amount of XP to add
     * @return New total XP
     */
    public int receiveXP(int xpReceived) {
        xp += xpReceived;
        gameScreen.getHUD().updatePlayerXP();
        return xp;
    }

    /**
     * Applies damage or healing to the player.
     * @param damage Positive for damage, negative for healing
     * @return New health value
     */
    public int getDamageOrHeal(int damage) {
        if (damage > 0) {
            soundController.playPlayerHitSound(1f);
        }

        // negative damage for heal
        if (currentHealth - damage < 0) {
            currentHealth = 0;
        } else if (currentHealth - damage > maxHealth) {
            currentHealth = maxHealth;
        } else {
            currentHealth -= damage;
        }
        if (this.gameScreen.getHUD() != null) {
            this.gameScreen.getHUD().renderPlayerHP();
        }
        return currentHealth;
    }

    /**
     * Increases player's maximum and current health.
     * @param extraHealth Amount of health to add
     * @return New maximum health
     */
    public int increaseMaxHealth(int extraHealth) {
        this.maxHealth += extraHealth;
        this.currentHealth += extraHealth;
        if (this.gameScreen.getHUD() != null) {
            this.gameScreen.getHUD().renderPlayerHP();
        }
        return this.maxHealth;
    }

    @Override
    public void animate(Batch batch) {
        if (!this.hasEffect(FlickerHidden.class)) {
            this.drawable.animate(batch, this.position, this.spriteSizes);
        }
    }


    /**
     * Ends a specific effect type on the player.
     * @param effectTypeClass The effect class to remove
     */
    public void endEffect(Class<? extends Effect> effectTypeClass) {
        // removes only one Effect (if exists), triggers onEnd
        for (int i = 0; i < effects.size(); i++) {
            Effect e = effects.get(i);
            if (e.getClass() == effectTypeClass) {
                e.onEnd();
                effects.get(i).onEnd();
                effects.remove(i);
                break;
            }
        }
        this.gameScreen.getHUD().updatePlayerStatusEffects();
    }

    private void endEffectNoOnEndTrigger(Class<? extends Effect> effectType) {
        // removes only one Effect (if exists), does not trigger onEnd
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

    /**
     * Adds or refreshes an effect on the player.
     * @param effect The effect to apply
     */
    public void addOrReapplyEffect(Effect effect) {
        if (effect.getClass() == Dash.class && this.isDashInCooldown()) { // do not apply dash if it's in cooldown
            return;
        }
        if (effect.getClass() == Invis.class && this.isInvisInCooldown()) { // do not apply invis if it's in cooldown
            return;
        }
        if (this.hasEffect(GodMode.class) && Arrays.asList(Freeze.class, Poison.class).contains(effect.getClass())) { // do not apply negative effects while in god mode
            return;
        }

        this.endEffectNoOnEndTrigger(effect.getClass());
        effect.onStart();
        this.effects.add(effect);
        this.gameScreen.getHUD().updatePlayerStatusEffects();
    }

    /**
     * Updates all active effects on the player.
     * @param deltaTime Time since last tick
     */
    public void tickEffects(float deltaTime) {
        ArrayList<Class<? extends Effect>> effectsToRemove = new ArrayList<>();
        for (Effect effect : new ArrayList<>(effects)) { // create a temporary copy of effects list to avoid onTick modifying effects
            boolean isFinished = effect.onTick(deltaTime);
            if (isFinished) {
                effectsToRemove.add(effect.getClass());
            }
        }
        for (Class<? extends Effect> effectType : effectsToRemove) {
            this.endEffectNoOnEndTrigger(effectType);
        }
    }

    public void changeAnimation(AnimationType animationType) {
        super.changeAnimation(animationType);

        // if attack
        if (animationType == AnimationType.CHARACTER_ATTACK_UP ||
                animationType == AnimationType.CHARACTER_ATTACK_DOWN ||
                animationType == AnimationType.CHARACTER_ATTACK_LEFT ||
                animationType == AnimationType.CHARACTER_ATTACK_RIGHT) {

            //double width
            this.spriteSizes.set(0.8f, 0.8f);

        } else {
            //normal
            this.spriteSizes.set(0.4f, 0.8f);
        }
    }

    /**
     * Gets current health value.
     * @return Current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets current health value.
     * @param currentHealth New health value
     */
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    /**
     * Gets maximum health value.
     * @return Maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets maximum health value.
     * @param maxHealth New maximum health
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Gets remaining duration of an active effect.
     * @param effectType The effect class to check
     * @return Time remaining, or 0 if not active
     */
    public float getEffectDuration(Class<? extends Effect> effectType) {
        for (Effect effect : effects) {
            if (effect.getClass() == effectType) {
                return effect.getTimeLeft();
            }
        }
        return 0f;
    }

    /**
     * Checks if player has a specific effect active.
     * @param effectType The effect class to check
     * @return True if effect is active
     */
    public boolean hasEffect(Class<? extends Effect> effectType) {
        for (Effect effect : effects) {
            if (effect.getClass() == effectType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the player as a GameObject.
     * @return This player instance
     */
    public GameObject getPlayerGameObject() {
        return this;
    }

    /**
     * Collects a key and updates inventory.
     */
    public void collectKey() {
        this.currentKeys++;
        if (currentKeys == requiredKeys) {
            this.hasKeys = true;
        } else if (currentKeys > requiredKeys) {
            this.currentKeys = requiredKeys;
        }

        System.out.println("Key collected!");
    }

    /**
     * Checks if player is currently attacking.
     * @return True if attacking
     */
    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * Sets attacking state.
     * @param attacking New attacking state
     */
    public void setAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    /**
     * Handles player death sound.
     */
    public void die() {
        soundController.playPlayerDieSound(1f);
    }

    /**
     * Checks if player has all required keys.
     * @return True if has all keys
     */
    public boolean hasKeys() {
        return hasKeys;
    }

    private boolean isDashInCooldown() {
        return hasEffect(DashCooldown.class);
    }

    private boolean isInvisInCooldown() {
        return hasEffect(InvisCooldown.class);
    }

    /**
     * Gets player XP.
     * @return Current XP
     */
    public int getXp() {
        return xp;
    }

    /**
     * Sets player XP.
     * @param xp New XP value
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * Checks if player can use dash ability.
     * @return True if dash is unlocked
     */
    public boolean isCanDash() {
        return canDash;
    }

    /**
     * Sets dash ability availability.
     * @param canDash New dash availability
     */
    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    /**
     * Checks if player can use invisibility ability.
     * @return True if invis is unlocked
     */
    public boolean isCanInvis() {
        return canInvis;
    }

    /**
     * Sets invisibility ability availability.
     * @param canInvis New invis availability
     */
    public void setCanInvis(boolean canInvis) {
        this.canInvis = canInvis;
    }

    /**
     * Gets the sound controller.
     * @return Sound controller instance
     */
    public SoundController getSoundController() {
        return soundController;
    }

    /**
     * Sets whether player has all keys.
     * @param hasKey New key status
     */
    public void setHasKeys(boolean hasKey) {
        this.hasKeys = hasKey;
    }

    /**
     * Gets current number of keys collected.
     * @return Number of keys
     */
    public int getCurrentKeys() {
        return currentKeys;
    }

    /**
     * Sets current number of keys.
     * @param keys New key count
     */
    public void setCurrentKeys(int keys) {
        this.currentKeys = keys;
    }

    /**
     * Gets number of keys required.
     * @return Required key count
     */
    public int getRequiredKeys() {
        return requiredKeys;
    }

    /**
     * Sets number of keys required.
     * @param requiredKeys New required count
     */
    public void setRequiredKeys(int requiredKeys) {
        this.requiredKeys = requiredKeys;
    }

    /**
     * Sets player position.
     * @param pos New position vector
     */
    public void setPosition(Vector2 pos) {
        if (pos != null)
            this.position = pos;
    }

    /**
     * Gets achievement UI.
     * @return Achievement UI instance
     */
    public AchievementUI getAchievementUI() {
        return achievementUI;
    }

    /**
     * Gets number of kills.
     * @return Kill count
     */
    public int getKills() {
        return kills;
    }

    /**
     * Sets number of kills.
     * @param kills New kill count
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * Updates required keys from current game field.
     */
    public void updateRequiredKeys() {
        this.requiredKeys = gameScreen.getGameField().getRequiredKeys();
    }

    @Override
    public PlayerMemento getMemento() {
        System.out.println("GETMEMENTO ANIM TYPE" + this.currentAnimationType);
        return new PlayerMemento(
                this.currentHealth,
                this.maxHealth,
                this.xp,
                this.canDash,
                this.canInvis,
                this.hasKeys,
                this.currentKeys,
                this.requiredKeys,
                this.kills,
                this.position.x,
                this.position.y,
                this.velocity.x,
                this.velocity.y,
                this.speed,
                this.currentAnimationType
        );
    }

    @Override
    public void loadFromMemento(PlayerMemento memento) {
        this.currentHealth = memento.currentHealth;
        this.maxHealth = memento.maxHealth;
        this.xp = memento.xp;
        this.canDash = memento.canDash;
        this.canInvis = memento.canInvis;
        this.hasKeys = memento.hasKeys;
        this.currentKeys = memento.currentKeys;
        this.requiredKeys = memento.requiredKeys;
        this.kills = memento.kills;
        this.position.set(memento.positionX, memento.positionY);
        this.velocity.set(memento.velocityX, memento.velocityY);
        this.speed = memento.speed;
        this.currentAnimationType = memento.animationType;
        System.out.println("ANIMTYPE" +  this.currentAnimationType);
        System.out.println("XP" +  this.xp);
        if (this.gameScreen.getHUD() != null) {
            this.gameScreen.getHUD().renderPlayerHP();
            this.gameScreen.getHUD().updatePlayerKeys();
            this.gameScreen.getHUD().updatePlayerXP();
        }
    }
}
