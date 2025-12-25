package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;

public class Key extends Collectable {
    public Key(Vector2 startpos) {
        super(startpos,
                new Vector2(0.91f, 0.7f),
                new Vector2(0.03f, 0.15f),
                AnimationType.KEY_SHINE_ANIMATION);
    }
}
