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
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;

public class EndlessModeScreen {
    private Table table;
    private SoundController soundController;
    public EndlessModeScreen(Stage stage, MazeRunnerGame game, MenuScreen menuScreen) {
        soundController = menuScreen.getSoundController();
        table = new Table();
        table.setVisible(false);
        table.setFillParent(true);
        stage.addActor(table);
        table.top();

        soundController = menuScreen.getSoundController();

        Label label = new Label("Survival Mode", game.getSkin(), "vampire-style");
        label.setFontScale(3.0f);
        table.add(label).expandX().center().padTop(50).padBottom(80).row();

        TextButton continueGame = new TextButton("Continue", game.getSkin(), "vampire-style");
        table.add(continueGame).width(300).left().padLeft(50).padBottom(50);
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

        table.row();

        TextButton newGame = new TextButton("New Game", game.getSkin(), "vampire-style");
        table.add(newGame).width(300).left().padLeft(50).padBottom(50);
        newGame.getLabel().setTouchable(Touchable.disabled);
//        newGame.setDisabled(true);
        newGame.setDisabled(false);
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
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                soundController.dispose();
//                // TODO add logic
//            }
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
		        soundController.dispose();
		        game.disposeAllScreens();
		        game.setScreen(new GameScreen(game, true));
	        }
        });

        table.row();

        TextButton goToStatisticsButton = new TextButton("Statistics", game.getSkin(), "vampire-style");
        table.add(goToStatisticsButton).width(300).left().padLeft(50).padBottom(50);
        goToStatisticsButton.getLabel().setTouchable(Touchable.disabled);
//        goToStatisticsButton.setDisabled(true);
        goToStatisticsButton.setDisabled(false);
		goToStatisticsButton.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
			    game.goToLeaderboard();
		    }
		});
        goToStatisticsButton.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !goToStatisticsButton.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        table.row();

        TextButton toMainMenu = new TextButton("Main Menu", game.getSkin(), "vampire-style");
        table.add(toMainMenu).width(300).left().padLeft(50).padBottom(50);
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


    }

    public Table getEndlessTable() {
        return table;
    }
}
