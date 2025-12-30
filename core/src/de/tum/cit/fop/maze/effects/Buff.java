package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Buff extends Effect {
    private static final float SPEED_MULTIPLIER = 1.6f;

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
