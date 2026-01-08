package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;

public class AchievementController{
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
    public static  AchievementController loadAchievements(){
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);
        if(!achievements_file.exists()){
            System.err.println("Could not find achievements file!");
            return createDefaultAchievements();
        }
        Json json = new Json();
        return json.fromJson(AchievementController.class, achievements_file);
    }

    public static  AchievementController createDefaultAchievements(){
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);

        AchievementController achievementController = new AchievementController();

        Json json = new Json();
        achievements_file.writeString(json.toJson(achievementController), false);
        return achievementController;
    }

    private void saveToJson(){
        FileHandle achievements_file = Gdx.files.local(JSON_PATH);

        Json json = new Json();
        achievements_file.writeString(json.toJson(this), false);
    }



    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
        saveToJson();
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
        saveToJson();
    }

    public boolean isFirstBlood() {
        return firstBlood;
    }

    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
        saveToJson();
    }

    public boolean isUpgrades() {
        return upgrades;
    }

    public void setUpgrades(boolean upgrades) {
        this.upgrades = upgrades;
        saveToJson();
    }

    public boolean isKills() {
        return kills;
    }

    public void setKills(boolean kills) {
        this.kills = kills;
        saveToJson();
    }

    public boolean isInvis() {
        return invis;
    }

    public void setInvis(boolean invis) {
        this.invis = invis;
        saveToJson();
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
        saveToJson();
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
        saveToJson();
    }



    public int getKillsAmount() {
        return killsAmount;
    }

    public void setKillsAmount(int killsAmount) {
        this.killsAmount = killsAmount;
        saveToJson();
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    public void setPointsNumber(int pointsNumber) {
        this.pointsNumber = pointsNumber;
        saveToJson();
    }
}
