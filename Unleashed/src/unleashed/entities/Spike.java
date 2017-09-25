package unleashed.entities;


import java.awt.Rectangle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import unleashed.Game;
import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class is a representation of an entity that is affected by gravity, this entity does damage"
		)


public class Spike extends Entity{
	//is the image
	Image spike;
	
	//is the player being referenced
	Player player;
	
	//is the length and height
	public static final int SPIKE_LENGTH = 32;
	public static final int SPIKE_HEIGHT = 32;
	
	//is this object still good?
	private boolean remove = false;
	
	
	
	//constructor
	public Spike(int level, Time timer, double speed, Player player) throws SlickException {
		super(level, timer, (int) (Math.random() * Game.WINDOW_LENGTH), -40, speed);
		
		spike = new Image("res/Entities/GravitySpike.png");
		
		this.player = player;
	}
	
	
	
	//returns if it should be removed or not
	public boolean isRemove(){
		return remove;
	}
	
	
	
	//draws the spike
	public void drawSpike(Graphics g){
		g.drawImage(spike, getXPos(), getYPos());
	}

	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	
	
	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
		
		
		if(getYPos() < -200){
			remove = true;
		}
	}

	@Override
	public void decrementYPos(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
		
		
		if(getYPos() > Game.WINDOW_HEIGHT + 200){
			remove = true;
		}
	}
	
	//adds to entities's coord's on screen directly
	public void addToXPosDirectly(double increment) throws SlickException{
		xPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
	}
	
	public void addToYPosDirectly(double increment) throws SlickException{
		yPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle spikeRectangle = new Rectangle(getXPos(), getYPos(), SPIKE_LENGTH, SPIKE_HEIGHT);
		
		if(playerRectangle.intersects(spikeRectangle)){
			player.removeHealth();
		}
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}
	
}
