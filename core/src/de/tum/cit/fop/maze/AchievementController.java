package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Manages the achievements in the game, including loading, saving, and updating their status.
 * It handles the persistence of achievement data to a JSON file.
 */
public class AchievementController {
    private static final String JSON_PATH = "data/achievements.json";

    private boolean start;
    private boolean end;
    private boolean firstBlood;
    private boolean upgrades;
    private boolean kills;
    private boolean invis;
    private boolean console;
    private boolean points;

    private int killsAmount;
    private int pointsNumber;


    /**
     * Default constructor initializing all achievements to false and counts to zero.
     */
    public AchievementController() {
        this.start = false;
        this.end = false;
        this.firstBlood = false;
        this.upgrades = false;
        this.kills = false;
        this.invis = false;
        this.console = false;
        this.points = false;
    }

    /**
     * Loads achievements from the local JSON file.
     * If the file does not exist, it creates default achievements.
     * @return The loaded or newly created AchievementController instance.
     */
    public static AchievementController loadAchievements() {
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);
        if (!achievements_file.exists()) {
            System.err.println("Could not find achievements file!");
            return createDefaultAchievements();
        }
        Json json = new Json();
        return json.fromJson(AchievementController.class, achievements_file);
    }

    /**
     * Creates a new AchievementController with default values and saves it to the JSON file.
     * @return The newly created AchievementController instance.
     */
    public static AchievementController createDefaultAchievements() {
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);

        AchievementController achievementController = new AchievementController();

        Json json = new Json();
        achievements_file.writeString(json.toJson(achievementController), false);
        return achievementController;
    }

    private void saveToJson() {
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);

        Json json = new Json();
        achievements_file.writeString(json.toJson(this), false);
    }


    /**
     * Checks if the start achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Sets the start achievement status and saves to JSON.
     * @param start The new status of the start achievement.
     */
    public void setStart(boolean start) {
        this.start = start;
        saveToJson();
    }

    /**
     * Checks if the end achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * Sets the end achievement status and saves to JSON.
     * @param end The new status of the end achievement.
     */
    public void setEnd(boolean end) {
        this.end = end;
        saveToJson();
    }

    /**
     * Checks if the first blood achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isFirstBlood() {
        return firstBlood;
    }

    /**
     * Sets the first blood achievement status and saves to JSON.
     * @param firstBlood The new status of the first blood achievement.
     */
    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
        saveToJson();
    }

    /**
     * Checks if the upgrades achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isUpgrades() {
        return upgrades;
    }

    /**
     * Sets the upgrades achievement status and saves to JSON.
     * @param upgrades The new status of the upgrades achievement.
     */
    public void setUpgrades(boolean upgrades) {
        this.upgrades = upgrades;
        saveToJson();
    }

    /**
     * Checks if the kills achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isKills() {
        return kills;
    }

    /**
     * Sets the kills achievement status and saves to JSON.
     * @param kills The new status of the kills achievement.
     */
    public void setKills(boolean kills) {
        this.kills = kills;
        saveToJson();
    }

    /**
     * Checks if the invisibility achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isInvis() {
        return invis;
    }

    /**
     * Sets the invisibility achievement status and saves to JSON.
     * @param invis The new status of the invisibility achievement.
     */
    public void setInvis(boolean invis) {
        this.invis = invis;
        saveToJson();
    }

    /**
     * Checks if the console achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isConsole() {
        return console;
    }

    /**
     * Sets the console achievement status and saves to JSON.
     * @param console The new status of the console achievement.
     */
    public void setConsole(boolean console) {
        this.console = console;
        saveToJson();
    }

    /**
     * Checks if the points achievement is unlocked.
     * @return True if unlocked, false otherwise.
     */
    public boolean isPoints() {
        return points;
    }

    /**
     * Sets the points achievement status and saves to JSON.
     * @param points The new status of the points achievement.
     */
    public void setPoints(boolean points) {
        this.points = points;
        saveToJson();
    }


    /**
     * Gets the total number of kills.
     * @return The number of kills.
     */
    public int getKillsAmount() {
        return killsAmount;
    }

    /**
     * Sets the total number of kills and saves to JSON.
     * @param killsAmount The new kill count.
     */
    public void setKillsAmount(int killsAmount) {
        this.killsAmount = killsAmount;
        saveToJson();
    }

    /**
     * Gets the total number of points.
     * @return The number of points.
     */
    public int getPointsNumber() {
        return pointsNumber;
    }

    /**
     * Sets the total number of points and saves to JSON.
     * @param pointsNumber The new point count.
     */
    public void setPointsNumber(int pointsNumber) {
        this.pointsNumber = pointsNumber;
        saveToJson();
    }
}
