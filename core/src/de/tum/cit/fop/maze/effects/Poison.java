package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Poison extends Effect {
    private static final float DURATION = 3;
    private static final float SPEED_MULTIPLIER = 0.7f;
    private static final int TOTAL_DAMAGE_TICKS = 2;

    private int currentTick;

    public Poison(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
        this.currentTick = 0;
    }

    @Override
    public void onStart() {
        this.player.getPlayerGameObject().multiplySpeed(SPEED_MULTIPLIER);
    }


    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        // spread damage ticks over duration evenly
        float timeBetweenTicks = DURATION / (TOTAL_DAMAGE_TICKS + 1);
        int currentTickByTime = TOTAL_DAMAGE_TICKS - (int) (this.timeLeft / timeBetweenTicks);
        if (this.currentTick <= TOTAL_DAMAGE_TICKS && currentTickByTime == this.currentTick) {
            if (this.currentTick != 0) { // do not damage immediately
                this.player.getDamageOrHeal(1);
            }
            this.currentTick++;
        }
        return this.timeLeft <= 0;
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
