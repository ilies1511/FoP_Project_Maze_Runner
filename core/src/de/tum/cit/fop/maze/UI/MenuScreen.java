package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private Table table;
    private final StoryModeScreen storyModeScreen;
    private final SettingsScreen settingsScreen;
    private final SoundController soundController;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        soundController = new SoundController();
        soundController.playMainMenuMusic(0.5f);


        Viewport viewport = new FitViewport(2000, 1200); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        table.top();

        Label label = new Label("Main Menu", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();



        // Create and add a button to go to the game screen
        TextButton goToStoryMode = new TextButton("Story Mode", game.getSkin(), "vampire-style");
        table.add(goToStoryMode).width(300).left().padLeft(50).padBottom(50);
        goToStoryMode.getLabel().setTouchable(Touchable.disabled);
        goToStoryMode.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToStoryMode.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });


        table.row();

        TextButton goToEndlessButton = new TextButton("Endless Mode", game.getSkin(), "vampire-style");
        table.add(goToEndlessButton).width(300).left().padLeft(50).padBottom(50);
        goToEndlessButton.getLabel().setTouchable(Touchable.disabled);
        goToEndlessButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToEndlessButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();

        TextButton goToSettingsButton = new TextButton("Settings", game.getSkin(), "vampire-style");
        table.add(goToSettingsButton).width(300).left().padLeft(50).padBottom(50);
        goToSettingsButton.getLabel().setTouchable(Touchable.disabled);
        goToSettingsButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToSettingsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();

        TextButton goToStatisticsButton = new TextButton("Statistics", game.getSkin(), "vampire-style");
        table.add(goToStatisticsButton).width(300).left().padLeft(50).padBottom(50);
        goToStatisticsButton.getLabel().setTouchable(Touchable.disabled);
        goToStatisticsButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToStatisticsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();

        TextButton goToExitButton = new TextButton("Exit", game.getSkin(), "vampire-style");
        table.add(goToExitButton).width(300).left().padLeft(50).padBottom(50);
        goToExitButton.getLabel().setTouchable(Touchable.disabled);
        goToExitButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToExitButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });


        goToStoryMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              goToStory();
            }
        });

        goToExitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        goToSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToSettings();
            }
        });

        goToEndlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO Iziane add logic
            }
        });

        storyModeScreen = new StoryModeScreen(stage, game, this);
        settingsScreen = new SettingsScreen(stage,game,this);
        // TODO add listeners to Statistics, EndlessMode
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

    public void goToStory(){
        table.setVisible(false);
        storyModeScreen.getStoryTable().setVisible(true);
    }
    public void goBackToMenu(){
        table.setVisible(true);
        storyModeScreen.getStoryTable().setVisible(false);
        settingsScreen.getTable().setVisible(false);
    }
    public void goToSettings(){
        table.setVisible(false);
        settingsScreen.getTable().setVisible(true);
    }

    public SoundController getSoundController() {
        return soundController;
    }

}
