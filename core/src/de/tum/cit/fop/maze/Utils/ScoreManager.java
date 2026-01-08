package de.tum.cit.fop.maze.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreManager {
	// BEGIN -- Attributes
	private static final String FILE_NAME = "data/leaderboard.json";
	private static ScoreManager instance;
	private ArrayList<Long> scores;
	// END -- Attributes

	// BEGIN -- Constructor
	private ScoreManager() {
		scores = new ArrayList<>();
		loadScores();
	}
	// END -- Constructor

	// BEGIN -- Getters
	public static ScoreManager getInstance() {
		if (instance == null) {
			instance = new ScoreManager();
		}
		return instance;
	}

	public ArrayList<Long> getScores() {
		return scores;
	}
	// END -- Getters

	// BEGIN -- Setters
	public void addScore(long score) {
		scores.add(score);
		Collections.sort(scores, Collections.reverseOrder());
		if (scores.size() > 10) {
			scores.remove(scores.size() - 1);
		}
		saveScores();
	}
	// END -- Setters

	// BEGIN -- Methods

	private void saveScores() {
		Json json = new Json();
		FileHandle file = Gdx.files.local(FILE_NAME);
		file.writeString(json.toJson(scores), false);
	}

	@SuppressWarnings("unchecked")
	private void loadScores() {
		FileHandle file = Gdx.files.local(FILE_NAME);
		if (file.exists()) {
			Json json = new Json();
			try {
				scores = json.fromJson(ArrayList.class, file.readString());
			} catch (Exception e) {
				Gdx.app.error("ScoreManager", "Error loading scores", e);
				scores = new ArrayList<>();
			}
		}
        //TODO: if file missing, create file
	}
	// END -- Methods
}