package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private float animationTime = 0f;

    private Animation<TextureRegion> currentAnimation;

    private final Player player;
    private int playerHealth;

    private final Stage stage;

    private Entity playerEntity;

    private GameField gameField;

    Table table;

  //  int i = 0;
    //For testing
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        player = new Player(); //create an instance of the player

        playerEntity = new Entity(new Vector2(0f, 0f), new Vector2(0f, 0f), 3f, new Vector2(1f, 1f));

        gameField = new GameField();

        float viewportWidth = 25f;  // Visible world width
        float viewportHeight = 18.75f; // Visible world height

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.zoom = 0.75f;

        // Start by looking down
        currentAnimation = game.getCharacterDownAnimation();

        // Get the font from the game's skin
        font = game.getSkin().getFont("vampire-font");

        Viewport viewport = new ScreenViewport(); // TODO maybe add camera in the brackets
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        renderHUD();
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

        // Move all the entities
        updateEntitiesPositions(delta);

        // Update camera to move with the player
        moveCamera(delta);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        animationTime += delta;

        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        drawGameField();

        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                currentAnimation.getKeyFrame(animationTime, true),
                playerEntity.position.x,
                playerEntity.position.y,
                0.4f,
                0.8f
        );

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

// For Testing
//        i++;
//        if(i == 1){
//            updatePlayerHealth(player.getDamage(1));
//        }
//        System.out.println(player.getHealth());
    }
    private void handleInput(float delta) {
        boolean isMoving = false;

        // Speed boost // TODO: Add boost to keybindings, handle it via playerEntity
//        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
//            currentSpeed *= 1.8f;
//        }

        // --- MOVEMENT LOGIC ---

        // Y-Axis
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
              playerEntity.velocity.y = playerEntity.speed;
//            yPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterUpAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            playerEntity.velocity.y = -playerEntity.speed;
//            yPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterDownAnimation();
            isMoving = true;
        }

        else {
            playerEntity.velocity.y = 0f;
        }

        // X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            playerEntity.velocity.x = -playerEntity.speed;
//            xPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterLeftAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            playerEntity.velocity.x = playerEntity.speed;
//            xPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterRightAnimation();
            isMoving = true;
        }

        else {
            playerEntity.velocity.x = 0f;
        }

        // Jump (Fix proper logic when final image of dracula is confirmed) // TODO: handle via playerEntity, add to keybindings
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            // Check which direction we are currently facing/moving to pick the right jump
//            if (currentAnimation == game.getCharacterUpAnimation() || currentAnimation == game.getCharacterUpJumpAnimation()) {
//                currentAnimation = game.getCharacterUpJumpAnimation();
//            } else if (currentAnimation == game.getCharacterLeftAnimation() || currentAnimation == game.getCharacterLeftJumpAnimation()) {
//                currentAnimation = game.getCharacterLeftJumpAnimation();
//            } else if (currentAnimation == game.getCharacterRightAnimation() || currentAnimation == game.getCharacterRightJumpAnimation()) {
//                currentAnimation = game.getCharacterRightJumpAnimation();
//            } else {
//                currentAnimation = game.getCharacterDownJumpAnimation();
//            }
//            isMoving = true; // Keep animating while jumping
//        }

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

    private void updateEntitiesPositions(float delta) {
        this.playerEntity.move(delta);
    }

    private void moveCamera(float delta) {
        camera.position.x = playerEntity.position.x;
        camera.position.y = playerEntity.position.y;
        camera.update();
    }

    private void drawGameField() {
        for (int x = 0; x < gameField.getHeight(); x++) {
            for (int y = 0; y < gameField.getWidth(); y++) {
                TileType tileType = gameField.getTile(x, y);
                if (tileType == TileType.WALL) {
                    game.getSpriteBatch().draw(currentAnimation.getKeyFrame(animationTime, true), x, y, 1, 1);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    private void renderHUD(){
        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        table.debug();
        stage.addActor(table);
        table.bottom().left();
        playerHealth = player.getHealth();
        // int numberOfFullHearts;
        // float notFullHeart;
        //  numberOfFullHearts = player.getHealth();
        // notFullHeart = player.getHealth() - numberOfFullHearts;
        for(int i = 0;i<playerHealth; i++ ){
            Image heart = new Image(game.getSkin().getDrawable("heart"));
            table.add(heart).width(100).height(100).padBottom(30).padLeft(30);
        }

    }

    private void updatePlayerHealth(int damage){
        table.clearChildren();
        for(int i = 0;i<playerHealth - damage; i++ ){
            Image heart = new Image(game.getSkin().getDrawable("heart"));
            table.add(heart).width(100).height(100).padBottom(30).padLeft(30);
        }
        for(int i =0; i < damage; i++){
            Image emptyHeart = new Image(game.getSkin().getDrawable("heart-bg"));
            table.add(emptyHeart).width(100).height(100).padBottom(30).padLeft(30);
        }
    }
    // Additional methods and logic can be added as needed for the game screen
}
