package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Flicker extends Effect {
    private static final float DURATION = 3f;
    private static final float HIDE_INTERVAL = 0.2f;

    private float hide_count;

    public Flicker(Player player) {
        this.player = player;
        this.timeLeft = DURATION;
        this.hide_count = 0.0f;
    }

    @Override
    public void onStart() {
    }

    @Override
    public boolean onTick(float deltaTime) {
        this.hide_count -= deltaTime;
        if (this.hide_count <= 0.0f) {
            this.hide_count = HIDE_INTERVAL;
            this.player.addOrReapplyEffect(new FlickerHidden(this.player));
        }

        this.timeLeft -= deltaTime;
        return this.timeLeft <= 0; // finish effect at the end of duration
    } // returns True when effect should be finished

    @Override
    public void onEnd() {
    }
}
