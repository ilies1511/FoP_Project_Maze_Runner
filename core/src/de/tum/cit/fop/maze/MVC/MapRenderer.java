package de.tum.cit.fop.maze.MVC;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.Parser.GameField;
import de.tum.cit.fop.maze.Parser.TileType;
import de.tum.cit.fop.maze.objects.Dynamic.Enemy.Enemy;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.collectables.Collectable;
import de.tum.cit.fop.maze.objects.traps.Trap;

/**
 * Handles rendering of the game field including floors, walls, and objects.
 */
public class MapRenderer {
    private final MazeRunnerGame game;
    private final SpriteBatch batch;

    public MapRenderer(MazeRunnerGame game) {
        this.game = game;
        this.batch = game.getSpriteBatch();
    }

	private TextureRegion getExplicitFloorTexture(TileType tile) {
		return switch (tile) {
			case EMPTY_1 -> game.getEmptyTile1();
			case EMPTY_2 -> game.getEmptyTile2();
			case EMPTY_3 -> game.getEmptyTile3();
			case EMPTY, EMPTY_4 -> game.getEmptyTile4();
			case EMPTY_5 -> game.getEmptyTile5();
			case EMPTY_6 -> game.getEmptyTile6();
			case EMPTY_7 -> game.getEmptyTile7();
			case DANCE_FLOOR -> game.getDanceFloorTile();
			default -> null;
		};
	}

	private TextureRegion inferFloorFromNeighbors(GameField field, int x, int y) {
		int[][] offsets = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

		for (int[] offset : offsets) {
			int nx = x + offset[0];
			int ny = y + offset[1];

			if (nx >= 0 && nx < field.getWidth() && ny >= 0 && ny < field.getHeight()) {
				TileType neighbor = field.getTile(nx, ny);
				TextureRegion neighborTex = getExplicitFloorTexture(neighbor);
				if (neighborTex != null) {
					return neighborTex;
				}
			}
		}
		return null;
	}

    private boolean isDecor(TileType t) {
        return t == TileType.DECOR_1
                || t == TileType.DECOR_2
                || t == TileType.DECOR_3
                || t == TileType.DECOR_4
                || t == TileType.DECOR_5
                || t == TileType.DECOR_6
                || t == TileType.DECOR_7
                || t == TileType.DECOR_8
                || t == TileType.DECOR_9
                || t == TileType.DECOR_10
                || t == TileType.CASKET;
    }

    private TextureRegion getDecorTexture(TileType t) {
        return switch (t) {
            case DECOR_1 -> game.getDecor1();
            case DECOR_2 -> game.getDecor2();
            case DECOR_3 -> game.getDecor3();
            case DECOR_4 -> game.getDecor4();
            case DECOR_5 -> game.getDecor5();
            case DECOR_6 -> game.getDecor6();
            case DECOR_7 -> game.getDecor7();
            case DECOR_8 -> game.getDecor8();
            case DECOR_9 -> game.getDecor9();
            case DECOR_10 -> game.getDecor10();
            case CASKET -> game.getCasketTile();
            default -> null;
        };
    }

    private boolean isWall(TileType t) {
        return t == TileType.WALL
                || t == TileType.WALL_1
                || t == TileType.WALL_2
                || t == TileType.WALL_3
                || t == TileType.WALL_4
                || t == TileType.WALL_5
                || t == TileType.WALL_6
                || t == TileType.WALL_7
                || t == TileType.WALL_8
                || t == TileType.WALL_HIGH_1
                || t == TileType.WALL_HIGH_2
                || t == TileType.WALL_HIGH_3
                || t == TileType.WALL_HIGH_4;
    }

    private boolean isSingleWall(GameField f, int x, int y) {
        if (!isWallAt(f, x, y) || y == 0) {
            return false;
        }
        return !isWallAt(f, x, y - 1)
                && !isWallAt(f, x, y + 1);
    }

    private boolean isWallAt(GameField f, int x, int y) {
        if (x < 0 || y < 0 || x >= f.getWidth() || y >= f.getHeight()) {
            return false;
        }
        return isWall(f.getTile(x, y));
    }

    private TextureRegion getWallTop(TileType t) {
        return game.getBuildingTile();
    }

    private TextureRegion getWallFront(TileType t) {
        return switch (t) {
            case WALL, WALL_1 ->
                    game.getWallHigh1();
            case WALL_2 -> game.getWallHigh2();
            case WALL_3 -> game.getWallHigh3();
            case WALL_4 -> game.getWallHigh4();
            case WALL_5 -> game.getWallHigh5();
            case WALL_6 -> game.getWallHigh6();
            case WALL_7 -> game.getWallHigh7();
            default -> game.getWallHigh1();
        };
    }

