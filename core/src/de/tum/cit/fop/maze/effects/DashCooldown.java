package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A cooldown effect for the Dash ability.
 * Prevents the player from dashing again until the duration expires.
 */
public class DashCooldown extends Effect {
    private static final float DURATION = 0.3f;

    /**
     * Creates a new DashCooldown effect.
     * @param player The player to apply the cooldown to.
     */
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
