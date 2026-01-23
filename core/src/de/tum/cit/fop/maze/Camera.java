package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Manages the game camera, including following the player, zooming, and shake effects.
 * It handles the camera's position and updates it based on game logic.
 */
public class Camera {
    private static final float VIEWPORT_WIDTH = 25f;  // Visible world width
    private static final float VIEWPORT_HEIGHT = 18.75f; // Visible world height
    private static final float INITIAL_ZOOM_LEVEL = 0.75f;
    private static final float ZOOM_CHANGE_MULTIPLIER = 1.01f;
    private static final float MAXIMUM_ZOOM = 1.1f;
    private static final float CAMERA_LERP_SPEED = 3f;
    private static final float SHAKE_SPEED = 30f;
    private final OrthographicCamera camera;
    private boolean zooming;
    private float currentShakeTime = 0f; // when currentShakeTime > 0, camera is going to shake
    
    private float currentShakeDurationTotal = 0f;

    private float currentShakePowerDegrees = 0f;


    /**
     * Creates a new Camera instance with default settings.
     */
    public Camera() {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        this.camera.zoom = INITIAL_ZOOM_LEVEL;

    }

    /**
     * Gets the underlying OrthographicCamera instance.
     * @return The LibGDX OrthographicCamera.
     */
    public OrthographicCamera getCamera() {
        return this.camera;
    }

    /**
     * Updates the camera position to follow the player and handle effects.
     * @param delta The time elapsed since the last frame.
     * @param playerPos The current position of the player.
     * @param gameFieldWidth The width of the game field.
     * @param gameFieldHeight The height of the game field.
     */
    public void update(float delta, Vector2 playerPos, int gameFieldWidth, int gameFieldHeight) {
        // Make sure camera stays inside maze always
        if (VIEWPORT_WIDTH > gameFieldWidth && !zooming) {
            camera.zoom = gameFieldWidth / VIEWPORT_WIDTH;
            camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 4.0f);
        } else if (VIEWPORT_WIDTH <= gameFieldWidth && !zooming) {
            camera.zoom = INITIAL_ZOOM_LEVEL;
            camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 4.0f);

        }
        float minCameraX = VIEWPORT_WIDTH * this.camera.zoom / 2;
        float minCameraY = VIEWPORT_HEIGHT * this.camera.zoom / 2;
        float maxCameraX = gameFieldWidth - VIEWPORT_WIDTH * this.camera.zoom / 2;
        float maxCameraY = gameFieldHeight - VIEWPORT_HEIGHT * this.camera.zoom / 2;
        float playerX = playerPos.x;
        float playerY = playerPos.y;
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

        shakeCamera(delta);

        this.camera.update();
    }

    /**
     * Zooms the camera out by a small factor.
     */
    public void zoom_out() {
        zooming = true;
        if (this.camera.zoom < MAXIMUM_ZOOM) {
            this.camera.zoom *= ZOOM_CHANGE_MULTIPLIER;
            camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 4.0f);
        }
    }

    /**
     * Zooms the camera in by a small factor.
     */
    public void zoom_in() {

        zooming = true;
        this.camera.zoom /= ZOOM_CHANGE_MULTIPLIER;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 4.0f);
    }


    /**
     * Triggers a camera shake effect.
     * @param shake_duration The duration of the shake in seconds.
     * @param shake_power_degrees The intensity of the shake in degrees.
     */
    public void shake(float shake_duration, float shake_power_degrees) {
        this.currentShakePowerDegrees = shake_power_degrees;
        this.currentShakeTime = shake_duration;
        this.currentShakeDurationTotal = shake_duration;
    }

    private void shakeCamera(float delta) {
        this.currentShakeTime -= delta;
        if (this.currentShakeTime < 0f) {
            setCameraRotation(0f);
        } else {
            // damped sine wave
            float decayFactor = 1.0f - (this.currentShakeTime / this.currentShakeDurationTotal);
            float oscillation = MathUtils.cos(this.currentShakeTime * SHAKE_SPEED);
            float currentAngle = this.currentShakePowerDegrees * oscillation * decayFactor;
            setCameraRotation(currentAngle);
        }
    }

    private void setCameraRotation(float angle) {
        this.camera.up.set(0, 1, 0);
        this.camera.direction.set(0, 0, -1);
        this.camera.rotate(angle);
    }
}
