package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.CollectableCreator;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy extends GameObject {
    protected boolean isDead = false;
    private static TextureRegion shadowTexture;
    private static final float detectionRange = 5.5f; // if within 5.5 tiles

    public static void setShadowTexture(TextureRegion texture) {
        shadowTexture = texture;
    }

    public Enemy(Vector2 startPos, Vector2 size, AnimationType animationType) {
        super(
                startPos,
                new Vector2(0f, 0f),
                2.0f,
                new Vector2(0.8f, 0.8f),
                size,
                animationType // temporary, might move to each enemy separately
        );
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
    }

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        if (isDead) {return;}
        if (position.dst(playerPosition) < detectionRange && !player.isInvis()) {
            Vector2 direction = new Vector2(playerPosition).sub(position).nor(); //chase character
            this.velocity = direction.scl(speed);
        } else {
            velocity.set(0, 0);
        }
    }

    public Collectable onDeath(Player player) {
        player.receiveXP(15);
        player.addOrReapplyEffect(new Buff(player, 2f));
        Collectable drop = CollectableCreator.createRandomCollectable(this.position);
        return drop;
    }

    public void animate(Batch batch) {
        if (shadowTexture != null) {
            float shadowWidth = spriteSizes.x * 1.5f;
            batch.draw(shadowTexture, position.x + (spriteSizes.x - shadowWidth) / 2f, position.y - 0.05f, shadowWidth, shadowWidth);
        super.animate(batch);
    }}

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


    public Vector2 getPosition() {
        return position;
    }
}