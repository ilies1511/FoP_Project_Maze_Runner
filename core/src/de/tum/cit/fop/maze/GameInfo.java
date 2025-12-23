package de.tum.cit.fop.maze;

public class GameInfo {
    private GameSettings settings;
//    private final GameField gameField;

    public  GameInfo() {
        this.settings = null; // Will be initialized in initializeSettings()
    }

    public void initializeSettings() {
        this.settings = GameSettings.loadGameSettings();
    }

    public GameSettings getSettings() {
        if (this.settings == null) {
            throw new IllegalStateException("GameSettings is not initialized");
        }
        return this.settings;
    }

//    public GameField getGameField() {
//        return this.gameField;
//    }
}
