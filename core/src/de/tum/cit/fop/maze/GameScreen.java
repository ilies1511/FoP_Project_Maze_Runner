package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Objects;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private float animationTime = 0f;

    private Animation<TextureRegion> currentAnimation;

    private final Player player;
    private int playerHealth;

    private final Stage stage;

    private Entity playerEntity;

    private GameField gameField;

    private Table table;
    private Table uiContainer;
    private Table heartTable;
    private Table statusEffectsTable;

    private int timeForBuff = 0;
    private int timeForPoison = 0;

    private Stack bloodEffect;
    private Stack poisonEffect;

    private ShapeRenderer shapeRenderer;

    int i = 0;
    //For testing
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        this.player = new Player(); //create an instance of the player

        this.gameField = new GameField();

        // Use startpos from the GameField
        this.playerEntity = new Entity(this.gameField.getPlayerStartPos(), new Vector2(0f, 0f), 3f, new Vector2(0.4f, 0.4f));

        float viewportWidth = 25f;  // Visible world width
        float viewportHeight = 18.75f; // Visible world height

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.zoom = 0.75f;

        // Start by looking down
        currentAnimation = game.getCharacterDownAnimation();

        // Get the font from the game's skin
        font = game.getSkin().getFont("vampire-font");

        Viewport viewport = new ScreenViewport(); // TODO maybe add camera in the brackets
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        shapeRenderer = new ShapeRenderer();

        renderHUD();
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        // Input Handling New Class
        handleInput(delta);

        // Move all the entities
        updateEntitiesPositions(delta);

        // Update camera to move with the player
        moveCamera(delta);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        animationTime += delta;

        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        drawGameField();

        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                currentAnimation.getKeyFrame(animationTime, true),
                playerEntity.position.x,
                playerEntity.position.y,
                0.4f,
                0.8f
        );

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

// For Testing
//        i++;
//        if(i == 1){
//           player.getPoisoned(5f);
//           player.getBuffed(2f);
//           updatePlayerStatusEffects();
//           updatePlayerHP(player.getDamage(1));
//
//        }



    }
    private void handleInput(float delta) {
        boolean isMoving = false;

        // Speed boost // TODO: Add boost to keybindings, handle it via playerEntity
//        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
//            currentSpeed *= 1.8f;
//        }

        // --- MOVEMENT LOGIC ---

        // Y-Axis
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
              playerEntity.velocity.y = playerEntity.speed;
//            yPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterUpAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            playerEntity.velocity.y = -playerEntity.speed;
//            yPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterDownAnimation();
            isMoving = true;
        }

        else {
            playerEntity.velocity.y = 0f;
        }

        // X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            playerEntity.velocity.x = -playerEntity.speed;
//            xPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterLeftAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            playerEntity.velocity.x = playerEntity.speed;
//            xPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterRightAnimation();
            isMoving = true;
        }

        else {
            playerEntity.velocity.x = 0f;
        }

        // Jump (Fix proper logic when final image of dracula is confirmed) // TODO: handle via playerEntity, add to keybindings
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//            // Check which direction we are currently facing/moving to pick the right jump
//            if (currentAnimation == game.getCharacterUpAnimation() || currentAnimation == game.getCharacterUpJumpAnimation()) {
//                currentAnimation = game.getCharacterUpJumpAnimation();
//            } else if (currentAnimation == game.getCharacterLeftAnimation() || currentAnimation == game.getCharacterLeftJumpAnimation()) {
//                currentAnimation = game.getCharacterLeftJumpAnimation();
//            } else if (currentAnimation == game.getCharacterRightAnimation() || currentAnimation == game.getCharacterRightJumpAnimation()) {
//                currentAnimation = game.getCharacterRightJumpAnimation();
//            } else {
//                currentAnimation = game.getCharacterDownJumpAnimation();
//            }
//            isMoving = true; // Keep animating while jumping
//        }

        // Victory pose
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            currentAnimation = game.getCharacterDownWin();
            isMoving = true;
        }
        // Loser pose
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            currentAnimation = game.getCharacterDownSad();
            isMoving = true;
        }

        // Animation Timer
        if (isMoving) {
            animationTime += delta;
        } else {
            animationTime = 0; // Reset to standing frame when not moving
        }

        // Go to Menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
    }

    private void updateEntitiesPositions(float delta) {
        // X and Y are calculated separately to avoid going through walls diagonally

        float nextX = playerEntity.position.x + playerEntity.velocity.x * delta;
        float currentY = playerEntity.position.y;

        if (!this.gameField.isInsideWall(nextX, currentY, this.playerEntity.getSize())) {
            this.playerEntity.position.x = nextX;
        }

        float currentX = playerEntity.position.x;
        float nextY = playerEntity.position.y + playerEntity.velocity.y * delta;

        if (!this.gameField.isInsideWall(currentX, nextY, this.playerEntity.getSize())) {
            playerEntity.position.y = nextY;
        }
    }

    private void moveCamera(float delta) {
        camera.position.x = playerEntity.position.x;
        camera.position.y = playerEntity.position.y;
        camera.update();
    }

    private void drawGameField() {
        for (int y = 0; y < gameField.getHeight(); y++) {
            for (int x = 0; x < gameField.getWidth(); x++) {
                TileType tileType = gameField.getTile(x, y);
                if (tileType == TileType.WALL) {
                    game.getSpriteBatch().draw(
                            game.getWallTile(),
                            x, y, 1, 1
                    );
                } else if (tileType == TileType.EMPTY) {
                    game.getSpriteBatch().draw(
                            game.getEmptyTile(),
                            x, y, 1, 1
                    );
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    private void renderHUD(){
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

    private void updatePlayerHP(int damage){
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
    private void updatePlayerStatusEffects(){
       // statusEffectsTable.clearChildren();


// 4. Tell the table to reorganize
//        statusEffectsTable.invalidateHierarchy();
        if(player.isBuffed()){
            drawRing(new Image(game.getBloodEffect()), player.getBuffDuration(), "Buffed");
//            Image blood = new Image(game.getBloodEffect());
//            statusEffectsTable.add(blood).height(60).width(60).padTop(20);
//            blood.addAction(Actions.sequence(
//
//                    Actions.delay(player.getBuffDuration()),
//                    Actions.run(new Runnable() {
//                        @Override
//                        public void run() {
//                            player.setBuffed(false);
//                            updatePlayerStatusEffects();
//                        }
//                    })));

        }
        if(player.isPoisoned()){
           drawRing(new Image(game.getPoisonEffect()), player.getPoisonDuration(), "Poisoned");

//            poison.addAction(Actions.sequence(
//
//                    Actions.delay(player.getPoisonDuration()),
//                    Actions.run(new Runnable() {
//                        @Override
//                        public void run() {
//                            player.setPoisoned(false);
//                            updatePlayerStatusEffects();
//                        }
//                    })));
//            statusEffectsTable.add(poison).height(60).width(60).padTop(20);

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

                Gdx.gl.glLineWidth(4);

                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(Color.GREEN);

                float r = Math.min(getWidth(), getHeight())/1.8f;
                float cx = getX() + getWidth() / 2f;
                float cy = getY() + getHeight() / 2f;

                float percentage = timeLeft / maxTime;

                // 3. Manual Loop Calculation
                // We want 30 segments for smoothness. Only draw if percentage > 0.
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
                //  Gdx.gl.glLineWidth(1);
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
    // Additional methods and logic can be added as needed for the game screen
}
