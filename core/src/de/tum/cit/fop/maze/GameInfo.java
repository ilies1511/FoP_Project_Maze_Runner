package de.tum.cit.fop.maze;

/**
 * Holds global game information, including settings and achievement controllers.
 * It serves as a central access point for persistent game state.
 */
public class GameInfo {
    private GameSettings settings;
    private AchievementController achievementController;
    private String selectedLevel = "level-1.properties";

    /**
     * Creates a new GameInfo instance.
     * Settings and achievement controller are initially null and must be initialized.
     */
    public GameInfo() {
        this.settings = null;
        this.achievementController = null; // Will be initialized in initializeSettings()
    }

    /**
     * Initializes the game settings by loading them from storage.
     */
    public void initializeSettings() {
        this.settings = GameSettings.loadGameSettings();
    }

    /**
     * Initializes the achievement controller by loading achievements from storage.
     */
    public void initializeAchievements() {
        this.achievementController = AchievementController.loadAchievements();
    }

    /**
     * Gets the achievement controller.
     * @return The initialized AchievementController.
     * @throws IllegalStateException if the controller has not been initialized.
     */
    public AchievementController getAchievementController() {
        if (this.achievementController == null) {
            throw new IllegalStateException("AchievementController is not initialized");
        }
        return this.achievementController;
    }

    /**
     * Restores the default achievements, resetting progress.
     */
    public void restoreDefaultAchievements() {
        this.achievementController = AchievementController.createDefaultAchievements();
    }

    /**
     * Gets the game settings.
     * @return The initialized GameSettings.
     * @throws IllegalStateException if settings have not been initialized.
     */
    public GameSettings getSettings() {
        if (this.settings == null) {
            throw new IllegalStateException("GameSettings is not initialized");
        }
        return this.settings;
    }

    /**
     * Restores default game settings.
     */
    public void restoreDefaultSettings() {
        this.settings = GameSettings.createDefaultSettings();
    }

    /**
     * Gets the path of the currently selected level.
     * @return The file path of the selected level.
     */
    public String getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * Sets the path for the selected level.
     * @param selectedLevel The file path of the level to select.
     */
    public void setSelectedLevel(String selectedLevel) {
        this.selectedLevel = selectedLevel;
    }
}
