package de.tum.cit.fop.maze;

import com.badlogic.gdx.math.Vector2;

public class GameField {
    private final int width;
    private final int height;
    private final TileType[] grid;
    private final Vector2 playerStartPos;

    public TileType getTile(int x, int y) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            return TileType.WALL; // boundaries are walls to avoid going outside of the map
        }
        return this.grid[y * width + x];
    }

    private void setTile(int x, int y, TileType tile) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            System.err.println("Trying to set a tile at (" + x + ", " + y + ") out of bounds!");
            return;
        }
        this.grid[y * width + x] = tile;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public GameField() { // temporary constructor: emulates loading from a File
        this.height = 15;
        this.width = 20;
        this.grid = new TileType[this.width * this.height];
        String[] layout = {
                "####################", // Row 0
                "#..................#", // Row 1
                "#.###.########.###.#", // Row 2
                "#.#...#......#...#.#", // Row 3
                "#.#.###.####.###.#.#", // Row 4
                "#.#.....#........#.#", // Row 5
                "#.#######.########.#", // Row 6
                "#..................#", // Row 7
                "#####.##########.###", // Row 8
                "#...#......#.......#", // Row 9
                "#.#####.##.#####.###", // Row 10
                "#.......##.........#", // Row 11
                "#.################.#", // Row 12
                "#..................#", // Row 13
                "####################"  // Row 14
        };
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {

                    char c = layout[y].charAt(x);

                    if (c == '#') {
                        this.setTile(x, y, TileType.WALL);
                    } else {
                        this.setTile(x, y, TileType.EMPTY);
                    }
                }
            }
        this.playerStartPos = new Vector2(1f, 1f);
    }

    public Vector2 getPlayerStartPos() {
        return  this.playerStartPos;
    }
}
