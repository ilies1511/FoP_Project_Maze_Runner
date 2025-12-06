package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float xPlayer = 0f;
    private float yPlayer = 0f;
    private float speed = 120f;
    private float animationTime = 0f;

    private Animation<TextureRegion> currentAnimation;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Start by looking down
        currentAnimation = game.getCharacterDownAnimation();

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        // Input Handling New Class
        handleInput(delta);

        //uncomment to center stage player
        //camera.position.x = xPlayer + 8; // +8 to center on a 16px sprite
        //camera.position.y = yPlayer + 8;
        camera.update(); // Update the camera

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        animationTime += delta;

        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                currentAnimation.getKeyFrame(animationTime, true),
                xPlayer,
                yPlayer,
                64,
                128
        );

        game.getSpriteBatch().end(); // Important to call this after drawing everything
    }
    private void handleInput(float delta) {
        boolean isMoving = false;
        float currentSpeed = speed;

        // Speed boost
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            currentSpeed *= 1.8f;
        }

        // --- MOVEMENT LOGIC ---

        // Y-Axis
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            yPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterUpAnimation();
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            yPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterDownAnimation();
            isMoving = true;
        }

        // X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            xPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterLeftAnimation();
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            xPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterRightAnimation();
            isMoving = true;
        }

        // Jump (Fix proper logic when final image of dracula is confirmed)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Check which direction we are currently facing/moving to pick the right jump
            if (currentAnimation == game.getCharacterUpAnimation() || currentAnimation == game.getCharacterUpJumpAnimation()) {
                currentAnimation = game.getCharacterUpJumpAnimation();
            } else if (currentAnimation == game.getCharacterLeftAnimation() || currentAnimation == game.getCharacterLeftJumpAnimation()) {
                currentAnimation = game.getCharacterLeftJumpAnimation();
            } else if (currentAnimation == game.getCharacterRightAnimation() || currentAnimation == game.getCharacterRightJumpAnimation()) {
                currentAnimation = game.getCharacterRightJumpAnimation();
            } else {
                currentAnimation = game.getCharacterDownJumpAnimation();
            }
            isMoving = true; // Keep animating while jumping
        }

        // Victory pose
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            currentAnimation = game.getCharacterDownWin();
            isMoving = true;
        }
        // Loser pose
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            currentAnimation = game.getCharacterDownSad();
            isMoving = true;
        }

        // Animation Timer
        if (isMoving) {
            animationTime += delta;
        } else {
            animationTime = 0; // Reset to standing frame when not moving
        }

        // Go to Menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    // Additional methods and logic can be added as needed for the game screen
}
