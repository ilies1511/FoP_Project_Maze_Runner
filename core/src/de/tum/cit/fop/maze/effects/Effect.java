package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

/**
 * Abstract base class for all status effects applied to the player.
 * Handles the duration and lifecycle (start, tick, end) of effects.
 */
public abstract class Effect {
    /** The player affected by this effect. */
    protected Player player;
    /** The remaining time for this effect. */
    protected float timeLeft;

    /**
     * Gets the remaining time of the effect.
     * @return The time left in seconds.
     */
    public float getTimeLeft() {
        return this.timeLeft;
    }

    /**
     * Called when the effect is first applied.
     */
    public abstract void onStart();

    /**
     * Called every frame to update the effect.
     * @param deltaTime The time elapsed since the last frame.
     * @return True if the effect has expired, false otherwise.
     */
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    } // returns True when effect should be finished

    /**
     * Called when the effect expires or is removed.
     */
    public abstract void onEnd();
}
