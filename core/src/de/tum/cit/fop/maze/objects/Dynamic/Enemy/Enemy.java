package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.CollectableCreator;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy extends GameObject implements Enemies {
    protected boolean isDead = false;
    private float deathTime = 0f; // fade
    private static final float detectionRange = 5.5f; // if within 5.5 tiles
    private static final float fadeDuration = 1.0f;


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
        if (position.dst(playerPosition) < detectionRange && !player.hasEffect(Invis.class)) {
            Vector2 direction = new Vector2(playerPosition).sub(position).nor(); //chase character
            this.velocity = direction.scl(speed);
        } else {
            velocity.set(0, 0);
        }
    }
    public void updateDeathAnimation(float delta) {
        if (isDead) {
            deathTime += delta;
        }
    }

    public boolean shouldRemove() {
        return isDead && deathTime >= fadeDuration;
    }

    protected boolean WallOrObstacle(float x, float y, Vector2 size, GameField gameField) {
        if (!gameField.isInsideWall(x, y, size)) {
            return false;
        }

        int minX = (int) x;
        int maxX = (int) (x + size.x - 0.001f);
        int minY = (int) y;
        int maxY = (int) (y + size.y - 0.001f);

        for (int row = minY; row <= maxY; row++) {
            for (int col = minX; col <= maxX; col++) {
                TileType tile = gameField.getTile(col, row);
                if (tile != TileType.EMPTY) {
                    return true;
                }
            }
        }
        // if DAWN
        return false;
    }

    public Collectable onDeath(Player player) {
        player.receiveXP(20);
        player.addOrReapplyEffect(new Buff(player, 2f));
        Collectable drop = CollectableCreator.createRandomCollectable(this.position);
        player.setKills(player.getKills()+1);
        if(player.getKills() == 1){
            player.getAchievementUI().firstBloodAchievement();
        }
        if(player.getKills() == 50){
            player.getAchievementUI().killsAchievement();
        }

        return drop;
    }

    public void animate(Batch batch) {
        if (isDead) {
            // Move and Transparent on each kill
            float alpha = 1.0f - (deathTime / fadeDuration);
            if (alpha < 0) {
                alpha = 0;
            }
            float floatOffset = (deathTime / fadeDuration) * 0.5f;
            float oldAlpha = batch.getColor().a;
            //transparent
            batch.setColor(1f, 1f, 1f, alpha);
            // offset
            float originalY = position.y;
            position.y += floatOffset;
            super.animate(batch);
            position.y = originalY;
            // back to normal
            batch.setColor(1f, 1f, 1f, oldAlpha);
        } else {
            super.animate(batch);}
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


    public Vector2 getPosition() {
        return position;
    }
}