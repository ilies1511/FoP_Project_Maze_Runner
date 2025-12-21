package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Player;

public class Dash extends Effect {
    private static final float DURATION = 0.1f;
    private static final float SPEED_BOOST = 8f;

    private float timeLeft;


    public Dash(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    @Override
    public void onStart() {
        this.player.addOrReapplyEffect(new DashCooldown(this.player));
        this.player.getPlayerEntity().increaseSpeed(SPEED_BOOST);
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    }

    @Override
    public void onEnd() {
        this.player.getPlayerEntity().increaseSpeed(-SPEED_BOOST);
    }
}
