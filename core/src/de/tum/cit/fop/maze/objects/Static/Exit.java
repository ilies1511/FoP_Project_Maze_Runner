package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.MazeRunnerGame;

public class Exit extends GameObject {
	private boolean isLocked = true;

	public Exit(float x, float y, MazeRunnerGame game) {
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

	public void open() {
		if (isLocked) {
			this.isLocked = false;
			this.changeAnimation(AnimationType.EXIT_OPEN);
		}
	}

	public boolean isLocked() {
		return isLocked;
	}
}