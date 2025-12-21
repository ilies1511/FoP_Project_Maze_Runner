package de.tum.cit.fop.maze;

import de.tum.cit.fop.maze.effects.Effect;

import java.util.ArrayList;

public class Player {
    private int health;
    private boolean isPoisoned;
    private boolean isBuffed;
    private float poisonDuration;
    private float buffDuration;
    private final ArrayList<Effect> effects;

    public Player() {
        this.health = 3; //player has x hps
        this.effects = new ArrayList<>();
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

    public float getBuffed(float buffDuration){
        isBuffed = true;
        this.buffDuration = buffDuration;
        return buffDuration;
    }

    public float getPoisoned(float poisonDuration){
        isPoisoned = true;
        this.poisonDuration = poisonDuration;
        return poisonDuration;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public float getPoisonDuration() {
        return poisonDuration;
    }

    public void setPoisonDuration(float poisonDuration) {
        this.poisonDuration = poisonDuration;
    }

    public boolean isBuffed() {
        return isBuffed;
    }

    public void setBuffed(boolean buffed) {
        isBuffed = buffed;
    }

    public float getBuffDuration() {
        return buffDuration;
    }

    public void setBuffDuration(float buffDuration) {
        this.buffDuration = buffDuration;
    }
}
