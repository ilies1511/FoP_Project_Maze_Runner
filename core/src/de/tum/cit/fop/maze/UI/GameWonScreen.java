package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class GameWonScreen implements Screen {
	private final MazeRunnerGame game;
	private final Stage stage;
	private final SoundController soundController;

	public GameWonScreen(MazeRunnerGame game) {
		this.game = game;

		this.soundController = new SoundController();
		soundController.playWinSound(0.5f);

		Viewport viewport = new FitViewport(2000, 1200);
		this.stage = new Stage(viewport, game.getSpriteBatch());

		Table table = new Table();
		table.setFillParent(true);
		table.center();

		Label label = new Label("Game Won", game.getSkin(), "vampire-style");
		label.setFontScale(1.7f);
		table.add(label).center().padTop(50).padBottom(80).row();

		TextButton goToMainMenu = new TextButton("Exit", game.getSkin(), "vampire-style");
		goToMainMenu.getLabel().setTouchable(Touchable.disabled);

		goToMainMenu.addListener(new ClickListener(){
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1 && !goToMainMenu.isDisabled()) {
					soundController.playUI_hoverOverSound(1);
				}
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				soundController.playUI_buttonClickSound(1);
			}
		});

		goToMainMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soundController.dispose();
				game.goToMenu();
			}
		});

		table.add(goToMainMenu).width(300).padBottom(50).center();
		stage.addActor(table);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCursorCatched(false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
		soundController.dispose();
	}

	@Override public void hide() {
		if (soundController != null) {
			soundController.dispose();
		}
	}

	@Override public void pause() {}
	@Override public void resume() {}
}