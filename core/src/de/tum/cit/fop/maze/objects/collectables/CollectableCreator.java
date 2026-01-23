package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

public class CollectableCreator {
    public static Collectable createRandomCollectable(Vector2 startpos) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
        if (randomNum == 1) {
            return new Heart(startpos);
        }
        else if(randomNum == 2){
            return new Pill(startpos);
        }
        throw new AssertionError("Unreachable: number is not in possible range!");
    }
}
