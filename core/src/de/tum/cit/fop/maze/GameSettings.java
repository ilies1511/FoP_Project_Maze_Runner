package de.tum.cit.fop.maze;

import com.badlogic.gdx.Input;

public class GameSettings {
    private int maxHealth;
    private int upKey;
    private int leftKey;
    private int downKey;
    private int rightKey;

    public GameSettings() {
        this.maxHealth = 10;
        this.upKey = Input.Keys.W;
        this.leftKey = Input.Keys.A;
        this.downKey = Input.Keys.S;
        this.rightKey = Input.Keys.D;
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
}
