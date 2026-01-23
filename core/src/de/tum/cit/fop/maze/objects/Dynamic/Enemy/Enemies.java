package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

public interface Enemies {
    // must move randomly and intelligently (see below)
    // throughout the maze, but never leave it. Each enemy type must use a
    // unique behavior (e.g.,Patrol, Attack, Evade, Retreat) depending on the situation.
    void flee();

    void patrol(float delta);

    void chase(float deltaTime, Vector2 playerPosition, GameField gameField);

    void AnimationDirection();
}
