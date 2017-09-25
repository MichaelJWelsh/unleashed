package unleashed.terrain;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import org.newdawn.slick.*;
import unleashed.util.*;
import unleashed.entities.Player;
import unleashed.Game;


@Info(
		dateLastEdited = "11/15/2015",
		purpose = "This class serves to organize and group all blocks for each level",
		otherInfo = "Instances of this class are intertwined with instances of the 'Player' class,"
				   +"  because instances of this class require many fields from instances of the 'Player' class."
				   +"Terrain = a collage of block-objects to create a landscape for the entities."
		)


public class Terrain {
	
	//contains all the information on the blocks
	private Set<Block> terrain = new HashSet<Block>();
	
	//contains the ID of this level
	private final int level;
	
	//contains the file.txt location of the terrain
	String txtFileTerrainInfoLocation;
	
	//contains the Player_object which can be referred to
	Player player;
	
	
	
	//constructor, initializes many members of this class
	public Terrain(Player player, String txtFileTerrainInfoLocation) throws SlickException{
		//sets level variable for reference
		level = player.getLevel();
		
		//sets the file.txt location of the terrain
		this.txtFileTerrainInfoLocation = txtFileTerrainInfoLocation;
		
		//sets the Player_object which can be later referred to
		this.player = player;
		
		//setting up the 'terrain' set
		initializeTerrain(player, txtFileTerrainInfoLocation);
		
	}
	
	
	
