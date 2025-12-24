package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.effects.DashCooldown;
import de.tum.cit.fop.maze.effects.Effect;
import de.tum.cit.fop.maze.effects.Poisoned;

import java.util.ArrayList;

public class Player extends GameObject {
    private final static float DEFAULT_PLAYER_SPEED = 3f;

    private int health;
    private int xp;
    private final ArrayList<Effect> effects;

    private boolean canDash;
    private boolean canInvis;

    private boolean isInGodMode;


    public Player(GameScreen gameScreen) {
        super(gameScreen.getGameField().getPlayerStartPos(), new Vector2(0f, 0f), DEFAULT_PLAYER_SPEED, new Vector2(0.4f, 0.4f), new Vector2(0.4f,
                0.8f));
        canDash = false;
        canInvis = false;
        isInGodMode = false;
        this.xp = 0;
        this.health = 3; //player has x hps
        this.effects = new ArrayList<>();
    }

    public int receiveXP(int xpReceived){
        return xp+=xpReceived;
    }

    public int getDamage(int damage){
        if(health - damage < 0){
            health = 0;
            return health;
        }
        health -= damage;
        return damage;
    }

    private void removeEffect(Class<? extends Effect> effectType) {
        // removes only one Effect (if exists)
        for (int i = 0; i < effects.size(); i++) {
            Effect e =  effects.get(i);
            if (e.getClass() == effectType) {
                e.onEnd();
                effects.remove(i);
                break;
            }
        }
    }

    public void addOrReapplyEffect(Effect effect) {
        if (effect.getClass() == Dash.class && this.isDashInCooldown()) { // do not apply dash if it's in cooldown
            return;
        }
        this.removeEffect(effect.getClass());
        effect.onStart();
        this.effects.add(effect);
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isPoisoned() {
        return effectInEffects(Poisoned.class);
    }

    public float getPoisonDuration() {
        for (Effect effect : effects) {
            if (effect.getClass() == Poisoned.class) {
                return ((Poisoned) effect).getTimeLeft();
            }
        }
        return 0f;
    }

    public GameObject getPlayerGameObject() {
        return (GameObject) this;
    }

    private boolean effectInEffects(Class<? extends Effect> effectType) {
        for (Effect effect : effects) {
            if (effect.getClass() == effectType) {
                return true;
            }
        }
        return false;
    }

    private boolean isDashInCooldown() {
        return effectInEffects(DashCooldown.class);
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

    public void multiplySpeed(float multiplier){
        this.multiplySpeed(multiplier);
    }

    public boolean isInGodMode() {
        return isInGodMode;
    }

    public void setInGodMode(boolean inGodMode) {
        isInGodMode = inGodMode;
    }
}
