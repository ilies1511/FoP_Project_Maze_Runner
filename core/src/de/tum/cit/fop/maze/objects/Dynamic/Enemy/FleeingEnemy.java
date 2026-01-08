package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;

public class FleeingEnemy extends Enemy {
    private float danceTimer = 0f;

    public FleeingEnemy(Vector2 startPos) {
        super(startPos, AnimationType.TOURIST_DOWN);
        this.speed = 2.5f; // faster than normal enemies
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {

        Vector2 separation = new Vector2();
        List<FleeingEnemy> friends = gameField.getObjectsByType(FleeingEnemy.class);
        for (FleeingEnemy other : friends) {
            if (other == this) continue; // skip if urself
            if (position.dst(other.position) < 0.8f) {
                // push away
                Vector2 push = new Vector2(position).sub(other.getPosition()).nor().scl(1.5f);
                separation.add(push);
            }
        }

        float distToPlayer = position.dst(playerPosition);
        if (this.position.dst(playerPosition) < 4.0f && !player.isInvis()) {
            Vector2 runDir = new Vector2(position).sub(playerPosition).add(separation).nor();
            velocity = runDir.scl(speed);
            danceTimer = 0;
            updateAnimationDirection();
        } else {
            //dance logic idle
            velocity.set(0, 0);
            danceTimer += deltaTime;

            if (danceTimer > 0.25f) { // turn every 0.25 seconds
                danceTimer = 0;
                //  random dir
                AnimationType[] idleAnims = {
                        AnimationType.TOURIST_DOWN, AnimationType.TOURIST_LEFT,
                        AnimationType.TOURIST_UP, AnimationType.TOURIST_RIGHT
                };
                changeAnimation(idleAnims[(int)(Math.random() * idleAnims.length)]);}
    }}

    private Vector2 getPosition() {
        return this.position;
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