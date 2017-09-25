package unleashed.entities;


import java.awt.Rectangle;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;

import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class serves to represent a single block that is not affected by gravity"
		)


public class JuggernautStationary extends Entity{
	// is the image of the juggernautstationary
	Image juggernautStationary;
	
	//is the player to reference
	Player player;
	
	//is the length and height of the entity
	public static final int LENGTH_JUGGERNAUT_STATIONARY = 32;
	public static final int HEIGHT_JUGGERNAUT_STATIONARY = 32;
	
	
	
	//constructor
	public JuggernautStationary(int level, Time timer, int initialXPos, int initialYPos, double speed, Player player) throws SlickException {
		super(level, timer, initialXPos, initialYPos, speed);
		
		juggernautStationary = new Image("res/Entities/Spikey Block.png");
		
		this.player = player;
	}

	
	
	//draws the image to the screen
	public void drawJuggernautStationary(Graphics g){
		g.drawImage(juggernautStationary, getXPos(), getYPos());
	}
	
	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void decrementYPos(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}
	
	//adds to entities's coord's on screen directly
	public void addToXPosDirectly(double increment) throws SlickException{
		xPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}
	
	public void addToYPosDirectly(double increment) throws SlickException{
		yPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle juggernautStationaryRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_JUGGERNAUT_STATIONARY, HEIGHT_JUGGERNAUT_STATIONARY);
		
		if(playerRectangle.intersects(juggernautStationaryRectangle)){
			player.removeHealth();
		}
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}
	
}
