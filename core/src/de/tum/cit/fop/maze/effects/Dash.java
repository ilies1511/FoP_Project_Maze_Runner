package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Player;

public class Dash extends Effect {
    private static final float DURATION = 1f;

    private float timeLeft;


    public Dash(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
        System.out.println("Applied dash");
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    }

    @Override
    public void onEnd() {
        System.out.println("Finished dash");
    }
}
