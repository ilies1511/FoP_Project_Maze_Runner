package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class Camera {
    private static final float VIEWPORT_WIDTH = 25f;  // Visible world width
    private static final float VIEWPORT_HEIGHT = 18.75f; // Visible world height
    private static final float INITIAL_ZOOM_LEVEL = 0.75f;
    private static final float ZOOM_CHANGE_MULTIPLIER = 1.01f;
    private static final float MAXIMUM_ZOOM = 1.1f;
    private static final float CAMERA_LERP_SPEED = 3f;

    private final OrthographicCamera camera;
    private final GameScreen gameScreen;

    public Camera(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        this.camera.zoom = INITIAL_ZOOM_LEVEL;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void update(float delta) {
        // Make sure camera stays inside maze always
        float minCameraX = VIEWPORT_WIDTH * this.camera.zoom / 2;
        float minCameraY = VIEWPORT_HEIGHT * this.camera.zoom / 2 - 1.6f;
        float maxCameraX = this.gameScreen.getGameField().getWidth() - VIEWPORT_WIDTH * this.camera.zoom / 2;
        float maxCameraY = this.gameScreen.getGameField().getHeight() - VIEWPORT_HEIGHT * this.camera.zoom / 2;
        float playerX = this.gameScreen.getPlayer().getPlayerGameObject().position.x;
        float playerY = this.gameScreen.getPlayer().getPlayerGameObject().position.y;
        float target_x = 0f;
        if (minCameraX < maxCameraX) {
            target_x = MathUtils.clamp(playerX, minCameraX, maxCameraX);
        } else {
            target_x = minCameraX;
        }
        float target_y = 0f;
        if (minCameraY < maxCameraY) {
            target_y = MathUtils.clamp(playerY, minCameraY, maxCameraY);
        } else {
            target_y = minCameraY;
        }
        this.camera.position.x = MathUtils.lerp(this.camera.position.x, target_x, delta * CAMERA_LERP_SPEED);
        this.camera.position.y = MathUtils.lerp(this.camera.position.y, target_y, delta * CAMERA_LERP_SPEED);
        this.camera.update();
    }

    public void zoom_out() {
        if (this.camera.zoom < MAXIMUM_ZOOM) {
            this.camera.zoom *= ZOOM_CHANGE_MULTIPLIER;
        }
    }

    public void zoom_in() {
        this.camera.zoom /= ZOOM_CHANGE_MULTIPLIER;
    }
}
