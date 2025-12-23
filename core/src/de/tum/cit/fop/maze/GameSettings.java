package de.tum.cit.fop.maze;

import com.badlogic.gdx.Input;

public class GameSettings {
    private int maxHealth;
    private int upKey;
    private int leftKey;
    private int downKey;
    private int rightKey;
    private int attackKey;
    private int dashKey;
    private int zoomInKey;
    private int zoomOutKey;

    public GameSettings() {
        this.maxHealth = 10;
        this.upKey = Input.Keys.W;
        this.leftKey = Input.Keys.A;
        this.downKey = Input.Keys.S;
        this.rightKey = Input.Keys.D;
        this.attackKey = Input.Keys.SPACE;
        this.dashKey = Input.Keys.SHIFT_LEFT;
        this.zoomInKey = Input.Keys.EQUALS;
        this.zoomOutKey = Input.Keys.MINUS;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getUpKey() {
        return this.upKey;
    }

    public int getLeftKey() {
        return this.leftKey;
    }

    public int getDownKey() {
        return this.downKey;
    }

    public int getRightKey() {
        return this.rightKey;
    }

    public int getDashKey() {
        return this.dashKey;
    }

    public int getAttackKey() {
        return this.attackKey;
    }

    public int getZoomInKey() {
        return this.zoomInKey;
    }
    public int getZoomOutKey() {
        return this.zoomOutKey;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
    }

    public void setDashKey(int dashKey) {
        this.dashKey = dashKey;
    }

    public void setAttackKey(int attackKey) {
        this.attackKey = attackKey;
    }

    public void setZoomInKey(int zoomInKey) {
        this.zoomInKey = zoomInKey;
    }

    public void setZoomOutKey(int zoomOutKey) {
        this.zoomOutKey = zoomOutKey;
    }
}
