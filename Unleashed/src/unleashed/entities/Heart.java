package unleashed.entities;


import java.awt.Rectangle;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;

import unleashed.util.*;

@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class represents a heart entity, which, when collected, will restore 1 health to the player"
		)


public class Heart extends Entity{
	//is the Image for heart
	private Image heart;
	
	//is the heart length and height
	public static final int HEART_LENGTH = 24;
	public static final int HEART_HEIGHT = 24;
	
	//is the object no longer good?
	private boolean remove = false;
	
	//player to reference
	Player player;
	
	
	//constructor
	public Heart(int level, Time timer, int initialXPos, int initialYPos, double speed, Player player) throws SlickException {
		super(level, timer, initialXPos, initialYPos, speed);
		
		heart = new Image("res/Entities/HeartBoost.png");
		
		this.player = player;
	}
	
	
	
	//draws the heart to the screen
	public void drawHeart(Graphics g){
		g.drawImage(heart, getXPos(), getYPos());
	}
	
	
	
	//is removable?
	public boolean isRemove(){
		return remove;
	}

	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	
	
	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
		}
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
		}
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
			
		}
	}

	@Override
	public void decrementYPos(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
		}
	}
	
	//adds to entities's coord's on screen directly
	public void addToXPosDirectly(double increment) throws SlickException{
		xPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
		}
	}
	
	public void addToYPosDirectly(double increment) throws SlickException{
		yPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle heartRectangle = new Rectangle(getXPos(), getYPos(), HEART_LENGTH, HEART_HEIGHT);
		
		if(playerRectangle.intersects(heartRectangle) && !remove){
			if(player.getHealth() != 2){
				player.addHealth();
				remove = true;
			}
		}
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}
	
}
