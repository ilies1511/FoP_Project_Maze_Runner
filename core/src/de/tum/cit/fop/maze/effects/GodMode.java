package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A God Mode effect that grants invulnerability or other cheats.
 * It has infinite duration.
 */
public class GodMode extends Effect {
    /**
     * Creates a new GodMode effect.
     * @param player The player to apply god mode to.
     */
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
