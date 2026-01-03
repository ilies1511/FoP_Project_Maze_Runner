package de.tum.cit.fop.maze.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

public class MapConverter {

	private static final Map<Character, Integer> LEGEND = new HashMap<>();
	static {
		LEGEND.put('#', 0);
		LEGEND.put('0', 0);
		LEGEND.put('S', 1);
		LEGEND.put('1', 1);
		LEGEND.put('E', 2);
		LEGEND.put('2', 2);
		LEGEND.put('T', 3);
		LEGEND.put('3', 3);
		LEGEND.put('M', 4);
		LEGEND.put('4', 4);
		LEGEND.put('K', 5);
		LEGEND.put('5', 5);
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
					int typeId = LEGEND.get(c);
					int gameY = (height - 1) - yIndex;
					propertiesContent.append(xIndex).append(",").append(gameY).append("=").append(typeId).append("\n");
				}
			}
		}
		return propertiesContent;
	}

	private static File init(String inputPath, List<String> lines ) {
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
				{"maps/tmp/berghain_floor.txt", "maps/story_mode/berghain_floor.properties"},
				// TODO: 03.01 Add more
		};

		for (String[] mapPair : mapsToConvert) {
			convertMap(mapPair[0], mapPair[1]);
		}
	}
}