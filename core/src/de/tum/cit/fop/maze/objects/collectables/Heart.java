package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.objects.Player;

public class Heart extends Collectable {
    public Heart(Vector2 startpos) {
        super(startpos,
                new Vector2(1.0f, 1.2f),
                new Vector2(0.0f, 0.0f),
                AnimationType.VAMPIRIC_HEART);
    }

    @Override
    protected void onPickUp(Player player, HUD hud) {
        player.getDamage(-1);
        hud.updatePlayerHP(0);
    }
}
