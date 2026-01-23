package de.tum.cit.fop.maze.objects.traps;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.effects.Poison;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.GarlicTrapMemento;
import de.tum.cit.fop.maze.objects.Player;

/**
 * Trap that applies poison effect to damage player over time.
 */
public class GarlicTrap extends Trap implements Saveable<GarlicTrapMemento> {
    public GarlicTrap(Vector2 startpos) {
        super(startpos,
                new Vector2(1.2f, 1.2f),
                new Vector2(-0.1f, -0.1f),
                AnimationType.GARLIC_TRAP);
    }

    @Override
    protected void onEnter(Player player, HUD hud) {
        player.addOrReapplyEffect(new Poison(player));
    }

    @Override
    public GarlicTrapMemento getMemento() {
        return new GarlicTrapMemento(this.position.x, this.position.y);
    }

    @Override
    public void loadFromMemento(GarlicTrapMemento memento) {
        this.position.set(memento.positionX, memento.positionY);
    }
}
