package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Player;

public class Poisoned extends Effect {
    private static final float DURATION = 3f;
    private static final float SPEED_MULTIPLIER = 0.5f;
    private static final float DAMAGE_FREQUENCY = 1f;
    private static final int DAMAGE = 1;

    private float timeLeft;
    private float timeFromLastDamage;

    public Poisoned(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
        this.timeFromLastDamage = 0;
    }

    public float getTimeLeft() {
        return this.timeLeft;
    }

    @Override
    public void onStart() {
        this.player.getPlayerEntity().multiplySpeed(SPEED_MULTIPLIER);
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        if (this.timeLeft <= 0) {
            return true;
        }
        this.timeFromLastDamage += deltaTime;
        if (this.timeFromLastDamage >= DAMAGE_FREQUENCY) {
            this.timeFromLastDamage -= DAMAGE_FREQUENCY;
            this.player.getGameScreen().getHUD().updatePlayerHP(player.getDamage(1));
        }
        return false;
    }

    @Override
    public void onEnd() {
        this.player.getPlayerEntity().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
