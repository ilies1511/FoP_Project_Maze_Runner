package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.CollectableCreator;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends GameObject {
    boolean isDead;
    private static TextureRegion shadowTexture;

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

    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        float detectionRange = 5.5f; // if within 1.5 tiles
        if (this.position.dst(playerPosition) < detectionRange && !player.hasEffect(Invis.class)) {
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
        player.setKills(player.getKills()+1);
        if(player.getKills() == 1){
            player.getAchievementController().firstBloodAchievement();
        }
        if(player.getKills() == 50){
            player.getAchievementController().killsAchievement();
        }

        return drop;
    }

    public void animate(Batch batch) {
        if (shadowTexture != null) {
            float width = this.spriteSizes.x;
            float height = this.spriteSizes.y;
            float shadowWidth = width * 1.5f;
            float shadowHeight = shadowWidth;

            float x = position.x + (width - shadowWidth) / 2f;
            float y = position.y - 0.05f; // near feet
            batch.draw(shadowTexture, x, y, shadowWidth, shadowHeight);        }

        super.animate(batch);
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