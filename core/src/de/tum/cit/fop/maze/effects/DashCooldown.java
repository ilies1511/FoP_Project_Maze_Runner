package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class DashCooldown extends Effect {
    private static final float DURATION = 0.3f;

    private float timeLeft;

    public DashCooldown(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0;
    }

    @Override
    public void onEnd() {
    }
}
