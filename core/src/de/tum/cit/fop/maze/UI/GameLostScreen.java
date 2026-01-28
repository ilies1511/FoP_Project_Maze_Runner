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
import de.tum.cit.fop.maze.MVC.GameController;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

import java.util.List;

/**
 * Screen displayed when the player loses the game.
 * Provides an option to exit to the main menu.
 */
public class GameLostScreen implements Screen {
    private final MazeRunnerGame game;
    private Stage stage;
    private final SoundController soundController;

    /**
     * Constructs a GameLostScreen.
     *
     * @param game The main game instance.
     */
    public GameLostScreen(MazeRunnerGame game, int score) {
        this.game = game;
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        soundController = new SoundController();
        var camera = new OrthographicCamera();
        camera.zoom = 1f;

        Viewport viewport = new FitViewport(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
        stage = new Stage(viewport, game.getSpriteBatch());

        Table table = new Table();
        stage.addActor(table);
        Label label = new Label("Game Lost", game.getSkin(), "vampire-style");
        label.setFontScale(1.7f);
        table.setFillParent(true);
        table.top();
        table.add(label).expandX().center().padTop(50).padBottom(80).row();


        TextButton goToMainMenu = new TextButton("Exit", game.getSkin(), "vampire-style");

        switch (game.getGameMode()){
            case ENDLESS_MODE:
                Label scoreLbl = new Label("Your score: " + score, game.getSkin(), "vampire-style");
                table.add(scoreLbl).expandX().center().padTop(50).padBottom(80).row();


                table.add(goToMainMenu).width(300).padLeft(50).padBottom(50).center();
                goToMainMenu.getLabel().setTouchable(Touchable.disabled);
                goToMainMenu.addListener(new ClickListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        if (pointer == -1 && !goToMainMenu.isDisabled()) {
                            soundController.playUI_hoverOverSound(1);
                        }
                    }

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        soundController.playUI_buttonClickSound(1);
                    }
                });
                goToMainMenu.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        soundController.dispose();
                        game.goToMenu();
                    }
                });
            break;
            case STORY_MODE:
                table.add(goToMainMenu).width(300).padLeft(50).padBottom(50).center();
                goToMainMenu.getLabel().setTouchable(Touchable.disabled);
                goToMainMenu.addListener(new ClickListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        if (pointer == -1 && !goToMainMenu.isDisabled()) {
                            soundController.playUI_hoverOverSound(1);
                        }
                    }

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        soundController.playUI_buttonClickSound(1);
                    }
                });
                goToMainMenu.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        soundController.dispose();
                        game.goToMenu();
                    }
                });
                break;
            case LEVELS_MODE:
                table.add(goToMainMenu).width(300).padLeft(50).padBottom(50).center();
                goToMainMenu.getLabel().setTouchable(Touchable.disabled);
                goToMainMenu.addListener(new ClickListener() {
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        if (pointer == -1 && !goToMainMenu.isDisabled()) {
                            soundController.playUI_hoverOverSound(1);
                        }
                    }

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        soundController.playUI_buttonClickSound(1);
                    }
                });
                goToMainMenu.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        soundController.dispose();
                        game.goToMenu();
                    }
                });
                break;

        }






    }
    

    /**
     * Renders the screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen size changes.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of the screen and its resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called when this screen becomes the current screen for the game.
     */
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the application is paused.
     */
    @Override
    public void pause() {

    }

    /**
     * Called when the application is resumed from a paused state.
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for the game.
     */
    @Override
    public void hide() {

    }


}
