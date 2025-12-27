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
import de.tum.cit.fop.maze.effects.Buffed;
import de.tum.cit.fop.maze.effects.Poisoned;
import de.tum.cit.fop.maze.objects.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevConsole {
    private final Map<String, Consumer<String>> commandMap = new HashMap<>();
    Pattern pattern;
    private MazeRunnerGame game;
    private GameScreen gameScreen;
    private Stage stage;
    private Table table;
    ;
    private String setCommands;
    private String getCommands;
    private Player player;
    private HUD hud;

    public DevConsole(MazeRunnerGame game, GameScreen gameScreen, Stage stage, Player player, HUD hud) {

        setCommands = "XP|HP|SpeedMultiplier|Dash|Invis|GodMode";
        getCommands = "Buff|Debuff|Key|Help";

        String regex = String.format(
                "^(?=(?:set(?:%s):\\d+|get(?:%s))$)([a-zA-Z]+)(?::(\\d+))?$",
                setCommands,
                getCommands
        );


        Pattern pattern = Pattern.compile(regex);

        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        this.hud = hud;

//        Pixmap pixmap = new Pixmap(1000, 950, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.BLACK);
//        pixmap.fill();
//        TextureRegionDrawable blackDrawable = new TextureRegionDrawable(new Texture(pixmap));
//        pixmap.dispose();

        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);
        table.top().left();
        //  table.debug();

        Label consoleLabel = new Label("Developer console", game.getSkin(), "title");
        consoleLabel.setFontScale(0.4f);
        consoleLabel.setWrap(true); // Important: Allows text to wrap to next line
        consoleLabel.setAlignment(Align.topLeft);

        ScrollPane scrollPane = new ScrollPane(consoleLabel);
//        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);

//        ScrollPane.ScrollPaneStyle myConsoleStyle = new ScrollPane.ScrollPaneStyle(scrollPane.getStyle());
//        myConsoleStyle.vScroll = null;
//        myConsoleStyle.vScrollKnob = blackDrawable;
//        scrollPane.setStyle(myConsoleStyle);

        commandMap.put("setHP", val -> {
            player.setCurrentHealth(Integer.parseInt(val));
            player.setMaxHealth(Integer.parseInt(val));
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
                player.setInGodMode(true);
            } else if (Integer.parseInt(val) == 0) {
                player.setInGodMode(false);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("setDash", val -> {
            if (Integer.parseInt(val) == 1) {
                player.setCanDash(true);
                ;
            } else if (Integer.parseInt(val) == 0) {
                player.setCanDash(false);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("setInvis", val -> {
            if (Integer.parseInt(val) == 1) {
                player.setCanInvis(true);
                ;
            } else if (Integer.parseInt(val) == 0) {
                player.setCanInvis(false);
            } else {
                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Please enter a value between 1 and 0");
            }
        });
        commandMap.put("getHelp", val -> {
            consoleLabel.setText(consoleLabel.getText() + "\n> " + "set[...]: " + "\n> " + setCommands + "\n> " + "get[...]: " + "\n> " + getCommands);
        });
        commandMap.put("getBuff", val -> {
            this.player.addOrReapplyEffect(new Buffed(player, 10.0f));
            hud.updatePlayerStatusEffects();
        });
        commandMap.put("getDebuff", val -> {
            this.player.addOrReapplyEffect(new Poisoned(player));
            hud.updatePlayerStatusEffects();
        });
        commandMap.put("getKey", val -> {
            //TODO when keys are done
        });
        TextField inputField = new TextField("", game.getSkin(), "default");
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

                    //TODO LOGIC
                    Matcher matcher = pattern.matcher(command);
                    if (matcher.matches()) {
                        consoleLabel.setText(consoleLabel.getText() + "\n> " + command);
                        String stringCommand = matcher.group(1);

                        if (matcher.group(2) != null) {
                            String intCommand = matcher.group(2);
                            Consumer<String> action = commandMap.get(stringCommand);

                            if (action != null) {
                                action.accept(intCommand);
                            } else {
                                consoleLabel.setText(consoleLabel.getText() + "\n> " + "Unknown command logic: " + stringCommand);
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
        table.add(inputField).fillX();

        this.stage = stage;
        stage.addActor(table);
    }

    public void hideConsole() {
        table.setVisible(false);
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
    }

    public void showConsole() {
        table.setVisible(true);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }

    //TODO INVIS COMMAND
}
