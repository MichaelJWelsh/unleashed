package unleashed.util;

import java.util.*;

import unleashed.Game;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class serves to keep track of the players' score when playing the game."
				+ "Much functionality with the score is located in this class."
		)


public class Score {
	//stores the score of each level, keys are referred to as "Game.(insert level here)"
	private Map<Integer,Integer> scoreByLevel = new HashMap<Integer,Integer>();
	
	
	
	//constructor: initializing elements in scoreByLevel
	public Score(){
		scoreByLevel.put(Game.LEVEL_ONE, 0);
		scoreByLevel.put(Game.LEVEL_TWO, 0);
		scoreByLevel.put(Game.LEVEL_THREE, 0);
	}
	
	
	
	//gains direct access to 'scoreByLevel'
	public Map<Integer, Integer> getScoreMap(){
		return scoreByLevel;
	}
	
	
	
	//adds to score
	public void addToScore(Integer level, int scoreToAddBy){
		scoreByLevel.put(level, scoreByLevel.get(level) + scoreToAddBy);
	}
	
	
	
	//gets the total score
	public int getTotalScore(){
		int total = 0;
		for(Map.Entry<Integer, Integer> entry: scoreByLevel.entrySet()){
			total += entry.getValue();
		}
		
		return total;
	}
	
	//gets the score of level
	public int getScoreOfLevel(Integer level){
		return scoreByLevel.get(level);
	}
	
	
	
	//resets all scores
	public void resetAllScores(){
		for(Map.Entry<Integer, Integer> entry: scoreByLevel.entrySet()){
			entry.setValue(0);
		}
	}
	
	//resets score of level
	public void resetScoreOfLevel(Integer level){
		scoreByLevel.put(level, 0);
	}
}
