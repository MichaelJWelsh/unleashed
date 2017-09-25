package unleashed.entities;


import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.*;
import org.newdawn.slick.SlickException;

import unleashed.Game;
import unleashed.terrain.Block;
import unleashed.terrain.Terrain;
import unleashed.util.*;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class represents a BlackHole entity, which moves over blocks and makes them void"
		)

public class BlackHole extends Entity{
	//is the BlackHole animation
	private Animation blackHole;
	
	//controls how long each frame is played through animation
	private int[] duration = {100,100,100,100};
	
	//is the terrain which is referenced
	Terrain terrain;
	
	//is the temporary terrain
	Set<Block> tempTerrain = new HashSet<Block>();
	
	//is the blackhole length and height
	public static final int LENGTH_OF_BLACKHOLE = 64;
	public static final int HEIGHT_OF_BLACKHOLE = 64;
	
	//is this object still good?
	private boolean remove = false;
	
	
	
	//constructor
	public BlackHole(int level, Time timer, double speed, Terrain terrain) throws SlickException {
		super(level, timer, 
				Game.WINDOW_LENGTH + 20, new Block(1,1,Game.WINDOW_HEIGHT * Math.random(),1).getYPos() + (int)(Math.abs(terrain.getTerrainSet().iterator().next().getTranslatedDistanceVertical()) % Block.BLOCK_SIDE_LENGTH) - 10, speed);
		
		this.terrain = terrain;
		
		Image bl1 = new Image("res/Entities/Black Hole1.png");
		Image bl2 = new Image("res/Entities/Black Hole2.png");
		Image bl3 = new Image("res/Entities/Black Hole3.png");
		Image bl4 = new Image("res/Entities/Black Hole4.png");
		
		bl1.setFilter(Image.FILTER_NEAREST);
		bl2.setFilter(Image.FILTER_NEAREST);
		bl3.setFilter(Image.FILTER_NEAREST);
		bl4.setFilter(Image.FILTER_NEAREST);
		
		bl1 = bl1.getScaledCopy(2);
		bl2 = bl2.getScaledCopy(2);
		bl3 = bl3.getScaledCopy(2);
		bl4 = bl4.getScaledCopy(2);
		
		Image[] blackHoles = {bl1,bl2,bl3,bl4};
		
		blackHole = new Animation(blackHoles, duration, false);
	}
	
	
	
	//draws the BlackHole
	public void drawBlackHole(Graphics g){
		g.drawAnimation(blackHole, getXPos(), getYPos());
	}
	
	
	
	//updates animation
	public void updateAnimation(int delta){
		blackHole.update(delta);
	}
	
	
	
	//returns if it should be removed or not
	public boolean isRemove(){
		return remove;
	}

	
	
	//throws in the effect when changing coordinates, of the black hole
	public void incrementXPosWithEffect(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		effect();
	}
	
	public void decrementXPosWithEffect(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		effect();
	}

	public void incrementYPosWithEffect(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		if(getYPos() < -200){
			remove = true;
			if(!tempTerrain.isEmpty()){
				tempTerrain.forEach(e -> {
					terrain.getTerrainSet().add(e);
				});
				
				tempTerrain.clear();
			}
			return;
		}
		effect();
	}

	public void decrementYPosWithEffect(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		if(getYPos() > Game.WINDOW_HEIGHT + 200){
			remove = true;
			if(!tempTerrain.isEmpty()){
				tempTerrain.forEach(e -> {
					terrain.getTerrainSet().add(e);
				});
				
				tempTerrain.clear();
			}
			return;
		}
		effect();
	}
	
	
	
	//is the blackhole effect
	double translatedHorizontalPrevious = 0;
	double translatedVerticalPrevious = 0;
	public void effect(){
		if(!tempTerrain.isEmpty()){
			tempTerrain.forEach(e -> {
				e.addToXPosDirectly(terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal() - translatedHorizontalPrevious);
				e.addToYPosDirectly(terrain.getTerrainSet().iterator().next().getTranslatedDistanceVertical() - translatedVerticalPrevious);
				terrain.getTerrainSet().add(e);
			});
			
			tempTerrain.clear();
		}
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceRectangle = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			Rectangle blackHoleRectangle = new Rectangle(getXPos(), getYPos(), LENGTH_OF_BLACKHOLE, HEIGHT_OF_BLACKHOLE);
			
			translatedHorizontalPrevious = referenceBlock.getTranslatedDistanceHorizontal();
			translatedVerticalPrevious = referenceBlock.getTranslatedDistanceVertical();
			
			if(referenceRectangle.intersects(blackHoleRectangle)){
				tempTerrain.add(referenceBlock);
			}
			
		}
		
		tempTerrain.forEach(e -> {
			terrain.getTerrainSet().remove(e);
		});
	}
	
	
	
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException {}

	
	
	@Override
	public void incrementXPos(double delta) throws SlickException {
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}

	@Override
	public void decrementXPos(double delta) throws SlickException {
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}

	@Override
	public void incrementYPos(double delta) throws SlickException {
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}

	@Override
	public void decrementYPos(double delta) throws SlickException {
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}
	
	//adds to entities's coord's on screen directly
	public void addToXPosDirectly(double increment) throws SlickException{
		xPos += increment;
	}
	
	public void addToYPosDirectly(double increment) throws SlickException{
		yPos += increment;
	}

	
	
	@Override
	public void switchGravity() throws SlickException {}

}
