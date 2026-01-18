package de.tum.cit.fop.maze.game_saves.mementos;

public class BouncerMemento extends GameObjectMemento {
    public int lives;

    public BouncerMemento() {
        super();
    }

    public BouncerMemento(float positionX, float positionY, float velocityX, float velocityY, 
                          float speed, String animationType, int lives) {
        super(positionX, positionY, velocityX, velocityY, speed, animationType, "Bouncer");
        this.lives = lives;
    }
}
