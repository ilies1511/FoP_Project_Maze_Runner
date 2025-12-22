package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.effects.Poisoned;

import java.util.Objects;

import java.util.ArrayList;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final Camera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;
    private float animationTime = 0f;
    private float flickerTimer = 0f;

    private GameField gameField;

    private Animation<TextureRegion> currentAnimation;

    private final Player player;


    private final Stage stage;

    private ArrayList<Enemy> enemies;

    private final HUD hud;
    private final PausedOverlay pausedScreen;

    private boolean isPaused = false;


//  For testing
    int i = 0;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        this.enemies = new ArrayList<>();
        //this.enemies.add(new Enemy(new Vector2(10f, 3f)));
        this.enemies.add(new patrolEnemy(new Vector2(5f, 5f)));
        this.enemies.add(new patrolEnemy(new Vector2(15f, 5f)));


	    this.gameField = MazeParser.loadMap("level-1.properties", this.game);
	    if (this.gameField == null) {
		    System.err.println("Map failed to load! Check file path.");
		    this.gameField = new GameField(5, 5);
	    }
	    else {
		    System.out.println("Map loaded successfully!");
		    System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
	    }

        this.player = new Player(this); //create an instance of the player

        player.addOrReapplyEffect(new Dash(player)); // temp for debug

        // Create and configure the camera for the game view
        this.camera = new Camera(this);

        // Start by looking down
        currentAnimation = game.getCharacterDownAnimation();

        // Get the font from the game's skin
        font = game.getSkin().getFont("vampire-font");

        Viewport viewport = new FitViewport(2000,1200); // TODO maybe add camera in the brackets
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        this.hud = new HUD(player, stage, game, this);
        pausedScreen = new PausedOverlay(stage, game,this);
//        shapeRenderer = new ShapeRenderer();

    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        if(!isPaused){
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                pausedScreen.showPausedMenu();
            }
            ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

            // Input Handling New Class
            handleInput(delta);
        // Tick all the effects
        this.player.tickEffects(delta);

        // Input Handling New Class
        handleInput(delta);

            // Move all the entities
            updateEntitiesPositions(delta);

            // Update camera to move with the player
            this.camera.update(delta);
            animationTime += delta;
            i++;
//            if(i == 1){
//                player.getPoisoned(5f);
//                player.getBuffed(2f);
//                hud.updatePlayerStatusEffects();
//                hud.updatePlayerHP(player.getDamage(1));
//
//            }
            // Set up and begin drawing with the sprite batch

        }
       else if(isPaused){
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                pausedScreen.hidePausedMenu();
            }

        }

        game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        if (flickerTimer > 0f) {
            flickerTimer -= delta;
        }
        //player visible if timer is 0 or if flickering
        boolean isVisible = (flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f);

        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        drawGameField();


        // Draw the character next to the text :) / We can reuse sinusInput here
        if (isVisible) {
        game.getSpriteBatch().draw(
                currentAnimation.getKeyFrame(animationTime, true),
                player.getPlayerEntity().position.x,
                player.getPlayerEntity().position.y,
                0.4f,
                0.8f
        );}

        for (Enemy enemy : enemies) {
            game.getSpriteBatch().draw(
                    game.getCharacterDownSad().getKeyFrame(animationTime, true), // Static Victory for testing
                    enemy.getEnemyEntity().position.x, enemy.getEnemyEntity().position.y,
                    0.5f,   0.8f
            );
        }

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

// For Testing
//        i++;
//        if(i == 1){
//           player.getPoisoned(5f);
//           player.getBuffed(2f);
//           hud.updatePlayerStatusEffects();
//           hud.updatePlayerHP(player.getDamage(1));
//
//        }
    }

    public GameField getGameField() {
        return gameField;
    }

    private void handleInput(float delta) {
        boolean isMoving = false;

        if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) {
            this.player.addOrReapplyEffect(new Dash(player));
        }

        // Temporary: keybinding for poisoned // TODO: remove it
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            this.player.addOrReapplyEffect(new Poisoned(player));
            hud.updatePlayerStatusEffects();
        }

        // --- MOVEMENT LOGIC ---

        // Y-Axis
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
              player.getPlayerEntity().velocity.y = player.getPlayerEntity().getSpeed();
