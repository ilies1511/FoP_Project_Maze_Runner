package de.tum.cit.fop.maze.effects;

import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.objects.Player;

public class Invis extends Effect{
    private MazeRunnerGame game;
    public Invis(Player player, float duration, MazeRunnerGame game) {
        this.player = player;
        this.timeLeft = duration;
        this.game = game;
    }

    @Override
    public void onStart() {
        player.setInvis(true);
        this.player.addOrReapplyEffect(new InvisCooldown(this.player));

    }

    @Override
    public void onEnd() {
       player.setInvis(false);
    }
}
