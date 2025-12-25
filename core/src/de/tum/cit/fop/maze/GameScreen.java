package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.Drawing.Drawable;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.MazeParser;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.UI.DevConsole;
import de.tum.cit.fop.maze.UI.HUD;
import de.tum.cit.fop.maze.UI.PausedOverlay;
import de.tum.cit.fop.maze.UI.ProgressionOverlay;
import de.tum.cit.fop.maze.effects.Dash;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.GameObjectTemp;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.collectables.Key;

import java.util.ArrayList;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final Camera camera;
    private final BitmapFont font;
    private final Player player;
    private final Stage stage;
    private final GameObject playerGameObject;
    private final HUD hud;
    private final PausedOverlay pausedScreen;
    private final ProgressionOverlay progressionOverlay;
    private final DevConsole devConsole;
    private final SoundController soundController;
    private float flickerTimer = 0f;
    private GameField gameField;
    private ArrayList<Enemy> enemies;
    private ArrayList<Collectable> collectables;
    private boolean isPaused = false;
    private boolean isInProgression = false;
    private boolean isInDevConsole = false;


//  For testing

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     */

    public GameScreen(MazeRunnerGame game) {


        this.game = game;

        Drawable.initializeTextureMap(game);

        allEnemies();
        allCollectables();

        this.gameField = MazeParser.loadMap("level-1.properties", this.game);
        if (this.gameField == null) {
            System.err.println("Map failed to load! Check file path.");
            this.gameField = new GameField(5, 5);
        } else {
            System.out.println("Map loaded successfully!");
            System.out.println("Size: " + gameField.getWidth() + "x" + gameField.getHeight());
        }

        this.player = new Player(this); //create an instance of the player
        this.playerGameObject = this.player.getPlayerGameObject();

        // Create and configure the camera for the game view
        this.camera = new Camera(this);

        // Get the font from the game's skin
        font = game.getSkin().getFont("vampire-font");

        Viewport viewport = new FitViewport(2000, 1200); // TODO maybe add camera in the brackets
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        this.hud = new HUD(player, stage, game, this);
        pausedScreen = new PausedOverlay(stage, game, this);
        progressionOverlay = new ProgressionOverlay(stage, game, this, player, hud);
        devConsole = new DevConsole(game, this, stage, player, hud);
        soundController = new SoundController();
        soundController.playBackgroundMusic(0.5f);
//        shapeRenderer = new ShapeRenderer();

    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE) && !isInDevConsole) {
            devConsole.showConsole();
            isInDevConsole = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE) && isInDevConsole) {
            devConsole.hideConsole();
            isInDevConsole = false;
        }
        if (!isPaused) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                pausedScreen.showPausedMenu();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !isInDevConsole) {
                progressionOverlay.showProgressionMenu();
                isPaused = true;
                isInProgression = true;
            }
            ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

            // Tick all the effects
            this.player.tickEffects(delta);

            // Input Handling New Class
            handleInput(delta);

            // Move all the entities
            updateEntitiesPositions(delta);

            // Update camera to move with the player
            this.camera.update(delta);

            // Update all the animations
            this.player.updateAnimation(delta);
            for (Enemy enemy : enemies) {
                enemy.updateAnimation(delta);
            }
            for (Collectable collectable : collectables) {
                collectable.updateAnimation(delta);
            }

        } else if (isPaused) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isInProgression) {
                pausedScreen.hidePausedMenu();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.I) && isInProgression) {
                progressionOverlay.hideProgressionMenu();
                isInProgression = false;
            }

        }

        game.getSpriteBatch().setProjectionMatrix(camera.getCamera().combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        drawGameField();

        //TODO (Connect Parser to Game Assets): iziane
        for (GameObjectTemp obj : gameField.getGameObjects()) {
            obj.draw(game.getSpriteBatch());
        }

        // Draw the character next to the text :) / We can reuse sinusInput here
        if (flicker(delta)) {
            player.animate(game.getSpriteBatch());
        }

        for (Enemy enemy : enemies) {
            enemy.animate(game.getSpriteBatch());
        }

        for (Collectable collectable : collectables) {
            collectable.animate(game.getSpriteBatch());
        }

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public GameField getGameField() {
        return gameField;
    }

    private void handleInput(float delta) {
        boolean isMoving = false;
        soundController.stopSteppingSound();
        // Dash
        if ((Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getDashKey())) && player.isCanDash()) {
            this.player.addOrReapplyEffect(new Dash(player));
        }

        // Y-Axis
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
            player.getPlayerGameObject().velocity.y = player.getPlayerGameObject().getSpeed();
            this.player.changeAnimation(AnimationType.CHARACTER_UP);
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            player.getPlayerGameObject().velocity.y = -player.getPlayerGameObject().getSpeed();
            this.player.changeAnimation(AnimationType.CHARACTER_DOWN);
            isMoving = true;
        } else {
            player.getPlayerGameObject().velocity.y = 0f;
        }

        // X-Axis (Using 'else if' prevents diagonal animation glitches, though movement is still diagonal)
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            player.getPlayerGameObject().velocity.x = -player.getPlayerGameObject().getSpeed();
            this.player.changeAnimation(AnimationType.CHARACTER_LEFT);
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            player.getPlayerGameObject().velocity.x = player.getPlayerGameObject().getSpeed();
            this.player.changeAnimation(AnimationType.CHARACTER_RIGHT);
            isMoving = true;
        } else {
            player.getPlayerGameObject().velocity.x = 0f;
        }
        if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
            SoundController.playPlayerHitSound(1);
            System.out.println("Attacking!"); // Debug print

            var iterator = enemies.iterator();
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();

                // distance between Player and Enemy
                float distance = player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position);

                //if enemy within 1.5, kill
                if (distance < 1.5f) {
                    iterator.remove(); // remove enemy from the game
                    hud.updatePlayerXP(15);
                    System.out.println("Enemy Killed!");
                }
            }
        }

        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomInKey())) {
            this.camera.zoom_in();
        }

        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getZoomOutKey())) {
            this.camera.zoom_out();
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
            this.player.changeAnimation(AnimationType.CHARACTER_VICTORY_POSE);
            SoundController.playWinSound(1);
            isMoving = true;
        }

        // Loser pose
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            this.player.changeAnimation(AnimationType.CHARACTER_LOSER_POSE);
            SoundController.playPlayerDieSound(1);
            isMoving = true;
        }

        // Animation Timer
        if (isMoving) {
            this.player.updateAnimation(delta);
            soundController.playSteppingSound(1);

        } else {
            this.player.setAnimationTime(0f);
        }

        // Go to Menu

    }

    private void updateEntitiesPositions(float delta) {
        // X and Y are calculated separately to avoid going through walls diagonally
        float velocityX = player.getPlayerGameObject().velocity.x;
        float velocityY = player.getPlayerGameObject().velocity.y;
        float maxSpeed = player.getPlayerGameObject().getSpeed();

        float diagonalMultiplier = 1.0f; // so that diagonal movement is not faster than normal one
        float currentSpeedSqr = velocityX * velocityX + velocityY * velocityY;

        if (currentSpeedSqr > (maxSpeed * maxSpeed) + 0.001f) {
            float currentSpeed = (float) Math.sqrt(currentSpeedSqr);
            diagonalMultiplier = maxSpeed / currentSpeed;
        }

        float moveAmountX = velocityX * diagonalMultiplier * delta;
        float moveAmountY = velocityY * diagonalMultiplier * delta;

        float nextX = player.getPlayerGameObject().position.x + moveAmountX;
        float currentY = player.getPlayerGameObject().position.y;

        if (!this.gameField.isInsideWall(nextX, currentY, this.player.getPlayerGameObject().getSize())) {
            this.player.getPlayerGameObject().position.x = nextX;
        }

        float currentX = player.getPlayerGameObject().position.x;
        float nextY = player.getPlayerGameObject().position.y + player.getPlayerGameObject().velocity.y * delta;

        if (!this.gameField.isInsideWall(currentX, nextY, this.player.getPlayerGameObject().getSize())) {
            player.getPlayerGameObject().position.y = nextY;
        }

        var iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            GameObject enemyGameObject = enemy.getEnemyEntity(); // Get Character

            enemy.updateBehavior(delta, player.getPlayerGameObject().position, gameField);
            Vector2 nextPos = enemyGameObject.nextPosition(delta);

            //gameField.getTile((int) nextPos.x, (int) nextPos.y) == TileType.EMPTY to prev test
            if (!gameField.isInsideWall(nextPos.x, nextPos.y, enemyGameObject.getSize())) {
                enemyGameObject.move(delta);
            }

            // Check if they touch
            if (GameObject.entitiesOverlap(playerGameObject, enemyGameObject)) {
                if (enemy instanceof FleeingEnemy) {
                    //if a tourist(fleeing Enemy)
                    Vector2 pushDir = new Vector2(playerGameObject.position).sub(enemyGameObject.position).nor();
                    Vector2 pushAmount = new Vector2(pushDir).scl(0.2f); // push by 0.2

                    float newX = playerGameObject.position.x + pushAmount.x;
                    float newY = playerGameObject.position.y + pushAmount.y;

                    //if player not near wall
                    if (!gameField.isInsideWall(newX, newY, playerGameObject.getSize())) {
                        playerGameObject.position.x = newX;
                        playerGameObject.position.y = newY;
                    }
                    //otherwise same
                    playerGameObject.position.sub(playerGameObject.velocity.x * delta, playerGameObject.velocity.y * delta);
                } else {
                    if (flickerTimer <= 0 && !player.isCanInvis()) {    //canInvis to be implimented here
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

    private void allEnemies() {
        this.enemies = new ArrayList<>();
        //this.enemies.add(new Enemy(new Vector2(10f, 3f)));
        //this.enemies.add(new PatrolEnemy(new Vector2(5f, 5f)));
        //this.enemies.add(new PatrolEnemy(new Vector2(15f, 5f)));
        this.enemies.add(new FleeingEnemy(new Vector2(7f, 7f)));
        //this.enemies.add(new Bouncer(new Vector2(7f, 7f)));
    }

    private void allCollectables() {
        this.collectables = new ArrayList<>();
        this.collectables.add(new Key(new Vector2(5.5f - 0.5f, 5.5f - 0.71f))); // substract half of sizes
    }

    public boolean flicker(float delta) {
        if (flickerTimer > 0f) {
            flickerTimer -= delta;
        }
        //player visible if timer is 0 or if flickering
        return ((flickerTimer <= 0) || (flickerTimer % 0.2f > 0.1f));
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
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    // Additional methods and logic can be added as needed for the game screen
}
