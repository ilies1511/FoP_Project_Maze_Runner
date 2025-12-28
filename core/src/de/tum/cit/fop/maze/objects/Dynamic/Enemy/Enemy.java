package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.CollectableCreator;

public class Enemy extends GameObject {
    boolean isDead;

    public Enemy(Vector2 startPos, Vector2 size, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f),
                size,
                animationType // temporary, might move to each enemy separately
        );
        this.isDead = false;
    }

    public Enemy(Vector2 startPos, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f),
                new Vector2(0.5f, 0.8f),
                animationType // temporary, might move to each enemy separately
        );
        this.isDead = false;
    }

    public Enemy(Vector2 startPos) {
        super(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f),
                new Vector2(0.5f, 0.8f),
                AnimationType.TOURIST_DOWN // temporary, might move to each enemy separately
        );
        this.isDead = false;
    }

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField) {
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

    public Collectable onDeath(Player player) {
        player.receiveXP(15);
        player.addOrReapplyEffect(new Buff(player, 2f));
        Collectable drop = CollectableCreator.createRandomCollectable(this.position);
        return drop;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        //TODO for Saad: implement enemyDie/Hit sounds
    //    SoundController.playEnemyDieSound(1);
        isDead = dead;
    }

    public GameObject getEnemyEntity() {
        return (GameObject) this;
    }


}