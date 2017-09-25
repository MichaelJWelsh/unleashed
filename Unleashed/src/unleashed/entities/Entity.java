package unleashed.entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import unleashed.Game;
import unleashed.terrain.Terrain;
import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class is extended by all entities.  The components of this class make up the skeleton of all entities.",
		otherInfo = "All entites must become a subclass of Entity."
				   +"Components of this class include: coordinate manipulation, speed per frame manipulation, and Animation manipulation"
		)


abstract class Entity{
	
	/*	SPEED */
	//is the entity speed (i.e: (this number * Game.FPS) == number of pixels traveled per second
	protected double speed;
	
	/*	COORDINATE */
	/* VERY IMPORTANT:
	 * 	the yPos is counted from the top-left which means the coordinate (0,0) is found in the top-left
	 */
	protected double xPos;
	protected double yPos;
	
	/*	DIRECTION FACING */
	public boolean facingRight;
	public boolean facingLeft;
	
	/*	LEVEL */
	private int level;
	
	/*	ANIMATION */
	//is the overall animation worked with
	protected Animation moving;
	
	/*	TIME */
	protected Time timer;
	
	/*	TERRAIN */
	protected Terrain terrain = null;
	
	/* ACCELERATION DUE TO GRAVITY */
	//is measured in pixels
	public static final double ACCELERATION_DUE_TO_GRAVITY = 1.1;
	
	// Keeps track if the player is jumping
	protected boolean jumping = false;
	
	//keeps track of what direction the gravity is
	protected boolean gravityDownward = true;
	
	
	
	//constructor, initializes members of this class
	public <T extends Number> Entity(int level, Time timer, int initialXPos, int initialYPos, T speed){
		setXPos(initialXPos);
		setYPos(initialYPos);
		setSpeedPerFrame(speed);
		setLevel(level);
		this.timer = timer;
	}
	
	
	
	//sets the 'moving' animation to an animation (MUST BE OVERRIDDEN BY SUBCLASS)
	public abstract void setMovingAnimation(Integer motion) throws SlickException;
	//gets the 'moving' animation
	public Animation getMovingAnimation(){
		return moving;
	}
	
	
	
	//checking to see what direction entity is facing
	public boolean isFacingLeft(){
		if(facingLeft == true){
			return true;
		}else{
			return false;
		}
	}
	public boolean isFacingRight(){
		if(facingRight == true){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	//gets speed of entity per frame
	public double getSpeedPerFrame(){
		return speed;
	}
	//sets speed of entity per frame
	public <T extends Number> void setSpeedPerFrame(T speed){
		if(speed.doubleValue() <= 0){
			this.speed = 1;
			System.err.println("Negative Speed, resulting to a speed of 1!");
		}else{
			this.speed = speed.doubleValue();
		}
	}
	//increments speed of entity per frame
	public <T extends Number> void incrementSpeedPerFrame(T increment){
		speed += increment.doubleValue();
		if(speed <= 0){
			speed = 1;
		}
	}
		
		
	
	//gets coordinates
	public int getXPos(){
		return (int) Math.round(xPos);
	}
	public int getYPos(){
		return (int) Math.round(yPos);
	}
	//gets inverted yPos (i.e: the yPos as if (0,0) is in the bottom-left)
	public int getInvertedYPos(){
		return (int) (Game.WINDOW_HEIGHT - yPos);
	}
	
	
	
	//sets coodinates
	public <T extends Number> void setXPos(T newXPos){
		xPos = newXPos.doubleValue();
	}
	public <T extends Number> void setYPos(T newYPos){
		yPos = newYPos.doubleValue();
	}
	
	
	
	//incrementing/decrementing x - coords (MUST BE OVERRIDEN BY SUBCLASS)
	public abstract void incrementXPos(double delta) throws SlickException;
	public abstract void decrementXPos(double delta) throws SlickException;
	
	//incrementing/decrementing y - coords (MUST BE OVERRIDEN BY SUBCLASS)
	public abstract void incrementYPos(double delta) throws SlickException;
	public abstract void decrementYPos(double delta) throws SlickException;
	
	
	
	//switches the gravity (MUST BE OVERRIDEN BY SUBCLASS)
	public abstract void switchGravity() throws SlickException;
	
	
	
	//gets level
	public int getLevel(){
		return level;
	}
	
	// TODO finish this (there may be more levels?!?)
	//sets level
	private void setLevel(int level){
		switch(level){
			case Game.LEVEL_ONE:
			case Game.LEVEL_TWO:
			case Game.LEVEL_THREE:
				this.level = level;
				break;
			default:
				System.err.println("Improper level input, use 'Game.LEVEL_#' to accurately input a level into this method");
				break;
		}
	}
	
	
	
	//Sets the Terrain to be referenced/manipulated by the entity
	public void setReferenceTerrain(Terrain terrain){
		this.terrain = terrain;
	}
	
	
	
	//returns whether or not the entity is currently jumping
	public boolean isJumping(){
		if(jumping){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	//returns whether or not the force of gravity is pointing downward
	public boolean isGravityDownward(){
		if(gravityDownward){
			return true;
		}else{
			return false;
		}
	}
	
	
}
