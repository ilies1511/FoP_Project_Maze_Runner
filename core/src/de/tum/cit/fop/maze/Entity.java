package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public Vector2 position;
    public Vector2 velocity;
    public float speed;
    private float speedMultiplier;
    private Vector2 sizes;

    public Entity(Vector2 position, Vector2 velocity, float speed, Vector2 sizes) {
        this.position = position;
        this.velocity = velocity;
        this.speed = speed;
        this.speedMultiplier = 1f;
        this.sizes = sizes;
    }

    public Vector2 getSize() {
        return this.sizes;
    }

    public float getSpeed() {
        float actualSpeed = this.speed * this.speedMultiplier;
        if (actualSpeed < 0) {
            return 0;
        }
        return actualSpeed;
    }

    public void increaseSpeed(float extraSpeed) {
        this.speed += extraSpeed;
    }

    public void multiplySpeed(float multiplier) {
        this.speedMultiplier *= multiplier;
    }

    private Rectangle getBoundingBox() {
        return new Rectangle(this.position.x, this.position.y, this.sizes.x, this.sizes.y);
    }

    public static Boolean entitiesOverlap(Entity entity1, Entity entity2) {
        return entity1.getBoundingBox().overlaps(entity2.getBoundingBox());
    }

    public Vector2 nextPosition(float deltaTime) {
        return new Vector2(this.position.x + deltaTime * this.velocity.x, this.position.y + deltaTime * this.velocity.y);
    }

    public void move(float deltaTime) {
        this.position =  this.nextPosition(deltaTime);
    }
}
