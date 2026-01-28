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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.effects.Freeze;
import de.tum.cit.fop.maze.effects.Poison;
import de.tum.cit.fop.maze.effects.Sunscreen;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Static.Exit;

import java.util.List;

/**
 * Head-Up Display (HUD) for the game.
 * Displays player health, experience, keys, status effects, and navigation arrows.
 */
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
    private final ShapeRenderer shapeRenderer;
    private int playerXP;
    private int playerKeys;
    private int requiredKeys;
    private Stack[] keyStack;
    private Image arrow;
    private Exit exit;
    private boolean hasArrow;


    /**
     * Constructs the HUD.
     *
     * @param player     The player object to track stats from.
     * @param stage      The stage to add HUD actors to.
     * @param game       The main game instance.
     * @param gameScreen The game screen instance.
     */
    public HUD(Player player, Stage stage, MazeRunnerGame game, GameScreen gameScreen) {
        shapeRenderer = new ShapeRenderer();
        this.player = player;
        this.stage = stage;
        this.game = game;
        this.gameScreen = gameScreen;

        renderHUD();
    }


    /**
     * Renders the HUD elements on the screen.
     */
    public void renderHUD() {

        table = new Table();
        table.setFillParent(true);
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
        uiContainer.top();

        heartTable = new Table();

        statusEffectsTable = new Table();
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

    /**
     * Creates a navigation arrow pointing to the exit.
     */
    public void createArrow() {
        if (!hasArrow) {
            arrow = new Image(game.getArrow());
            arrow.setOrigin(Align.center);
            arrow.setSize(100, 100);
            stage.addActor(arrow);
            arrowFollow();
            exit = null;
            float closestDist = Float.MAX_VALUE;
            List<Exit> exits = gameScreen.getGameField().getObjectsByType(Exit.class);
            for (Exit e : exits) {
                float currentDist = gameScreen.getPlayer().position.dst2(e.position);
                if (currentDist < closestDist) {
                    closestDist = currentDist;
                    exit = e;
                }
            }
        }

        hasArrow = true;
    }

    private void arrowFollow() {
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

    /**
     * Updates the rotation of the navigation arrow to point towards the exit.
     */
    public void rotateArrow() {
        if (exit != null) {
            arrowFollow();
            arrow.setOrigin(Align.center);
            float angle = exit.position.cpy().sub(gameScreen.getPlayer().position).angleDeg();
            arrow.setRotation(angle);
        }
    }

    /**
     * Removes the navigation arrow from the screen.
     */
    public void deleteArrow() {
        if (hasArrow) {
            arrow.remove();
        }
        hasArrow = false;

    }

    /**
     * Updates the key display based on the player's current key count.
     */
    public void updatePlayerKeys() {
        requiredKeys = player.getRequiredKeys();
        keyStack = new Stack[requiredKeys];
        keyOutlineTable.clearChildren();
        Label keyLabel = new Label("Keys", game.getSkin(), "vampire-style");
        keyLabel.setFontScale(2);
        keyOutlineTable.add(keyLabel);
        keyOutlineTable.row();
        playerKeys = player.getCurrentKeys();
        for (int i = 0; i < requiredKeys; i++) {
            keyStack[i] = new Stack();
            Image keyOutline = new Image(game.getKeyOutline());
            keyStack[i].add(keyOutline);
            keyOutlineTable.add(keyStack[i]).size(170, 100);
        }
        for (int i = 0; i < playerKeys; i++) {
            Image key = new Image(game.getKey());
            keyStack[i].add(key);
        }

    }

    /**
     * Updates the XP label with the player's current XP.
     */
    public void updatePlayerXP() {
        playerXP = player.getXp();

        xpLabel.setText("XP:" + playerXP);
    }

    /**
     * Renders the player's health using heart icons.
     */
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

    /**
     * Updates the display of active status effects on the player.
     */
    public void updatePlayerStatusEffects() {

        if (player.hasEffect(Buff.class)) {
            if (bloodEffect == null) {
                addEffect(new Image(game.getBloodEffect()), player.getEffectDuration(Buff.class), "Buff");
            }
        } else {
            if (bloodEffect != null) {
                removeStatusEffectUI(bloodEffect);
                bloodEffect = null;
            }
        }
        if (player.hasEffect(Poison.class)) {
            if (poisonEffect == null) {
                addEffect(new Image(game.getPoisonEffect()), player.getEffectDuration(Poison.class), "Poison");
            }
        } else {
            if (poisonEffect != null) {
                removeStatusEffectUI(poisonEffect);
                poisonEffect = null;
            }
        }
        if (player.hasEffect(Freeze.class)) {
            if (freezeEffect == null) {
                addEffect(new Image(game.getFreezeEffect()), player.getEffectDuration(Freeze.class), "Freeze");
            }
        } else {
            if (freezeEffect != null) {
                removeStatusEffectUI(freezeEffect);
                freezeEffect = null;
            }
        }
        if (player.hasEffect(Sunscreen.class)) {
            if (sunscreenEffect == null) {
                addEffect(new Image(game.getSunscreenEffect()), player.getEffectDuration(Sunscreen.class), "Sunscreen");
            }
        } else {
            if (sunscreenEffect != null) {
                removeStatusEffectUI(sunscreenEffect);
                sunscreenEffect = null;
            }
        }
    }

    private void removeStatusEffectUI(Stack stack) {
        if (stack != null) {
            Cell cell = statusEffectsTable.getCell(stack);
            if (cell != null) {
                cell.setActor(null);
                cell.size(0).pad(0);
            }
            statusEffectsTable.invalidateHierarchy();
        }
    }

    private void addEffect(Image img, float maxT, String effectName) {
        Stack stack = new Stack() {
            @Override
            public void act(float delta) {
                if (!gameScreen.isPaused()) super.act(delta);
            }
        };

        stack.add(img);
        Actor cooldownOverlay = new Actor() {
            private final float maxTime = maxT;
            private float timeLeft = maxT;

            public void act(float delta) {
                super.act(delta);
                timeLeft = Math.max(0, timeLeft - delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (timeLeft <= 0) return;

                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glLineWidth(8);

                shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
                shapeRenderer.setTransformMatrix(new Matrix4());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);

                float r = Math.min(getWidth(), getHeight()) / 1.8f;
                float cx = getX() + getWidth() / 2f;
                float cy = getY() + getHeight() / 2f;
                float percentage = timeLeft / maxTime;

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

                shapeRenderer.end();
                Gdx.gl.glLineWidth(1);
                batch.begin();
            }
        };
        stack.add(cooldownOverlay);

        Stack previousStack = getActiveEffect(effectName);

        if (previousStack != null) {
            Cell cell = statusEffectsTable.getCell(previousStack);
            if (cell != null) {
                cell.setActor(stack);
            }
            previousStack.clearActions();
        } else {
            statusEffectsTable.add(stack).size(60, 60).pad(5);
        }

        statusEffectsTable.pack();

        setActiveEffect(effectName, stack);

        Stack finalStack = stack;
        stack.addAction(Actions.sequence(
                Actions.delay(maxT),
                Actions.run(() -> removeEffectIfExpired(effectName, finalStack))
        ));
    }


    private Stack getActiveEffect(String effectName) {
        switch (effectName) {
            case "Poison":
                return poisonEffect;
            case "Buff":
                return bloodEffect;
            case "Freeze":
                return freezeEffect;
            case "Sunscreen":
                return sunscreenEffect;
            default:
                return null;
        }
    }

    private void setActiveEffect(String effectName, Stack stack) {
        switch (effectName) {
            case "Poison":
                poisonEffect = stack;
                break;
            case "Buff":
                bloodEffect = stack;
                break;
            case "Freeze":
                freezeEffect = stack;
                break;
            case "Sunscreen":
                sunscreenEffect = stack;
                break;
        }
    }

    private void removeEffectIfExpired(String effectName, Stack stackToRemove) {
        if (getActiveEffect(effectName) == stackToRemove) {
            Cell cell = statusEffectsTable.getCell(stackToRemove);
            stackToRemove.remove();

            if (cell != null) {
                cell.setActor(null);
                cell.size(0).pad(0);
            }
            statusEffectsTable.invalidateHierarchy();

            setActiveEffect(effectName, null);
        }
    }
}
