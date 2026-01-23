package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory class for creating random collectables.
 */
public class CollectableCreator {
    /**
     * Creates a random collectable at a position.
     * @param startpos Starting position
     * @return New collectable instance
     */
    public static Collectable createRandomCollectable(Vector2 startpos) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 2);
        if (randomNum == 1) {
            return new Heart(startpos);
        }
        throw new AssertionError("Unreachable: number is not in possible range!");
    }
}
