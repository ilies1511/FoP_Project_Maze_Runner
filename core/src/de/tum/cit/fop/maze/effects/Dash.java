package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.objects.Player;

/**
 * A Dash effect that provides a short burst of speed to the player.
 * It also triggers a dash sound and applies a cooldown.
 */
public class Dash extends Effect {
    private static final float DURATION = 0.1f;
    private static final float SPEED_BOOST = 8f;

    private final SoundController soundController;

    /**
     * Creates a new Dash effect.
     * @param player The player performing the dash.
     */
    public Dash(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
        soundController = player.getSoundController();
    }

    @Override
    public void onStart() {
        this.player.addOrReapplyEffect(new DashCooldown(this.player));
        this.player.getPlayerGameObject().increaseSpeed(SPEED_BOOST);
        soundController.playPlayerDashSound(1f);
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().increaseSpeed(-SPEED_BOOST);
    }
}
