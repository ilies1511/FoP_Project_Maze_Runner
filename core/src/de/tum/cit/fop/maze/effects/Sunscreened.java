package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Sunscreened extends Effect {
    private static final float DURATION = 5f;

    public Sunscreened(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {}

    @Override
    public void onEnd() {}
}
