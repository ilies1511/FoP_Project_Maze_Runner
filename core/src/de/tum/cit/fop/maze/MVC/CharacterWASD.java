package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.tum.cit.fop.maze.Drawing.AnimationType;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.SoundController;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;

import java.util.ListIterator;

public class CharacterWASD {
    public  CharacterWASD() {}
    public static void basic2d(Player player, Boolean isMoving, MazeRunnerGame game) {
        // Y
        float speed = player.getPlayerGameObject().getSpeed();
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getUpKey())) {
            player.getPlayerGameObject().velocity.y = speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_UP);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_UP);
            }
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getDownKey())) {
            player.getPlayerGameObject().velocity.y = -speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_DOWN);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_DOWN);
            }
            isMoving = true;
        }
        else {
            player.getPlayerGameObject().velocity.y = 0f;
        }

        // X
        if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getLeftKey())) {
            player.getPlayerGameObject().velocity.x = -speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_LEFT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_LEFT);
            }
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(game.getGameInfo().getSettings().getRightKey())) {
            player.getPlayerGameObject().velocity.x = speed;
            if(player.isInvis()){
                player.changeAnimation(AnimationType.CHARACTER_INVIS_RIGHT);
            }
            else{
                player.changeAnimation(AnimationType.CHARACTER_RIGHT);
            }
            isMoving = true;
        }
        else {
            player.getPlayerGameObject().velocity.x = 0f;
        }

    }

    public static void basicAttack(Player player, MazeRunnerGame game, GameField gameField, SoundController soundController) {
        if (Gdx.input.isKeyJustPressed(game.getGameInfo().getSettings().getAttackKey())) {
            player.setAttacking(true);
            player.setInvis(false); // attacking breaks invisibility
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
            soundController.playPlayerHitSound(1);
            System.out.println("Attacking!");

            ListIterator<GameObject> iter = gameField.getGameObjects().listIterator();
            while (iter.hasNext()) {
                GameObject obj = iter.next();
                if (obj instanceof Enemy) {
                    Enemy enemy = (Enemy) obj;
                    if (player.getPlayerGameObject().position.dst(enemy.getEnemyEntity().position) < 1.5f) {
                        iter.remove();
                        Collectable drop = enemy.onDeath(player);
                        iter.add(drop);
                        System.out.println("Enemy Killed!");
                    }
                }
            }
            return;
        }
    }
}
