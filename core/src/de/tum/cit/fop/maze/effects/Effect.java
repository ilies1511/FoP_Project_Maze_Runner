package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Player;

public abstract class Effect {
    protected Player player;

    public abstract void onStart();
    public abstract boolean onTick(float deltaTime); // returns True when effect should be finished
    public abstract void onEnd();
}
