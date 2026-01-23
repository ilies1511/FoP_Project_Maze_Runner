package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A cooldown effect for Invisibility.
 * Prevents the player from becoming invisible again until the duration expires.
 */
public class InvisCooldown extends Effect {
    private static final float DURATION = 10f;

    /**
     * Creates a new InvisCooldown effect.
     * @param player The player to apply the cooldown to.
     */
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
