package de.tum.cit.fop.maze.Drawing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.MazeRunnerGame;

import java.util.Map;
import java.util.LinkedHashMap;

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
		Map<AnimationType, Animation<TextureRegion>> tempMap = new LinkedHashMap<>();
		tempMap.put(AnimationType.CHARACTER_UP, game.getCharacterUpAnimation());
		tempMap.put(AnimationType.CHARACTER_DOWN, game.getCharacterDownAnimation());
		tempMap.put(AnimationType.CHARACTER_LEFT, game.getCharacterLeftAnimation());
		tempMap.put(AnimationType.CHARACTER_RIGHT, game.getCharacterRightAnimation());
        tempMap.put(AnimationType.TOURIST_DOWN, game.getTouristDown());
        tempMap.put(AnimationType.TOURIST_UP, game.getTouristUp());
        tempMap.put(AnimationType.TOURIST_LEFT, game.getTouristLeft());
        tempMap.put(AnimationType.TOURIST_RIGHT, game.getTouristRight());
		tempMap.put(AnimationType.CHARACTER_VICTORY_POSE, game.getCharacterDownWin());
		tempMap.put(AnimationType.CHARACTER_LOSER_POSE, game.getCharacterDownSad());
		tempMap.put(AnimationType.KEY_SHINE_ANIMATION, game.getKeyShineAnimation());
		tempMap.put(AnimationType.EXIT_CLOSED, createSingleFrameAnim(game.getExitClosedTexture()));
		tempMap.put(AnimationType.EXIT_OPEN, createSingleFrameAnim(game.getExitOpenTexture()));
		tempMap.put(AnimationType.VAMPIRIC_HEART, game.getVampiricHeartAnimation());
		tempMap.put(AnimationType.GARLIC_TRAP, game.getGarlicTrapAnimation());
		tempMap.put(AnimationType.FREEZE_TRAP, game.getFreezeTrapAnimation());
		tempMap.put(AnimationType.DAWN_THIN_ANIMATION, game.getDawnThinAnimation());
		tempMap.put(AnimationType.DAWN_WIDE_ANIMATION, game.getDawnWideAnimation());
		textureMap = Map.copyOf(tempMap);
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
