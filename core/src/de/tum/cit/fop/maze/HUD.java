package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

public class HUD {

    private Table table;
    private Table uiContainer;
    private Table heartTable;
    private Table statusEffectsTable;

    private Stack bloodEffect;
    private Stack poisonEffect;

    private ShapeRenderer shapeRenderer;

    private final Player player;
    private int playerHealth;

    private final MazeRunnerGame game;

    private final Stage stage;
    public HUD(Player player, Stage stage, MazeRunnerGame game){
        shapeRenderer = new ShapeRenderer();

     this.player = player;
     this.stage = stage;
     this.game = game;

        renderHUD();
    }


    public void renderHUD(){
        table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        //table.debug();
        stage.addActor(table);
        table.bottom().left();
        playerHealth = player.getHealth();

        uiContainer = new Table();
        //uiContainer.debug();
        uiContainer.top();
        TextureRegionDrawable signDrawable = new TextureRegionDrawable(game.getWoodenSign());
        uiContainer.setBackground(signDrawable);

        heartTable = new Table();
        // heartTable.debug();

        statusEffectsTable = new Table();
        //statusEffectsTable.debug();
        for(int i = 0;i<playerHealth; i++ ){
            Image heart = new Image(game.getSkin().getDrawable("heart"));
            heartTable.add(heart).width(50).height(50);
        }
        table.add(uiContainer).width(500).height(200).padBottom(30);
        uiContainer.add(heartTable).padTop(20);
        uiContainer.row();
        uiContainer.add(statusEffectsTable).bottom();


    }

    public void updatePlayerHP(int damage){
        heartTable.clearChildren();
        for(int i = 0;i<playerHealth - damage; i++ ){
            Image heart = new Image(game.getSkin().getDrawable("heart"));
            heartTable.add(heart).width(50).height(50).padRight(10).padTop(10);
        }
        if(damage>0){
            for(int i =0; i < damage; i++){
                Image emptyHeart = new Image(game.getSkin().getDrawable("heart-bg"));
                heartTable.add(emptyHeart).width(50).height(50).padRight(10).padTop(10);
            }
        }


    }
    public void updatePlayerStatusEffects(){

        if(player.isBuffed()){
            drawRing(new Image(game.getBloodEffect()), player.getBuffDuration(), "Buffed");
        }
        if(player.isPoisoned()){
            drawRing(new Image(game.getPoisonEffect()), player.getPoisonDuration(), "Poisoned");
        }
    }

    private void drawRing(Image img, float maxT, String effect ){
        Stack stack = new Stack();
        Image newEffect = img;
        stack.add(newEffect);

        if(Objects.equals(effect, "Poisoned")){
            poisonEffect = stack;
        }
        else if(Objects.equals(effect, "Buffed")){
            bloodEffect = stack;
        }
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

                float r = Math.min(getWidth(), getHeight())/1.8f;
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

        statusEffectsTable.add(stack).size(60, 60).pad(5);

        stack.addAction(Actions.sequence(
                Actions.delay(maxT),
                Actions.run(() -> {
                    if(Objects.equals(effect, "Poisoned")){
                        player.setPoisoned(false);
                        Cell cell = statusEffectsTable.getCell(poisonEffect);
                        poisonEffect.remove();
                        statusEffectsTable.getCells().removeValue(cell, true);
                    }
                    else if(Objects.equals(effect, "Buffed")) {
                        player.setBuffed(false);
                        Cell cell = statusEffectsTable.getCell(bloodEffect);
                        bloodEffect.remove();
                        statusEffectsTable.getCells().removeValue(cell, true);
                    }
                })
        ));
    }
}