//            yPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterUpAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            player.getPlayerEntity().velocity.y = -player.getPlayerEntity().getSpeed();
//            yPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterDownAnimation();
            isMoving = true;
        }

        else {
            player.getPlayerEntity().velocity.y = 0f;
        }

        // X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            player.getPlayerEntity().velocity.x = -player.getPlayerEntity().getSpeed();
//            xPlayer -= currentSpeed * delta;
            currentAnimation = game.getCharacterLeftAnimation();
            isMoving = true;
        }

        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            player.getPlayerEntity().velocity.x = player.getPlayerEntity().getSpeed();
//            xPlayer += currentSpeed * delta;
            currentAnimation = game.getCharacterRightAnimation();
            isMoving = true;
        }

        else {
            player.getPlayerEntity().velocity.x = 0f;
        }
        if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
            System.out.println("Attacking!"); // Debug print

            var iterator = enemies.iterator();
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();

                // distance between Player and Enemy
                float distance = player.getPlayerEntity().position.dst(enemy.getEnemyEntity().position);

                //if enemy within 1.5, kill
                if (distance < 1.5f) {
                    iterator.remove(); // remove enemy from the game
                    System.out.println("Enemy Killed!");
                }
            }
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

    }

    private void updateEntitiesPositions(float delta) {
        // X and Y are calculated separately to avoid going through walls diagonally
        float velocityX = player.getPlayerEntity().velocity.x;
        float velocityY = player.getPlayerEntity().velocity.y;
        float maxSpeed = player.getPlayerEntity().getSpeed();

        float diagonalMultiplier = 1.0f; // so that diagonal movement is not faster than normal one
        float currentSpeedSqr = velocityX * velocityX + velocityY * velocityY;

        if (currentSpeedSqr > (maxSpeed * maxSpeed) + 0.001f) {
            float currentSpeed = (float) Math.sqrt(currentSpeedSqr);
            diagonalMultiplier = maxSpeed / currentSpeed;
        }

        float moveAmountX = velocityX * diagonalMultiplier * delta;
        float moveAmountY = velocityY * diagonalMultiplier * delta;

        float nextX = player.getPlayerEntity().position.x + moveAmountX;
        float currentY = player.getPlayerEntity().position.y;

        if (!this.gameField.isInsideWall(nextX, currentY, this.player.getPlayerEntity().getSize())) {
            this.player.getPlayerEntity().position.x = nextX;
        }

        float currentX = player.getPlayerEntity().position.x;
        float nextY = player.getPlayerEntity().position.y + moveAmountY;

        if (!this.gameField.isInsideWall(currentX, nextY, this.player.getPlayerEntity().getSize())) {
            player.getPlayerEntity().position.y = nextY;
        }

        var iterator = enemies.iterator();
        while(iterator.hasNext()){
            Enemy enemy = iterator.next();
            Entity enemyEntity = enemy.getEnemyEntity(); // Get Character

            enemy.updateBehavior(delta, player.getPlayerEntity().position, gameField);
            Vector2 nextPos = enemyEntity.nextPosition(delta);

            //gameField.getTile((int) nextPos.x, (int) nextPos.y) == TileType.EMPTY to prev test
            if (!gameField.isInsideWall(nextPos.x, nextPos.y, enemyEntity.getSize())) {
                enemyEntity.move(delta);
            }

            // Check if they touch
            if (Entity.entitiesOverlap(player.getPlayerEntity(), enemyEntity)) {
                if (flickerTimer <= 0){
                    // print to console. .
                    System.out.println("YOU DIED!");
                    iterator.remove();
//                pdatePlayerHealth(1);
                    hud.updatePlayerHP(1);
                    flickerTimer = 3f;
                }

            }
        }
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

    public Player getPlayer() {
        return player;
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

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isPaused() {
        return isPaused;
    }
    // Additional methods and logic can be added as needed for the game screen
}
