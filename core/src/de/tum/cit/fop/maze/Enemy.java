package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.objects.GameObject;

import java.util.Vector;

public class Enemy {
    boolean isDead;
    Entity enemyEntity;

    public Enemy(Vector2 startPos){
	    this.isDead = false;
        this.enemyEntity = new Entity(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f)
        );
    }

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField ) {
        float detectionRange = 5.5f; // if within 1.5 tiles
        if (this.enemyEntity.position.dst(playerPosition) < detectionRange) {
            Vector2 direction = new Vector2(playerPosition).sub(this.enemyEntity.position); //chase character
            direction.nor(); // Normalize to length 1
            this.enemyEntity.velocity = direction.scl(this.enemyEntity.getSpeed());
        } else {
            // Stand still
            this.enemyEntity.velocity.set(0, 0);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Entity getEnemyEntity() {
        return enemyEntity;
    }


}