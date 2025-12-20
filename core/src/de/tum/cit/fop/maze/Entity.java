package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    public Vector2 position;
    public Vector2 velocity;
    public float speed;
    private Vector2 sizes;

    public Entity(Vector2 position, Vector2 velocity, float speed, Vector2 sizes) {
        this.position = position;
        this.velocity = velocity;
        this.speed = speed;
        this.sizes = sizes;
    }

    private Rectangle getBoundingBox() {
        return new Rectangle(this.position.x, this.position.y, this.sizes.x, this.sizes.y);
    }

    public static Boolean entitiesOverlap(Entity entity1, Entity entity2) {
        return entity1.getBoundingBox().overlaps(entity2.getBoundingBox());
    }
}
