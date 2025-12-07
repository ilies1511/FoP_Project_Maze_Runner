package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    private Player player;
    private int playerHealth;

    private final Stage stage;

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

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.update(); // Update the camera




        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text
        font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                textX - 96,
                textY - 64,
                64,
                128
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
