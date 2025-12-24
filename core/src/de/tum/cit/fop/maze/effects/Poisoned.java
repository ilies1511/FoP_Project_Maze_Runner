package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Poisoned extends Effect {
    private static final float DURATION = 3;
    private static final float SPEED_MULTIPLIER = 0.5f;

    private float timeLeft;

    public Poisoned(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
    }

    public float getTimeLeft() {
        return this.timeLeft;
    }

    @Override
    public void onStart() {
        if(!player.isInGodMode()){
            this.player.getPlayerGameObject().multiplySpeed(SPEED_MULTIPLIER);
        }
    }


    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0;
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().multiplySpeed(1 / SPEED_MULTIPLIER);
    }
}
