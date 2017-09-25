package unleashed.entities;


import java.awt.Rectangle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import unleashed.Game;
import unleashed.terrain.Block;
import unleashed.terrain.Terrain;
import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class is a representation of an entity that is affected by gravity, this entity does damage"
		)


public class Fireball extends Entity{
	//is the image
	Image fireball;
	
	//is the player being referenced
	Player player;
	
	//is the length and height
	public static final int FIREBALL_LENGTH = 64;
	public static final int FIREBALL_HEIGHT = 28;
	
	//is this object still good?
	private boolean remove = false;
	
	
	
	
	//constructor
	public Fireball(int level, Time timer, double speed, Player player, Terrain terrain) throws SlickException {
		super(level, timer, Game.WINDOW_LENGTH + 20, 
				new Block(1,1,Game.WINDOW_HEIGHT * Math.random(),1).getYPos() + (int)(Math.abs(terrain.getTerrainSet().iterator().next().getTranslatedDistanceVertical()) % Block.BLOCK_SIDE_LENGTH) - 10,speed);
		
		fireball = new Image("res/Entities/Fireball.png");
		
		this.player = player;
	}
	
	
	
	//returns if it should be removed or not
	public boolean isRemove(){
		return remove;
	}
	
	
	
	//draws the fireball
	public void drawFireball(Graphics g){
		g.drawImage(fireball, getXPos(), getYPos());
	}

	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	
	
	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
			player.removeHealth();
		}
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
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
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
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
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
			player.removeHealth();
		}
	}
	
	public void addToYPosDirectly(double increment) throws SlickException{
		yPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle fireballRectangle = new Rectangle(getXPos(), getYPos() + 2, FIREBALL_LENGTH, FIREBALL_HEIGHT);
		
		if(playerRectangle.intersects(fireballRectangle)){
			player.removeHealth();
		}
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}
	
}
