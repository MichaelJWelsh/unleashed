package unleashed.terrain;

import unleashed.util.*;
import org.newdawn.slick.*;
import unleashed.Game;


@Info(
		dateLastEdited = "12/1/2015",
		purpose = "An object of this class represents a 32x32 block in the terrain of a set level"
		)


public class Block {
	
	//Dimensions of block (the side lengths will always be equal because it is a square)
	public static final int BLOCK_SIDE_LENGTH = 32;
	
	//the ID's of each block (which essentially assigns the block to a specific 32x32 image)
	public static final int DIRT = 1;
	public static final int GRASS = 2;
	public static final int STONE_WALL = 3;
	public static final int BARS = 4;
	public static final int CRACKED_STONE_WALL = 5;
	public static final int CRACKED_STONE = 6;
	public static final int MOSSY_STONE = 7;
	public static final int MOSSY_STONE_WALL = 8;
	public static final int STONE = 9;
	
	//total number of different blocks
	public static final int NUMBER_OF_DIFFERENT_BLOCKS = 9;
	
	//The image of the block
	private Image block;
	
	//coordinates of the block ( (0,0) would be in the top-left with these coordinates ) )
	private double xPos;
	private double yPos;
	
	//original coordinates of the block ( (0,0) would be in the top-left with these coordinates)) before any manipulation
	private final double originalXPos;
	private final double originalYPos;
	
	//is the speed per frame (in pixels) of this block moving in the background
	private double speed;
	
	//is the block ID number
	private int ID;
	
	
	
	//constructor(s), initializes many members of this class
	//This constructor ultimately sets up a block that is fit to be drawn to the screen using the Java Graphics class
	public <T extends Number> Block(int ID, double xPos, double yPos, T speed) throws SlickException{
		
		//setting actual X-Position
		if(xPos > BLOCK_SIDE_LENGTH - 1){
			xPos = (int) xPos;
			while(xPos % BLOCK_SIDE_LENGTH != 0){
				xPos--;
			}
			this.xPos = xPos;
		}else if(xPos <= BLOCK_SIDE_LENGTH - 1 && xPos >= 0){
			this.xPos = 0;
		}else if (xPos < 0 && xPos >= -BLOCK_SIDE_LENGTH){
			this.xPos = -BLOCK_SIDE_LENGTH;
		}else if(xPos < -BLOCK_SIDE_LENGTH){
			xPos = (int) xPos;
			while(Math.abs(xPos) % BLOCK_SIDE_LENGTH != 0){
				xPos--;
			}
			this.xPos = xPos;
		}
		
		//setting actual Y-Position
		if(yPos > BLOCK_SIDE_LENGTH - 1){
			yPos = (int) yPos;
			while(yPos % BLOCK_SIDE_LENGTH != 0){
				yPos++;
			}
			this.yPos = Game.WINDOW_HEIGHT - yPos;
		}else if(yPos <= BLOCK_SIDE_LENGTH && yPos >= 0){
			this.yPos = Game.WINDOW_HEIGHT - BLOCK_SIDE_LENGTH;
		}else if(yPos < 0 && yPos >= -BLOCK_SIDE_LENGTH){
			this.yPos = Game.WINDOW_HEIGHT;
		}else if(yPos < -BLOCK_SIDE_LENGTH){
			yPos = (int) yPos;
			while(Math.abs(yPos) % BLOCK_SIDE_LENGTH != 0){
				yPos++;
			}
			this.yPos = Game.WINDOW_HEIGHT - yPos;
		}
		
		
		
		originalXPos = this.xPos;
		originalYPos = this.yPos;
		
		this.ID = ID;
		
		setBlockImage(ID);
		setSpeed(speed);
	}
	
