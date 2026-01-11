package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.Player;

import java.util.*;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bouncer extends Enemy {

    private int lives = 2;
    private static final float MOVE_SPEED = 1.5f;
    private static final float DETECTION_RANGE = 7.0f;
    private float fleeTimer = 0f;
    private float flickerTimer = 0f;
    private Vector2 currentDirection = new Vector2();
    private boolean isFlickering = false;

    public Bouncer(Vector2 startPos) {
        super(startPos, AnimationType.BOUNCER_DOWN);
        this.speed = MOVE_SPEED;}



    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        //TODO: Redirect when engaging with tourist
        if (flickerTimer > 0) {
            flickerTimer -= deltaTime;
            if (flickerTimer <= 0) {
                isFlickering = false;
            }
        }
        if (fleeTimer > 0) {
            fleeTimer -= deltaTime;
            if (isBlocked(currentDirection, deltaTime, gameField)) {
                if (Math.abs(currentDirection.x) > 0 && !isBlocked(new Vector2(Math.signum(currentDirection.x), 0), deltaTime, gameField)) {
                    currentDirection.set(Math.signum(currentDirection.x), 0);
                }
                else if (Math.abs(currentDirection.y) > 0 && !isBlocked(new Vector2(0, Math.signum(currentDirection.y)), deltaTime, gameField)) {
                    currentDirection.set(0, Math.signum(currentDirection.y));
                }
                else {
                    velocity.set(0, 0);
                    return;
                }
            }
            velocity.set(currentDirection).scl(speed);
            AnimationDirection();
            return;
        }
        if (position.dst(playerPosition) > DETECTION_RANGE || player.hasEffect(Invis.class)) {
            velocity.set(0, 0);
            return;
        }

        chase(deltaTime, playerPosition, gameField);
        AnimationDirection();
    }

    private boolean isBlocked(Vector2 dir, float delta, GameField gameField) {
        float lookAhead = speed * delta * 5; //5
        return gameField.isInsideWall(position.x + (dir.x * lookAhead), position.y + (dir.y * lookAhead), getSize());
    }


    public void animate(Batch batch) {
        if (isFlickering) {
            if (MathUtils.sin(flickerTimer * 30) > 0) {
                super.animate(batch);
            }
        } else {
            super.animate(batch);
        }
    }

    public void AnimationDirection() {
        if (velocity.len2() < 0.1f) return;

        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) changeAnimation(AnimationType.BOUNCER_RIGHT);
            else changeAnimation(AnimationType.BOUNCER_LEFT);
        } else {
            if (velocity.y > 0) changeAnimation(AnimationType.BOUNCER_UP);
            else changeAnimation(AnimationType.BOUNCER_DOWN);
        }
    }

    public boolean takeDamage(Vector2 playerPos) {
        lives--;
        if (lives <= 0) {
            return true;
        }
        currentDirection.set(position).sub(playerPos).nor();
        fleeTimer = 2.0f;
        isFlickering = true;
        flickerTimer = 2.0f;
        return false; //survive
    }

    @Override
    public void flee() {
        if (fleeTimer > 0) return;
        fleeTimer = 2.0f; // 2 sec buffer
        float dirX = MathUtils.randomBoolean() ? 1f : -1f;
        float dirY = MathUtils.randomBoolean() ? 1f : -1f;
        currentDirection.set(dirX, dirY).nor();
    }

    public void patrol(float delta) {
        //Bouncer will not patrol, only chase wherevr the drac is on the map
    }

    public void chase(float deltaTime,  Vector2 playerPosition,  GameField gameField) {
        //chase until hidden behind a wall
        Vector2 targetDir = new Vector2(playerPosition).sub(position).nor();
        if (!isBlocked(targetDir, deltaTime, gameField)) {
            velocity.set(targetDir).scl(speed);
        }
        else if (!isBlocked(new Vector2(Math.signum(targetDir.x), 0), deltaTime, gameField)) {
            velocity.set(Math.signum(targetDir.x) * speed, 0);
        }
        else if (!isBlocked(new Vector2(0, Math.signum(targetDir.y)), deltaTime, gameField)) {
            velocity.set(0, Math.signum(targetDir.y) * speed);
        }
        else {
            velocity.set(0, 0);
        }
    }

}