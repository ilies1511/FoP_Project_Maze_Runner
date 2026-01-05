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
    public int interactKey;
    public int abilityKey;
    public int consoleKey;
    public int progressionTreeKey;

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
        this.interactKey = Input.Keys.E;
        this.abilityKey = Input.Keys.Q;
        this.consoleKey = Input.Keys.SLASH;
        this.progressionTreeKey = Input.Keys.I;
    }

    static public GameSettings loadGameSettings() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        if (!settings_file.exists()) {
            System.err.println("Could not find settings file!");
            return createDefaultSettings();
        }

        Json json = new Json();
        return json.fromJson(GameSettings.class, settings_file);
    }

    public static GameSettings createDefaultSettings() {
        // can be used to restore default settings
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        GameSettings gameSettings = new GameSettings();

        Json json = new Json();
        settings_file.writeString(json.toJson(gameSettings), false);
        return gameSettings;
    }

    // Key getters/setters
    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        saveToJson();
    }

    public int getUpKey() {
        return this.upKey;
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
        saveToJson();
    }

    public int getLeftKey() {
        return this.leftKey;
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
        saveToJson();
    }

    public int getDownKey() {
        return this.downKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
        saveToJson();
    }

    public int getRightKey() {
        return this.rightKey;
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
        saveToJson();
    }

    public int getProgressionTreeKey() {
        return progressionTreeKey;
    }

    public void setProgressionTreeKey(int progressionTreeKey) {
        this.progressionTreeKey = progressionTreeKey;
    }

    public int getDashKey() {
        return this.dashKey;
    }

    public void setDashKey(int dashKey) {
        this.dashKey = dashKey;
        saveToJson();
    }

    public int getAttackKey() {
        return this.attackKey;
    }

    public void setAttackKey(int attackKey) {
        this.attackKey = attackKey;
        saveToJson();
    }

    public int getZoomInKey() {
        return this.zoomInKey;
    }

    public void setInteractKey(int interactKey) {
        this.interactKey = interactKey;
    }

    public void setAbilityKey(int abilityKey) {
        this.abilityKey = abilityKey;
    }

    public void setConsoleKey(int consoleKey) {
        this.consoleKey = consoleKey;
    }

    public int getInteractKey() {
        return interactKey;
    }

    public int getAbilityKey() {
        return abilityKey;
    }

    public int getConsoleKey() {
        return consoleKey;
    }

    public void setZoomInKey(int zoomInKey) {
        this.zoomInKey = zoomInKey;
        saveToJson();
    }

    public int getZoomOutKey() {
        return this.zoomOutKey;
    }

    public void setZoomOutKey(int zoomOutKey) {
        this.zoomOutKey = zoomOutKey;
    }

    private void saveToJson() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        Json json = new Json();
        settings_file.writeString(json.toJson(this), false);
    }
}
