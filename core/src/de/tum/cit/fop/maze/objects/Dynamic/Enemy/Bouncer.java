package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;

public class Bouncer extends Enemy {
    private boolean isCharging = false;
    private float cooldown = 0f; // rest

    public Bouncer(Vector2 startPos) {
        super(startPos);
        this.speed = 1.0f; // slow
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
        // move until wall
        if (isCharging) {
            Vector2 nextPos = this.nextPosition(deltaTime);

            // wall
            if (gameField.isInsideWall(nextPos.x, nextPos.y, this.getSize())) {
                isCharging = false;
                this.speed = 1.0f;
                this.cooldown = 2.0f; // pause
                this.velocity.set(0, 0);
            }
            return;
        }

        // cooldown
        if (cooldown > 0) {
            cooldown -= deltaTime;
            this.velocity.set(0, 0);
            return;
        }

        // if in front
        float diffX = Math.abs(this.position.x - playerPosition.x);
        float diffY = Math.abs(this.position.y - playerPosition.y);
        float range = 7.0f; //detection range

        // same column
        if (diffX < 0.5f && diffY < range) {
            startCharge(new Vector2(0, Math.signum(playerPosition.y - this.position.y)));
        }
        // same row
        else if (diffY < 0.5f && diffX < range) {
            startCharge(new Vector2(Math.signum(playerPosition.x - this.position.x), 0));
        } else {
            // attentive when char far away
            if (Math.random() < 0.02) {
                // Random nudges
                this.velocity.set((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).nor();
            }
        }
    }

    private void startCharge(Vector2 direction) {
        this.isCharging = true;
        this.speed = 6.0f; //run
        this.velocity = direction.scl(this.speed);
    }
}