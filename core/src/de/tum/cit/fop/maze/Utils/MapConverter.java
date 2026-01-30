package de.tum.cit.fop.maze.Utils;

import de.tum.cit.fop.maze.Parser.TileType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/*
	Value | Type
	-------------------
	0 or #     | Wall
	S or 1     | Entry point
	E or 2     | Exit
	T or 3     | Trap (static obstacle)
	M or 4     | Enemy (dynamic obstacle)
	K or 5     | Key
*/

/**
 * Utility class for converting text-based maze maps to properties format.
 */
public class MapConverter {

    private static final Map<Character, TileType> LEGEND = new HashMap<>();

    static {
        LEGEND.put('.', TileType.EMPTY);
        LEGEND.put('#', TileType.WALL);
        LEGEND.put('S', TileType.ENTRY_POINT);
        LEGEND.put('E', TileType.EXIT);
        LEGEND.put('T', TileType.TRAP);
        LEGEND.put('M', TileType.ENEMY);
        LEGEND.put('K', TileType.KEY);

        // BEGIN -- Story Mode
        // Walls Variations
        LEGEND.put('=', TileType.WALL_1);
        LEGEND.put('%', TileType.WALL_2);
        LEGEND.put('|', TileType.WALL_3);
        LEGEND.put('-', TileType.WALL_4);
        LEGEND.put('+', TileType.WALL_5);
        LEGEND.put('[', TileType.WALL_6);
        LEGEND.put(']', TileType.WALL_7);
        LEGEND.put('^', TileType.WALL_8);
        LEGEND.put('1', TileType.WALL_HIGH_1);
        LEGEND.put('2', TileType.WALL_HIGH_2);
        LEGEND.put('3', TileType.WALL_HIGH_3);
        LEGEND.put('4', TileType.WALL_HIGH_4);

        // Floor Variations
        LEGEND.put('*', TileType.DANCE_FLOOR);
        LEGEND.put(',', TileType.EMPTY_1);
        LEGEND.put(':', TileType.EMPTY_2);
        LEGEND.put(';', TileType.EMPTY_3);
        LEGEND.put('"', TileType.EMPTY_4);
        LEGEND.put('`', TileType.EMPTY_5);
        LEGEND.put('~', TileType.EMPTY_6);
        LEGEND.put('_', TileType.EMPTY_7);

        //Decor
        LEGEND.put('a', TileType.DECOR_1);
        LEGEND.put('b', TileType.DECOR_2);
        LEGEND.put('c', TileType.DECOR_3);
        LEGEND.put('d', TileType.DECOR_4);
        LEGEND.put('e', TileType.DECOR_5);
        LEGEND.put('f', TileType.DECOR_6);
        LEGEND.put('g', TileType.DECOR_7);
        LEGEND.put('h', TileType.DECOR_8);
        LEGEND.put('i', TileType.DECOR_9);
        LEGEND.put('j', TileType.DECOR_10);
        LEGEND.put('k', TileType.CASKET);
        // EMD -- Story Mode
    }

    private static void write_to_properties_File(String outputPath, StringBuilder propertiesContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(propertiesContent.toString());
            System.out.println("Saved: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static StringBuilder parse_and_convert(List<String> lines, String inputPath) {
        int height = lines.size();

        System.out.println("Processing " + inputPath + " (" + height + " rows)...");

        StringBuilder propertiesContent = new StringBuilder();
        propertiesContent.append("# Generated map from ").append(inputPath).append("\n");

        for (int yIndex = 0; yIndex < height; yIndex++) {
            String row = lines.get(yIndex);
            for (int xIndex = 0; xIndex < row.length(); xIndex++) {
                char c = row.charAt(xIndex);

                if (LEGEND.containsKey(c)) {
                    TileType type = LEGEND.get(c);
                    int typeId = type.ordinal();
                    int gameY = (height - 1) - yIndex;
                    propertiesContent.append(xIndex).append(",").append(gameY)
                            .append("=").append(typeId).append("\n");
                }
            }
        }
        return propertiesContent;
    }

    private static File init(String inputPath, List<String> lines) {
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            System.err.println("Source file not found: " + inputFile.getAbsolutePath());
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return inputFile;
    }

    private static void convertMap(String inputPath, String outputPath) {
        List<String> lines = new ArrayList<>();
        File inputFile = init(inputPath, lines);

        if (inputFile == null) {
            return;
        }
        StringBuilder propertiesContent = parse_and_convert(lines, inputPath);
        write_to_properties_File(outputPath, propertiesContent);
    }

    public static void main(String[] args) {
        String[][] mapsToConvert = {
                {"maps/tmp/01.txt", "maps/story_mode/01.properties"},
                {"maps/tmp/02.txt", "maps/story_mode/02.properties"},
                {"maps/tmp/03.txt", "maps/story_mode/03.properties"},
                {"maps/tmp/04.txt", "maps/story_mode/04.properties"},
                {"maps/tmp/05.txt", "maps/story_mode/05.properties"},
                {"maps/tmp/06.txt", "maps/story_mode/06.properties"},
                {"maps/tmp/berghain_main_big.txt", "maps/story_mode/berghain_main_big.properties"},
                {"maps/tmp/berghain_darkroom.txt", "maps/story_mode/berghain_darkroom.properties"},
        };

        for (String[] mapPair : mapsToConvert) {
            convertMap(mapPair[0], mapPair[1]);
        }
    }
}