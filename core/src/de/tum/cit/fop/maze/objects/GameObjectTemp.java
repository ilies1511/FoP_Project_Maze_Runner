package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*
Value | Type
-------------------
0     | Wall
1     | Entry point
2     | Exit
3     | Trap (static obstacle)
4     | Enemy (dynamic obstacle)
5     | Key
*/

/**
 * Temporary/legacy game object class for simple static objects.
 */
public abstract class GameObjectTemp {
    protected Vector2 position;
    protected TextureRegion texture;
    protected Rectangle bounds;

    public GameObjectTemp(float x, float y, TextureRegion texture) {
        this.position = new Vector2(x, y);
        this.texture = texture;
        this.bounds = new Rectangle(x, y, 1.0f, 1.0f);
    }

    /**
     * Draws the object.
     * @param batch Sprite batch for rendering
     */
    public void draw(Batch batch) {
        if (texture != null) {
            batch.draw(texture, position.x, position.y, 1.0f, 1.0f);
        }
    }

    /**
     * Updates the object state.
     * @param delta Time since last update
     */
    public void update(float delta) {
    }

    /**
     * Gets the object's bounds.
     * @return Bounds rectangle
     */
    public Rectangle getBounds() {
        return bounds;
    }
}