	//constructor(s), initializes many members of this class
	//This constructor ultimately sets up a block that is fit to be drawn to the screen using the Java Graphics class,
	// and adjusts the block's coords accordingly if the terrain is already translated
	public <T extends Number> Block(int ID, double xPos, double yPos, T speed, Block referenceBlock) throws SlickException{
		//making helping variables
		double translatedGridHorizontal, translatedGridVertical;
		double givenXPos = xPos, givenYPos = yPos;
		givenYPos = Game.WINDOW_HEIGHT - givenYPos;
		
		
		
		if(referenceBlock.getTranslatedDistanceHorizontal() < 0){
			translatedGridHorizontal = (Math.abs(referenceBlock.getTranslatedDistanceHorizontal()) % BLOCK_SIDE_LENGTH) * -1;
		}else{
			translatedGridHorizontal = referenceBlock.getTranslatedDistanceHorizontal() % BLOCK_SIDE_LENGTH;
		}
		if(referenceBlock.getTranslatedDistanceVertical() < 0){
			translatedGridVertical = (Math.abs(referenceBlock.getTranslatedDistanceVertical()) % BLOCK_SIDE_LENGTH) * -1;
		}else{
			translatedGridVertical = referenceBlock.getTranslatedDistanceVertical() % BLOCK_SIDE_LENGTH;
		}
		
		
		
		//setting actual X-Position
		if(xPos > BLOCK_SIDE_LENGTH - 1){
			xPos = (int) xPos;
			while(xPos % BLOCK_SIDE_LENGTH != 0){
				xPos--;
			}
			this.xPos = xPos + translatedGridHorizontal;
		}else if(xPos <= BLOCK_SIDE_LENGTH - 1 && xPos >= 0){
			this.xPos = translatedGridHorizontal;
		}else if (xPos < 0 && xPos >= -BLOCK_SIDE_LENGTH){
			this.xPos = -BLOCK_SIDE_LENGTH + translatedGridHorizontal;
		}else if(xPos < -BLOCK_SIDE_LENGTH){
			xPos = (int) xPos;
			while(Math.abs(xPos) % BLOCK_SIDE_LENGTH != 0){
				xPos--;
			}
			this.xPos = xPos + translatedGridHorizontal;
		}
		
		//setting actual Y-Position
		if(yPos > BLOCK_SIDE_LENGTH - 1){
			yPos = (int) yPos;
			while(yPos % BLOCK_SIDE_LENGTH != 0){
				yPos++;
			}
			this.yPos = Game.WINDOW_HEIGHT - yPos + translatedGridVertical;
		}else if(yPos <= BLOCK_SIDE_LENGTH && yPos >= 0){
			this.yPos = Game.WINDOW_HEIGHT - BLOCK_SIDE_LENGTH + translatedGridVertical;
		}else if(yPos < 0 && yPos >= -BLOCK_SIDE_LENGTH){
			this.yPos = Game.WINDOW_HEIGHT + translatedGridVertical;
		}else if(yPos < -BLOCK_SIDE_LENGTH){
			yPos = (int) yPos;
			while(Math.abs(yPos) % BLOCK_SIDE_LENGTH != 0){
				yPos++;
			}
			this.yPos = Game.WINDOW_HEIGHT - yPos + translatedGridVertical;
		}
		
		
		
		//adjusting coordinates to eliminate offset of intended block placement
		if(givenXPos < this.xPos){
			this.xPos -= BLOCK_SIDE_LENGTH;
		}else if(givenXPos - BLOCK_SIDE_LENGTH > this.xPos){
			this.xPos += BLOCK_SIDE_LENGTH;
		}
		if(givenYPos < this.yPos){
			this.yPos -= BLOCK_SIDE_LENGTH;
		}else if(givenYPos > this.yPos + BLOCK_SIDE_LENGTH){
			this.yPos += BLOCK_SIDE_LENGTH;
		}
		
		
		
		//setting other coordinates, which are then not subject to change (which the exception of the 'speed')
		originalXPos = this.xPos - referenceBlock.getTranslatedDistanceHorizontal();
		originalYPos = this.yPos - referenceBlock.getTranslatedDistanceVertical();
		
		this.ID = ID;
		
		setBlockImage(ID);
		setSpeed(speed);
	}
	
	
	
