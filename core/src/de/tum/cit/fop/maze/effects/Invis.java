package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.objects.Player;

public class Invis extends Effect {

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
