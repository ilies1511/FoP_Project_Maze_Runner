package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A short-duration effect that hides the player during the Flicker effect.
 */
public class FlickerHidden extends Effect {
    // effect used for hiding player animation during flicker
    private static final float DURATION = 0.1f;

    /**
     * Creates a new FlickerHidden effect.
     * @param player The player to hide.
     */
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
