package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

public abstract class Collectable extends GameObject {
    private final Vector2 spriteShift;

    public Collectable(Vector2 startPos, Vector2 spriteSizes, Vector2 spriteShift, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                0f,
                new Vector2(1f, 1f),
                spriteSizes,
                animationType
        );
        this.spriteShift = spriteShift;
    }

    @Override
    public void animate(Batch batch) {
        this.drawable.animate(batch, this.position.cpy().add(this.spriteShift), this.spriteSizes);
    }

    abstract protected void onPickUp(Player player);

    public boolean checkPlayerCollision(Player player) {
        if (GameObject.entitiesOverlap(player, this)) {
            this.onPickUp(player);
            return true;
        }
        return false;
    }
}
