package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;

public class Junkie extends Enemy {
    private final float moveInterval = 2.0f; // Direction CHange
    private float moveTimer;

    public Junkie(Vector2 startPos) {
        super(startPos, AnimationType.JUNKIE_DOWN);
        this.moveTimer = 0;
        pickRandomDirection();
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        moveTimer += deltaTime;

        Vector2 nextPos = this.nextPosition(deltaTime);
        boolean willHitWall = gameField.isInsideWall(nextPos.x, nextPos.y, this.getSize());
        if (moveTimer >= moveInterval || willHitWall) {
            pickRandomDirection();
            moveTimer = 0;
        }
        repulsion(gameField, deltaTime);
        AnimationDirection();
    }

    private void pickRandomDirection() {
        if (MathUtils.randomBoolean()) {
            this.velocity.set(MathUtils.randomSign(), 0);
        } else {
            this.velocity.set(0, MathUtils.randomSign());
        }
        this.velocity.scl(this.getSpeed());
    }


    @Override
    public void flee() {

    }

    @Override
    public void patrol(float delta) {

    }

    @Override
    public void chase(float deltaTime, Vector2 playerPosition, GameField gameField) {

    }

    private void repulsion(GameField gameField, float deltaTime) {
        List<Enemy> enemies = gameField.getObjectsByType(Enemy.class);

        float repulsionRadius = 0.9f;
        float pushStrength = 2.0f;

        for (Enemy other : enemies) {
            if (other == this) continue;

            float dist = position.dst(other.getPosition());
            if (dist < repulsionRadius && dist > 0.001f) {
                Vector2 pushDir = new Vector2(position).sub(other.getPosition()).nor();
                velocity.mulAdd(pushDir, pushStrength * deltaTime);
            }
        }
    }

    @Override
    public void AnimationDirection() {
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) {
                changeAnimation(AnimationType.JUNKIE_RIGHT);
            } else {
                changeAnimation(AnimationType.JUNKIE_LEFT);
            }
        } else {
            if (velocity.y > 0) {
                changeAnimation(AnimationType.JUNKIE_UP);
            } else {
                changeAnimation(AnimationType.JUNKIE_DOWN);
            }
        }
    }
}