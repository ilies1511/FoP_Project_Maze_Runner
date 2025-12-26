package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.objects.Key;

public class BackgroundStoryScreen implements Screen {
    private Label storyLabel;
    private String fullText = "Wake up child...     " +
            "\nYou have been away for too long.     " +
            "\nWho am I? I am your inner dark urge, the one you can trust above all others.     " +
            "\nWake up and make up for 1,000 years spent in this prison, and yearn for blood. But first and foremost, you must find your way home, for your casket has strayed too far from Transylvania.     " +
            "\nI know you have questions, and I know you don't understand much right now, but trust me, your time has come. Wake up and reach the portal I have opened for you; it will take you home, to safety.     " +
            "\nHow, you may ask? It is close, but you must find a way to it, on the top floor of Berghain, where you have been sleeping. No more time to speak. Go on, spill the blood of mortals.     " +
            "\n(Press Space to continue)";
    private float timeSeconds = 0f;
    private Stage stage;
    private boolean isTextSkipped = false;
    private final MazeRunnerGame game;
    private final SoundController soundController;

    public BackgroundStoryScreen( MazeRunnerGame game) {
        soundController = new SoundController();
        soundController.playBackgrounStorydMusic(0.5f);
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1f; // Set camera zoom for a closer view

        Viewport viewport = new FitViewport(2000, 1200); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table();
        storyLabel = new Label("", game.getSkin(), "vampire-style");
        storyLabel.setFontScale(1.7f);
        storyLabel.setWrap(true);
        storyLabel.setAlignment(Align.topLeft);
        table.setFillParent(true);
        table.center();
        table.add(storyLabel).width(1500f);
        stage.addActor(table);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isTextSkipped){
            storyLabel.setText(fullText);
            isTextSkipped = true;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isTextSkipped){
            soundController.stopBackgroundStoryMusic();
            game.goToGame();
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


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {

    }
}
