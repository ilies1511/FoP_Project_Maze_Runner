package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class AchievementController{
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

    private Stage stage;
    private MazeRunnerGame game;
    private Table table;
    private Label label;
    private Label text;
    private SoundController soundController;

    public AchievementController(Stage stage, MazeRunnerGame game, SoundController soundController) {
        this.start = false;
        this.end = false;
        this.firstBlood = false;
        this.upgrades = false;
        this.kills = false;
        this.invis = false;
        this.console = false;
        this.points = false;

        this.stage = stage;
        this.game = game;
        this.soundController = soundController;

        table = new Table();
        //table.setFillParent(true);
        stage.addActor(table);

        table.setWidth(550);
        table.setHeight(250);
        table.setPosition(stage.getWidth() - table.getWidth(), 0);
        TextureRegionDrawable signDrawable = new TextureRegionDrawable(game.getWoodenSign());
        table.setColor(Color.GRAY);
        table.setBackground(signDrawable);
        label = new Label("", game.getSkin(), "vampire-style");
        label.setFontScale(1.5f);
        table.add(label).center();
        text= new Label("", game.getSkin(), "vampire-style");
        text.setFontScale(1f);
        table.row();
        table.add(text);
        table.setVisible(false);
    }

    public void startAchievement(){
        table.setVisible(true);
        label.setText("Wake up");
        text.setText("Start your first story mode game");
        start = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));


    }
    public void endAchievement(){
        table.setVisible(true);
        label.setText("And so it ends");
        text.setText("Finish the story mode");
        end = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void firstBloodAchievement(){
        table.setVisible(true);
        label.setText("First blood");
        text.setText("Get your first kill");
        firstBlood = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void upgradesAchievement(){
        table.setVisible(true);
        label.setText("Graph Dracula");
        text.setText("Reach maximum upgrades");
        upgrades = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void killsAchievement(){
        table.setVisible(true);
        label.setText("Thirst for blood");
        text.setText("50 kills");
        kills = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void invisAchievement(){
        table.setVisible(true);
        label.setText("Now you see me,\nnow you don’t");
        text.setText("Become invisible for the first time");
        invis = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void consoleAchievement(){
        table.setVisible(true);
        label.setText("Cheater!");
        text.setText("Use a console command");
        console = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }
    public void pointsAchievement(){
        table.setVisible(true);
        label.setText("Gamer");
        text.setText("Reach xyz points in endless mode"); //TODO
        points = true;
        soundController.playWinSound(0.2f);
        table.clearActions();
        table.addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.visible(false)
        ));
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isFirstBlood() {
        return firstBlood;
    }

    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
    }

    public boolean isUpgrades() {
        return upgrades;
    }

    public void setUpgrades(boolean upgrades) {
        this.upgrades = upgrades;
    }

    public boolean isKills() {
        return kills;
    }

    public void setKills(boolean kills) {
        this.kills = kills;
    }

    public boolean isInvis() {
        return invis;
    }

    public void setInvis(boolean invis) {
        this.invis = invis;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public boolean isPoints() {
        return points;
    }

    public void setPoints(boolean points) {
        this.points = points;
    }

    private void hideAchievement(){
        table.setVisible(false);
    }
}
