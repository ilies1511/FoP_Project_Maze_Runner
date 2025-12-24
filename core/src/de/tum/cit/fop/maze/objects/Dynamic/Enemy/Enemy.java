package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.Parser.GameField;

public class Enemy {
    boolean isDead;
    GameObject enemyGameObject;

    public Enemy(Vector2 startPos){
	    this.isDead = false;
        this.enemyGameObject = new GameObject(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f)
        );
    }

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField ) {
        float detectionRange = 5.5f; // if within 1.5 tiles
        if (this.enemyGameObject.position.dst(playerPosition) < detectionRange) {
            Vector2 direction = new Vector2(playerPosition).sub(this.enemyGameObject.position); //chase character
            direction.nor(); // Normalize to length 1
            this.enemyGameObject.velocity = direction.scl(this.enemyGameObject.getSpeed());
        } else {
            // Stand still
            this.enemyGameObject.velocity.set(0, 0);
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public GameObject getEnemyEntity() {
        return enemyGameObject;
    }


}