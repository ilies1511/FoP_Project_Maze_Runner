package de.tum.cit.fop.maze.game_saves.mementos;

import de.tum.cit.fop.maze.game_saves.Memento;

public class GameScreenMemento implements Memento {
    public GameFieldMemento gameField;
    public PlayerMemento player;
    public String gameMode; // STORY_MODE, ENDLESS_MODE, or LEVELS_MODE
    public int currentStoryLevelIndex;
    public String selectedLevel; // For LEVELS_MODE

    public GameScreenMemento() {
        // Required for JSON deserialization
    }

    public GameScreenMemento(GameFieldMemento gameField, PlayerMemento player,
                             String gameMode, int currentStoryLevelIndex, String selectedLevel) {
        this.gameField = gameField;
        this.player = player;
        this.gameMode = gameMode;
        this.currentStoryLevelIndex = currentStoryLevelIndex;
        this.selectedLevel = selectedLevel;
    }
}
