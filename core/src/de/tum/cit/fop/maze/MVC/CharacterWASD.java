package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.effects.Invis;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.FleeingEnemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;

import java.util.ListIterator;

public class CharacterWASD {
    public  CharacterWASD() {}
    public static boolean yAxis(Player player, MazeRunnerGame game) {
        boolean isXmoving = Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey()) || Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey());
        float speed = player.getPlayerGameObject().getSpeed();
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
            player.getPlayerGameObject().velocity.y = speed;
            if (!isXmoving) {
                if (player.hasEffect(Invis.class)) {
                    player.changeAnimation(AnimationType.CHARACTER_INVIS_UP);
                } else {
                    player.changeAnimation(AnimationType.CHARACTER_UP);
                }
            }
            return true;
        } else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            player.getPlayerGameObject().velocity.y = -speed;
            if (!isXmoving) {
                if (player.hasEffect(Invis.class)) {
                    player.changeAnimation(AnimationType.CHARACTER_INVIS_DOWN);
                } else {
                    player.changeAnimation(AnimationType.CHARACTER_DOWN);
                }
            }
            return true;
        }
        else {
            player.getPlayerGameObject().velocity.y = 0f;
            return false;
        }
    }

    public static boolean xAxis(Player player, MazeRunnerGame game) {
        float speed = player.getPlayerGameObject().getSpeed();
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            player.getPlayerGameObject().velocity.x = -speed;
            if(player.hasEffect(Invis.class)){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LEFT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_LEFT);
            }
            return true;
        }
        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            player.getPlayerGameObject().velocity.x = speed;
            if(player.hasEffect(Invis.class)){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_RIGHT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_RIGHT);
            }
            return true;
        }
        else {
            player.getPlayerGameObject().velocity.x = 0f;
            return false;
        }
    }
    public static void attackChange(Player player, MazeRunnerGame game) {
        Animation<?> currentAnim = player.getDrawable().getCurrentAnimation();

        if (currentAnim == game.getCharacterUpAnimation() || currentAnim == game.getCharacterInvisUpAnimation()) {
            player.changeAnimation(AnimationType.CHARACTER_ATTACK_UP);
        } else if (currentAnim == game.getCharacterLeftAnimation() || currentAnim == game.getCharacterInvisLeftAnimation()) {
            player.changeAnimation(AnimationType.CHARACTER_ATTACK_LEFT);
        } else if (currentAnim == game.getCharacterRightAnimation() || currentAnim == game.getCharacterInvisRightAnimation()) {
            player.changeAnimation(AnimationType.CHARACTER_ATTACK_RIGHT);
        } else {
            // idle
            player.changeAnimation(AnimationType.CHARACTER_ATTACK_DOWN);
        }
        System.out.println("Attacking!");
    }
}
