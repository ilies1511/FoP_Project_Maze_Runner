package de.tum.cit.fop.maze.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.effects.Buff;
import de.tum.cit.fop.maze.effects.GodMode;
import de.tum.cit.fop.maze.effects.Poison;
import de.tum.cit.fop.maze.objects.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Developer console for debugging and testing.
 * Allows executing commands to modify game state and player stats.
 */
public class DevConsole {
    private final Map<String, Consumer<String>> commandMap = new HashMap<>();
    private final Stage stage;
    private final Table table;
    private final String setCommands;
    private final String getCommands;
    private final Player player;
    private final TextField inputField;
    private final MazeRunnerGame game;
    private final HUD hud;
    private final GameScreen gameScreen;


    /**
     * Constructs a DevConsole.
     *
     * @param game       The main game instance.
     * @param gameScreen The game screen instance.
     * @param hud        The HUD instance for UI updates.
     * @param stage      The stage to add the console to.
     * @param player     The player object to modify.
     */
    public DevConsole(MazeRunnerGame game, GameScreen gameScreen, HUD hud, Stage stage, Player player) {
        this.game = game;
        this.hud = hud;
        this.gameScreen = gameScreen;

        setCommands = "XP|HP|SpeedMultiplier|Dash|Invis|GodMode";
        getCommands = "Buff|Debuff|Keys|Help";

        String regex = String.format(
                "^(?=(?:set(?:%s):\\d+|get(?:%s))$)([a-zA-Z]+)(?::(\\d+))?$",
                setCommands,
                getCommands
        );


        Pattern pattern = Pattern.compile(regex);

        this.player = player;

        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);
        table.top().left();


        Label consoleLabel = new Label("Developer console \ntype \"getHelp\" for the list of commands", game.getSkin(), "title");
        consoleLabel.setFontScale(0.4f);
        consoleLabel.setWrap(true); // Important: Allows text to wrap to next line
        consoleLabel.setAlignment(Align.topLeft);

        ScrollPane scrollPane = new ScrollPane(consoleLabel);
        scrollPane.setFadeScrollBars(false);

        commandMap.put("setHP", val -> {
            player.increaseMaxHealth(Integer.parseInt(val) + player.getCurrentHealth());
            hud.renderPlayerHP();
        });
        commandMap.put("setXP", val -> {
            player.setXp(Integer.parseInt(val));
            hud.updatePlayerXP();
        });
        commandMap.put("setSpeedMultiplier", val -> {
            player.multiplySpeed(Float.parseFloat(val));
        });
        commandMap.put("setGodMode", val -> {
            if (Integer.parseInt(val) == 1) {
                player.addOrReapplyEffect(new GodMode(player,  Float.POSITIVE_INFINITY));
            } else if (Integer.parseInt(val) == 0) {
                player.endEffect(GodMode.class);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("setDash", val -> {
            if (Integer.parseInt(val) == 1) {
                player.setCanDash(true);
            } else if (Integer.parseInt(val) == 0) {
                player.setCanDash(false);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("setInvis", val -> {
            if (Integer.parseInt(val) == 1) {
                player.setCanInvis(true);
            } else if (Integer.parseInt(val) == 0) {
                player.setCanInvis(false);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("getHelp", val -> {
            consoleLabel.setText(consoleLabel.getText() + "\n> " + "set[...]: " + "\n> " + setCommands + "\n> " + "get[...]: " + "\n> " + getCommands + "\n> E.g.: setXP:50, getDebuff");
        });
        commandMap.put("getBuff", val -> {
            this.player.addOrReapplyEffect(new Buff(player, 10.0f));
            hud.updatePlayerStatusEffects();
        });
        commandMap.put("getDebuff", val -> {
            this.player.addOrReapplyEffect(new Poison(player));
            hud.updatePlayerStatusEffects();
        });
        commandMap.put("getKeys", val -> {
            player.setHasKeys(true);
            player.setCurrentKeys(player.getRequiredKeys());
            hud.updatePlayerKeys();
            hud.createArrow();
        });
        inputField = new TextField("", game.getSkin(), "default");
        TextField.TextFieldStyle style = inputField.getStyle();

        if (style.background != null) {
            style.background.setRightWidth(20);
        }

        inputField.setStyle(style);
        inputField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if ((c == '\r' || c == '\n') && !textField.getText().isEmpty()) {
                    String command = textField.getText();

                    Matcher matcher = pattern.matcher(command);
                    if (matcher.matches()) {
                        gameScreen.getAchievementUI().consoleAchievement();
                        consoleLabel.setText(consoleLabel.getText() + "\n> " + command);
                        String stringCommand = matcher.group(1);

                        if (matcher.group(2) != null) {
                            try {
                                if (Integer.parseInt(matcher.group(2)) < 2147483647 && Integer.parseInt(matcher.group(2)) > 0) {
                                    String intCommand = matcher.group(2);
                                    Consumer<String> action = commandMap.get(stringCommand);

                                    if (action != null) {
                                        action.accept(intCommand);
                                    } else {
                                        consoleLabel.setText(consoleLabel.getText() + "\n> " + "Unknown command logic: " + stringCommand);
                                    }
                                }
                            } catch (Exception e) {
                                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Out of bounds value");
                            }

                        } else {
                            Consumer<String> action = commandMap.get(stringCommand);

                            if (action != null) {
                                action.accept(null);
                            } else {
                                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Unknown command logic: " + stringCommand);
                            }
                        }

                    } else {
                        consoleLabel.setText(consoleLabel.getText() + "\n> " + "Invalid command");
                    }

                    scrollPane.layout();
                    scrollPane.scrollTo(0, 0, 0, 0);

                    textField.setText(""); // Clear input
                }
            }
        });

        table.add(scrollPane).width(1000).height(950);
        table.row();
        table.add(inputField).width(1000);

        this.stage = stage;
        stage.addActor(table);
    }

    /**
     * Hides the developer console.
     */
    public void hideConsole() {
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        inputField.setText("");
    }

    /**
     * Shows the developer console.
     */
    public void showConsole() {
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }


}
