package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.objects.GameObject;

public abstract class Collectable extends GameObject {
    public Collectable(Vector2 startPos, Vector2 spriteSizes, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                0f,
                new Vector2(1f, 1f),
                spriteSizes,
                animationType
        );
    }
}
