package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
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

/**
 * Base enemy class with AI behavior, death handling, and collision detection.
 */
public abstract class Enemy extends GameObject implements Enemies {
    private static final float DETECTION_RANGE = 5.5f; // if within 5.5 tiles
    private static final float FADE_DURATION = 1.0f;
    protected boolean isDead = false;
    private float deathTime = 0f; // fade


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

    /**
     * Updates enemy behavior including chasing and idle states.
     * @param deltaTime Time since last update
     * @param playerPosition Player's current position
     * @param gameField Game field for navigation
     * @param player Player reference
     */
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        if (isDead) {
            return;
        }
        if (position.dst(playerPosition) < DETECTION_RANGE && !player.hasEffect(Invis.class)) {
            Vector2 direction = new Vector2(playerPosition).sub(position).nor(); //chase character
            this.velocity = direction.scl(speed);
        } else {
            velocity.set(0, 0);
        }
    }

    /**
     * Updates death animation fade timer.
     * @param delta Time since last update
     */
    public void updateDeathAnimation(float delta) {
        if (isDead) {
            deathTime += delta;
        }
    }

    /**
     * Checks if enemy should be removed after death.
     * @return True if fade animation is complete
     */
    public boolean shouldRemove() {
        return isDead && deathTime >= FADE_DURATION;
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

    /**
     * Handles enemy death rewards and collectables.
     * @param player The player who killed the enemy
     * @return Collectable drop
     */
    public Collectable onDeath(Player player) {
        player.receiveXP(20);
        player.addOrReapplyEffect(new Buff(player, 2f));
        Collectable drop = CollectableCreator.createRandomCollectable(this.position);
        player.setKills(player.getKills() + 1);
        if (player.getKills() == 1) {
            player.getAchievementUI().firstBloodAchievement();
        }
        if (player.getKills() == 50) {
            player.getAchievementUI().killsAchievement();
        }

        return drop;
    }

    public void animate(Batch batch) {
        if (isDead) {
            // Move and Transparent on each kill
            float alpha = 1.0f - (deathTime / FADE_DURATION);
            if (alpha < 0) {
                alpha = 0;
            }
            float floatOffset = (deathTime / FADE_DURATION) * 0.5f;
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
            super.animate(batch);
        }
    }


    /**
     * Checks if enemy is dead.
     * @return True if dead
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Sets enemy death state.
     * @param dead New death state
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Gets enemy as GameObject.
     * @return This enemy instance
     */
    public GameObject getEnemyEntity() {
        return this;
    }


    /**
     * Gets enemy position.
     * @return Position vector
     */
    public Vector2 getPosition() {
        return position;
    }
}