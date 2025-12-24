package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.Parser.GameField;

public class Enemy extends GameObject {
    boolean isDead;

    public Enemy(Vector2 startPos){
        super(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f),
                new Vector2(0.5f,   0.8f),
                AnimationType.CHARACTER_DOWN // temporary, might move to each enemy separately
        );
	    this.isDead = false;
    }

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField ) {
        float detectionRange = 5.5f; // if within 1.5 tiles
        if (this.position.dst(playerPosition) < detectionRange) {
            Vector2 direction = new Vector2(playerPosition).sub(this.position); //chase character
            direction.nor(); // Normalize to length 1
            this.velocity = direction.scl(this.getSpeed());
        } else {
            // Stand still
            this.velocity.set(0, 0);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public GameObject getEnemyEntity() {
        return (GameObject) this;
    }


}