package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

/**
 * Base class for collectables that can be picked up by the player.
 */
public abstract class Collectable extends GameObject {
    private final Vector2 spriteShift;

    public Collectable(Vector2 startPos, Vector2 sizes, Vector2 spriteSizes, Vector2 spriteShift, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                0f,
                sizes,
                spriteSizes,
                animationType
        );
        this.spriteShift = spriteShift;
    }

    @Override
    public void animate(Batch batch) {
        this.drawable.animate(batch, this.position.cpy().add(this.spriteShift), this.spriteSizes);
    }

    abstract protected void onPickUp(Player player, HUD hud);

    /**
     * Checks collision with player and triggers pickup.
     * @param player The player
     * @param hud HUD for UI updates
     * @return True if collected
     */
    public boolean checkPlayerCollision(Player player, HUD hud) {
        if (GameObject.entitiesOverlap(player, this)) {
            this.onPickUp(player, hud);
            return true;
        }
        return false;
    }
}
