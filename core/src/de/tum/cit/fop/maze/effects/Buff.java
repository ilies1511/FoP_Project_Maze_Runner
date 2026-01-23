package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * A Buff effect that increases the player's speed for a duration.
 */
public class Buff extends Effect {
    private static final float SPEED_MULTIPLIER = 1.6f;

    /**
     * Creates a new Buff effect.
     * @param player The player to apply the buff to.
     * @param duration The duration of the buff.
     */
    public Buff(Player player, float duration) {
        this.player = player;
        this.timeLeft = duration;
    }

    @Override
    public void onStart() {
        this.player.getPlayerGameObject().multiplySpeed(SPEED_MULTIPLIER);
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
