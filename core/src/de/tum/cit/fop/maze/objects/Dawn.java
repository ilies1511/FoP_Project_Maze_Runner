package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.effects.Sunscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dawn extends GameObject {
    private static final int DAWN_SPAWN_RADIUS = 2;
    private static final float DAWN_SPAWN_INTERVAL = 1.6f;
    private static float timeBeforeSpawn = DAWN_SPAWN_INTERVAL;
    private static final Random rand = new Random();

    private boolean isActive = false;
    private float timeLeftBeforeActive = 1.8f;
    public float timeLeftBeforeRemoved = 1.2f;

    public static void spawnNewDawn(float delta, GameField gamefield, Vector2 playerPosition, List<GameObject> gameObjects) {
        // check if enough time left since last spawn
        timeBeforeSpawn -= delta;
        if (timeBeforeSpawn <= 0.0f) {
            timeBeforeSpawn = DAWN_SPAWN_INTERVAL;
        } else {
            return;
        }

        // spawns dawn on empty cell near player in (2 * DAWN_SPAWN_RADIUS + 1) x (2 * DAWN_SPAWN_RADIUS + 1) square
        List<Vector2> candidatePositions  = new ArrayList<Vector2>();
        int player_x_int = (int) playerPosition.x;
        int player_y_int = (int) playerPosition.y;
        for (int y = player_y_int - DAWN_SPAWN_RADIUS; y <= player_y_int + DAWN_SPAWN_RADIUS; y++) {
            for (int x = player_x_int - DAWN_SPAWN_RADIUS; x <= player_x_int + DAWN_SPAWN_RADIUS; x++) {
                if (gamefield.getTile(x, y) == TileType.EMPTY) {
                    candidatePositions.add(new Vector2(x, y));
                }
            }
        }

        if (candidatePositions.isEmpty()) {
            // shouldn't happen actually, but whatever
            return;
        }

        int randomIndex = rand.nextInt(candidatePositions.size());
        Vector2 spawnPosition = candidatePositions.get(randomIndex).cpy().add(new Vector2(-0.15f, -0.05f)); // shift for centering it in the cell
        gameObjects.add(new Dawn(spawnPosition));
    }

    public Dawn(Vector2 startPos) {
        super(
                startPos,
                new Vector2(0f, 0f),
                0f,
                new Vector2(0.8f, 0.8f),
                new Vector2(1.3f, 1.1f),
                AnimationType.DAWN_THIN_ANIMATION
        );
    }

    public boolean updateDawn(float delta) {
        // returns true if dawn life is over and it must be deleted
        if (!this.isActive) {
            this.timeLeftBeforeActive -= delta;
            if (this.timeLeftBeforeActive <= 0.0f) {
                activate();
            }
            return false;
        }
        else {
            this.timeLeftBeforeRemoved -= delta;
            return this.timeLeftBeforeRemoved <= 0.0f;
        }
    }

    private void activate() {
        this.isActive = true;
        this.changeAnimation(AnimationType.DAWN_WIDE_ANIMATION);
    }

    public boolean checkPlayerCollision(Player player) {
        if (this.isActive && !player.hasEffect(Sunscreen.class) && GameObject.entitiesOverlap(player, this)) {
            player.getDamageOrHeal(1);
            return true;
        }
        return false;
    }
}
