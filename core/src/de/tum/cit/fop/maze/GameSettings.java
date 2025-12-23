package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class GameSettings {
    private static final String JSON_PATH = "data/settings.json";

    public int maxHealth;
    public int upKey;
    public int leftKey;
    public int downKey;
    public int rightKey;
    public int attackKey;
    public int dashKey;
    public int zoomInKey;
    public int zoomOutKey;

    static public GameSettings loadGameSettings() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        if (!settings_file.exists()) {
            System.err.println("Could not find settings file!");
            return createDefaultSettings();
        }

        Json json = new Json();
        return json.fromJson(GameSettings.class, settings_file);
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getUpKey() {
        return this.upKey;
    }

    public int getLeftKey() {
        return this.leftKey;
    }

    public int getDownKey() {
        return this.downKey;
    }

    public int getRightKey() {
        return this.rightKey;
    }

    public int getDashKey() {
        return this.dashKey;
    }

    public int getAttackKey() {
        return this.attackKey;
    }

    public int getZoomInKey() {
        return this.zoomInKey;
    }
    public int getZoomOutKey() {
        return this.zoomOutKey;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        saveToJson();
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
        saveToJson();
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
        saveToJson();
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
        saveToJson();
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
        saveToJson();
    }

    public void setDashKey(int dashKey) {
        this.dashKey = dashKey;
        saveToJson();
    }

    public void setAttackKey(int attackKey) {
        this.attackKey = attackKey;
        saveToJson();
    }

    public void setZoomInKey(int zoomInKey) {
        this.zoomInKey = zoomInKey;
        saveToJson();
    }

    public void setZoomOutKey(int zoomOutKey) {
        this.zoomOutKey = zoomOutKey;
    }

    public GameSettings() {
        this.maxHealth = 10;
        this.upKey = Input.Keys.W;
        this.leftKey = Input.Keys.A;
        this.downKey = Input.Keys.S;
        this.rightKey = Input.Keys.D;
        this.attackKey = Input.Keys.SPACE;
        this.dashKey = Input.Keys.SHIFT_LEFT;
        this.zoomInKey = Input.Keys.EQUALS;
        this.zoomOutKey = Input.Keys.MINUS;
    }

    private void saveToJson() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        Json json = new Json();
        settings_file.writeString(json.toJson(this), false);
    }

    private static GameSettings createDefaultSettings() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        GameSettings gameSettings = new GameSettings();

        Json json = new Json();
        settings_file.writeString(json.toJson(gameSettings), false);
        return gameSettings;
    }
}
