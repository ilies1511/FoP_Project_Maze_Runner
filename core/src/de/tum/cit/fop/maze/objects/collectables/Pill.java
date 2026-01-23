package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.effects.GodMode;
import de.tum.cit.fop.maze.objects.Player;

public class Pill extends Collectable{
    public Pill(Vector2 startpos) {
        super(startpos,
                new Vector2(0.5f, 0.5f),
                new Vector2(0.65f, 0.8f),
                new Vector2(0.0f, 0.0f),
               AnimationType.PILL);
    }

    @Override
    protected void onPickUp(Player player, HUD hud) {
        player.getSoundController().playPlayerHealSound(1f);
        player.addOrReapplyEffect(new GodMode(player, 5f));
    }
}
