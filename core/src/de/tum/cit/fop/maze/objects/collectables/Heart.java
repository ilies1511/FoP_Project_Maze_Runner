package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.HeartMemento;
import de.tum.cit.fop.maze.objects.Player;

/**
 * Heart collectable that heals the player.
 */
public class Heart extends Collectable implements Saveable<HeartMemento> {
    public Heart(Vector2 startpos) {
        super(startpos,
                new Vector2(0.5f, 0.5f),
                new Vector2(0.65f, 0.8f),
                new Vector2(0.0f, 0.0f),
                AnimationType.VAMPIRIC_HEART);
    }

    @Override
    protected void onPickUp(Player player, HUD hud) {
        player.getSoundController().playPlayerHealSound(1f);
        player.getDamageOrHeal(-1);
    }

    @Override
    public HeartMemento getMemento() {
        return new HeartMemento(this.position.x, this.position.y);
    }

    @Override
    public void loadFromMemento(HeartMemento memento) {
        this.position.set(memento.positionX, memento.positionY);
    }
}
