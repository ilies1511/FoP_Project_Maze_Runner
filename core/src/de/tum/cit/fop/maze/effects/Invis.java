package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * An Invisibility effect that makes the player invisible to enemies.
 */
public class Invis extends Effect {

    /**
     * Creates a new Invis effect.
     * @param player The player to make invisible.
     * @param duration The duration of invisibility.
     */
    public Invis(Player player, float duration) {
        this.player = player;
        this.timeLeft = duration;
    }

    @Override
    public void onStart() {
        this.player.addOrReapplyEffect(new InvisCooldown(this.player));

    }

    @Override
    public void onEnd() {
    }
}
