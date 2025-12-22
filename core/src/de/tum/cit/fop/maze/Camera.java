package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import static java.lang.Math.max;

public class Camera {
    private static final float VIEWPORT_WIDTH = 25f;  // Visible world width
    private static final float VIEWPORT_HEIGHT = 18.75f; // Visible world height
    private static final float ZOOM_LEVEL = 0.75f;
    private static final float CAMERA_LERP_SPEED = 3f;

    private final OrthographicCamera camera;
    private final GameScreen gameScreen;

    public Camera(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        this.camera.zoom = ZOOM_LEVEL;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void update(float delta) {
        // Make sure camera stays inside maze always
        float minCameraX = VIEWPORT_WIDTH * ZOOM_LEVEL / 2;
        float minCameraY = VIEWPORT_HEIGHT * ZOOM_LEVEL / 2;
        float maxCameraX = this.gameScreen.getGameField().getWidth()  - VIEWPORT_WIDTH * ZOOM_LEVEL / 2;
        float maxCameraY = this.gameScreen.getGameField().getHeight()  - VIEWPORT_HEIGHT * ZOOM_LEVEL / 2;
        float playerX = this.gameScreen.getPlayer().getPlayerEntity().position.x;
        float playerY = this.gameScreen.getPlayer().getPlayerEntity().position.y;
        float target_x = MathUtils.clamp(playerX, minCameraX, maxCameraX);
        float target_y = MathUtils.clamp(playerY, minCameraY, maxCameraY);
        this.camera.position.x = MathUtils.lerp(this.camera.position.x, target_x, delta * CAMERA_LERP_SPEED);
        this.camera.position.y = MathUtils.lerp(this.camera.position.y, target_y, delta * CAMERA_LERP_SPEED);
        this.camera.update();
    }
}
