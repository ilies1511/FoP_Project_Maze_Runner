package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import static java.lang.Math.max;

public class Camera {
    private static final float VIEWPORT_WIDTH = 25f;  // Visible world width
    private static final float VIEWPORT_HEIGHT = 18.75f; // Visible world height
    private static final float ZOOM_LEVEL = 0.75f;

    private OrthographicCamera camera;
    private GameScreen gameScreen;

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
        float minCameraX = VIEWPORT_WIDTH * ZOOM_LEVEL / 2;
        float minCameraY = VIEWPORT_HEIGHT * ZOOM_LEVEL / 2;
        float maxCameraX = this.gameScreen.getGameField().getWidth()  - VIEWPORT_WIDTH * ZOOM_LEVEL / 2;
        float maxCameraY = this.gameScreen.getGameField().getHeight()  - VIEWPORT_HEIGHT * ZOOM_LEVEL / 2;
        float playerX = this.gameScreen.getPlayer().getPlayerEntity().position.x;
        float playerY = this.gameScreen.getPlayer().getPlayerEntity().position.y;
        this.camera.position.x = MathUtils.clamp(playerX, minCameraX, maxCameraX);
        this.camera.position.y = MathUtils.clamp(playerY, minCameraY, maxCameraY);
        this.camera.update();
    }
}
