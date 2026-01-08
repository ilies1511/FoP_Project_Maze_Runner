package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.Player;

import java.util.*;

public class Bouncer extends Enemy {

    private static final float MOVE_SPEED = 1.5f;
    private static final float DETECTION_RANGE = 7.0f;
    private float fleeTimer = 0f;
    private Vector2 currentDirection = new Vector2();


    private Vector2 prevPosition;
    private Vector2 fleeVelocity = new Vector2();
    private Random random = new Random();

    public Bouncer(Vector2 startPos) {
        super(startPos, AnimationType.BOUNCER_DOWN);
        this.speed = MOVE_SPEED;}

    public void startFleeing() {
        fleeTimer = 2.0f; // 2 sec buffer
        float dirX = random.nextBoolean() ? 1f : -1f;
        float dirY = random.nextBoolean() ? 1f : -1f;
        fleeVelocity.set(dirX, dirY).nor();
    }
    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        if (fleeTimer > 0) {
            fleeTimer -= deltaTime;
            velocity.set(currentDirection).scl(speed);
            updateAnimationDirection();
            return;
        }

        if (position.dst(playerPosition) > DETECTION_RANGE || player.isInvis()) {
            velocity.set(0, 0);
            return;
        }

        Vector2 targetDir = new Vector2(playerPosition).sub(position).nor();
        velocity.set(targetDir).scl(speed);
        updateAnimationDirection();
    }


    private void updateAnimationDirection() {
        if (velocity.len2() < 0.1f) return;

        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) changeAnimation(AnimationType.BOUNCER_RIGHT);
            else changeAnimation(AnimationType.BOUNCER_LEFT);
        } else {
            if (velocity.y > 0) changeAnimation(AnimationType.BOUNCER_UP);
            else changeAnimation(AnimationType.BOUNCER_DOWN);
        }
    }

}