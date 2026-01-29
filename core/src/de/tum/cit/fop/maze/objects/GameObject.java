package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Drawing.Drawable;

/**
 * Base class for all game objects with position, movement, and animation.
 */
public abstract class GameObject {
    // BEGIN -- Attributes
    public Vector2 position;
    public Vector2 velocity;
    public float speed;
    protected TextureRegion texture;
    protected float speedMultiplier;
    protected Vector2 sizes;
    protected Vector2 spriteSizes;
    protected Drawable drawable;
    protected Rectangle bounds;
    protected AnimationType currentAnimationType;
    // END -- Attributes


    // BEGIN -- Constructor
    public GameObject(Vector2 position, Vector2 velocity, float speed, Vector2 sizes, Vector2 spriteSizes, AnimationType initialAnimationType) {
        this.position = position;
        this.velocity = velocity;
        this.speed = speed;
        this.speedMultiplier = 1f;
        this.sizes = sizes;
        this.spriteSizes = spriteSizes;
        this.drawable = new Drawable(initialAnimationType);
        this.bounds = new Rectangle(position.x, position.y, sizes.x, sizes.y);
    }

    
    public GameObject(float x, float y, TextureRegion texture) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0); // No movement
        this.speed = 0;

        this.sizes = new Vector2(1f, 1f);
        this.spriteSizes = new Vector2(1f, 1f);

        this.texture = texture;
        this.drawable = null;
        this.bounds = new Rectangle(x, y, 1f, 1f);
    }
    // END -- Constructor

    // BEGIN -- Methods
    /**
     * Checks if two game objects overlap.
     * @param gameObject1 First object
     * @param gameObject2 Second object
     * @return True if objects overlap
     */
    public static Boolean entitiesOverlap(GameObject gameObject1, GameObject gameObject2) {
        return gameObject1.getBounds().overlaps(gameObject2.getBounds());
    }

    // BEGIN -- Getters
    /**
     * Gets the object's hitbox size.
     * @return Size vector
     */
    public Vector2 getSize() {
        return this.sizes;
    }

    /**
     * Gets the object's current speed including modifiers.
     * @return Effective speed value
     */
    public float getSpeed() {
        float actualSpeed = this.speed * this.speedMultiplier;
        if (actualSpeed < 0) {
            return 0;
        }
        return actualSpeed;
    }
    // END -- Getters

    /**
     * Gets the object's collision bounds.
     * @return Updated bounds rectangle
     */
    public Rectangle getBounds() {
        bounds.setPosition(position.x, position.y);
        return bounds;
    }
    // END -- Setters

    // BEGIN -- Setters
    /**
     * Sets the animation time for the drawable.
     * @param time Animation time value
     */
    public void setAnimationTime(float time) {
        if (drawable != null) {
            drawable.setAnimationTime(time);
        }
    }

    /**
     * Increases the object's speed by a value.
     * @param extraSpeed Speed increment
     */
    public void increaseSpeed(float extraSpeed) {
        this.speed += extraSpeed;
    }

    /**
     * Multiplies the speed multiplier.
     * @param multiplier Multiplier value
     */
    public void multiplySpeed(float multiplier) {
        this.speedMultiplier *= multiplier;
    }

    /**
     * Calculates next position based on current velocity.
     * @param deltaTime Time delta for movement
     * @return Predicted position
     */
    public Vector2 nextPosition(float deltaTime) {
        return new Vector2(this.position.x + deltaTime * this.velocity.x, this.position.y + deltaTime * this.velocity.y);
    }

    /**
     * Moves the object by its velocity over time.
     * @param deltaTime Time delta for movement
     */
    public void move(float deltaTime) {
        this.position = this.nextPosition(deltaTime);
    }

    // ANIMATIONS //
    /**
     * Updates animation state and bounds.
     * @param delta Time since last update
     */
    public void updateAnimation(float delta) {
        bounds.setPosition(position.x, position.y);
        if (drawable != null) {
            drawable.update(delta);
        }
    }

    /**
     * Changes the current animation.
     * @param animationType New animation type
     */
    public void changeAnimation(AnimationType animationType) {
        if (drawable != null) {
            drawable.changeAnimation(animationType);
        }
    }

    // We can call it draw --> more intuative to use in GameScreen
	/**
     * Renders the object using its drawable or texture.
     * @param batch Sprite batch for rendering
     */
    public void animate(Batch batch) {
        if (drawable != null) {
            drawable.animate(batch, this.position, this.spriteSizes);
        } else if (texture != null) {
            batch.draw(texture, position.x, position.y, spriteSizes.x, spriteSizes.y);
        }
    }

    /**
     * Gets the drawable component.
     * @return Drawable instance
     */
    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * Gets the object's velocity vector.
     * @return Velocity vector
     */
    public Vector2 getVelocity() {
        return velocity;
    }

	public Vector2 getPosition() {
		return position;
	}
	// END -- Methods
}
