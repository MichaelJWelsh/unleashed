package unleashed.util;

import unleashed.Game;


@Info(
		dateLastEdited = "11/29/2015",
		purpose = "This class serves to efficiently keep track of how much in-game and real-life time has passed, and"
				 +" manipulate time in various ways.",
		otherInfo = "IT IS VERY IMPORTANT THAT AN OBJECT OF THIS CLASS CALLS THE 'maintainTime()' METHOD IN THE"
				   +" 'update()' METHOD OF EVERY LEVEL!"
		)


final public class Time {
	
	//contains the time-in-seconds that have went by
	private double timeInSeconds = 0;
	
	//this variable is used to keep track of how much time-in-seconds have gone by, by multiple algorithms and methods
	private double timeCounter = 0;
	
	//this variable is used to keep track of how much time is allocated in a level
	private int totalTimeInLevel;
	
	
	
	//constructor, helps initialize members of this class
	public Time(int totalTimeInLevel){
		this.totalTimeInLevel = totalTimeInLevel;
	}
	
	
	
	//maintains the 'timeInSeconds' variable, to accurately represent the time (in seconds) that has passed
	//returns false if the totalTimeInLevel - (int) timeInSeconds == 0
	public boolean maintainTime(double delta){
		timeCounter += 1 + delta / 1000;
		
		if(timeCounter >= Game.FPS){
			timeCounter = 0;
			timeInSeconds++;
		}
		
		if(getTimeRemaining() <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	//get's time remaining in level
	public int getTimeRemaining(){
		return totalTimeInLevel - getTimePassed();
	}
	
	
	
	//resets the 'timeInSeconds' and 'timeCounter' to zero
	public void resetTimePassed(){
		timeInSeconds = 0;
		timeCounter = 0;
	}
	
	/* Using this method is discouraged!
	 * Sets the time-in-seconds that have passed.
	 */
	@Deprecated
	public void setTimePassed(double time){
		timeInSeconds = time;
	}
	
	//gets the time-in-seconds that have went by
	public int getTimePassed(){
		return (int) timeInSeconds;
	}
	
	
}
