package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.Utils.ScoreManager;

import java.util.ArrayList;

public class LeaderboardScreen {
    private Table table;
    private SoundController soundController;
    private MenuScreen menuScreen;

    public LeaderboardScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
        this.soundController = menuScreen.getSoundController();
        
        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        Label title = new Label("High Scores", game.getSkin(), "vampire-style");
        title.setFontScale(3.0f);
        table.add(title).expandX().center().padTop(50).padBottom(80).row();

        ArrayList<Long> scores = ScoreManager.getInstance().getScores();
        if (scores.isEmpty()) {
            Label noScoresLabel = new Label("No scores yet!", game.getSkin(), "vampire-style");
            noScoresLabel.setFontScale(1.2f);
            table.add(noScoresLabel).padBottom(30).row();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                String entry = (i + 1) + ". " + scores.get(i) + " Points";
                Label scoreLabel = new Label(entry, game.getSkin(), "vampire-style");
                scoreLabel.setFontScale(1.0f);
                table.add(scoreLabel).padBottom(10).row();
            }
        }

        TextButton backButton = new TextButton("Back", game.getSkin(), "vampire-style");
        table.add(backButton).width(300).center().padTop(30);
        backButton.getLabel().setTouchable(Touchable.disabled);
        
        backButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !backButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBackToEndlessMode();
            }
        });
    }

    private void goBackToEndlessMode() {
        table.setVisible(false);
        menuScreen.goToEndless();
    }

    public Table getLeaderboardTable() {
        return table;
    }
}
