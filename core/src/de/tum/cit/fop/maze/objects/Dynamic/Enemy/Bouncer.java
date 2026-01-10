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

public class Bouncer extends Enemy {

    private static final float MOVE_SPEED = 1.5f;
    private static final float DETECTION_RANGE = 7.0f;
    private float fleeTimer = 0f;
    private Vector2 currentDirection = new Vector2();

    public Bouncer(Vector2 startPos) {
        super(startPos, AnimationType.BOUNCER_DOWN);
        this.speed = MOVE_SPEED;}



    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        //TODO: Redirect when enaging with tourist
        if (fleeTimer > 0) {
            fleeTimer -= deltaTime;
            velocity.set(currentDirection).scl(speed);
            if (isBlocked(currentDirection, deltaTime, gameField)) {
                flee();
                fleeTimer = 0;
            }
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