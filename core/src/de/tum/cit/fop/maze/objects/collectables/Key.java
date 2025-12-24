package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;

public class Key extends Collectable {
    public Key(Vector2 startpos) {
        super(startpos,
                new Vector2(1f, 1.42f),
                AnimationType.KEY_SHINE_ANIMATION);
    }
}
