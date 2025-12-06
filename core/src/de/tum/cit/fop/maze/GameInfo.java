package de.tum.cit.fop.maze;

public class GameInfo {
    private final GameSettings settings;

    public  GameInfo() {
        this.settings = new GameSettings();
    }

    public GameSettings getSettings() {
        return this.settings;
    }
}
