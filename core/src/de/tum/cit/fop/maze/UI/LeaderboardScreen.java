package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Utils.ScoreManager;

import java.util.ArrayList;

public class LeaderboardScreen implements Screen {
	// BEGIN -- Attributes
	private final MazeRunnerGame game;
	private final Stage stage;
	// END -- Attributes

	// BEGIN -- Constructor
	public LeaderboardScreen(MazeRunnerGame game) {
		this.game = game;
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		Label title = new Label("High Scores", game.getSkin(), "vampire-style");
		title.setFontScale(1.5f);
		table.add(title).padBottom(30).row();

		ArrayList<Long> scores = ScoreManager.getInstance().getScores();
		if (scores.isEmpty()) {
			table.add(new Label("No scores yet!", game.getSkin(), "vampire-style")).padBottom(10).row();
		}
		else {
			for (int i = 0; i < scores.size(); i++) {
				String entry = (i + 1) + ". " + scores.get(i) + " Points";
				table.add(new Label(entry, game.getSkin(), "vampire-style")).padBottom(10).row();
			}
		}

		TextButton backButton = new TextButton("Back", game.getSkin(), "vampire-style");
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.goToMenu();
			}
		});
		table.add(backButton).padTop(30);
	}
	// END -- Constructor

	// BEGIN -- Getters
	// END -- Getters

	// BEGIN -- Setters
	// END -- Setters

	// BEGIN -- Methods
	// END -- Methods

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	// Unused methods
	@Override
	public void show() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}
}
