package unleashed.entities;

import java.awt.Rectangle;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;

import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class represents a coin entity.  When 'collecting' a coin, the player gets a higher score"
		)


public class Coin extends Entity{
	//is the animation of the coin
	private Animation coin;
	
	//is the player to reference
	Player player;
	
	//is the score to reference
	Score score;
	
	//controls the sequence of events of the coin
	int[] duration = {250,100,100,100,100,100,100,100,100};
	
	//is the coins length and height
	public static final int COIN_LENGTH = 22;
	public static final int COIN_HEIGHT = 22;
	
	//is this object no longer good?
	private boolean remove = false;
	
	
	

	//constructor
	public Coin(int level, Time timer, int initialXPos, int initialYPos, double speed, Player player, Score score) throws SlickException{
		super(level, timer, initialXPos, initialYPos, speed);
		Image[] coins = {new Image("res/Entities/Coin.png"),
				new Image("res/Entities/Coin2.png"),
				new Image("res/Entities/Coin3.png"),
				new Image("res/Entities/Coin4.png"),
				new Image("res/Entities/Coin5.png"),
				new Image("res/Entities/Coin6.png"),
				new Image("res/Entities/Coin7.png"),
				new Image("res/Entities/Coin8.png"),
				new Image("res/Entities/Coin9.png")};
		
		//initializing animation
		coin = new Animation(coins, duration, false);
		
		this.player = player;
		this.score = score;
	}
	
	
	
	//draws the coin to screen
	public void drawCoin(Graphics g){
		g.drawAnimation(coin, getXPos(), getYPos());
	}
	
	
	
	//updates the animation
	public void updateAnimation(int delta){
		coin.update(delta);
	}
	
	
	
	//gets the remove boolean
	public boolean isRemove(){
		return remove;
	}

	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	
	
	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}

	@Override
	public void decrementYPos(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}
	
	//adds to entities's coord's on screen directly
	public void addToXPosDirectly(double increment) throws SlickException{
		xPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}
	
	public void addToYPosDirectly(double increment){
		yPos += increment;
		
		Rectangle playerRectangle = new Rectangle(player.getXPos(), player.getYPos(), Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT);
		Rectangle coinRectangle = new Rectangle(getXPos() + 1, getYPos() + 1, COIN_LENGTH, COIN_HEIGHT);
		
		if(playerRectangle.intersects(coinRectangle) && !remove){
			score.addToScore(this.getLevel(), 10);
			remove = true;
		}
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}

	
}
