package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public abstract class Effect {
    protected Player player;
    protected float timeLeft;

    public float getTimeLeft() {
        return this.timeLeft;
    }

    public abstract void onStart();

    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    } // returns True when effect should be finished

    public abstract void onEnd();
}