    private TextureRegion getWallFront_Small(TileType t) {
        return switch (t) {
            case WALL, WALL_1 ->
                    game.getWallTile1();
            case WALL_2 -> game.getWallTile2();
            case WALL_3 -> game.getWallTile3();
            case WALL_4 -> game.getWallTile4();
            case WALL_5, WALL_6, WALL_7, WALL_8 -> game.getWallTile8();
            default -> game.getWallTile1();
        };
    }

    private void drawDecors(GameField gameField) {
        for (int y = 0; y < gameField.getHeight(); y++) {
            for (int x = 0; x < gameField.getWidth(); x++) {
                TileType tile = gameField.getTile(x, y);
                if (!isDecor(tile)) {
                    continue;
                }
                TextureRegion decor = getDecorTexture(tile);
                if (decor == null) {
                    continue;
                }
                batch.draw(decor, x, y, 1, 1);
            }
        }
    }

    private void drawWalls2_5D(GameField gameField) {
        for (int y = 0; y < gameField.getHeight(); y++) {
            for (int x = 0; x < gameField.getWidth(); x++) {
                TextureRegion wallToDraw;
                TileType tile = gameField.getTile(x, y);

                if (!isWall(tile)) {
                    continue;
                }
                if (isSingleWall(gameField, x, y)) {
                    wallToDraw = getWallFront_Small(tile);
                    batch.draw(wallToDraw, x, y, 1, 1);
                    continue;
                }
                boolean wallBelow = isWallAt(gameField, x, y - 1);
                wallToDraw = wallBelow ? getWallTop(tile) : getWallFront(tile);
                batch.draw(wallToDraw, x, y, 1, 1);
            }
        }
    }

    private void drawFloors(GameField gameField) {
        for (int y = 0; y < gameField.getHeight(); y++) {
            for (int x = 0; x < gameField.getWidth(); x++) {
                TileType tile = gameField.getTile(x, y);

//                TextureRegion floor = game.getEmptyTile4();
//
//                switch (tile) {
//                    case EMPTY_1 -> floor = game.getEmptyTile1();
//                    case EMPTY_2 -> floor = game.getEmptyTile2();
//                    case EMPTY_3 -> floor = game.getEmptyTile3();
//                    case EMPTY, EMPTY_4 -> floor = game.getEmptyTile4();
//                    case EMPTY_5 -> floor = game.getEmptyTile5();
//                    case EMPTY_6 -> floor = game.getEmptyTile6();
//                    case EMPTY_7 -> floor = game.getEmptyTile7();
//                    case DANCE_FLOOR -> floor = game.getDanceFloorTile();
//                    default -> {
//                    }
//                }
	            TextureRegion floorToDraw = getExplicitFloorTexture(tile);

	            if (floorToDraw == null) {
		            floorToDraw = inferFloorFromNeighbors(gameField, x, y);
	            }

	            if (floorToDraw == null) {
		            floorToDraw = game.getEmptyTile1(); // Default
	            }

                batch.draw(floorToDraw, x, y, 1, 1);
            }
        }
    }

	/**
	 * Determines the rendering layer order.
	 * Higher numbers are drawn LAST (on top).
	 */
	private int getRenderLayer(GameObject obj) {
		if (obj instanceof Trap || obj instanceof Collectable) {
			return 0;
		}
		if (obj instanceof Player || obj instanceof Enemy) {
			return 1;
		}
		return 0;
	}

    /**
     * Renders the complete game scene including field, player, and objects.
     * @param gameField The game field to render
     * @param player The player to render
     * @param delta Time since last render
     */
    public void render(GameField gameField, Player player, float delta) {
        batch.begin();

        drawFloors(gameField);
        drawWalls2_5D(gameField);
        drawDecors(gameField);

//        gameField.getGameObjects().sort((o1, o2) -> Float.compare(o2.position.y, o1.position.y));
	    gameField.getGameObjects().sort((o1, o2) -> {
		    float y1 = o1.getPosition().y;
		    float y2 = o2.getPosition().y;

		    int tileY1 = (int) Math.floor(y1);
		    int tileY2 = (int) Math.floor(y2);

		    if (tileY1 != tileY2) {
			    return Float.compare(y2, y1);
		    }
		    int layer1 = getRenderLayer(o1);
		    int layer2 = getRenderLayer(o2);
		    if (layer1 != layer2) {
			    return Integer.compare(layer1, layer2);
		    }
		    return Float.compare(y2, y1);
	    });

        for (GameObject obj : gameField.getGameObjects()) {
            obj.animate(batch);
        }
        player.animate(batch);
        batch.end();
    }
}
