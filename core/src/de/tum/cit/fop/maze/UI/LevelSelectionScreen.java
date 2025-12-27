package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class LevelSelectionScreen {
    private Table levelSelectionTable;
    private SoundController soundController;
    private MazeRunnerGame game;
    private StoryModeScreen storyModeScreen;

    public LevelSelectionScreen(Stage stage, MazeRunnerGame game, StoryModeScreen storyModeScreen, MenuScreen menuScreen) {
        this.game = game;
        this.storyModeScreen = storyModeScreen;
        this.soundController = menuScreen.getSoundController();
        
        levelSelectionTable = new Table();
        levelSelectionTable.setVisible(false);
        levelSelectionTable.setFillParent(true);
        stage.addActor(levelSelectionTable);
        levelSelectionTable.top();

        Label titleLabel = new Label("Select Level", game.getSkin(), "vampire-style");
        titleLabel.setFontScale(3.0f);
        levelSelectionTable.add(titleLabel).expandX().center().padTop(50).padBottom(80).row();

        // create level buttons and make it scrollable (in case when too many levels)
        Table levelButtonsTable = new Table();
        ScrollPane scrollPane = new ScrollPane(levelButtonsTable, game.getSkin());
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        createLevelButtons(levelButtonsTable);
        
        levelSelectionTable.add(scrollPane).expand().fill().pad(20).row();

        // back button
        TextButton backButton = new TextButton("Back", game.getSkin(), "vampire-style");
        levelSelectionTable.add(backButton).width(300).center().padBottom(50);
        backButton.getLabel().setTouchable(Touchable.disabled);
        
        backButton.addListener(new ClickListener(){
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
                goBackToStoryMode();
            }
        });
    }

    private void createLevelButtons(Table levelButtonsTable) {
        FileHandle mapsDir = Gdx.files.internal("maps");
        if (mapsDir.exists()) {
            FileHandle[] mapFiles = mapsDir.list(".properties");
            
            java.util.Arrays.sort(mapFiles, (a, b) -> a.name().compareTo(b.name()));
            
            for (FileHandle mapFile : mapFiles) {
                String fileName = mapFile.name();
                String displayName = fileName.replace(".properties", "");
                
                TextButton levelButton = new TextButton(displayName, game.getSkin(), "vampire-style");
                levelButtonsTable.add(levelButton).width(400).center().padBottom(20).row();
                levelButton.getLabel().setTouchable(Touchable.disabled);
                
                levelButton.addListener(new ClickListener(){
                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        if (pointer == -1 && !levelButton.isDisabled()) {
                            soundController.playUI_hoverOverSound(1);
                        }
                    }
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        soundController.playUI_buttonClickSound(1);
                    }
                });
                
                // Add level selection logic
                levelButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        startGameWithLevel(fileName);
                    }
                });
            }
        }
    }

    private void startGameWithLevel(String levelFileName) {
        game.getGameInfo().setSelectedLevel(levelFileName);
        
        // Dispose sound controller and go to background story
        soundController.dispose();
        game.goToBackgroundStory();
    }

    private void goBackToStoryMode() {
        levelSelectionTable.setVisible(false);
        storyModeScreen.getStoryTable().setVisible(true);
    }

    public Table getLevelSelectionTable() {
        return levelSelectionTable;
    }
}
