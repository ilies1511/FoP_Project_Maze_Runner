package de.tum.cit.fop.maze.objects.Dynamic.Enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Flicker;
import de.tum.cit.fop.maze.effects.GodMode;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;

public class Junkie extends Enemy {
    private final float moveInterval = 2.0f;
    private float moveTimer;
    private boolean isAttacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.5f;
    private static final float ATTACK_COOLDOWN = 1.5f;
    private static final float ATTACK_RANGE = 0.3f; //Dracula range
    private float cooldownTimer = 0f;

    public Junkie(Vector2 startPos) {
        super(startPos, AnimationType.JUNKIE_DOWN);
        this.moveTimer = 0;
        flee();
        this.speed = 0.8f;
    }

    @Override
    public void updateBehavior(float deltaTime, Vector2 playerPosition, GameField gameField, Player player) {
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
        }

        if (isAttacking) {
            attackTimer += deltaTime;
            this.velocity.set(0, 0);
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0;
                flee();
            }
            return;
        }

        float distanceToPlayer = this.position.dst(playerPosition);
        if (distanceToPlayer < ATTACK_RANGE && cooldownTimer <= 0) { //if nearby
            performAttack(player);
            return;
        }

        moveTimer += deltaTime;
        Vector2 nextPos = this.nextPosition(deltaTime);
        boolean willHitWall = gameField.isInsideWall(nextPos.x, nextPos.y, this.getSize());
        if (moveTimer >= moveInterval || willHitWall) {
            flee();
            moveTimer = 0;
        }
        repulsion(gameField, deltaTime);
        AnimationDirection();
    }

    private void performAttack(Player player) {
        isAttacking = true;
        cooldownTimer = ATTACK_COOLDOWN;

        Vector2 toPlayer = new Vector2(player.getPlayerGameObject().position).sub(this.position).nor();
        if (Math.abs(toPlayer.x) > Math.abs(toPlayer.y)) {
            this.velocity.set(Math.signum(toPlayer.x), 0);
        } else {
            this.velocity.set(0, Math.signum(toPlayer.y));
        }
        AnimationDirection();
        if (!player.hasEffect(Flicker.class) && !player.hasEffect(Invis.class) && !player.hasEffect(GodMode.class)) {
            player.getDamageOrHeal(1);
        }
    }

    public void flee() {
        if (MathUtils.randomBoolean()) {
            this.velocity.set(MathUtils.randomSign(), 0);
        } else {
            this.velocity.set(0, MathUtils.randomSign());
        }
        this.velocity.scl(this.getSpeed());
    }


    @Override
    public void patrol(float delta) {
    }

    @Override
    public void chase(float deltaTime, Vector2 playerPosition, GameField gameField) {
    }

    private void repulsion(GameField gameField, float deltaTime) {
        List<Enemy> enemies = gameField.getObjectsByType(Enemy.class);

        float repulsionRadius = 0.9f;
        float pushStrength = 2.0f;

        for (Enemy other : enemies) {
            if (other == this) continue;

            float dist = position.dst(other.getPosition());
            if (dist < repulsionRadius && dist > 0.001f) {
                Vector2 pushDir = new Vector2(position).sub(other.getPosition()).nor();
                velocity.mulAdd(pushDir, pushStrength * deltaTime);
            }
        }
    }

    @Override
    public void AnimationDirection() {
        if (isAttacking) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
                if (velocity.x > 0) changeAnimation(AnimationType.JUNKIE_ATTACK_RIGHT);
                else changeAnimation(AnimationType.JUNKIE_ATTACK_LEFT);
            } else {
                if (velocity.y > 0) changeAnimation(AnimationType.JUNKIE_ATTACK_UP);
                else changeAnimation(AnimationType.JUNKIE_ATTACK_DOWN);
            }
            return;
        }
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x > 0) {
                changeAnimation(AnimationType.JUNKIE_RIGHT);
            } else {
                changeAnimation(AnimationType.JUNKIE_LEFT);
            }
        } else {
            if (velocity.y > 0) {
                changeAnimation(AnimationType.JUNKIE_UP);
            } else {
                changeAnimation(AnimationType.JUNKIE_DOWN);
            }
        }
    }
}