	//returns block image
	public Image getBlockImage(){
		return block;
	}
	
	//sets block image
	private void setBlockImage(int type) throws SlickException{
		switch(type){
			case DIRT:
				block = new Image("res/Terrain/Soil.png");
				break;
			case GRASS:
				block = new Image("res/Terrain/Topsoil.png");
				break;
			case STONE_WALL:
				block = new Image("res/Terrain/Stonewall.png");
				break;
			case BARS:
				block = new Image("res/Terrain/Bars.png");
				break;
			case CRACKED_STONE:
				block = new Image("res/Terrain/CrackedStone.png");
				break;
			case CRACKED_STONE_WALL:
				block = new Image("res/Terrain/CrackedStoneWall.png");
				break;
			case MOSSY_STONE:
				block = new Image("res/Terrain/MossyStone.png");
				break;
			case MOSSY_STONE_WALL:
				block = new Image("res/Terrain/MossyStoneWall.png");
				break;
			case STONE:
				block = new Image("res/Terrain/Stone.png");
				break;
			default:
				System.err.println("Error in input, make sure you are using (for example) 'Block.DIRT' when calling the method: setBlockImage(int type) ");
				break;
		}
	}
	
	
	
	//gets the block ID
	public int getBlockID(){
		return ID;
	}
	
	
	
	//these methods count (0,0) as the top-left
	//gets the amount of pixels the image has been translated across the screen
	public double getTranslatedDistanceHorizontal(){
		return xPos - originalXPos;
	}
	public double getTranslatedDistanceVertical(){
		return yPos - originalYPos;
	}
	
	//these are different forms where the original coords are rounded first
	public int getTranslatedDistanceHorizontalEstimated(){
		return getXPos() - getOriginalXPos();
	}
	public int getTranslatedDistanceVerticalEstimated(){
		return getYPos() - getOriginalYPos();
	}
	
	
	
	//gets original coordinates of block (initial coordinates)
	public int getOriginalXPos(){
		return (int) Math.round(originalXPos);
	}
	public int getOriginalYPos(){
		return (int) Math.round(originalYPos);
	}
	
	
	
	//gets current/original inverted yPos (as if (0,0) is in the bottom left)
	public int getInvertedOriginalYPos(){
		return (int) Math.round(Game.WINDOW_HEIGHT - originalYPos);
	}
	public int getInvertedYPos(){
		return (int) Math.round(Game.WINDOW_HEIGHT - yPos);
	}
	
	
	
	//gets corresponding coordinates of block (with the top-left being (0,0))
	public int getXPos(){
		return (int) Math.round(xPos);
	}
	public int getYPos(){
		return (int) Math.round(yPos);
	}
	
	//increments/decrements block on screen based on speed (i.e: speed per frame)
	public void incrementXPos(double delta){
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}
	public void decrementXPos(double delta){
		xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}
	
	public void incrementYPos(double delta){
		yPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}
	public void decrementYPos(double delta){
		yPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
	}
	
	
	
	//adds to block's coord's on screen directly
	public void addToXPosDirectly(double increment){
		xPos += increment;
	}
	
	public void addToYPosDirectly(double increment){
		yPos += increment;
	}
	
	
	
	//resets the xPos and yPos of the block_object, back to the unmanipulated/untranslated (original) coordinates
	void resetPosition(){
		xPos = getOriginalXPos();
		yPos = getOriginalYPos();
	}
	
	
	
	//sets the speed of block
	public <T extends Number> void setSpeed(T speed){
		this.speed = speed.doubleValue();
		if(this.speed <= 0){
			this.speed = 1;
			System.err.println("Speed of block is <= 0 !");
		}
	}
	
	//gets the speed of block
	public double getSpeedPerFrame(){
		return speed;
	}
	
}
