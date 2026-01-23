package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class FlickerHidden extends Effect {
    // effect used for hiding player animation during flicker
    private static final float DURATION = 0.1f;

    public FlickerHidden(Player player) {
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
