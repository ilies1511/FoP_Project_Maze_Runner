package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

public class FleeingEnemy extends Enemy {

    public FleeingEnemy(Vector2 startPos) {
        super(startPos);
        this.speed = 2.5f; // Slightly faster than normal enemies
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
        float detectionRange = 2.0f;

        // if player comes close
        if (this.position.dst(playerPosition) < detectionRange) {
            float diffX = this.position.x - playerPosition.x;
            float diffY = this.position.y - playerPosition.y;

            Vector2 runHoriz = new Vector2(Math.signum(diffX) * this.speed, 0);
            Vector2 runVert = new Vector2(0, Math.signum(diffY) * this.speed);
            boolean canGoHoriz = !willHitWall(runHoriz, deltaTime, gameField);
            boolean canGoVert = !willHitWall(runVert, deltaTime, gameField);

            if (canGoHoriz && canGoVert) {
                // both open
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
                this.velocity.set(0, 0); // panik do nothing
            }
        } else {
            this.velocity.set(0, 0); // stay idle
        }
    }

    private boolean willHitWall(Vector2 velocity, float delta, GameField gameField) {
        Vector2 nextPos = new Vector2(this.position).mulAdd(velocity, delta);
        return gameField.isInsideWall(nextPos.x, nextPos.y, this.getSize());
    }
}