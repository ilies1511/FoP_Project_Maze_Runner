package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Gameplay.GameMode;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;
    private Table table;
    private final StoryModeScreen storyModeScreen;
    private final LevelModeScreen levelModeScreen;
    private final SettingsScreen settingsScreen;
    private final SoundController soundController;
    private final EndlessModeScreen endlessModeScreen;
    private final LeaderboardScreen leaderboardScreen;
    private final AchievementScreen achievementScreen;
	private MazeRunnerGame game;
    private Viewport viewport;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
		this.game = game;
        soundController = new SoundController();
        soundController.playMainMenuMusic(0.5f);


        viewport = new FitViewport(game.getGameInfo().getSettings().getResolutionHeight(), game.getGameInfo().getSettings().getResolutionWidth());
        stage = new Stage(viewport, game.getSpriteBatch());

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.top();

        Label label = new Label("Main Menu", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();




        TextButton goToStoryMode = new TextButton("Story Mode", game.getSkin(), "vampire-style");
        table.add(goToStoryMode).width(300).left().padLeft(50).padBottom(50);
        goToStoryMode.getLabel().setTouchable(Touchable.disabled);
        goToStoryMode.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToStoryMode.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        //TODO change story mode logic
        goToStoryMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToStory();
            }
        });


        table.row();
        TextButton goToLevels = new TextButton("Levels", game.getSkin(), "vampire-style");
        table.add(goToLevels).width(300).left().padLeft(50).padBottom(50);
        goToStoryMode.getLabel().setTouchable(Touchable.disabled);
        goToStoryMode.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToStoryMode.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        //TODO change levels logic
        goToLevels.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToLevel();
            }
        });


        table.row();

        TextButton goToEndlessButton = new TextButton("Endless Mode", game.getSkin(), "vampire-style");
        table.add(goToEndlessButton).width(300).left().padLeft(50).padBottom(50);
        goToEndlessButton.getLabel().setTouchable(Touchable.disabled);
        goToEndlessButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToEndlessButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();

        TextButton goToSettingsButton = new TextButton("Settings", game.getSkin(), "vampire-style");
        table.add(goToSettingsButton).width(300).left().padLeft(50).padBottom(50);
        goToSettingsButton.getLabel().setTouchable(Touchable.disabled);
        goToSettingsButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToSettingsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();



        TextButton goToExitButton = new TextButton("Exit", game.getSkin(), "vampire-style");
        table.add(goToExitButton).width(300).left().padLeft(50).padBottom(50);
        goToExitButton.getLabel().setTouchable(Touchable.disabled);
        goToExitButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToExitButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });

        table.row();

        Drawable trophy = new TextureRegionDrawable(game.getTrophy());
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = trophy;             // Image when not pressed
        style.down = trophy;           // Image when pressed (can be a different region)
        style.over = trophy;
        Button achievementsButton = new Button(style);
        table.add(achievementsButton).width(150).height(150).bottom().right().padBottom(20).padRight(20);
        achievementsButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !achievementsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToAchievements();
            }
        });


        goToExitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        goToSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToSettings();
            }
        });

        goToEndlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToEndless();
            }
        });

        storyModeScreen = new StoryModeScreen(stage, game, this);
        endlessModeScreen = new EndlessModeScreen(stage, game, this);
        settingsScreen = new SettingsScreen(stage,game,this);
        levelModeScreen = new LevelModeScreen(stage, game, this);
        // TODO add listeners to Statistics, EndlessMode
        leaderboardScreen = new LeaderboardScreen(stage, game, this);
        achievementScreen = new AchievementScreen(stage,game,this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    private void hideAllTables() {
        table.setVisible(false);
        storyModeScreen.getStoryTable().setVisible(false);
        if(levelModeScreen.getLevelSelectionScreen() != null) {
            levelModeScreen.getLevelSelectionScreen().getLevelSelectionTable().setVisible(false);
        }
        levelModeScreen.getLevelTable().setVisible(false);
        settingsScreen.getTable().setVisible(false);
        endlessModeScreen.getEndlessTable().setVisible(false);
        leaderboardScreen.getLeaderboardTable().setVisible(false);
    }

    public void goToStory(){
        hideAllTables();
		game.setGameMode(GameMode.STORY_MODE);
        storyModeScreen.getStoryTable().setVisible(true);
    }
    public void goToLevel(){
        hideAllTables();
	    game.setGameMode(GameMode.LEVELS_MODE);
        levelModeScreen.getLevelTable().setVisible(true);
    }

    public void goBackToMenu(){
        hideAllTables();
        table.setVisible(true);
    }

    public void goToSettings(){
        hideAllTables();
        settingsScreen.getTable().setVisible(true);
    }

    public void goToEndless(){
        hideAllTables();
	    game.setGameMode(GameMode.ENDLESS_MODE);
        endlessModeScreen.getEndlessTable().setVisible(true);
    }

    public void goToLeaderboard(){
        hideAllTables();
        leaderboardScreen.getLeaderboardTable().setVisible(true);
    }
    public void goToAchievements(){
        hideAllTables();
        achievementScreen.getTable().setVisible(true);
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
