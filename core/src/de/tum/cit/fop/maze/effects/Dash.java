package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.objects.Player;

public class Dash extends Effect {
    private static final float DURATION = 0.1f;
    private static final float SPEED_BOOST = 8f;

    private float timeLeft;

    private final SoundController soundController;

    public Dash(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
        soundController = player.getSoundController();
    }

    @Override
    public void onStart() {
//        if( player.isCanDash()){

            this.player.addOrReapplyEffect(new DashCooldown(this.player));
            this.player.getPlayerGameObject().increaseSpeed(SPEED_BOOST);
            soundController.playPlayerDashSound(1f);
//        }

    }

    @Override
    public boolean onTick(float deltaTime) {
        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    }

    @Override
    public void onEnd() {
        this.player.getPlayerGameObject().increaseSpeed(-SPEED_BOOST);
    }
}
