package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.objects.Player;

public class ProgressionOverlay {
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Table table;
    private int healthUpgrades;
    private int speedUpgrades;
    private boolean dashDisable;
    private boolean healthDisable;
    private boolean speedDisable;
    private boolean invisDisable;
    private final MazeRunnerGame game;
    private final Player player;

    private final SoundController soundController;

    public ProgressionOverlay(MazeRunnerGame game, Stage stage, GameScreen gameScreen) {
        this.game = game;
        this.player = gameScreen.getPlayer();

        this.gameScreen = gameScreen;

        soundController = gameScreen.getSoundController();

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

        TextButton dash = new TextButton("Upgrade dash", game.getSkin(), "vampire-style");
        table.add(dash).width(300).left().padLeft(50).padBottom(50);
        dash.setDisabled(true);

        table.row();

        TextButton maxSpeed = new TextButton("Upgrade max speed", game.getSkin(), "vampire-style");
        table.add(maxSpeed).width(300).left().padLeft(50).padBottom(50);
        maxSpeed.setDisabled(true);

        table.row();

        TextButton invis = new TextButton("Upgrade invisibility", game.getSkin(), "vampire-style");
        table.add(invis).width(300).left().padLeft(50).padBottom(50);
        invis.setDisabled(true);

        table.row();

        TextButton hide = new TextButton("Done", game.getSkin(), "vampire-style");
        table.add(hide).width(300).left().padLeft(50).padBottom(50);

        maxHealth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (player.getXp() >= 15) {
                    healthUpgrades++;

                    player.receiveXP(-15);
                    player.setMaxHealth(player.getMaxHealth() + 1);
                    player.getDamageOrHeal(-1);
                    if (!dashDisable) {
                        dash.setDisabled(false);
                        dashDisable = true;
                    }

                    if (healthUpgrades >= 10) {
                        maxHealth.setDisabled(true);
                        healthDisable = true;
                    }
                }

            }
        });
        maxHealth.getLabel().setTouchable(Touchable.disabled);
        maxHealth.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !maxHealth.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        dash.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (player.getXp() >= 15) {
                    dash.setDisabled(true);
                    if (!speedDisable) {
                        maxSpeed.setDisabled(false);
                        speedDisable = true;
                    }
                    player.setCanDash(true);
                    player.receiveXP(-15);

                }

            }
        });
        dash.getLabel().setTouchable(Touchable.disabled);
        dash.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !dash.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        maxSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (player.getXp() >= 15) {
                    speedUpgrades++;
                    player.receiveXP(-15);
                    player.multiplySpeed(1.15f);
                    if (!invisDisable) {
                        invis.setDisabled(false);
                        invisDisable = false;
                    }
                    if (speedUpgrades >= 5) {
                        maxSpeed.setDisabled(true);
                    }
                }

            }
        });
        maxSpeed.getLabel().setTouchable(Touchable.disabled);
        maxSpeed.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !maxSpeed.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        invis.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                
                if (player.getXp() >= 15) {
                    invis.setDisabled(true);
                    invisDisable = true;
                    player.setCanInvis(true);
                    player.receiveXP(-15);
                }
            }
        });
        invis.getLabel().setTouchable(Touchable.disabled);
        invis.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!invis.isDisabled()) {
                    if (pointer == -1) {
                        soundController.playUI_hoverOverSound(1);
                    }
                }

            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });
        hide.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideProgressionMenu();
            }
        });
        hide.getLabel().setTouchable(Touchable.disabled);
        hide.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !hide.isDisabled()) {
                    soundController.playUI_hoverOverSound(1);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundController.playUI_buttonClickSound(1);
            }
        });


    }

    public void showProgressionMenu() {
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        gameScreen.setPaused(true);
    }

    public void hideProgressionMenu() {
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        gameScreen.setPaused(false);
        if (healthDisable && speedDisable && invisDisable && dashDisable) {
            gameScreen.getAchievementUI().upgradesAchievement();
        }

    }
}
