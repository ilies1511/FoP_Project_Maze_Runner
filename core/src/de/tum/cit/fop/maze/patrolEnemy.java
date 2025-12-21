package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class patrolEnemy extends Enemy {
    private float moveTimer;
    private final float moveInterval = 2.0f; // Direction CHange

    public patrolEnemy(Vector2 startPos) {
        super(startPos);
        this.moveTimer = 0;
        pickRandomDirection();
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
        moveTimer += deltaTime;

        Vector2 nextPos = this.enemyEntity.nextPosition(deltaTime);

        // Check if next position is inside a wall and do change dir if it is
        boolean willHitWall = gameField.isInsideWall(nextPos.x, nextPos.y, this.enemyEntity.getSize());

        // Change direction when timer is up or about ot hit wall
        if (moveTimer >= moveInterval || willHitWall) {
            pickRandomDirection();
            moveTimer = 0;
        }
    }
    private void pickRandomDirection() {
        if (MathUtils.randomBoolean()) {
            // Left or Right
            this.enemyEntity.velocity.set(MathUtils.randomSign(), 0);
        } else {
            // Up or Down
            this.enemyEntity.velocity.set(0, MathUtils.randomSign());
        }
        this.enemyEntity.velocity.scl(this.enemyEntity.getSpeed());
    }


}