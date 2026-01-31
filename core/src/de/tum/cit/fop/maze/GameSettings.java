package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Manages game settings and key bindings.
 * It handles loading and saving settings to a JSON file.
 */
public class GameSettings {
    private static final String JSON_PATH = "data/settings.json";

    /** Maximum health of the player. */
    public int maxHealth;
    /** Key binding for moving up. */
    public int upKey;
    /** Key binding for moving left. */
    public int leftKey;
    /** Key binding for moving down. */
    public int downKey;
    /** Key binding for moving right. */
    public int rightKey;
    /** Key binding for attacking. */
    public int attackKey;
    /** Key binding for dashing. */
    public int dashKey;
    /** Key binding for zooming in. */
    public int zoomInKey;
    /** Key binding for zooming out. */
    public int zoomOutKey;
    /** Key binding for interacting. */
    public int interactKey;
    /** Key binding for using ability. */
    public int abilityKey;
    /** Key binding for opening the console. */
    public int consoleKey;
    /** Key binding for opening the progression tree. */
    public int progressionTreeKey;
    /** Screen resolution height. */
    public int resolutionHeight;
    /** Screen resolution width. */
    public int resolutionWidth;

    /**
     * Creates a new GameSettings instance with default values.
     */
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
        this.resolutionWidth = 1500;
        this.resolutionHeight = 1200;
    }

    /**
     * Loads game settings from the local JSON file.
     * If the file is missing, default settings are created.
     * @return The loaded or newly created GameSettings instance.
     */
    static public GameSettings loadGameSettings() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        if (!settings_file.exists()) {
            System.err.println("Could not find settings file!");
            return createDefaultSettings();
        }

        Json json = new Json();
        return json.fromJson(GameSettings.class, settings_file);
    }

    /**
     * Creates default game settings and saves them to the JSON file.
     * @return The newly created GameSettings instance.
     */
    public static GameSettings createDefaultSettings() {
        // can be used to restore default settings
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        GameSettings gameSettings = new GameSettings();

        Json json = new Json();
        settings_file.writeString(json.toJson(gameSettings), false);
        return gameSettings;
    }

    // Key getters/setters

    /**
     * Gets the maximum health setting.
     * @return The max health value.
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Sets the maximum health and saves settings.
     * @param maxHealth The new max health value.
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        saveToJson();
    }

    /**
     * Gets the up movement key code.
     * @return The key code for moving up.
     */
    public int getUpKey() {
        return this.upKey;
    }

    /**
     * Sets the up movement key code and saves settings.
     * @param upKey The new key code.
     */
    public void setUpKey(int upKey) {
        this.upKey = upKey;
        saveToJson();
    }

    /**
     * Gets the left movement key code.
     * @return The key code for moving left.
     */
    public int getLeftKey() {
        return this.leftKey;
    }

    /**
     * Sets the left movement key code and saves settings.
     * @param leftKey The new key code.
     */
    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
        saveToJson();
    }

    /**
     * Gets the down movement key code.
     * @return The key code for moving down.
     */
    public int getDownKey() {
        return this.downKey;
    }

    /**
     * Sets the down movement key code and saves settings.
     * @param downKey The new key code.
     */
    public void setDownKey(int downKey) {
        this.downKey = downKey;
        saveToJson();
    }

    /**
     * Gets the right movement key code.
     * @return The key code for moving right.
     */
    public int getRightKey() {
        return this.rightKey;
    }

    /**
     * Sets the right movement key code and saves settings.
     * @param rightKey The new key code.
     */
    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
        saveToJson();
    }

    /**
     * Gets the progression tree key code.
     * @return The key code for opening the progression tree.
     */
    public int getProgressionTreeKey() {
        return progressionTreeKey;
    }

    /**
     * Sets the progression tree key code.
     * @param progressionTreeKey The new key code.
     */
    public void setProgressionTreeKey(int progressionTreeKey) {
        this.progressionTreeKey = progressionTreeKey;
    }

    /**
     * Gets the dash key code.
     * @return The key code for dashing.
     */
    public int getDashKey() {
        return this.dashKey;
    }

    /**
     * Sets the dash key code and saves settings.
     * @param dashKey The new key code.
     */
    public void setDashKey(int dashKey) {
        this.dashKey = dashKey;
        saveToJson();
    }

    /**
     * Gets the attack key code.
     * @return The key code for attacking.
     */
    public int getAttackKey() {
        return this.attackKey;
    }

    /**
     * Sets the attack key code and saves settings.
     * @param attackKey The new key code.
     */
    public void setAttackKey(int attackKey) {
        this.attackKey = attackKey;
        saveToJson();
    }

    /**
     * Gets the zoom in key code.
     * @return The key code for zooming in.
     */
    public int getZoomInKey() {
        return this.zoomInKey;
    }

    /**
     * Sets the zoom in key code and saves settings.
     * @param zoomInKey The new key code.
     */
    public void setZoomInKey(int zoomInKey) {
        this.zoomInKey = zoomInKey;
        saveToJson();
    }

    /**
     * Gets the interact key code.
     * @return The key code for interacting.
     */
    public int getInteractKey() {
        return interactKey;
    }

    /**
     * Sets the interact key code.
     * @param interactKey The new key code.
     */
    public void setInteractKey(int interactKey) {
        this.interactKey = interactKey;
    }

    /**
     * Gets the ability key code.
     * @return The key code for using ability.
     */
    public int getAbilityKey() {
        return abilityKey;
    }

    /**
     * Sets the ability key code.
     * @param abilityKey The new key code.
     */
    public void setAbilityKey(int abilityKey) {
        this.abilityKey = abilityKey;
    }

    /**
     * Gets the console key code.
     * @return The key code for opening the console.
     */
    public int getConsoleKey() {
        return consoleKey;
    }

    /**
     * Sets the console key code.
     * @param consoleKey The new key code.
     */
    public void setConsoleKey(int consoleKey) {
        this.consoleKey = consoleKey;
    }

    /**
     * Gets the resolution height.
     * @return The resolution height in pixels.
     */
    public int getResolutionHeight() {
        return resolutionHeight;
    }

    /**
     * Sets the resolution height.
     * @param resolutionHeight The new height in pixels.
     */
    public void setResolutionHeight(int resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    /**
     * Gets the resolution width.
     * @return The resolution width in pixels.
     */
    public int getResolutionWidth() {
        return resolutionWidth;
    }

    /**
     * Sets the resolution width.
     * @param getResolutionWidth The new width in pixels.
     */
    public void setResolutionWidth(int getResolutionWidth) {
        this.resolutionWidth = getResolutionWidth;
    }

    /**
     * Gets the zoom out key code.
     * @return The key code for zooming out.
     */
    public int getZoomOutKey() {
        return this.zoomOutKey;
    }

    /**
     * Sets the zoom out key code.
     * @param zoomOutKey The new key code.
     */
    public void setZoomOutKey(int zoomOutKey) {
        this.zoomOutKey = zoomOutKey;
    }

    private void saveToJson() {
        FileHandle settings_file = Gdx.files.local(JSON_PATH);

        Json json = new Json();
        settings_file.writeString(json.toJson(this), false);
    }
}
