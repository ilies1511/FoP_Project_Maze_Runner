package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.game_saves.SaveManager;
import de.tum.cit.fop.maze.game_saves.mementos.GameScreenMemento;

/**
 * Screen for the Endless Mode menu.
 * Allows the player to start a new endless game, continue an existing one, or view statistics.
 */
public class EndlessModeScreen {
    private final Table table;
    private final SoundController soundController;

    /**
     * Constructs an EndlessModeScreen.
     *
     * @param stage      The stage to add actors to.
     * @param game       The main game instance.
     * @param menuScreen The main menu screen for navigation and sound control.
     */
    public EndlessModeScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        soundController = menuScreen.getSoundController();
        table = new Table();
        Texture backgroundTexture = new Texture(Gdx.files.internal("mainMenu_background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        table.setBackground(backgroundDrawable);
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        Label label = new Label("Endless Mode", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();

        TextButton continueGame = new TextButton("Continue", game.getSkin(), "vampire-style");
        table.add(continueGame).width(300).left().padLeft(50).padBottom(50);
        continueGame.getLabel().setTouchable(Touchable.disabled);

        // Check if save file exists and enable button accordingly
        continueGame.setDisabled(!SaveManager.hasSave(false));

        continueGame.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !continueGame.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!continueGame.isDisabled()) {
                    soundController.playUI_buttonClickSound(1);
                }
            }
        });

        continueGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!continueGame.isDisabled()) {
                    // Load the saved game
                    GameScreenMemento memento = SaveManager.loadGame(false);
                    if (memento != null) {
                        soundController.dispose();
                        game.setGameMode(GameMode.ENDLESS_MODE);
                        game.setScreen(new GameScreen(game, memento));
                    }
                }
            }
        });

        table.row();

        TextButton newGame = new TextButton("New Game", game.getSkin(), "vampire-style");
        table.add(newGame).width(300).left().padLeft(50).padBottom(50);
        newGame.getLabel().setTouchable(Touchable.disabled);
        newGame.setDisabled(false);
        newGame.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !newGame.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundController.dispose();
                game.setGameMode(GameMode.ENDLESS_MODE);
                game.setScreen(new GameScreen(game));
            }
        });

        table.row();

        TextButton goToStatisticsButton = new TextButton("Statistics", game.getSkin(), "vampire-style");
        table.add(goToStatisticsButton).width(300).left().padLeft(50).padBottom(50);
        goToStatisticsButton.getLabel().setTouchable(Touchable.disabled);
        goToStatisticsButton.setDisabled(false);
        goToStatisticsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.goToLeaderboard();
            }
        });
        goToStatisticsButton.addListener(new ClickListener() {
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

        TextButton toMainMenu = new TextButton("Main Menu", game.getSkin(), "vampire-style");
        table.add(toMainMenu).width(300).left().padLeft(50).padBottom(50);
        toMainMenu.getLabel().setTouchable(Touchable.disabled);
        toMainMenu.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !toMainMenu.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        toMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.goBackToMenu();
            }
        });


    }

    /**
     * Returns the table containing the endless mode UI.
     *
     * @return The endless mode table.
     */
    public Table getEndlessTable() {
        return table;
    }
}
