package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;

public class FleeingEnemy extends Enemy {

    private float danceTimer = 0f;
    private int danceStep = 0;

    public FleeingEnemy(Vector2 startPos) {
        super(startPos, AnimationType.TOURIST_DOWN);
        this.speed = 2.5f; // faster than normal enemies
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        float detectionRange = 4.0f; // range

        Vector2 separation = new Vector2(0, 0);
        List<FleeingEnemy> friends = gameField.getObjectsByType(FleeingEnemy.class);

        for (FleeingEnemy other : friends) {
            if (other == this) continue; // skip if urself

            float dist = this.position.dst(other.position);

            if (dist < 0.8f) { // if felow is in 0.8 tiles
                // push away
                Vector2 push = new Vector2(this.position).sub(other.position);
                push.nor().scl(1.5f); // repel strength
                separation.add(push);
            }
        }

        if (this.position.dst(playerPosition) < detectionRange && !player.isInvis()) {

            float diffX = (this.position.x - playerPosition.x) + separation.x;
            float diffY = (this.position.y - playerPosition.y) + separation.y;

            Vector2 runHoriz = new Vector2(Math.signum(diffX) * this.speed, 0);
            Vector2 runVert = new Vector2(0, Math.signum(diffY) * this.speed);

            boolean canGoHoriz = !willHitWall(runHoriz, deltaTime, gameField);
            boolean canGoVert = !willHitWall(runVert, deltaTime, gameField);

            if (canGoHoriz && canGoVert) {
                // main threat
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    this.velocity = runHoriz;
                } else {
                    this.velocity = runVert;
                }
            } else if (canGoHoriz) {
                this.velocity = runHoriz;
            } else if (canGoVert) {
                this.velocity = runVert;
            } else {
                this.velocity.set(0, 0); // Cornered!
            }

            // dancetime is now 0
            danceTimer = 0;

        } else {
            //dance logic
            this.velocity.set(0, 0);

            danceTimer += deltaTime;
            if (danceTimer > 0.25f) { // turn every 0.25 seconds
                danceTimer = 0;
                danceStep = (int)(Math.random() *4); // 0 to 4
                //danceStep = (danceStep + 1) % 4; // alt approach

                switch (danceStep) {
                    case 0: changeAnimation(AnimationType.TOURIST_DOWN); break;
                    case 1: changeAnimation(AnimationType.TOURIST_LEFT); break;
                    case 2: changeAnimation(AnimationType.TOURIST_UP); break;
                    case 3: changeAnimation(AnimationType.TOURIST_RIGHT); break;
                }
            }
        }

        // dont dance when moving
        if (this.velocity.len2() > 0.1f) {
            updateAnimationDirection();
        }
    }

    private void updateAnimationDirection() {
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) {
                changeAnimation(AnimationType.TOURIST_RIGHT);
            } else {
                changeAnimation(AnimationType.TOURIST_LEFT);
            }
        } else {
            if (velocity.y > 0) {
                changeAnimation(AnimationType.TOURIST_UP);
            } else {
                changeAnimation(AnimationType.TOURIST_DOWN);
            }
        }
    }

    private boolean willHitWall(Vector2 velocity, float delta, GameField gameField) {
        Vector2 nextPos = new Vector2(this.position).mulAdd(velocity, delta);
        return gameField.isInsideWall(nextPos.x, nextPos.y, this.getSize());
    }
}