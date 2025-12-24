package de.tum.cit.fop.maze;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class KeyBindingButton extends TextButton {
    private final String actionName;
    private final KeyBindingCallback callback;
    private int keycode;
    private boolean isListeningKeys;

    public KeyBindingButton(String actionName, int keycode, Skin skin, KeyBindingCallback callback) {
        super("", skin);
        this.keycode = keycode;
        this.actionName = actionName;
        this.callback = callback;
        this.isListeningKeys = false;
        this.updateBindingText();

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                KeyBindingButton.this.startListening();
            }
        });

        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (!KeyBindingButton.this.isListeningKeys) {
                    return false;
                }
                KeyBindingButton.this.keycode = keycode;
                KeyBindingButton.this.callback.handle(keycode);
                KeyBindingButton.this.stopListening();
                return true;
            }
        });
    }

    private void updateBindingText() {
        this.setText(this.actionName + " | " + Input.Keys.toString(this.keycode));
    }

    private void startListening() {
        this.setText("Press any key");
        this.isListeningKeys = true;
        this.getStage().setKeyboardFocus(this);
    }

    private void stopListening() {
        this.updateBindingText();
        this.isListeningKeys = false;
        this.getStage().setKeyboardFocus(null);
    }

    public interface KeyBindingCallback {
        public void handle(int keycode);
    }
}
