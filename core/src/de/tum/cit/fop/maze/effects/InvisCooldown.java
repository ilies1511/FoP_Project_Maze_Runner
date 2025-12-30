package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class InvisCooldown extends Effect {
    private static final float DURATION = 5.5f;

    public InvisCooldown(Player player) {
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
