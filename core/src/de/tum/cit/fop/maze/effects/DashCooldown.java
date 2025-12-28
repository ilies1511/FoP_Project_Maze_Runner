package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class DashCooldown extends Effect {
    private static final float DURATION = 0.3f;

    public DashCooldown(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onEnd() {
    }
}
