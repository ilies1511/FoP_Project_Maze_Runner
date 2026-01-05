package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Camera;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.effects.Freeze;
import de.tum.cit.fop.maze.effects.Poison;
import de.tum.cit.fop.maze.effects.Sunscreen;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Static.Exit;

import java.util.List;

import java.util.ArrayList;
import java.util.Objects;

public class HUD {

    private final Player player;
    private final MazeRunnerGame game;
    private final GameScreen gameScreen;
    private final Stage stage;
    Label xpLabel;
    private Table table;
    private Table uiContainer;
    private Table heartTable;
    private Table statusEffectsTable;
    private Table keyOutlineTable;
    private Table arrowTable;
    private Stack bloodEffect;
    private Stack poisonEffect;
    private Stack freezeEffect;
    private Stack sunscreenEffect;
    private ShapeRenderer shapeRenderer;
    private int playerXP;
    private int playerKeys;
    private int requiredKeys;
    private Stack[] keyStack;
    private Image arrow;
    private Exit exit;
    private boolean hasArrow;


    public HUD(Player player, Stage stage, MazeRunnerGame game, GameScreen gameScreen) {
        shapeRenderer = new ShapeRenderer();
        this.player = player;
        this.stage = stage;
        this.game = game;
        this.gameScreen = gameScreen;

        renderHUD();
    }


    public void renderHUD() {

        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        keyOutlineTable = new Table();
        keyOutlineTable.setFillParent(true);


        arrowTable = new Table();
        arrowTable.setFillParent(true);



        stage.addActor(table);
        stage.addActor(keyOutlineTable);
        stage.addActor(arrowTable);
        table.bottom();
        arrowTable.center();
        playerXP = player.getXp();

        uiContainer = new Table();
        //uiContainer.debug();
        uiContainer.top();
        TextureRegionDrawable signDrawable = new TextureRegionDrawable(game.getWoodenSign());
        uiContainer.setColor(Color.GRAY);
        uiContainer.setBackground(signDrawable);

        heartTable = new Table();
        // heartTable.debug();

        statusEffectsTable = new Table();
        //statusEffectsTable.debug();
        renderPlayerHP();
        table.add(uiContainer).width(3200).height(200);

        keyOutlineTable.top().right().padRight(40);
        updatePlayerKeys();
        uiContainer.add(heartTable).padTop(20);

        xpLabel = new Label("XP:" + playerXP, game.getSkin(), "vampire-style");
        uiContainer.row();
        uiContainer.add(xpLabel);
        uiContainer.row();
        uiContainer.add(statusEffectsTable).bottom();

    }

    public void createArrow(){

       if(!hasArrow){
           arrow = new Image(game.getArrow());
           //arrowTable.add(arrow).center().width(150).height(150);
           arrow.setOrigin(Align.center);
           arrow.setSize(100, 100);
           stage.addActor(arrow);
           arrowFollow();
           exit = null;
           float closestDist = Float.MAX_VALUE;
           List<Exit> exits = gameScreen.getGameField().getObjectsByType(Exit.class);
           for(Exit e : exits){
               float currentDist = gameScreen.getPlayer().position.dst2(e.position);
               if (currentDist < closestDist) {
                   closestDist = currentDist;
                   exit = e;
               }
           }
       }

        hasArrow = true;
    }
    private void arrowFollow(){
        if (arrow == null) return;

        float playerX = gameScreen.getPlayer().position.x;
        float playerY = gameScreen.getPlayer().position.y;


        Viewport gameViewport = gameScreen.getViewport();
        OrthographicCamera gameCamera = gameScreen.getCamera().getCamera();


        Vector3 screenPos = new Vector3(playerX, playerY, 0);

        gameCamera.project(screenPos,
                gameViewport.getScreenX(),
                gameViewport.getScreenY(),
                gameViewport.getScreenWidth(),
                gameViewport.getScreenHeight()
        );

        float flippedY = Gdx.graphics.getHeight() - screenPos.y;


        Vector2 stagePos = stage.screenToStageCoordinates(new Vector2(screenPos.x, flippedY));

        arrow.setPosition(
                stagePos.x - arrow.getWidth() / 2 + 20,
                stagePos.y - arrow.getHeight() / 2 + 100
        );

    }
    public void rotateArrow(){

        if(exit!=null){
           arrowFollow();
            arrow.setOrigin(Align.center);
            float angle = exit.position.cpy().sub(gameScreen.getPlayer().position).angleDeg();
            arrow.setRotation(angle);
        }
    }
    public void deleteArrow(){
        arrowTable.clearChildren();
        hasArrow = false;

    }

