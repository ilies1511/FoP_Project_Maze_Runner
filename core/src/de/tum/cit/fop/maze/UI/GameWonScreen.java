package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class GameWonScreen implements Screen {
	private final MazeRunnerGame game;
	private final Stage stage;
	private final SoundController soundController;
    private Table table;
    private Table storyTable;
    private Label storyLabel;
    private String fullText = "\"Breathe in the mist, child.\n" +
            "The stench of that prison is but a memory now.\n" +
            "Do you recognize this cold air? You shall step through the veil and return to the freezing peaks of Transylvania, where you belong.\n" +
            "You have done well to heed my voice.\n" +
            "You carved a path through the mortals who dared to keep you caged, and washed away the dust of a thousand years with their blood.\n" +
            "The portal will close behind you, and your exile will finally be at an end.\n" +
            "\n" +
            "The moon—it shines brighter there. Your true casket awaits in the halls of your ancestors.\n" +
            "No more hiding in the shadows of the living. You have returned to your throne, and the long night is yours to command. Rest now, for tomorrow... we feast.\"";
    private float timeSeconds = 0f;
    private boolean isTextSkipped = false;
    private Texture backgroundTexture;

	public GameWonScreen(MazeRunnerGame game) {
		this.game = game;

		this.soundController = new SoundController();
		soundController.playWinSound(0.5f);

		Viewport viewport = new FitViewport(2000, 1200);
		this.stage = new Stage(viewport, game.getSpriteBatch());

        switch (game.getGameMode()){
            case STORY_MODE:
                Gdx.input.setCursorCatched(true);
                Gdx.input.setInputProcessor(null);
                storyTable = new Table();
                storyLabel = new Label("", game.getSkin(), "vampire-style");
                storyLabel.setFontScale(1.7f);
                storyLabel.setWrap(true);
                storyLabel.setAlignment(Align.topLeft);
                storyTable.setFillParent(true);
                storyTable.center();
                backgroundTexture = new Texture(Gdx.files.internal("gameWonBackground.png"));
                backgroundTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

                TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

                storyTable.setBackground(backgroundDrawable);
                storyTable.add(storyLabel).width(1500f);

                stage.addActor(storyTable);
                break;
            case LEVELS_MODE:
                table = new Table();
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
            break;
        }
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCursorCatched(false);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(game.getGameMode() == GameMode.STORY_MODE){


            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isTextSkipped){
                storyLabel.setText(fullText);
                isTextSkipped = true;
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isTextSkipped){
                soundController.dispose();
                game.goToMenu();
            }

            if (storyLabel.getText().length < fullText.length()) {
                timeSeconds += delta;
                int charCount = (int) (timeSeconds / 0.1f);
                if (charCount > fullText.length()) {
                    charCount = fullText.length();
                }
                storyLabel.setText(fullText.substring(0, charCount));
            }
            else{
                isTextSkipped = true;
            }
        }
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