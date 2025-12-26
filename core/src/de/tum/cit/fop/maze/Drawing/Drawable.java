package de.tum.cit.fop.maze.Drawing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.MazeRunnerGame;

import java.util.Map;

/*
Animations:
- get via HashMap
- pass
- Should be instantiated in each object (animations are separate)
- Each Object has current animation time
- draw (delta) -> just moves animation by delta
- changeAnimation -> sets animation time to zero, switches animation to other one
- factory pattern for creating animations
 */

public class Drawable {
	private static Map<AnimationType, Animation<TextureRegion>> textureMap;

	private float animationTime;
	private Animation<TextureRegion> currentAnimation;

	public Drawable(AnimationType animationType) {
		assert textureMap != null; // should initialize before creating any drawable
		this.currentAnimation = textureMap.get(animationType);
		this.animationTime = 0;
	}

	public static void initializeTextureMap(MazeRunnerGame game) {
		textureMap = Map.of(
				AnimationType.CHARACTER_UP, game.getCharacterUpAnimation(),
				AnimationType.CHARACTER_DOWN, game.getCharacterDownAnimation(),
				AnimationType.CHARACTER_LEFT, game.getCharacterLeftAnimation(),
				AnimationType.CHARACTER_RIGHT, game.getCharacterRightAnimation(),
				AnimationType.CHARACTER_VICTORY_POSE, game.getCharacterDownWin(),
				AnimationType.CHARACTER_LOSER_POSE, game.getCharacterDownSad(),
				AnimationType.KEY_SHINE_ANIMATION, game.getKeyShineAnimation(),
				AnimationType.EXIT_CLOSED, createSingleFrameAnim(game.getExitClosedTexture()),
				AnimationType.EXIT_OPEN, createSingleFrameAnim(game.getExitOpenTexture())
		);
	}

	private static Animation<TextureRegion> createSingleFrameAnim(TextureRegion region) {
		Array<TextureRegion> frames = new Array<>();
		frames.add(region);
		return new Animation<>(1f, frames); // Duration doesn't matter for 1 frame
	}

	public void update(float delta) {
		this.animationTime += delta;
	}

	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}

	public void changeAnimation(AnimationType animationType) {
		this.currentAnimation = textureMap.get(animationType);
	}

	public void animate(Batch batch, Vector2 position, Vector2 spriteSizes) {
		batch.draw(this.currentAnimation.getKeyFrame(this.animationTime, true), position.x, position.y, spriteSizes.x, spriteSizes.y);
	}
}
