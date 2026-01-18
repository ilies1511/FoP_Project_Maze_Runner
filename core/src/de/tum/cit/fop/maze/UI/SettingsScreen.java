package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
        Texture backgroundTexture = new Texture(Gdx.files.internal("settings_background.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        table.setBackground(backgroundDrawable);
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        this.skin = game.getSkin();

        Label label = new Label("Settings", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().colspan(4).row();

        // Back to menu button
        TextButton goToMenuButton = new TextButton("Back to menu", game.getSkin(), "vampire-style");
        table.add(goToMenuButton).width(400).colspan(4).row();
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

        addKeyBindingButton("Interact",
                () -> game.getGameInfo().getSettings().getInteractKey(),
                (code) -> game.getGameInfo().getSettings().setInteractKey(code));

        addKeyBindingButton("Ability",
                () -> game.getGameInfo().getSettings().getAbilityKey(),
                (code) -> game.getGameInfo().getSettings().setAbilityKey(code));

        addKeyBindingButton("Console",
                () -> game.getGameInfo().getSettings().getConsoleKey(),
                (code) -> game.getGameInfo().getSettings().setConsoleKey(code));


        addKeyBindingButton("Progression Tree",
                () -> game.getGameInfo().getSettings().getProgressionTreeKey(),
                (code) -> game.getGameInfo().getSettings().setProgressionTreeKey(code));


        addKeyBindingButton("Zoom in",
                () -> game.getGameInfo().getSettings().getZoomInKey(),
                (code) -> game.getGameInfo().getSettings().setZoomInKey(code));

        addKeyBindingButton("Zoom out",
                () -> game.getGameInfo().getSettings().getZoomOutKey(),
                (code) -> game.getGameInfo().getSettings().setZoomOutKey(code));

        table.add(new Label("Resolution Height:", game.getSkin(), "vampire-style")).width(400);

        TextField resolutionHeight = new TextField("", game.getSkin(), "default");
        TextField.TextFieldStyle style = resolutionHeight.getStyle();

        if (style.background != null) {
            style.background.setRightWidth(20);
        }
        resolutionHeight.setStyle(style);
        resolutionHeight.setText(Integer.toString(game.getGameInfo().getSettings().getResolutionHeight()));
        resolutionHeight.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if ((c == '\r' || c == '\n') && !textField.getText().isEmpty()) {
                    String height = textField.getText();
                    if( Integer.parseInt(height) < 2147483647 && Integer.parseInt(height)> 0) {
                        try {
                        game.getGameInfo().getSettings().setResolutionHeight(Integer.parseInt(height));
                            Gdx.graphics.setWindowedMode(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
                            stage.getViewport().update(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight(),  true); // Update the stage viewport on resize
                        } catch (Exception e) {
                            resolutionHeight.setText("");
                            System.out.println("not an int: " + e.getMessage());
                        }
                    }
                    else{
                        resolutionHeight.setText("");
                    }

                }
            }
        });
        table.add(resolutionHeight).width(400);

        table.add(new Label("Resolution Width:", game.getSkin(), "vampire-style")).width(400);

        TextField resolutionWidth = new TextField("", game.getSkin(), "default");
        style = resolutionWidth.getStyle();
        if (style.background != null) {
            style.background.setRightWidth(20);
        }
        resolutionWidth.setStyle(style);
        resolutionWidth.setText(Integer.toString(game.getGameInfo().getSettings().getResolutionWidth()));
        resolutionWidth.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if ((c == '\r' || c == '\n') && !textField.getText().isEmpty()) {
                    String width = textField.getText();
                    if( Integer.parseInt(width) < 2147483647 && Integer.parseInt(width)> 0) {
                        try {
                            game.getGameInfo().getSettings().setResolutionWidth(Integer.parseInt(width));
                            Gdx.graphics.setWindowedMode(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
                            stage.getViewport().update(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight(),  true);
                        } catch (Exception e) {
                            resolutionWidth.setText("");
                            System.out.println("not an int: " + e.getMessage());
                        }
                    }
                    else{
                        resolutionWidth.setText("");
                    }

                }
            }
        });
        table.add(resolutionWidth).width(400).row();

        TextButton fsButton = new TextButton("Mode: Windowed", skin);

        fsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
                    stage.getViewport().update(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight(),  true); // Update the stage viewport on resize
                    fsButton.setText("Mode: Windowed");
                } else {
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(currentMode);
                    stage.getViewport().update(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight(),  true); // Update the stage viewport on resize
                    fsButton.setText("Mode: Fullscreen");
                }
            }
        });

        fsButton.setText(Gdx.graphics.isFullscreen() ? "Mode: Fullscreen" : "Mode: Windowed");

        table.add(fsButton).colspan(2);



        TextButton restoreDefaultSettingsButton = new TextButton("Restore default settings", game.getSkin(), "vampire-style");
        table.add(restoreDefaultSettingsButton).width(400).colspan(2).row();
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
                resolutionHeight.setText(Integer.toString(game.getGameInfo().getSettings().getResolutionHeight()));
                resolutionWidth.setText(Integer.toString(game.getGameInfo().getSettings().getResolutionWidth()));
                Gdx.graphics.setWindowedMode(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight());
                stage.getViewport().update(game.getGameInfo().getSettings().getResolutionWidth(), game.getGameInfo().getSettings().getResolutionHeight(),  true);
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

        this.table.add(binding).width(400).colspan(4).row();

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
