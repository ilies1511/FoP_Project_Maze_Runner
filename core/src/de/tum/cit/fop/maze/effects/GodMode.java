package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class GodMode extends Effect {
    public GodMode(Player player) {
        this.player = player;
        this.timeLeft = Float.POSITIVE_INFINITY;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onEnd() {
    }
}
