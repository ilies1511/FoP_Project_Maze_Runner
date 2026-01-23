package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A Sunscreen effect that protects the player.
 */
public class Sunscreen extends Effect {
    private static final float DURATION = 5f;

    /**
     * Creates a new Sunscreen effect.
     * @param player The player to apply sunscreen to.
     */
    public Sunscreen(Player player) {
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
