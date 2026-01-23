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
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

/**
 * Screen for the Level Mode menu.
 * Allows the player to start a new game (select a level) or return to the main menu.
 */
public class LevelModeScreen {
    private final Table levelTable;
    private final SoundController soundController;
    private final LevelSelectionScreen levelSelectionScreen;

    /**
     * Constructs a LevelModeScreen.
     *
     * @param stage      The stage to add actors to.
     * @param game       The main game instance.
     * @param menuScreen The main menu screen.
     */
    public LevelModeScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        levelTable = new Table();
        Texture backgroundTexture = new Texture(Gdx.files.internal("mainMenu_background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        levelTable.setBackground(backgroundDrawable);
        levelTable.setVisible(false);
        levelTable.setFillParent(true);
        stage.addActor(levelTable);
        levelTable.top();

        soundController = menuScreen.getSoundController();

        Label label = new Label("Level Mode", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        levelTable.add(label).expandX().center().padTop(50).padBottom(80).row();

        TextButton newGame = new TextButton("New Game", game.getSkin(), "vampire-style");
        levelTable.add(newGame).width(300).left().padLeft(50).padBottom(50);
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

                goToLevelSelection();
            }
        });


        levelTable.row();

        TextButton toMainMenu = new TextButton("Main Menu", game.getSkin(), "vampire-style");
        levelTable.add(toMainMenu).width(300).left().padLeft(50).padBottom(50);
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
        levelSelectionScreen = new LevelSelectionScreen(stage, game, this, menuScreen);
    }

    private void goToLevelSelection() {
        levelTable.setVisible(false);
        levelSelectionScreen.getLevelSelectionTable().setVisible(true);
    }

    /**
     * Returns the table containing the level mode UI.
     *
     * @return The level mode table.
     */
    public Table getLevelTable() {
        return levelTable;
    }

    /**
     * Returns the LevelSelectionScreen associated with this screen.
     *
     * @return The LevelSelectionScreen.
     */
    public LevelSelectionScreen getLevelSelectionScreen() {
        return levelSelectionScreen;
    }

}
