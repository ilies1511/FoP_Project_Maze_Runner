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

    public GameField() {
        this.height = 15;
        this.width = 20;
        this.grid = new TileType[this.width * this.height];

        // Visual representation (Top-Down view)
        String[] layout = {
                "####################", // Visual Top
                "#..................#",
                "#.###.########.###.#",
                "#.#...#......#...#.#",
                "#.#.###.####.###.#.#",
                "#.#.....#........#.#",
                "#.#######.########.#",
                "#..................#",
                "#####.##########.###",
                "#...#......#.......#",
                "#.#####.##.#####.###",
                "#.......##.........#",
                "#.################.#",
                "#..................#",
                "####################"  // Visual Bottom
        };

        // Iterate through the VISUAL layout rows (i = 0 is Top)
        for (int i = 0; i < this.height; i++) {

            // FLIP THE Y AXIS (because libgdx is stupid)
            int y = (this.height - 1) - i;

            for (int x = 0; x < this.width; x++) {
                char c = layout[i].charAt(x);

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
