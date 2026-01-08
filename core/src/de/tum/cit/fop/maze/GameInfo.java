package de.tum.cit.fop.maze;

public class GameInfo {
    private GameSettings settings;
    private AchievementController achievementController;
    private String selectedLevel = "level-1.properties";
//    private final GameField gameField;

    public GameInfo() {
        this.settings = null; this.achievementController = null; // Will be initialized in initializeSettings()
    }

    public void initializeSettings() {
        this.settings = GameSettings.loadGameSettings();
    }

    public void initializeAchievements(){this.achievementController = AchievementController.loadAchievements();}

    public AchievementController getAchievementController(){
        if(this.achievementController == null){
            throw new IllegalStateException("AchievementController is not initialized");
        }
        return this.achievementController;
    }
    public void restoreDefaultAchievements(){
        this.achievementController = AchievementController.createDefaultAchievements();
    }

    public GameSettings getSettings() {
        if (this.settings == null) {
            throw new IllegalStateException("GameSettings is not initialized");
        }
        return this.settings;
    }

    public void restoreDefaultSettings() {
        this.settings = GameSettings.createDefaultSettings();
    }

    public String getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(String selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

//    public GameField getGameField() {
//        return this.gameField;
//    }
}