	// TODO some sort of array compression
	//Essentially, this method reads information from a txt file that contains information on every block object in that level,
	//and adds the objects to the 'terrain' set
	@SuppressWarnings("unused")
	private void initializeTerrain(Player player, String txtFileTerrainInfoLocation) throws SlickException{
		//contains temporary information about block being analyzed
		String info;
		
		//reading from the file, and adding to the set 'terrain' accordingly
		try {
			Scanner input = new Scanner(new File(txtFileTerrainInfoLocation));
			
			while(input.hasNext()){
				//info's contents: "ID,xPos,yPos"
				info = input.next();
				
				//contains temporary information about the block being analyzed
				String workingString = "";
				int ID = 0, arrayLength = 0, arrayHeight = 0;
				byte numberOfCommas = 0;
				double xPos = 0, yPos = 0;
				boolean expectingID, expectingXPos, expectingYPos, expectingArrayLength, expectingArrayHeight;
				
			
				
				/*	ALGORITHM */
				for(int counter = 0; counter < info.length(); counter++){
					if(info.charAt(counter) == ','){
						numberOfCommas++;
					}
				}
				
				if(numberOfCommas == 4){		//ID, xPos, yPos, array_x, array_y
					expectingID = true;
					expectingXPos = true;
					expectingYPos = true;
					expectingArrayLength = true;
					expectingArrayHeight = true;
				}else if(numberOfCommas == 2){	//ID, xPos, yPos
					expectingID = true;
					expectingXPos = true;
					expectingYPos = true;
					expectingArrayLength = false;
					expectingArrayHeight = false;
				}else{
					System.err.println("Error in '" + txtFileTerrainInfoLocation + "' with " + info);
					input.close();
					return;
				}
				
				for(int counter = 0; counter < info.length(); counter++){
					if(info.charAt(counter) != ',' && (info.length() - 1 != counter)){
						workingString = workingString + String.valueOf(info.charAt(counter));
					}else if(expectingID){
						ID = Integer.parseInt(workingString);
						workingString = "";
						expectingID = false;
					}else if(expectingXPos){
						xPos = Double.parseDouble(workingString);
						workingString = "";
						expectingXPos = false;
					}else if(expectingYPos && numberOfCommas == 2){
						workingString = workingString + String.valueOf(info.charAt(counter));
						yPos = Double.parseDouble(workingString);
						workingString = "";
						expectingYPos = false;
					}else if(expectingYPos && numberOfCommas == 4){
						yPos = Double.parseDouble(workingString);
						workingString = "";
						expectingYPos = false;
					}else if(expectingArrayLength){
						arrayLength = Integer.parseInt(workingString);
						workingString = "";
						expectingArrayLength = false;
					}else if(expectingArrayHeight){
						workingString = workingString + String.valueOf(info.charAt(counter));
						arrayHeight = Integer.parseInt(workingString);
						workingString = "";
						expectingArrayHeight = false;
					}
				}
				Block block = new Block(ID, xPos, yPos, player.getSpeedPerFrame());
				Iterator<Block> iterator = terrain.iterator();
				boolean repeat = false;
				while(iterator.hasNext()){
					Block workingBlock = iterator.next();
					if(workingBlock.getXPos() == block.getXPos() && workingBlock.getYPos() == block.getYPos()){
						repeat = true;
					}
				}
				if(!repeat){
					terrain.add(block);
				}
				/*	END OF ALGORITHM */
			}
			input.close();
			
			if(terrain.isEmpty()){
				System.err.println("No map to load from directory: " + txtFileTerrainInfoLocation);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//gets level
	public int getLevel(){
		return level;
	}
	
	
	
	//gets set 'terrain'
	public Set<Block> getTerrainSet(){
		return terrain;
	}
	
	
	
	//draws out terrain on screen!  This method should be called in the 'render' method of each game state.
	//An algorithm in this method causes only blocks that need to be visually displayed to be visually displayed
	public void drawTerrain(Graphics g){
		terrain.forEach(e -> {
			if((e.getXPos() <= Game.WINDOW_LENGTH + 2 * Block.BLOCK_SIDE_LENGTH && e.getXPos() >= -2 * Block.BLOCK_SIDE_LENGTH) && 
					(e.getYPos() <= Game.WINDOW_HEIGHT + 2 * Block.BLOCK_SIDE_LENGTH && e.getYPos() >= -2 * Block.BLOCK_SIDE_LENGTH)){
				g.drawImage(e.getBlockImage(), e.getXPos(), e.getYPos());
			}
		});
	}
	
	
	
	//moves terrain accordingly
	public void incrementTerrainHorizontal(double delta){
		terrain.forEach(e -> e.incrementXPos(delta));
	}
	public void decrementTerrainHorizontal(double delta){
		terrain.forEach(e -> e.decrementXPos(delta));
	}
	
	public void incrementTerrainVertical(double delta){
		terrain.forEach(e -> e.incrementYPos(delta));
	}
	public void decrementTerrainVertical(double delta){
		terrain.forEach(e -> e.decrementYPos(delta));
	}
	
	
	
	//sets speed for each block
	public void setTerrainSpeed(double speedPerFrame){
		terrain.forEach(e -> {
			e.setSpeed(speedPerFrame);
		});
	}
	
	//gets speed shared between each block, and if a block exists with a different speed among the set, an error occurs!
	public double getTerrainSpeed(){
		double speed = -1;
		Iterator<Block> iterator = terrain.iterator();
		
		while(iterator.hasNext()){
			double iteratorSpeed = iterator.next().getSpeedPerFrame();
			if(speed == -1){
				speed = iteratorSpeed;
			}else if(iteratorSpeed != speed){
				System.err.println("Terrain contains blocks with varying speeds!  A speed should be shared throughout all members of the terrain!");
			}
		}
		
		return speed;
	}
	
	
	
	//resets the position of every block on screen
	public void resetTerrain(){
		terrain.forEach(e -> {
			e.resetPosition();
		});
	}
	
	
	
	/*
	 * Not recommended to use this method.
	 * This method is meant purely for the development of maps, and should not be used
	 *  at all in the actual finished game engine.
	 */
	@Deprecated
	public void writeToTerrainFile(String backupTextFileTerrainInfoLocation) throws SlickException{
		resetTerrain();
		
		int lineCounter = 0;
		PrintWriter x;
		try {
			x = new PrintWriter(backupTextFileTerrainInfoLocation);
			Iterator<Block> backupIterator = terrain.iterator();
			while(backupIterator.hasNext()){
				Block blockIterated = backupIterator.next();
				x.write(String.format("%d,%d,%d ", blockIterated.getBlockID(), blockIterated.getOriginalXPos(), blockIterated.getInvertedOriginalYPos()));
				
				if(++lineCounter == 13){
					lineCounter = 0;
					x.write("\n");
				}
			}
			lineCounter = 0;
			x.close();
			
			x = new PrintWriter(txtFileTerrainInfoLocation);
			Iterator<Block> iterator = terrain.iterator();
			while(iterator.hasNext()){
				Block blockIterated = iterator.next();
				x.write(String.format("%d,%d,%d ", blockIterated.getBlockID(), blockIterated.getOriginalXPos(), blockIterated.getInvertedOriginalYPos()));
				
				if(++lineCounter == 13){
					lineCounter = 0;
					x.write("\n");
				}
			}
			lineCounter = 0;
			x.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initializeTerrain(player, txtFileTerrainInfoLocation);
	}
	
	
	
}
