package de.tum.cit.fop.maze.game_saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.game_saves.mementos.*;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Bouncer;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Junkie;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Tourist;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Static.Exit;
import de.tum.cit.fop.maze.objects.collectables.Heart;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.traps.FreezeTrap;
import de.tum.cit.fop.maze.objects.traps.GarlicTrap;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String STORY_SAVE_PATH = "data/save_story.json";
    private static final String ENDLESS_SAVE_PATH = "data/save_endless.json";

    public static void saveGame(GameScreenMemento memento, boolean isStoryMode) {
        String path = isStoryMode ? STORY_SAVE_PATH : ENDLESS_SAVE_PATH;
        FileHandle saveFile = Gdx.files.local(path);

        Json json = new Json();
        json.setUsePrototypes(false);
        saveFile.writeString(json.prettyPrint(memento), false);

        System.out.println("Game saved to: " + path);
    }

    public static GameScreenMemento loadGame(boolean isStoryMode) {
        String path = isStoryMode ? STORY_SAVE_PATH : ENDLESS_SAVE_PATH;
        FileHandle saveFile = Gdx.files.local(path);

        if (!saveFile.exists()) {
            System.out.println("No save file found at: " + path);
            return null;
        }

        Json json = new Json();
        GameScreenMemento memento = json.fromJson(GameScreenMemento.class, saveFile);
        System.out.println("Game loaded from: " + path);
        return memento;
    }

    public static boolean hasSave(boolean isStoryMode) {
        String path = isStoryMode ? STORY_SAVE_PATH : ENDLESS_SAVE_PATH;
        return Gdx.files.local(path).exists();
    }

    // Helper method to convert GameField to GameFieldMemento
    public static GameFieldMemento createGameFieldMemento(GameField gameField) {
        // Convert TileType[] to String[]
        String[] gridStrings = new String[gameField.getWidth() * gameField.getHeight()];
        for (int y = 0; y < gameField.getHeight(); y++) {
            for (int x = 0; x < gameField.getWidth(); x++) {
                TileType tile = gameField.getTile(x, y);
                gridStrings[y * gameField.getWidth() + x] = tile.name();
            }
        }

        // Convert GameObjects to GameObjectMementos
        List<GameObjectMemento> gameObjectMementos = new ArrayList<>();
        for (GameObject obj : gameField.getGameObjects()) {
            GameObjectMemento memento = createGameObjectMemento(obj);
            if (memento != null) {
                gameObjectMementos.add(memento);
            }
        }

        return new GameFieldMemento(
                gameField.getWidth(),
                gameField.getHeight(),
                gridStrings,
                gameObjectMementos,
                gameField.getPlayerStartPos().x,
                gameField.getPlayerStartPos().y
        );
    }

    // Helper method to create appropriate memento for each GameObject type
    private static GameObjectMemento createGameObjectMemento(GameObject obj) {
        if (obj instanceof Exit) {
            return ((Exit) obj).getMemento();
        } else if (obj instanceof Key) {
            return ((Key) obj).getMemento();
        } else if (obj instanceof Heart) {
            return ((Heart) obj).getMemento();
        } else if (obj instanceof Bouncer) {
            return ((Bouncer) obj).getMemento();
        } else if (obj instanceof Tourist) {
            return ((Tourist) obj).getMemento();
        } else if (obj instanceof Junkie) {
            return ((Junkie) obj).getMemento();
        } else if (obj instanceof FreezeTrap) {
            return ((FreezeTrap) obj).getMemento();
        } else if (obj instanceof GarlicTrap) {
            return ((GarlicTrap) obj).getMemento();
        }
        return null;
    }

    // Helper method to restore GameField from GameFieldMemento
    public static GameField restoreGameField(GameFieldMemento memento) {
        GameField gameField = new GameField(memento.width, memento.height);

        // Restore tiles
        for (int y = 0; y < memento.height; y++) {
            for (int x = 0; x < memento.width; x++) {
                String tileString = memento.grid[y * memento.width + x];
                TileType tileType = TileType.valueOf(tileString);
                gameField.setTile(x, y, tileType);
            }
        }

        // Restore player start position
        gameField.setPlayerStartPos(new Vector2(memento.playerStartPosX, memento.playerStartPosY));

        // Restore game objects
        for (GameObjectMemento objMemento : memento.gameObjects) {
            GameObject obj = restoreGameObject(objMemento);
            if (obj != null) {
                gameField.addGameObject(obj);
            }
        }

        return gameField;
    }

    // Helper method to restore GameObject from memento
    private static GameObject restoreGameObject(GameObjectMemento memento) {
        Vector2 pos = new Vector2(memento.positionX, memento.positionY);

        switch (memento.objectType) {
            case "Exit":
                ExitMemento exitMem = (ExitMemento) memento;
                Exit exit = new Exit(pos.x, pos.y);
                exit.loadFromMemento(exitMem);
                if (!exitMem.isLocked) {
                    exit.open();
                }
                return exit;

            case "Key":
                Key key = new Key(pos);
                return key;

            case "Heart":
                Heart heart = new Heart(pos);
                return heart;

            case "Bouncer":
                BouncerMemento bouncerMem = (BouncerMemento) memento;
                Bouncer bouncer = new Bouncer(pos);
                bouncer.loadFromMemento(bouncerMem);
                return bouncer;

            case "Tourist":
                TouristMemento touristMem = (TouristMemento) memento;
                Tourist tourist = new Tourist(pos);
                tourist.loadFromMemento(touristMem);
                return tourist;

            case "Junkie":
                JunkieMemento junkieMem = (JunkieMemento) memento;
                Junkie junkie = new Junkie(pos);
                junkie.loadFromMemento(junkieMem);
                return junkie;

            case "FreezeTrap":
                FreezeTrap freezeTrap = new FreezeTrap(pos);
                return freezeTrap;

            case "GarlicTrap":
                GarlicTrap garlicTrap = new GarlicTrap(pos);
                return garlicTrap;

            default:
                System.err.println("Unknown GameObject type: " + memento.objectType);
                return null;
        }
    }
}
