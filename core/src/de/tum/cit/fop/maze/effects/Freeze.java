package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A Freeze effect that significantly slows down the player.
 */
public class Freeze extends Effect {
    private static final float DURATION = 3f;
    private static final float SPEED_MULTIPLIER = 0.3f;

    /**
     * Creates a new Freeze effect.
     * @param player The player to freeze.
     */
    public Freeze(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
        if (!player.hasEffect(GodMode.class)) {
            this.player.getPlayerGameObject().multiplySpeed(SPEED_MULTIPLIER);
        }
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
