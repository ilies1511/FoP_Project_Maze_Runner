package de.tum.cit.fop.maze.objects.traps;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory class for creating random traps.
 */
public class TrapCreator {
    /**
     * Creates a random trap at a position.
     * @param startpos Starting position
     * @return New trap instance
     */
    public static Trap createRandomTrap(Vector2 startpos) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
        if (randomNum == 1) {
            return new FreezeTrap(startpos);
        } else if (randomNum == 2) {
            return new GarlicTrap(startpos);
        }
        throw new AssertionError("Unreachable: number is not in possible range!");
    }
}
