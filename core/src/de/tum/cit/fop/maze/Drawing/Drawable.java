package de.tum.cit.fop.maze.Drawing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.MazeRunnerGame;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles drawing and animation management for game objects.
 * It maintains the state of the current animation and renders it to the batch.
 */
public class Drawable {
    private static Map<AnimationType, Animation<TextureRegion>> textureMap;

    private float animationTime;
    private Animation<TextureRegion> currentAnimation;


    /**
     * Creates a new Drawable instance with a specified initial animation.
     * @param animationType The initial animation type.
     */
    public Drawable(AnimationType animationType) {
        assert textureMap != null; // should initialize before creating any drawable
        this.currentAnimation = textureMap.get(animationType);
        this.animationTime = 0;
    }

    /**
     * Initializes the static texture map with animations from the game instance.
     * @param game The MazeRunnerGame instance containing loaded assets.
     */
    public static void initializeTextureMap(MazeRunnerGame game) {
        Map<AnimationType, Animation<TextureRegion>> tempMap = new LinkedHashMap<>();
        tempMap.put(AnimationType.CHARACTER_UP, game.getCharacterUpAnimation());
        tempMap.put(AnimationType.CHARACTER_DOWN, game.getCharacterDownAnimation());
        tempMap.put(AnimationType.CHARACTER_LEFT, game.getCharacterLeftAnimation());
        tempMap.put(AnimationType.CHARACTER_RIGHT, game.getCharacterRightAnimation());
        tempMap.put(AnimationType.CHARACTER_ATTACK_UP, game.getCharacterAttackUpAnimation());
        tempMap.put(AnimationType.CHARACTER_ATTACK_DOWN, game.getCharacterAttackDownAnimation());
        tempMap.put(AnimationType.CHARACTER_ATTACK_LEFT, game.getCharacterAttackLeftAnimation());
        tempMap.put(AnimationType.CHARACTER_ATTACK_RIGHT, game.getCharacterAttackRightAnimation());
        tempMap.put(AnimationType.TOURIST_DOWN, game.getTouristDown());
        tempMap.put(AnimationType.TOURIST_UP, game.getTouristUp());
        tempMap.put(AnimationType.TOURIST_LEFT, game.getTouristLeft());
        tempMap.put(AnimationType.TOURIST_RIGHT, game.getTouristRight());
        tempMap.put(AnimationType.BOUNCER_DOWN, game.getBouncerDown());
        tempMap.put(AnimationType.BOUNCER_UP, game.getBouncerUp());
        tempMap.put(AnimationType.BOUNCER_LEFT, game.getBouncerLeft());
        tempMap.put(AnimationType.BOUNCER_RIGHT, game.getBouncerRight());
        tempMap.put(AnimationType.JUNKIE_DOWN, game.getJunkieDown());
        tempMap.put(AnimationType.JUNKIE_UP, game.getJunkieUp());
        tempMap.put(AnimationType.JUNKIE_LEFT, game.getJunkieLeft());
        tempMap.put(AnimationType.JUNKIE_RIGHT, game.getJunkieRight());
        tempMap.put(AnimationType.JUNKIE_ATTACK_DOWN, game.getJunkieAttackDown());
        tempMap.put(AnimationType.JUNKIE_ATTACK_UP, game.getJunkieAttackUp());
        tempMap.put(AnimationType.JUNKIE_ATTACK_LEFT, game.getJunkieAttackLeft());
        tempMap.put(AnimationType.JUNKIE_ATTACK_RIGHT, game.getJunkieAttackRight());
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
        tempMap.put(AnimationType.CHARACTER_INVIS_UP, game.getCharacterInvisUpAnimation());
        tempMap.put(AnimationType.CHARACTER_INVIS_DOWN, game.getCharacterInvisDownAnimation());
        tempMap.put(AnimationType.CHARACTER_INVIS_LEFT, game.getCharacterInvisLeftAnimation());
        tempMap.put(AnimationType.CHARACTER_INVIS_RIGHT, game.getCharacterInvisRightAnimation());
        tempMap.put(AnimationType.CHARACTER_INVIS_VICTORY_POSE, game.getCharacterInvisDownWin());
        tempMap.put(AnimationType.CHARACTER_INVIS_LOSER_POSE, game.getCharacterInvisDownSad());
        textureMap = Map.copyOf(tempMap);
    }

    private static Animation<TextureRegion> createSingleFrameAnim(TextureRegion region) {
        Array<TextureRegion> frames = new Array<>();
        frames.add(region);
        return new Animation<>(1f, frames); // Duration doesn't matter for 1 frame
    }

    /**
     * Gets the current animation time.
     * @return The state time of the animation.
     */
    public float getAnimationTime() {
        return animationTime;
    }

    /**
     * Sets the animation time manually.
     * @param animationTime The new state time.
     */
    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }

    /**
     * Updates the animation state.
     * @param delta The time elapsed since the last frame.
     */
    public void update(float delta) {
        this.animationTime += delta;
    }

    /**
     * Changes the current animation to a new type.
     * Resets animation time if the type is different.
     * @param animationType The new animation type to switch to.
     */
    public void changeAnimation(AnimationType animationType) {
        Animation<TextureRegion> newAnimation = textureMap.get(animationType);
        if (currentAnimation != newAnimation) {
            currentAnimation = newAnimation;
            animationTime = 0f;
        }

    }

    /**
     * Draws the current animation frame to the batch.
     * @param batch The SpriteBatch to draw to.
     * @param position The position to draw at.
     * @param spriteSizes The size of the drawn sprite.
     */
    public void animate(Batch batch, Vector2 position, Vector2 spriteSizes) {
        batch.draw(this.currentAnimation.getKeyFrame(this.animationTime, true), position.x, position.y, spriteSizes.x, spriteSizes.y);
    }

    /**
     * Gets the current animation object.
     * @return The current animation.
     */
    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }
}
