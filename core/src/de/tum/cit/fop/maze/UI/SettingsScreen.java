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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.KeyBindingButton;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class SettingsScreen  {
   private Table table;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public SettingsScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view

        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        Label label = new Label("Settings", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();

        // Back to menu button
        TextButton goToMenuButton = new TextButton("Back to menu", game.getSkin(),  "vampire-style");
        table.add(goToMenuButton).width(300).row();
        goToMenuButton.getLabel().setTouchable(Touchable.disabled);
        goToMenuButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToMenuButton.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
             menuScreen.goBackToMenu(); // Change to the game screen when button is pressed
            }
        });

        // Binding buttons
        KeyBindingButton leftBinding = new KeyBindingButton("Move left", game.getGameInfo().getSettings().getLeftKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setLeftKey(keycode);
            }
        });
        leftBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        leftBinding.getLabel().setTouchable(Touchable.disabled);
        leftBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !leftBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton rightBinding = new KeyBindingButton("Move right", game.getGameInfo().getSettings().getRightKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setRightKey(keycode);
            }
        });
        rightBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        rightBinding.getLabel().setTouchable(Touchable.disabled);
        rightBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !rightBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton upBinding = new KeyBindingButton("Move up", game.getGameInfo().getSettings().getUpKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setUpKey(keycode);
            }
        });
        upBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        upBinding.getLabel().setTouchable(Touchable.disabled);
        upBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !upBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton downBinding = new KeyBindingButton("Move down", game.getGameInfo().getSettings().getDownKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setDownKey(keycode);
            }
        });
        downBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        downBinding.getLabel().setTouchable(Touchable.disabled);
        downBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !downBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton attackBinding = new KeyBindingButton("Attack", game.getGameInfo().getSettings().getAttackKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setAttackKey(keycode);
            }
        });
        attackBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        attackBinding.getLabel().setTouchable(Touchable.disabled);
        attackBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !attackBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton dashBinding = new KeyBindingButton("Dash", game.getGameInfo().getSettings().getDashKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setDashKey(keycode);
            }
        });
        dashBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        dashBinding.getLabel().setTouchable(Touchable.disabled);
        dashBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !dashBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton zoomInBinding = new KeyBindingButton("Dash", game.getGameInfo().getSettings().getZoomInKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setZoomInKey(keycode);
            }
        });
        zoomInBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        zoomInBinding.getLabel().setTouchable(Touchable.disabled);
        zoomInBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !zoomInBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        KeyBindingButton zoomOutBinding = new KeyBindingButton("Dash", game.getGameInfo().getSettings().getZoomOutKey(), game.getSkin(), new KeyBindingButton.KeyBindingCallback() {
            @Override
            public void handle(int keycode) {
                game.getGameInfo().getSettings().setZoomOutKey(keycode);
            }
        });
        zoomOutBinding.setStyle(game.getSkin().get("vampire-style", TextButton.TextButtonStyle.class));
        zoomOutBinding.getLabel().setTouchable(Touchable.disabled);
        zoomOutBinding.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !zoomOutBinding.isDisabled()) {
                    SoundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundController.playUI_buttonClickSound(1);
            }
        });

        table.add(leftBinding).width(300).row();
        table.add(rightBinding).width(300).row();
        table.add(upBinding).width(300).row();
        table.add(downBinding).width(300).row();
        table.add(attackBinding).width(300).row();
        table.add(dashBinding).width(300).row();
        table.add(zoomInBinding).width(300).row();
        table.add(zoomOutBinding).width(300).row();
    }

    public Table getTable() {
        return table;
    }
}
