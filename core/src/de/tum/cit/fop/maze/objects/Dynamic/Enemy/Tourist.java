package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.TouristMemento;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;

/**
 * Tourist enemy that dances idly and flees when startled by kills.
 */
public class Tourist extends Enemy implements Saveable<TouristMemento> {
    private float danceTimer = 0f;
    private float scareTimer = 0f;
    private final Vector2 fleeDir = new Vector2();


    public Tourist(Vector2 startPos) {
        super(startPos, AnimationType.TOURIST_DOWN);
        this.speed = 2.5f; // faster than normal enemies
    }

    /**
     * Scares all tourists near a death position.
     * @param gameField Game field to find tourists
     * @param deathPosition Position of death
     * @return True if any tourists were scared
     */
    public static boolean scaredTourists(GameField gameField, Vector2 deathPosition) {
        boolean scared = false;
        for (GameObject obj : gameField.getGameObjects()) {
            if (obj instanceof Tourist tourist) {
                if (tourist.getPosition().dst(deathPosition) < 6.0f) { //6 tiles
                    tourist.flee();
                    scared = true;
                }
            }
        }
        return scared;
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {

        Vector2 separation = new Vector2();
        List<Tourist> friends = gameField.getObjectsByType(Tourist.class);
        for (Tourist other : friends) {
            if (other == this) continue; // skip if urself
            if (position.dst(other.position) < 0.8f) {
                // push away
                Vector2 push = new Vector2(position).sub(other.getPosition()).nor().scl(1.5f);
                separation.add(push);
            }
        }

        if (scareTimer > 0) {
            scareTimer -= deltaTime;
            Vector2 finalDir = new Vector2(fleeDir).add(separation).nor();
            velocity.set(finalDir).scl(speed);
            Vector2 nextPos = new Vector2(position).mulAdd(velocity, deltaTime);
            if (WallOrObstacle(nextPos.x, nextPos.y, getSize(), gameField)) {
                flee();
            }
            danceTimer = 0;
            this.AnimationDirection();
        } else {
            //Tourist Dance's in place
            patrol(deltaTime);
        }
    }

    public void AnimationDirection() {
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) {
                changeAnimation(AnimationType.TOURIST_RIGHT);
            } else {
                changeAnimation(AnimationType.TOURIST_LEFT);
            }
        } else {
            if (velocity.y > 0) {
                changeAnimation(AnimationType.TOURIST_UP);
            } else {
                changeAnimation(AnimationType.TOURIST_DOWN);
            }
        }
    }

    private boolean willHitWall(Vector2 velocity, float delta, GameField gameField) {
        Vector2 nextPos = new Vector2(this.position).mulAdd(velocity, delta);
        return WallOrObstacle(nextPos.x, nextPos.y, this.getSize(), gameField);
    }


    @Override
    public void flee() {
        scareTimer = 2.0f; // 2 sec
        float x = MathUtils.randomBoolean() ? 1 : -1;
        float y = MathUtils.randomBoolean() ? 1 : -1;
        fleeDir.set(x, y).nor();
    }

    @Override
    public void patrol(float deltaTime) {
        velocity.set(0, 0);
        danceTimer += deltaTime;
        // Direction Change (0.25s)
        if (danceTimer > 0.25f) {
            danceTimer = 0;
            //  Randomizer
            AnimationType[] idleAnims = {
                    AnimationType.TOURIST_DOWN, AnimationType.TOURIST_LEFT,
                    AnimationType.TOURIST_UP, AnimationType.TOURIST_RIGHT
            };
            changeAnimation(idleAnims[(int) (Math.random() * idleAnims.length)]);
        }
    }

    //Tourist does not chase. Will flee when it sees a kill
    @Override
    public void chase(float deltaTime, Vector2 playerPosition, GameField gameField) {
    }

    @Override
    public TouristMemento getMemento() {
        return new TouristMemento(
                this.position.x,
                this.position.y,
                this.velocity.x,
                this.velocity.y,
                this.speed,
                this.currentAnimationType
        );
    }

    @Override
    public void loadFromMemento(TouristMemento memento) {
        this.position.set(memento.positionX, memento.positionY);
        this.velocity.set(memento.velocityX, memento.velocityY);
        this.speed = memento.speed;
    }
}