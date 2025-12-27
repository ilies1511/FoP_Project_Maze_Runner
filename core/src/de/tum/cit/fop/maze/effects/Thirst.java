package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Thirst extends Effect {
    private static final float DURATION = 0.5f;
    private static final float SPEED_MULTIPLIER = 3f;

    private float timeLeft;

    public Thirst(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
        if (!player.isInGodMode()) {
            this.player.getPlayerGameObject().multiplySpeed(SPEED_MULTIPLIER);
        }
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