    public void updatePlayerKeys(){
        requiredKeys = player.getRequiredKeys();
        keyStack = new Stack[requiredKeys];
        keyOutlineTable.clearChildren();
        Label keyLabel = new Label("Keys", game.getSkin(), "vampire-style");
        keyLabel.setFontScale(2);
        keyOutlineTable.add(keyLabel);
        keyOutlineTable.row();
        playerKeys = player.getCurrentKeys();
        for(int i = 0; i < requiredKeys; i++){
            keyStack[i] = new Stack();
            Image keyOutline =new Image(game.getKeyOutline());
//            keyOutlineTable.add(key).width(170).height(100).padRight(10).padTop(10);
            keyStack[i].add(keyOutline);
            keyOutlineTable.add(keyStack[i]).size(170,100);


//            keyOutlineTable.add(keyStack[i]).size(160,90);
        }
        for(int i = 0; i<playerKeys;i ++){
            Image key =new Image(game.getKey());
            keyStack[i].add(key);
        }

    }

    public void updatePlayerXP() {
        playerXP = player.getXp();

        xpLabel.setText("XP:" + playerXP);
    }

    public void renderPlayerHP() {
        int redHearts = player.getCurrentHealth();
        int emptyHearts = player.getMaxHealth() - player.getCurrentHealth();
        // call every time after updating player health
        heartTable.clearChildren();
        for (int i = 0; i < redHearts; i++) {
            Image heart = new Image(game.getHeart());
            heartTable.add(heart).width(70).height(60).padRight(15).padTop(10);
        }
        for (int i = 0; i < emptyHearts; i++) {
            Image emptyHeart = new Image(game.getSkin().getDrawable("heart-bg"));
            heartTable.add(emptyHeart).width(55).height(55).padRight(15).padTop(10);
        }
    }

    public void updatePlayerStatusEffects() {

        if (player.hasEffect(Buff.class)) {
            if (bloodEffect == null) {
                drawRing(new Image(game.getBloodEffect()), player.getEffectDuration(Buff.class), "Buff");
            }
        } else {
            if (bloodEffect != null) {
                removeStatusEffectUI(bloodEffect);
                bloodEffect = null;
            }
        }
        if (player.hasEffect(Poison.class)) {
            if (poisonEffect == null) {
                drawRing(new Image(game.getPoisonEffect()), player.getEffectDuration(Poison.class), "Poison");
            }
        } else {
            if (poisonEffect != null) {
                removeStatusEffectUI(poisonEffect);
                poisonEffect = null;
            }
        }
        if (player.hasEffect(Freeze.class)) {
            if (freezeEffect == null) {
                drawRing(new Image(game.getFreezeEffect()), player.getEffectDuration(Freeze.class), "Freeze");
            }
        } else {
            if (freezeEffect != null) {
                removeStatusEffectUI(freezeEffect);
                freezeEffect = null;
            }
        }
        if (player.hasEffect(Sunscreen.class)) {
            if (sunscreenEffect == null) {
                drawRing(new Image(game.getSunscreenEffect()), player.getEffectDuration(Sunscreen.class), "Sunscreen");
            }
        } else {
            if (sunscreenEffect != null) {
                removeStatusEffectUI(sunscreenEffect);
                sunscreenEffect = null;
            }
        }
    }
    private void removeStatusEffectUI(Stack stack){
        if (stack != null) {
            Cell cell = statusEffectsTable.getCell(stack);
            if (cell != null) {
                cell.setActor(null);
                cell.size(0).pad(0);
            }
            statusEffectsTable.invalidateHierarchy();
        }
    }

