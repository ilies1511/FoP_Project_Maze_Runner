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
 * Screen for the Story Mode menu.
 * Allows the player to start a new story game or continue an existing one.
 */
public class StoryModeScreen {
    private final Table storyTable;
    private final SoundController soundController;
    private final Table confirmationTable;

    /**
     * Constructs a StoryModeScreen.
     *
     * @param stage      The stage to add actors to.
     * @param game       The main game instance.
     * @param menuScreen The main menu screen for navigation.
     */
    public StoryModeScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        storyTable = new Table();
        Texture backgroundTexture = new Texture(Gdx.files.internal("mainMenu_background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        storyTable.setBackground(backgroundDrawable);
        storyTable.setVisible(false);
        storyTable.setFillParent(true);
        stage.addActor(storyTable);
        storyTable.top();

        soundController = menuScreen.getSoundController();

        Label label = new Label("Story Mode", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        storyTable.add(label).expandX().center().padTop(50).padBottom(80).row();

        TextButton continueGame = new TextButton("Continue", game.getSkin(), "vampire-style");
        storyTable.add(continueGame).width(300).left().padLeft(50).padBottom(50);
        continueGame.getLabel().setTouchable(Touchable.disabled);

        continueGame.setDisabled(!SaveManager.hasSave(true));

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
                    GameScreenMemento memento = SaveManager.loadGame(true);
                    if (memento != null) {
                        soundController.dispose();
                        game.setGameMode(GameMode.STORY_MODE);
                        game.setScreen(new GameScreen(game, memento));
                    }
                }
            }
        });

        storyTable.row();

        TextButton newGame = new TextButton("New Game", game.getSkin(), "vampire-style");
        storyTable.add(newGame).width(300).left().padLeft(50).padBottom(50);
        newGame.getLabel().setTouchable(Touchable.disabled);
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
                if (continueGame.isDisabled()) {
                    soundController.dispose();
                    game.goToBackgroundStory();
                } else {
                    confirmNewGame();
                }
            }
        });


        storyTable.row();

        TextButton toMainMenu = new TextButton("Main Menu", game.getSkin(), "vampire-style");
        storyTable.add(toMainMenu).width(300).left().padLeft(50).padBottom(50);
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
        confirmationTable = new Table();
        confirmationTable.setVisible(false);
        confirmationTable.setFillParent(true);
        stage.addActor(confirmationTable);
        confirmationTable.top();
        Label confLabel = new Label("Are you sure you want to start a new game?", game.getSkin(), "vampire-style");
        confLabel.setFontScale(2f);
        confirmationTable.add(confLabel).expandX().center().padTop(50).padBottom(400).row();

        TextButton yes = new TextButton("Yes", game.getSkin(), "vampire-style");
        confirmationTable.add(yes).width(300).padBottom(50).row();
        yes.getLabel().setTouchable(Touchable.disabled);
        yes.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !yes.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundController.dispose();
                game.goToBackgroundStory();
            }
        });
        TextButton no = new TextButton("No", game.getSkin(), "vampire-style");
        confirmationTable.add(no).width(300).padBottom(50);
        no.getLabel().setTouchable(Touchable.disabled);
        no.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !no.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backToStory();
            }
        });
        confirmationTable.invalidateHierarchy();

    }

    /**
     * Returns the table containing the story mode UI.
     *
     * @return The story mode table.
     */
    public Table getStoryTable() {
        return storyTable;
    }

    /**
     * Shows the confirmation dialog for starting a new game.
     */
    public void confirmNewGame() {
        storyTable.setVisible(false);
        confirmationTable.setVisible(true);
    }

    /**
     * Returns to the story mode menu from the confirmation dialog.
     */
    public void backToStory() {
        storyTable.setVisible(true);
        confirmationTable.setVisible(false);
    }
}
