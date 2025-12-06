package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsScreen implements Screen {

    private final Stage stage;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public SettingsScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Hello World from the Menu!", game.getSkin(), "title")).padBottom(80).row();

        // Back to menu button
        TextButton goToMenuButton = new TextButton("Back to menu", game.getSkin());
        table.add(goToMenuButton).width(300).row();

        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu(); // Change to the game screen when button is pressed
            }
        });

        // Binding buttons
        KeyBindingButton leftBinding = new KeyBindingButton("Move left", game.getGameInfo().getSettings().getLeftKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setLeftKey(keycode);
            }
        });

        KeyBindingButton rightBinding = new KeyBindingButton("Move right", game.getGameInfo().getSettings().getRightKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setRightKey(keycode);
            }
        });

        KeyBindingButton upBinding = new KeyBindingButton("Move up", game.getGameInfo().getSettings().getUpKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setUpKey(keycode);
            }
        });

        KeyBindingButton downBinding = new KeyBindingButton("Move down", game.getGameInfo().getSettings().getDownKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setDownKey(keycode);
            }
        });

        table.add(leftBinding).width(300).row();
        table.add(rightBinding).width(300).row();
        table.add(upBinding).width(300).row();
        table.add(downBinding).width(300).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
