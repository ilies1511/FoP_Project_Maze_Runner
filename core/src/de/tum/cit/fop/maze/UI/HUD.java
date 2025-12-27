package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.objects.Player;

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
    private Stack bloodEffect;
    private Stack poisonEffect;
    private Stack freezeEffect;
    private ShapeRenderer shapeRenderer;
    private int playerXP;
    private int playerKeys;
    private int requiredKeys;
    private Stack[] keyStack;

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
        //   table.defaults().space(10).expandX();
        //table.debug();
        stage.addActor(table);
        stage.addActor(keyOutlineTable);
        table.bottom();
        playerXP = player.getXp();

        uiContainer = new Table();
        //uiContainer.debug();
        uiContainer.top();
        TextureRegionDrawable signDrawable = new TextureRegionDrawable(game.getWoodenSign());
        uiContainer.setBackground(signDrawable);

        heartTable = new Table();
        // heartTable.debug();

        statusEffectsTable = new Table();
        //statusEffectsTable.debug();
        renderPlayerHP();
        table.add(uiContainer).width(2200).height(200);

        requiredKeys = player.getRequiredKeys();
         keyStack = new Stack[requiredKeys];

        keyOutlineTable.top().right().padRight(40);
       Label keyLabel = new Label("Keys", game.getSkin(), "vampire-style");
       keyLabel.setFontScale(2);
        keyOutlineTable.add(keyLabel);
        keyOutlineTable.row();
        for(int i = 0; i < requiredKeys; i++){
            keyStack[i] = new Stack();
            Image keyOutline =new Image(game.getKeyOutline());
//            keyOutlineTable.add(key).width(170).height(100).padRight(10).padTop(10);
            keyStack[i].add(keyOutline);
            keyOutlineTable.add(keyStack[i]).size(170,100);
        }

        uiContainer.add(heartTable).padTop(20);

        xpLabel = new Label("XP:" + playerXP, game.getSkin(), "vampire-style");
        uiContainer.row();
        uiContainer.add(xpLabel);
        uiContainer.row();
        uiContainer.add(statusEffectsTable).bottom();

    }

    public void updatePlayerKeys(){
        playerKeys = player.getCurrentKeys();
        for(int i = 0; i < playerKeys; i++){
            Image key =new Image(game.getKey());
            keyStack[i].add(key);

//            keyOutlineTable.add(keyStack[i]).size(160,90);

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
            Image heart = new Image(game.getSkin().getDrawable("heart"));
            heartTable.add(heart).width(50).height(50).padRight(10).padTop(10);
        }
        for (int i = 0; i < emptyHearts; i++) {
            Image emptyHeart = new Image(game.getSkin().getDrawable("heart-bg"));
            heartTable.add(emptyHeart).width(50).height(50).padRight(10).padTop(10);
        }
    }

    public void updatePlayerStatusEffects() {

        if(player.isBuffed()){
            drawRing(new Image(game.getBloodEffect()), player.getBuffDuration(), "Buffed");
        }
        if (player.isPoisoned()) {
            drawRing(new Image(game.getPoisonEffect()), player.getPoisonDuration(), "Poisoned");
        }
        if (player.isFrozen()) {
            drawRing(new Image(game.getFreezeEffect()), player.getFreezeDuration(), "Frozen");
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

                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);

                float r = Math.min(getWidth(), getHeight()) / 1.8f;
                float cx = getX() + getWidth() / 2f;
                float cy = getY() + getHeight() / 2f;

                float percentage = timeLeft / maxTime;

                if (percentage > 0) {
                    int segments = 30;
                    float startAngle = 90 * com.badlogic.gdx.math.MathUtils.degreesToRadians;
                    float totalSweep = -360 * percentage * com.badlogic.gdx.math.MathUtils.degreesToRadians;
                    float step = totalSweep / segments;
                    float lastX = cx + com.badlogic.gdx.math.MathUtils.cos(startAngle) * r;
                    float lastY = cy + com.badlogic.gdx.math.MathUtils.sin(startAngle) * r;
                    for (int i = 1; i <= segments; i++) {
                        float currentAngle = startAngle + (step * i);
                        float nextX = cx + com.badlogic.gdx.math.MathUtils.cos(currentAngle) * r;
                        float nextY = cy + com.badlogic.gdx.math.MathUtils.sin(currentAngle) * r;
                        shapeRenderer.line(lastX, lastY, nextX, nextY);

                        lastX = nextX;
                        lastY = nextY;
                    }
                }
                shapeRenderer.end();
                batch.begin();
            }
        };

        stack.add(cooldownOverlay);

        Stack previousStack = null;
        if (Objects.equals(effect, "Poisoned")) previousStack = poisonEffect;
        else if (Objects.equals(effect, "Buffed")) previousStack = bloodEffect;
        else if (Objects.equals(effect, "Frozen")) previousStack = freezeEffect;
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

        if (Objects.equals(effect, "Poisoned")) poisonEffect = stack;
        else if (Objects.equals(effect, "Buffed")) bloodEffect = stack;
        else if (Objects.equals(effect, "Frozen")) freezeEffect = stack;


        stack.addAction(Actions.sequence(
                Actions.delay(maxT),
                Actions.run(() -> {

                    if (Objects.equals(effect, "Poisoned") && poisonEffect == finalStack || Objects.equals(effect, "Buffed") && bloodEffect == finalStack || Objects.equals(effect, "Frozen") && freezeEffect == finalStack) {
                        Cell cell = statusEffectsTable.getCell(finalStack);
                        finalStack.remove();
                        if (cell != null) {
                            cell.setActor(null);
                            cell.size(0).pad(0);
                        }
                        statusEffectsTable.invalidateHierarchy();
                    }
                    if (Objects.equals(effect, "Poisoned") && poisonEffect == finalStack) {
                        poisonEffect = null;
                    } else if (Objects.equals(effect, "Buffed") && bloodEffect == finalStack) {
                        bloodEffect = null;
                    } else if  (Objects.equals(effect, "Frozen") && freezeEffect == finalStack) {
                        freezeEffect = null;
                    }
                })
        ));
    }
}
