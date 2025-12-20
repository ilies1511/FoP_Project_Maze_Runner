package de.tum.cit.fop.maze;

public class GameInfo {
    private final GameSettings settings;
    private final GameField gameField;

    public  GameInfo() {
        this.settings = new GameSettings();
        this.gameField = new GameField();
    }

    public GameSettings getSettings() {
        return this.settings;
    }

    public GameField getGameField() {
        return this.gameField;
    }
}
