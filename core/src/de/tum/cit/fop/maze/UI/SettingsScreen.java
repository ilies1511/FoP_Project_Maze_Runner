package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.tum.cit.fop.maze.GameSettings;
import de.tum.cit.fop.maze.KeyBindingButton;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class SettingsScreen {
    private Table table;
    private Skin skin;
    private SoundController soundController;
    private List<KeyBindingButton> bindingButtons = new ArrayList<KeyBindingButton>();

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public SettingsScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view

        this.soundController = menuScreen.getSoundController();

        this.table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        this.skin = game.getSkin();

        Label label = new Label("Settings", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();

        // Back to menu button
        TextButton goToMenuButton = new TextButton("Back to menu", game.getSkin(), "vampire-style");
        table.add(goToMenuButton).width(300).row();
        goToMenuButton.getLabel().setTouchable(Touchable.disabled);
        goToMenuButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToMenuButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.goBackToMenu(); // Change to the game screen when button is pressed
            }
        });

        // Key Binding buttons
        GameSettings settings = game.getGameInfo().getSettings();

        addKeyBindingButton("Move left",
                () -> game.getGameInfo().getSettings().getLeftKey(),
                (code) -> game.getGameInfo().getSettings().setLeftKey(code));

        addKeyBindingButton("Move right",
                () -> game.getGameInfo().getSettings().getRightKey(),
                (code) -> game.getGameInfo().getSettings().setRightKey(code));

        addKeyBindingButton("Move up",
                () -> game.getGameInfo().getSettings().getUpKey(),
                (code) -> game.getGameInfo().getSettings().setUpKey(code));

        addKeyBindingButton("Move down",
                () -> game.getGameInfo().getSettings().getDownKey(),
                (code) -> game.getGameInfo().getSettings().setDownKey(code));

        addKeyBindingButton("Attack",
                () -> game.getGameInfo().getSettings().getAttackKey(),
                (code) -> game.getGameInfo().getSettings().setAttackKey(code));

        addKeyBindingButton("Dash",
                () -> game.getGameInfo().getSettings().getDashKey(),
                (code) -> game.getGameInfo().getSettings().setDashKey(code));

        addKeyBindingButton("Zoom in",
                () -> game.getGameInfo().getSettings().getZoomInKey(),
                (code) -> game.getGameInfo().getSettings().setZoomInKey(code));

        addKeyBindingButton("Zoom out",
                () -> game.getGameInfo().getSettings().getZoomOutKey(),
                (code) -> game.getGameInfo().getSettings().setZoomOutKey(code));

        TextButton restoreDefaultSettingsButton = new TextButton("Restore default settings", game.getSkin(), "vampire-style");
        table.add(restoreDefaultSettingsButton).width(300).row();
        restoreDefaultSettingsButton.getLabel().setTouchable(Touchable.disabled);
        restoreDefaultSettingsButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !restoreDefaultSettingsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        restoreDefaultSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getGameInfo().restoreDefaultSettings(); // Restore default settings when pressed
                updateKeyBindingButtons();
            }
        });
    }

    private void addKeyBindingButton(String buttonName, IntSupplier keyGetter, IntConsumer keySetter) {
        KeyBindingButton binding = new KeyBindingButton(buttonName, keyGetter.getAsInt(), this.skin, new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                keySetter.accept(keycode);
            }
        }, keyGetter);
        binding.setStyle(skin.get("vampire-style", TextButton.TextButtonStyle.class));
        binding.getLabel().setTouchable(Touchable.disabled);
        binding.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !binding.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        this.table.add(binding).width(300).row();

        this.bindingButtons.add(binding);
    }

    private void updateKeyBindingButtons() {
        for (KeyBindingButton binding : bindingButtons) {
            binding.updateBindingText();
        }
    }

    public Table getTable() {
        return table;
    }
}
