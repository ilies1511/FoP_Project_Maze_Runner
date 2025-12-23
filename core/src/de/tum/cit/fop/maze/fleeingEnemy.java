package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

public class fleeingEnemy extends Enemy {

    public fleeingEnemy(Vector2 startPos) {
        super(startPos);
        this.enemyEntity.speed = 2.5f; // Slightly faster than normal enemies
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
        float detectionRange = 2.0f;

        // if player comes close
        if (this.enemyEntity.position.dst(playerPosition) < detectionRange) {
            float diffX = this.enemyEntity.position.x - playerPosition.x;
            float diffY = this.enemyEntity.position.y - playerPosition.y;

            Vector2 runHoriz = new Vector2(Math.signum(diffX) * this.enemyEntity.speed, 0);
            Vector2 runVert  = new Vector2(0, Math.signum(diffY) * this.enemyEntity.speed);
            boolean canGoHoriz = !willHitWall(runHoriz, deltaTime, gameField);
            boolean canGoVert  = !willHitWall(runVert, deltaTime, gameField);

            if (canGoHoriz && canGoVert) {
                // both open
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    this.enemyEntity.velocity = runHoriz;
                } else {
                    this.enemyEntity.velocity = runVert;
                }
            } else if (canGoHoriz) {
                this.enemyEntity.velocity = runHoriz;
            } else if (canGoVert) {
                this.enemyEntity.velocity = runVert;
            } else {
                this.enemyEntity.velocity.set(0, 0); // panik do nothing
            }
        } else {
            this.enemyEntity.velocity.set(0, 0); // stay idle
        }
    }

    private boolean willHitWall(Vector2 velocity, float delta, GameField gameField) {
        Vector2 nextPos = new Vector2(this.enemyEntity.position).mulAdd(velocity, delta);
        return gameField.isInsideWall(nextPos.x, nextPos.y, this.enemyEntity.getSize());
    }
}