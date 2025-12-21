package de.tum.cit.fop.maze.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class MazeParser {
	// BEGIN -- Attributes
	BufferedReader _mazeFile;
	Path _path;

	// END -- Attributes

	// BEGIN -- Constructor

	public MazeParser() {
		try {
			this._mazeFile =  new BufferedReader(new InputStreamReader(new FileInputStream("level-1.properties"), StandardCharsets.UTF_8));
		}
		catch (FileNotFoundException e) {
			System.out.println("Exception in " + getClass().getName() + ": " + e.getMessage());
		}
	}

	// END -- Constructor

	// BEGIN -- Getters
	// END -- Getters

	// BEGIN -- Setters
	// END -- Setters

	// BEGIN -- Methods

	// END -- Methods
}
