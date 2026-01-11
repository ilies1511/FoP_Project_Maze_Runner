package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.Player;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Bouncer extends Enemy {
//to loose bouncer you either run far away or hide behind a wall
    private int lives = 2;
    private static final float DETECTION_RANGE = 7.0f;
    private float fleeTimer = 0f;
    private float flickerTimer = 0f;
    private float Cooldown = 0f;
    private Vector2 currentDirection = new Vector2();
    private boolean isFlickering = false;

    public Bouncer(Vector2 startPos) {
        super(startPos, AnimationType.BOUNCER_DOWN);
        this.speed = 1.5f;}

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
            moveInCurrDirection(deltaTime, gameField);
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
        if (velocity.len2() < 0.5f) return;

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
        Vector2 awayFromPlayer = new Vector2(position).sub(playerPos).nor();
        finDirection(awayFromPlayer); // Helper to snap to valid grid direction
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
        Cooldown -= deltaTime;
        if (Cooldown <= 0 || isBlocked(currentDirection, deltaTime, gameField)) {
            Vector2 toPlayer = new Vector2(playerPosition).sub(position);
            Vector2[] directions = { // no diagonals
                    new Vector2(1, 0), new Vector2(-1, 0),
                    new Vector2(0, 1), new Vector2(0, -1)
            };

            Vector2 next = null;
            float maximum = -Float.MAX_VALUE;
            for (Vector2 curr : directions) {
                if (!isBlocked(curr, deltaTime, gameField)) {
                    float toDrac = toPlayer.dot(curr);
                    if (toDrac > maximum) {
                        maximum = toDrac;
                        next = curr;
                    }
                }
            }

            if (next != null && maximum > 0.2f) {
                currentDirection.set(next);
                Cooldown = 0.2f;
            } else {
                currentDirection.set(0,0);
            }
        }

        moveInCurrDirection(deltaTime, gameField);
    }

    private void moveInCurrDirection (float delta, GameField gameField) {
        if (currentDirection.len2() > 0 && !isBlocked(currentDirection, delta, gameField)) {
            velocity.set(currentDirection).scl(speed);
        } else {
            velocity.set(0, 0);
            Cooldown = 0;
        }
    }

    private void finDirection (Vector2 awayFromPlayer) {
        if (Math.abs(awayFromPlayer.x) > Math.abs(awayFromPlayer.y)) {
            currentDirection.set(Math.signum(awayFromPlayer.x), 0);
        } else {
            currentDirection.set(0, Math.signum(awayFromPlayer.y));
        }
        Cooldown = 0.5f;
    }

}