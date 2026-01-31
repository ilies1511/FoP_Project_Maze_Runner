package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

/**
 * Interface defining enemy behavior patterns like fleeing, patrolling, and chasing.
 */
public interface Enemies {
    /**
     * Makes the enemy flee in a random direction.
     */
    void flee();

    /**
     * Makes the enemy patrol around its area.
     * @param delta Time since last update
     */
    void patrol(float delta);

    /**
     * Makes the enemy chase the player.
     * @param deltaTime Time since last update
     * @param playerPosition Player's position
     * @param gameField Game field for navigation
     */
    void chase(float deltaTime, Vector2 playerPosition, GameField gameField);

    /**
     * Updates enemy animation based on movement direction.
     */
    void AnimationDirection();
}
