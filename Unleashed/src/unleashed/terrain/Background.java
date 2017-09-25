package unleashed.terrain;

import org.newdawn.slick.*;
import unleashed.util.Info;
import unleashed.Game;


@Info(
		dateLastEdited = "12/1/2015",
		purpose = "This class serves to neatly display the multi-image background and handle background movement.",
		otherInfo = "This class assumes every image used in the background is Game.WINDOW_LENGTH wide and Game.WINDOW_HEIGHT tall"
		)


final public class Background {
	//declaring the multi-image background
	private Image bricks;
	private Animation columns;
	
	//keeps track of how many milliseconds the images in the 'columns' animation will be displayed
	private int[] duration = {100};
	
	//is the terrain to reference for algorithm of background translations
	private Terrain terrain;
	
	//controls how fast the images/animations are translated
	private static final double BRICKS_TRANSLATION_FACTOR = 6.0;
	private static final double COLUMNS_TRANSLATION_FACTOR = 3.0;
	
	
	
	//constructor, initializes many members of this class
	public Background(Terrain terrain) throws SlickException{
		bricks = new Image("res/Background/BackgroundBricks.png");
		
		Image[] columnImages = {new Image("res/Background/Background_Columns.png")};
		columns = new Animation(columnImages, duration, false);
		
		
		
		//setting the reference terrain
		this.terrain = terrain;
	}
	
	
	
	//maintains the background animations
	public void maintainBackground(int delta){
		columns.update(delta);
	}
	
	
	
	//draws the background to the screen (bricks are always drawn first, then columns), is the bulk of this class
	private int translatedDistanceVertical, translatedDistanceHorizontal;
	public void drawBackground(Graphics g){
		
		//local class with a method for extra translation manipulation capability
		class TranslationManipulation{
			public void setTranslatedDistances(double translationFactor){
				translatedDistanceHorizontal = terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontalEstimated();
				translatedDistanceVertical = terrain.getTerrainSet().iterator().next().getTranslatedDistanceVerticalEstimated();
				
				
				while(Math.abs(translatedDistanceHorizontal) > Game.WINDOW_LENGTH * translationFactor){
					if(translatedDistanceHorizontal > 0){
						translatedDistanceHorizontal -= Game.WINDOW_LENGTH * translationFactor;
					}else{
						translatedDistanceHorizontal += Game.WINDOW_LENGTH * translationFactor;
					}
				}
				while(Math.abs(translatedDistanceVertical) > Game.WINDOW_HEIGHT * translationFactor){
					if(translatedDistanceVertical > 0){
						translatedDistanceVertical -= Game.WINDOW_HEIGHT * translationFactor;
					}else{
						translatedDistanceVertical += Game.WINDOW_HEIGHT * translationFactor;
					}
				}
			}
		}
		
		
		
		TranslationManipulation manipulator = new TranslationManipulation();
		manipulator.setTranslatedDistances(1);
		
		
		
		if(translatedDistanceHorizontal >= 0){  //player can't seemingly move in the negative coordinate plain; therefore, it is ">= 0"
			if(translatedDistanceVertical > 0){
				
				manipulator.setTranslatedDistances(BRICKS_TRANSLATION_FACTOR);
				g.drawImage(bricks, 0, (float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, 0, (float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR)  - Game.WINDOW_HEIGHT);
				
				manipulator.setTranslatedDistances(COLUMNS_TRANSLATION_FACTOR);
				g.drawAnimation(columns, 0, (float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, 0, (float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  - Game.WINDOW_HEIGHT);
				
			}else if(translatedDistanceVertical < 0){
				
				manipulator.setTranslatedDistances(BRICKS_TRANSLATION_FACTOR);
				g.drawImage(bricks, 0, (float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, 0, (float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR)  + Game.WINDOW_HEIGHT);
				
				manipulator.setTranslatedDistances(COLUMNS_TRANSLATION_FACTOR);
				g.drawAnimation(columns, 0, (float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, 0, (float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  + Game.WINDOW_HEIGHT);
				
			}else{
				
				g.drawImage(bricks, 0, 0);
				g.drawAnimation(columns, 0, 0);
				
			}
			
		}else{
			if(translatedDistanceVertical > 0){
				
				manipulator.setTranslatedDistances(BRICKS_TRANSLATION_FACTOR);
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR)  - Game.WINDOW_HEIGHT);
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR) - Game.WINDOW_HEIGHT);
				
				manipulator.setTranslatedDistances(COLUMNS_TRANSLATION_FACTOR);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR),
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  - Game.WINDOW_HEIGHT);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  - Game.WINDOW_HEIGHT);
				
			}else if(translatedDistanceVertical < 0){
				
				manipulator.setTranslatedDistances(BRICKS_TRANSLATION_FACTOR);
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR)  + Game.WINDOW_HEIGHT);
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_HEIGHT);
				
				manipulator.setTranslatedDistances(COLUMNS_TRANSLATION_FACTOR);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR),
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  + Game.WINDOW_HEIGHT);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR));
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						(float) Math.floor(translatedDistanceVertical / COLUMNS_TRANSLATION_FACTOR)  + Game.WINDOW_HEIGHT);
				
			}else{
				
				
				manipulator.setTranslatedDistances(BRICKS_TRANSLATION_FACTOR);
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR), 
						(float) Math.floor(translatedDistanceVertical / BRICKS_TRANSLATION_FACTOR));
				g.drawImage(bricks, (float) Math.floor(translatedDistanceHorizontal / BRICKS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						0);
				
				manipulator.setTranslatedDistances(COLUMNS_TRANSLATION_FACTOR);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR),
						0);
				g.drawAnimation(columns, (float) Math.floor(translatedDistanceHorizontal / COLUMNS_TRANSLATION_FACTOR) + Game.WINDOW_LENGTH, 
						0);
				
			}
		}
	}
	
	
}
