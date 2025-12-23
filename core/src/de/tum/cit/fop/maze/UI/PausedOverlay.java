package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;

public class PausedOverlay {

    private Stage stage;
    private MazeRunnerGame game;
    private final GameScreen gameScreen;

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

        TextButton goToMainMenu = new TextButton("Main menu", game.getSkin(),"vampire-style");
        table.add(goToMainMenu).width(300).left().padLeft(50).padBottom(50);

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               hidePausedMenu();
            }
        });
        goToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu();
            }
        });


    }

    public void showPausedMenu(){
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        gameScreen.setPaused(true);
    }

    public void hidePausedMenu(){
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        gameScreen.setPaused(false);
    }
}