    private void drawRing(Image img, float maxT, String effect) {
        Stack stack = new Stack() {
            @Override
            public void act(float delta) {
                // Only update this stack (and its children/actions) if the game is running
                if (!gameScreen.isPaused()) {
                    super.act(delta);
                }
            }
        };

        Image newEffect = img;
        stack.add(newEffect);

        Actor cooldownOverlay = new Actor() {

            float maxTime = maxT;
            float timeLeft = maxTime;

            @Override
            public void act(float delta) {
                super.act(delta);
                timeLeft -= delta;
                if (timeLeft < 0) timeLeft = 0;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.end(); // Pause sprite batch

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glLineWidth(8);

                shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
                shapeRenderer.setTransformMatrix(new Matrix4());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);

                Vector2 stagePos = localToStageCoordinates(new Vector2(0, 0));

                float r = Math.min(getWidth(), getHeight()) / 1.8f;
                float cx = getX() + getWidth() / 2f;
                float cy = getY() + getHeight() / 2f;

                float percentage = timeLeft / maxTime;

                if (percentage > 0) {
                    int segments = 30;
                    float startAngle = 90 * MathUtils.degreesToRadians;
                    float totalSweep = -360 * percentage * MathUtils.degreesToRadians;
                    float step = totalSweep / segments;
                    float lastX = cx + MathUtils.cos(startAngle) * r;
                    float lastY = cy + MathUtils.sin(startAngle) * r;
                    for (int i = 1; i <= segments; i++) {
                        float currentAngle = startAngle + (step * i);
                        float nextX = cx + MathUtils.cos(currentAngle) * r;
                        float nextY = cy + MathUtils.sin(currentAngle) * r;
                        shapeRenderer.line(lastX, lastY, nextX, nextY);

                        lastX = nextX;
                        lastY = nextY;
                    }
                }
                shapeRenderer.end();
                Gdx.gl.glLineWidth(1);
                batch.begin();
            }
        };

        stack.add(cooldownOverlay);

        Stack previousStack = null;
        if (Objects.equals(effect, "Poison")) previousStack = poisonEffect;
        else if (Objects.equals(effect, "Buff")) previousStack = bloodEffect;
        else if (Objects.equals(effect, "Freeze")) previousStack = freezeEffect;
        else if (Objects.equals(effect, "Sunscreen")) previousStack = sunscreenEffect;
        boolean replace = false;

        if (previousStack != null) {
            Cell cell = statusEffectsTable.getCell(previousStack);
            if (cell != null) {
                cell.setActor(stack);
                replace = true;
            }
            previousStack.clearActions();
        }
        if (!replace) {
            statusEffectsTable.add(stack).size(60, 60).pad(5);
        }

        // statusEffectsTable.add(stack).size(60, 60).pad(5);
        statusEffectsTable.pack();


        Stack finalStack = stack;

        if (Objects.equals(effect, "Poison")) poisonEffect = stack;
        else if (Objects.equals(effect, "Buff")) bloodEffect = stack;
        else if (Objects.equals(effect, "Freeze")) freezeEffect = stack;
        else if (Objects.equals(effect, "Sunscreen")) sunscreenEffect = stack;


        stack.addAction(Actions.sequence(
                Actions.delay(maxT),
                Actions.run(() -> {

                    if (Objects.equals(effect, "Poison") && poisonEffect == finalStack || Objects.equals(effect, "Buff") && bloodEffect == finalStack || Objects.equals(effect, "Freeze") && freezeEffect == finalStack || Objects.equals(effect, "Sunscreen") && sunscreenEffect == finalStack) {
                        Cell cell = statusEffectsTable.getCell(finalStack);
                        finalStack.remove();
                        if (cell != null) {
                            cell.setActor(null);
                            cell.size(0).pad(0);
                        }
                        statusEffectsTable.invalidateHierarchy();
                    }
                    if (Objects.equals(effect, "Poison") && poisonEffect == finalStack) {
                        poisonEffect = null;
                    } else if (Objects.equals(effect, "Buff") && bloodEffect == finalStack) {
                        bloodEffect = null;
                    } else if  (Objects.equals(effect, "Freeze") && freezeEffect == finalStack) {
                        freezeEffect = null;
                    } else if  (Objects.equals(effect, "Sunscreen") && sunscreenEffect == finalStack) {
                        sunscreenEffect = null;
                    }
                })
        ));
    }



}
