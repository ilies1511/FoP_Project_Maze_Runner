package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

public class PatrolEnemy extends Enemy {
    private float moveTimer;
    private final float moveInterval = 2.0f; // Direction CHange

    public PatrolEnemy(Vector2 startPos) {
        super(startPos);
        this.moveTimer = 0;
        pickRandomDirection();
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
        moveTimer += deltaTime;

        Vector2 nextPos = this.enemyGameObject.nextPosition(deltaTime);

        // Check if next position is inside a wall and do change dir if it is
        boolean willHitWall = gameField.isInsideWall(nextPos.x, nextPos.y, this.enemyGameObject.getSize());

        // Change direction when timer is up or about ot hit wall
        if (moveTimer >= moveInterval || willHitWall) {
            pickRandomDirection();
            moveTimer = 0;
        }
    }
    private void pickRandomDirection() {
        if (MathUtils.randomBoolean()) {
            // Left or Right
            this.enemyGameObject.velocity.set(MathUtils.randomSign(), 0);
        } else {
            // Up or Down
            this.enemyGameObject.velocity.set(0, MathUtils.randomSign());
        }
        this.enemyGameObject.velocity.scl(this.enemyGameObject.getSpeed());
    }


}