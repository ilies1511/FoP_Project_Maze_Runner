package de.tum.cit.fop.maze.objects.traps;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

public class TrapFactory {
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
