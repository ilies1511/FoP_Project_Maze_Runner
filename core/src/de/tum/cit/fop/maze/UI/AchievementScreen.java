package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class AchievementScreen {
    private final Table table;
    private final SoundController soundController;
    private final MenuScreen menuScreen;

    public AchievementScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        soundController = menuScreen.getSoundController();
        this.menuScreen = menuScreen;
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.top();
        Label label = new Label("Achievements", game.getSkin(), "vampire-style");
        label.setFontScale(3f);
        table.add(label).colspan(4).expandX().center().padTop(50).padBottom(200).row();
        Table table1 = new Table();
        Image image1 = new Image(game.getStartAchievement());
        table1.add(image1).row();
        Label label1 = new Label("Wake up\n(Start your first story mode game)", game.getSkin(), "vampire-style");
        label1.setFontScale(1f);
        label1.setAlignment(Align.center);
        table1.add(label1).center().expandX();
        Table table2 = new Table();
        Image image2 = new Image(game.getEndAchievement());
        table2.add(image2).row();
        Label label2 = new Label("And so it ends\n(Finish the story mode)", game.getSkin(), "vampire-style");
        label2.setFontScale(1f);
        label2.setAlignment(Align.center);
        table2.add(label2).center().expandX();
        Table table3 = new Table();
        Image image3 = new Image(game.getFirstBloodAchievement());
        table3.add(image3).row();
        Label label3 = new Label("First blood\n(Get your first kill)", game.getSkin(), "vampire-style");
        label3.setFontScale(1f);
        label3.setAlignment(Align.center);
        table3.add(label3).center().expandX();
        Table table4 = new Table();
        Image image4 = new Image(game.getUpgradesAchievement());
        table4.add(image4).row();
        Label label4 = new Label("Graph Dracula\n(Reach maximum upgrades)", game.getSkin(), "vampire-style");
        label4.setFontScale(1f);
        label4.setAlignment(Align.center);
        table4.add(label4).center().expandX();
        Table table5 = new Table();
        Image image5 = new Image(game.getKillsAchievement());
        table5.add(image5).row();
        Label label5 = new Label("Thirst for blood\n(50 kills)", game.getSkin(), "vampire-style");
        label5.setFontScale(1f);
        label5.setAlignment(Align.center);
        table5.add(label5).center().expandX();
        Table table6 = new Table();
        Image image6 = new Image(game.getInvisAchievement());
        table6.add(image6).row();
        Label label6 = new Label("Now you see me,\nnow you don’t\n(Become invisible for the first time)", game.getSkin(), "vampire-style");
        label6.setFontScale(1f);
        label6.setAlignment(Align.center);
        table6.add(label6).center().expandX();
        Table table7 = new Table();
        Image image7 = new Image(game.getConsoleAchievement());
        table7.add(image7).row();
        Label label7 = new Label("Cheater!\n(Use a console command)", game.getSkin(), "vampire-style");
        label7.setFontScale(1f);
        label7.setAlignment(Align.center);
        table7.add(label7).center().expandX();
        Table table8 = new Table();
        Image image8 = new Image(game.getPointsAchievement());
        table8.add(image8).row();
        Label label8 = new Label("Gamer\n(Reach 1000 points in endless mode)", game.getSkin(), "vampire-style");//TODO Nikita
        label8.setFontScale(1f);
        label8.setAlignment(Align.center);
        table8.add(label8).center().expandX();
        table.add(table1);
        table.add(table2);
        table.add(table3);
        table.add(table4);
        table.row();
        table.add(table5);
        table.add(table6);
        table.add(table7);
        table.add(table8);
        if (!game.getGameInfo().getAchievementController().isStart()) {
            image1.setColor(Color.GRAY);
            label1.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isEnd()) {
            image2.setColor(Color.GRAY);
            label2.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isFirstBlood()) {
            image3.setColor(Color.GRAY);
            label3.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isUpgrades()) {
            image4.setColor(Color.GRAY);
            label4.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isKills()) {
            image5.setColor(Color.GRAY);
            label5.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isInvis()) {
            image6.setColor(Color.GRAY);
            label6.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isConsole()) {
            image7.setColor(Color.GRAY);
            label7.setColor(Color.GRAY);
        }
        if (!game.getGameInfo().getAchievementController().isPoints()) {
            image8.setColor(Color.GRAY);
            label8.setColor(Color.GRAY);
        }
        table.row();
        TextButton restoreDefaultAchievementsButton = new TextButton("Delete achievements", game.getSkin(), "vampire-style");
        table.add(restoreDefaultAchievementsButton).width(400).colspan(4).padTop(100).center().row();
        restoreDefaultAchievementsButton.getLabel().setTouchable(Touchable.disabled);
        restoreDefaultAchievementsButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !restoreDefaultAchievementsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        restoreDefaultAchievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getGameInfo().restoreDefaultAchievements();
                image1.setColor(Color.GRAY);
                label1.setColor(Color.GRAY);
                image2.setColor(Color.GRAY);
                label2.setColor(Color.GRAY);
                image3.setColor(Color.GRAY);
                label3.setColor(Color.GRAY);
                image4.setColor(Color.GRAY);
                label4.setColor(Color.GRAY);
                image5.setColor(Color.GRAY);
                label5.setColor(Color.GRAY);
                image6.setColor(Color.GRAY);
                label6.setColor(Color.GRAY);
                image7.setColor(Color.GRAY);
                label7.setColor(Color.GRAY);
                image8.setColor(Color.GRAY);
                label8.setColor(Color.GRAY);
            }
        });
        TextButton goBackToMenu = new TextButton("Back To Menu", game.getSkin(), "vampire-style");
        table.add(goBackToMenu).width(400).colspan(4).padTop(50).center().row();
        goBackToMenu.getLabel().setTouchable(Touchable.disabled);
        goBackToMenu.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goBackToMenu.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        goBackToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backToMenu();
            }
        });
        table.setVisible(false);
    }

    public Table getTable() {
        return table;
    }

    private void backToMenu() {
        table.setVisible(false);
        menuScreen.goBackToMenu();
    }
}
