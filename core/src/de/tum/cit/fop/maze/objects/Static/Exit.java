package de.tum.cit.fop.maze.objects.Static;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.game_saves.Saveable;
import de.tum.cit.fop.maze.game_saves.mementos.ExitMemento;
import de.tum.cit.fop.maze.objects.GameObject;

/**
 * Exit portal that can be locked or unlocked with keys.
 */
public class Exit extends GameObject implements Saveable<ExitMemento> {
    private boolean isLocked = true;

    public Exit(float x, float y) {
        super(
                new Vector2(x, y),
                new Vector2(0, 0),
                0f,
                new Vector2(1f, 1f), // Hitbox size
                new Vector2(1f, 1f), // Sprite size
                AnimationType.EXIT_CLOSED // Start with CLOSED state
        );
    }

//	@Override
//	public void update(float delta) {
//		super.update(delta);
//		// Exit doesn't move, so no extra logic needed here.
//	}

    /**
     * Opens the exit by unlocking it.
     */
    public void open() {
        if (isLocked) {
            this.isLocked = false;
            this.changeAnimation(AnimationType.EXIT_OPEN);
        }
    }

    /**
     * Checks if exit is locked.
     * @return True if locked
     */
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public ExitMemento getMemento() {
        String animType = this.currentAnimationType != null ? this.currentAnimationType.name() : "EXIT_CLOSED";
        return new ExitMemento(this.position.x, this.position.y, this.isLocked, animType);
    }

    @Override
    public void loadFromMemento(ExitMemento memento) {
        this.position.set(memento.positionX, memento.positionY);
        this.isLocked = memento.isLocked;
    }
}