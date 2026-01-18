package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.KeyMemento;
import de.tum.cit.fop.maze.objects.Player;

public class Key extends Collectable implements Saveable<KeyMemento> {

    public Key(Vector2 startpos) {
        super(startpos,
                new Vector2(1f, 1f),
                new Vector2(0.91f, 0.7f),
                new Vector2(0.03f, 0.15f),
                AnimationType.KEY_SHINE_ANIMATION);

    }

    @Override
    protected void onPickUp(Player player, HUD hud) {
        player.getSoundController().playKeyPickupSound(1f);
        player.collectKey();
        hud.updatePlayerKeys();
        if(player.hasKeys()){
            hud.deleteArrow();
            hud.createArrow();
        }

        // hud.setHasKey(true); // TODO: Uncomment when HUD ready --> Nikita

    }

    @Override
    public KeyMemento getMemento() {
        return new KeyMemento(this.position.x, this.position.y);
    }

    @Override
    public void loadFromMemento(KeyMemento memento) {
        this.position.set(memento.positionX, memento.positionY);
    }
}
