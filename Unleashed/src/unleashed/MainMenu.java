package unleashed;

import org.newdawn.slick.state.*;

import unleashed.util.Info;

import org.newdawn.slick.*;
import org.lwjgl.input.*;


@Info(
	dateLastEdited = "11/29/2015",	
	purpose = "This is the main menu, very general functionality is found here",
	otherInfo = "This is the initial state (i.e: user will see the MainMenu upon loading-up game."
			   +"It's worth noting that when drawing graphics with java libraries, the coordinate (0,0) is located in the TOP LEFT of gui,"
			   +"while in the Slick2D library the coordinate (0,0) is located in the BUTTOM LEFT of gui."
	)


final public class MainMenu extends BasicGameState{
	//ID is needed to identify what state this class represents
	private final int ID;
	
	//length and height of all buttons on MainMenu
	private final short buttonLength = 300;
	private final short buttonHeight = 64;
	
	//Images for every button on screen
	private Image play;
	private Image options;
	private Image quitGame;
	
	//Image for background
	private Image background;
	
	//coordinates of everything in this class that appears on screen
	private int Mouse_XPos = 0;
	private int Mouse_YPos = 0;
	private final short Button_XPos = ((Game.WINDOW_LENGTH / 2) - (buttonLength / 2)); 	//all buttons share same x coord
	private final short PlayButton_YPos = Game.WINDOW_HEIGHT / 2 - 20; 					//50 pixels below center
	private final short OptionsButton_YPos = PlayButton_YPos + buttonHeight + 20; 		//20 pixel spacing
	private final short QuitButton_YPos = OptionsButton_YPos + buttonHeight + 20; 		//20 pixel spacing
	
	
	//sets ID
	public MainMenu(int state){
		ID = state;
	}


	//this method is called only once to initialize variables
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//initializing all default images
		play = new Image("res/Menu/PlayDefault.png");
		options = new Image("res/Menu/OptionsDefault.png");
		quitGame = new Image("res/Menu/QuitDefault.png");
		background = new Image("res/Background/Main Menu.png");
	}


	//this method is called on after update() is called on, used to display things on screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		//drawing background to screen
		g.drawImage(background, 0, 0);
		
		//drawing buttons to screen
		g.drawImage(play, Button_XPos, PlayButton_YPos);
		g.drawImage(options, Button_XPos, OptionsButton_YPos);
		g.drawImage(quitGame, Button_XPos, QuitButton_YPos);
	
	}
		
	
	//this method is called on every frame, the "delta" parameter deals with unwanted lag and helps smooth gameflow
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		
		Mouse_XPos = Mouse.getX();
		Mouse_YPos = Mouse.getY();
		
		//if left-clicked or right-clicked, identify which button and what happens
		if(input.isMousePressed(0) || input.isMousePressed(1)){
			if(Mouse_XPos >= Button_XPos && Mouse_XPos <= (Button_XPos + buttonLength)){
				//if "Play" button, start LevelOne
				if(Mouse_YPos <= (Game.WINDOW_HEIGHT - PlayButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - PlayButton_YPos - buttonHeight)){
					play = new Image("res/Menu/PlayClick.png");
					sbg.enterState(Game.LEVEL_ONE);
				}
				
				//if "Options" button
				if(Mouse_YPos <= (Game.WINDOW_HEIGHT - OptionsButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - OptionsButton_YPos - buttonHeight)){
					options = new Image("res/Menu/OptionsClick.png");
					sbg.enterState(Game.OPTION_MENU);
				}
				
				//if "Quit Game" button
				if(Mouse_YPos <= (Game.WINDOW_HEIGHT - QuitButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - QuitButton_YPos - buttonHeight)){
					quitGame = new Image("res/Menu/QuitClick.png");
					gc.exit();
				}
			}
		}else{ //if hovering over
			if(Mouse_XPos >= Button_XPos && Mouse_XPos <= (Button_XPos + buttonLength)){
				if(Mouse_YPos <= (Game.WINDOW_HEIGHT - PlayButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - PlayButton_YPos - buttonHeight)){
					play = new Image("res/Menu/PlayHover.png");
					options = new Image("res/Menu/OptionsDefault.png");
					quitGame = new Image("res/Menu/QuitDefault.png");
				}else if(Mouse_YPos <= (Game.WINDOW_HEIGHT - OptionsButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - OptionsButton_YPos - buttonHeight)){
					play = new Image("res/Menu/PlayDefault.png");
					options = new Image("res/Menu/OptionsHover.png");
					quitGame = new Image("res/Menu/QuitDefault.png");
				}else if(Mouse_YPos <= (Game.WINDOW_HEIGHT - QuitButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - QuitButton_YPos - buttonHeight)){
					play = new Image("res/Menu/PlayDefault.png");
					options = new Image("res/Menu/OptionsDefault.png");
					quitGame = new Image("res/Menu/QuitHover.png");
				}else{
					play = new Image("res/Menu/PlayDefault.png");
					options = new Image("res/Menu/OptionsDefault.png");
					quitGame = new Image("res/Menu/QuitDefault.png");
				}
			}else{
				play = new Image("res/Menu/PlayDefault.png");
				options = new Image("res/Menu/OptionsDefault.png");
				quitGame = new Image("res/Menu/QuitDefault.png");
			}
		}
		
	}

		
	//returns ID when referenced
	@Override
	public int getID() {
		return ID;
	}
	
}
