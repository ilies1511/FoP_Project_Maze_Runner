package de.tum.cit.fop.maze.objects.traps;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.objects.Player;

public class FreezeTrap extends Trap {
    public FreezeTrap(Vector2 startpos) {
        super(startpos,
                new Vector2(1.2f, 1.2f),
                new Vector2(-0.1f, -0.1f),
                AnimationType.FREEZE_TRAP);
    }

    @Override
    protected void onEnter(Player player, HUD hud) {

    }
}
