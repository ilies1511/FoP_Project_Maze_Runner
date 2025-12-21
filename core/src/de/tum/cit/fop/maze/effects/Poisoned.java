package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Player;

public class Poisoned extends Effect {
    private static final float DURATION = 3;
    private static final float SPEED_MULTIPLIER = 0.5f;

    private float timeLeft;

    public Poisoned(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
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
        return this.timeLeft <= 0;
    }

    @Override
    public void onEnd() {
        this.player.getPlayerEntity().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
