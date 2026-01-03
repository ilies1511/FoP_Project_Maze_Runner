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

public class StoryModeScreen {
    private Table storyTable;
    private SoundController soundController;
    private Table confirmationTable;
    
    public StoryModeScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        storyTable = new Table();
        storyTable.setVisible(false);
        storyTable.setFillParent(true);
        stage.addActor(storyTable);
        storyTable.top();

        soundController = menuScreen.getSoundController();

        Label label = new Label("Story Mode", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        storyTable.add(label).expandX().center().padTop(50).padBottom(80).row();

        TextButton continueGame = new TextButton("Continue", game.getSkin(), "vampire-style");
        storyTable.add(continueGame).width(300).left().padLeft(50).padBottom(50);
        continueGame.getLabel().setTouchable(Touchable.disabled);
        continueGame.setDisabled(true); //TODO Finish continue button after the save/load system is ready
        continueGame.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !continueGame.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        //TODO Finish continue button after the save/load system is ready
//        continueGame.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                goToStory();
//            }
//        });

        storyTable.row();

        TextButton newGame = new TextButton("New Game", game.getSkin(), "vampire-style");
        storyTable.add(newGame).width(300).left().padLeft(50).padBottom(50);
        newGame.getLabel().setTouchable(Touchable.disabled);
        newGame.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !newGame.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(continueGame.isDisabled()){
                    game.goToBackgroundStory();
                }
              else{
                    confirmNewGame();
                }
            }
        });


        storyTable.row();

        TextButton toMainMenu = new TextButton("Main Menu", game.getSkin(), "vampire-style");
        storyTable.add(toMainMenu).width(300).left().padLeft(50).padBottom(50);
        toMainMenu.getLabel().setTouchable(Touchable.disabled);
        toMainMenu.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !toMainMenu.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        toMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.goBackToMenu();
            }
        });
        confirmationTable = new Table();
        confirmationTable.setVisible(false);
        confirmationTable.setFillParent(true);
        stage.addActor(confirmationTable);
        confirmationTable.top();
        Label confLabel = new Label("Are you sure you want to start a new game?", game.getSkin(), "vampire-style");
        confLabel.setFontScale(2.7f);
        confirmationTable.add(confLabel).expandX().center().padTop(50).padBottom(400).row();

        TextButton yes = new TextButton("Yes", game.getSkin(), "vampire-style");
        confirmationTable.add(yes).width(300).left().padLeft(50).padBottom(50);
        yes.getLabel().setTouchable(Touchable.disabled);
        yes.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !yes.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        yes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToBackgroundStory();
            }
        });
        TextButton no = new TextButton("No", game.getSkin(), "vampire-style");
        confirmationTable.add(no).width(300).left().padLeft(50).padBottom(50);
        no.getLabel().setTouchable(Touchable.disabled);
        no.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !no.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        no.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backToStory();
            }
        });
    }

    public Table getStoryTable() {
        return storyTable;
    }
    public void confirmNewGame(){
        storyTable.setVisible(false);
        confirmationTable.setVisible(true);
    }
    public void backToStory(){
        storyTable.setVisible(true);
        confirmationTable.setVisible(false);
    }
}

