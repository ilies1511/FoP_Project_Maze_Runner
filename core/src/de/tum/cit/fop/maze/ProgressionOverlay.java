package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ProgressionOverlay {
    private Stage stage;
    private MazeRunnerGame game;
    private final GameScreen gameScreen;
    private final HUD hud;

    private Table table;

    private Player player;


    public ProgressionOverlay(Stage stage, MazeRunnerGame game, GameScreen gameScreen, Player player, HUD hud) {


        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        this.hud = hud;

        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);
        table.center();

        this.stage = stage;
        stage.addActor(table);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.5f));
        pixmap.fill();
        TextureRegionDrawable dim = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        table.setBackground(dim);

        TextButton maxHealth = new TextButton("Upgrade max health", game.getSkin(), "vampire-style");
        table.add(maxHealth).width(300).left().padLeft(50).padBottom(50);

        table.row();

        TextButton dash= new TextButton("Upgrade dash", game.getSkin(),"vampire-style");
        table.add(dash).width(300).left().padLeft(50).padBottom(50);
        dash.setDisabled(true);

        table.row();

        TextButton maxSpeed = new TextButton("Upgrade max speed", game.getSkin(),"vampire-style");
        table.add(maxSpeed).width(300).left().padLeft(50).padBottom(50);
        maxSpeed.setDisabled(true);

        table.row();

        TextButton invis = new TextButton("Upgrade invisibility", game.getSkin(),"vampire-style");
        table.add(invis).width(300).left().padLeft(50).padBottom(50);
        invis.setDisabled(true);

        table.row();

        TextButton hide = new TextButton("Done", game.getSkin(),"vampire-style");
        table.add(hide).width(300).left().padLeft(50).padBottom(50);

        maxHealth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(player.getXp() >= 15){

                    hud.updatePlayerXP(-15);
                    player.setHealth(player.getHealth()+1);
                    hud.updatePlayerHP(0);
                    dash.setDisabled(false);
                }

            }
        });
        dash.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(player.getXp() >= 15){
                    dash.setDisabled(true);
                    player.setCanDash(true);
                    hud.updatePlayerXP(-15);
                    maxSpeed.setDisabled(false);

                }

            }
        });
        maxSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(player.getXp() >= 15){
                    hud.updatePlayerXP(-15);;
                    player.multiplySpeed(1.15f);
                    invis.setDisabled(false);
                }

            }
        });
        invis.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(player.getXp() >= 15) {
                    dash.setDisabled(true);
                    player.setCanInvis(true);
                    hud.updatePlayerXP(-15);;
                }
            }
        });
        hide.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideProgressionMenu();
            }
        });



    }

    public void showProgressionMenu(){
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        gameScreen.setPaused(true);
    }

    public void hideProgressionMenu(){
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        gameScreen.setPaused(false);
    }
}
