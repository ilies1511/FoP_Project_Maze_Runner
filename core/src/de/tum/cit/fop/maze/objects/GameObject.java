package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Drawing.Drawable;

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
    //	public static Boolean entitiesOverlap(GameObject gameObject1, GameObject gameObject2) {
    //		return gameObject1.getBoundingBox().overlaps(gameObject2.getBoundingBox());
    //	}
    public static Boolean entitiesOverlap(GameObject gameObject1, GameObject gameObject2) {
//		return gameObject1.getBoundingBox().overlaps(gameObject2.getBoundingBox());
        return gameObject1.getBounds().overlaps(gameObject2.getBounds());
    }

    // BEGIN -- Getters
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
    // END -- Getters

    //	private Rectangle getBoundingBox() {
//		return new Rectangle(this.position.x, this.position.y, this.sizes.x, this.sizes.y);
//	}
    public Rectangle getBounds() {
        bounds.setPosition(position.x, position.y);
        return bounds;
    }
    // END -- Setters

    // BEGIN -- Setters
    public void setAnimationTime(float time) {
        if (drawable != null) {
            drawable.setAnimationTime(time);
        }
    }

    public void increaseSpeed(float extraSpeed) {
        this.speed += extraSpeed;
    }

    public void multiplySpeed(float multiplier) {
        this.speedMultiplier *= multiplier;
    }

    public Vector2 nextPosition(float deltaTime) {
        return new Vector2(this.position.x + deltaTime * this.velocity.x, this.position.y + deltaTime * this.velocity.y);
    }

    public void move(float deltaTime) {
        this.position = this.nextPosition(deltaTime);
    }

    // ANIMATIONS //
//	public void updateAnimation(float delta) {
//		this.drawable.update(delta);
//	}
    
    public void updateAnimation(float delta) {
//		if (velocity.x != 0 || velocity.y != 0) {
//			position.mulAdd(velocity, delta);
//		}
        bounds.setPosition(position.x, position.y);
        if (drawable != null) {
            drawable.update(delta);
        }
    }

//	public void setAnimationTime(float animationTime) {
//		this.drawable.setAnimationTime(animationTime);
//	}

    //	public void changeAnimation(AnimationType animationType) {
//		this.drawable.changeAnimation(animationType);
//	}
    public void changeAnimation(AnimationType animationType) {
        if (drawable != null) {
            drawable.changeAnimation(animationType);
        }
    }

    // We can call it draw --> more intuative to use in GameScreen
    public void animate(Batch batch) {
        if (drawable != null) {
            drawable.animate(batch, this.position, this.spriteSizes);
        } else if (texture != null) {
            batch.draw(texture, position.x, position.y, spriteSizes.x, spriteSizes.y);
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
    
    // New animate
//	public void draw(Batch batch) {
//		if (drawable != null) {
//			drawable.animate(batch, this.position, this.spriteSizes);
//		}
//		else if (texture != null) {
//			batch.draw(texture, position.x, position.y, spriteSizes.x, spriteSizes.y);
//		}
//	}
    // END -- Methods
}
