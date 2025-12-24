package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public Vector2 position;
    public Vector2 velocity;
    public float speed;
    private float speedMultiplier;
    private Vector2 sizes;
    private Vector2 spriteSizes;
    protected TextureRegion texture;

    public GameObject(Vector2 position, Vector2 velocity, float speed, Vector2 sizes, Vector2 spriteSizes) {
        this.position = position;
        this.velocity = velocity;
        this.speed = speed;
        this.speedMultiplier = 1f;
        this.sizes = sizes;
        this.spriteSizes = spriteSizes;
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

    public static Boolean entitiesOverlap(GameObject gameObject1, GameObject gameObject2) {
        return gameObject1.getBoundingBox().overlaps(gameObject2.getBoundingBox());
    }

    public Vector2 nextPosition(float deltaTime) {
        return new Vector2(this.position.x + deltaTime * this.velocity.x, this.position.y + deltaTime * this.velocity.y);
    }

    public void move(float deltaTime) {
        this.position =  this.nextPosition(deltaTime);
    }

    public void draw(Batch batch) {
        if (texture != null) {
            batch.draw(texture, position.x, position.y, spriteSizes.x, spriteSizes.y);
        }
    }

    public void update(float delta) {
        //
    }

}
