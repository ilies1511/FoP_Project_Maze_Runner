package de.tum.cit.fop.maze.objects.traps;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

/**
 * Base class for traps that apply effects on player contact.
 */
public abstract class Trap extends GameObject {
    private final Vector2 spriteShift;

    public Trap(Vector2 startPos, Vector2 spriteSizes, Vector2 spriteShift, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                0f,
                new Vector2(0.6f, 0.6f),
                spriteSizes,
                animationType
        );
        this.spriteShift = spriteShift;
    }

    @Override
    public void animate(Batch batch) {
        this.drawable.animate(batch, this.position.cpy().add(this.spriteShift), this.spriteSizes);
    }

    abstract protected void onEnter(Player player, HUD hud);

    /**
     * Checks collision with player and triggers trap effect.
     * @param player The player
     * @param hud HUD for UI updates
     */
    public void checkPlayerCollision(Player player, HUD hud) {
        if (GameObject.entitiesOverlap(player, this)) {
            this.onEnter(player, hud);
        }
    }
}
