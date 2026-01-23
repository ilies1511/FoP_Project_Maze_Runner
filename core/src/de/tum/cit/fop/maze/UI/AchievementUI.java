package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class AchievementUI {
    private final Stage stage;
    private final MazeRunnerGame game;
    private final Table table;
    private final Label label;
    private final Label text;
    private final SoundController soundController;
    private Cell<Actor> trophy;
    private Image image;

    public AchievementUI(Stage stage, MazeRunnerGame game, SoundController soundController) {
        this.stage = stage;
        this.game = game;
        this.soundController = soundController;

        table = new Table();
        //table.setFillParent(true);
        stage.addActor(table);
        trophy = new Cell<>();
        image = new Image(game.getTrophy());
        table.add(image).width(100).height(90).top();
        table.row();
        table.setWidth(550);
        table.setHeight(250);
        table.setPosition(stage.getWidth() - table.getWidth(), 0);
        TextureRegionDrawable signDrawable = new TextureRegionDrawable(game.getWoodenSign());
        table.setColor(Color.GRAY);
        table.setBackground(signDrawable);
        label = new Label("", game.getSkin(), "vampire-style");
        label.setFontScale(1.5f);
        table.add(label).center();
        text = new Label("", game.getSkin(), "vampire-style");
        text.setFontScale(1f);
        table.row();
        table.add(text);
        table.setVisible(false);
    }

    public void startAchievement() {
        if (!game.getGameInfo().getAchievementController().isStart()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getStartAchievement());
            trophy.setActor(image);
            label.setText("Wake up");
            text.setText("Start your first story mode game");
            game.getGameInfo().getAchievementController().setStart(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void endAchievement() {
        if (!game.getGameInfo().getAchievementController().isEnd()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getEndAchievement());
            trophy.setActor(image);
            label.setText("And so it ends");
            text.setText("Finish the story mode");
            game.getGameInfo().getAchievementController().setEnd(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void firstBloodAchievement() {
        if (!game.getGameInfo().getAchievementController().isFirstBlood()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getFirstBloodAchievement());
            trophy.setActor(image);
            label.setText("First blood");
            text.setText("Get your first kill");
            game.getGameInfo().getAchievementController().setFirstBlood(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void upgradesAchievement() {
        if (!game.getGameInfo().getAchievementController().isUpgrades()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getUpgradesAchievement());
            trophy.setActor(image);
            label.setText("Graph Dracula");
            text.setText("Reach maximum upgrades");
            game.getGameInfo().getAchievementController().setUpgrades(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void killsAchievement() {
        if (!game.getGameInfo().getAchievementController().isKills()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getKillsAchievement());
            trophy.setActor(image);
            label.setText("Thirst for blood");
            text.setText("50 kills");
            game.getGameInfo().getAchievementController().setKills(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void invisAchievement() {
        if (!game.getGameInfo().getAchievementController().isInvis()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getInvisAchievement());
            trophy.setActor(image);
            label.setText("Now you see me,\nnow you don’t");
            label.setAlignment(Align.center);
            text.setText("Become invisible for the first time");
            game.getGameInfo().getAchievementController().setInvis(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void consoleAchievement() {
        if (!game.getGameInfo().getAchievementController().isConsole()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getConsoleAchievement());
            trophy.setActor(image);
            label.setText("Cheater!");
            text.setText("Use a console command");
            game.getGameInfo().getAchievementController().setConsole(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    public void pointsAchievement() {
        if (!game.getGameInfo().getAchievementController().isPoints()) {
            table.setVisible(true);
            trophy = table.getCell(image);
            image = new Image(game.getPointsAchievement());
            trophy.setActor(image);
            label.setText("Gamer");
            text.setText("Reach 1000 points in endless mode"); //TODO Nikita
            game.getGameInfo().getAchievementController().setPoints(true);
            soundController.playWinSound(0.2f);
            table.clearActions();
            table.addAction(Actions.sequence(
                    Actions.delay(10f),
                    Actions.visible(false)
            ));
        }
    }

    private void hideAchievement() {
        table.setVisible(false);
    }
}
