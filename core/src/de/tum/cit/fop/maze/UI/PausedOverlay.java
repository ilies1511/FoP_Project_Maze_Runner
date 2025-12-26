package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class PausedOverlay {

    private final GameScreen gameScreen;
    private Stage stage;
    private MazeRunnerGame game;
    private Table table;

    public PausedOverlay(Stage stage, MazeRunnerGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);
        table.center();

        this.stage = stage;
        stage.addActor(table);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.5f));
        pixmap.fill();
        TextureRegionDrawable dim = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(dim);

        TextButton goToGameButton = new TextButton("Resume", game.getSkin(), "vampire-style");
        table.add(goToGameButton).width(300).left().padLeft(50).padBottom(50);

        table.row();

        TextButton goToMainMenu = new TextButton("Main menu", game.getSkin(), "vampire-style");
        table.add(goToMainMenu).width(300).left().padLeft(50).padBottom(50);

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                hidePausedMenu();
            }
        });
        goToGameButton.getLabel().setTouchable(Touchable.disabled);
        goToGameButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToGameButton.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        goToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.getSoundController().stopMainMenuMusic();
                game.goToMenu();
            }
        });
        goToMainMenu.getLabel().setTouchable(Touchable.disabled);
        goToMainMenu.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToMainMenu.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });


    }

    public void showPausedMenu() {
        gameScreen.getSoundController().stopBackgroundMusic();
        gameScreen.getSoundController().playMainMenuMusic(0.5f);
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        gameScreen.setPaused(true);
    }

    public void hidePausedMenu() {
        gameScreen.getSoundController().stopMainMenuMusic();
        gameScreen.getSoundController().playBackgroundMusic(0.5f);
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        gameScreen.setPaused(false);
    }
}
