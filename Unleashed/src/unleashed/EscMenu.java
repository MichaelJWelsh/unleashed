package unleashed;

import org.newdawn.slick.state.*;

import unleashed.Game;
import unleashed.util.Info;



import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;


@Info(
	dateLastEdited = "12/1/2015",
	purpose = "This class is the menu that pops-up when the 'escape' key is hit while in game."
			+ "This class serves to make quick changes to game functionality while playing the game, with ease.",
	otherInfo = "IT IS EXTREMELY IMPORTANT THAT WHEN SWITCHING TO THIS STATE, the 'enterState(Game.ESC_MENU)' is not called first,"
			   +" but rather the 'setStatePrerequisites()' is called first, and then the 'enterState(Game.ESC_MENU)' "
	)


public class EscMenu extends BasicGameState{
	//ID is needed to identify what state this class represents
	private final int ID;
	
	//is the screenshot of the previous gamestate
	private static Image screenshotOfPreviousGameState;
	
	//is the ID of the previous gamestate
	private static int IDOfPreviousGameState;
	
	//length and height of all buttons on MainMenu
	private final short buttonLength = 300;
	private final short buttonHeight = 64;
		
	//Images for every button on screen
	private Image resume;
	private Image options;
	private Image quitGame;
		
	//coordinates of everything in this class that appears on screen
	private int Mouse_XPos = 0;
	private int Mouse_YPos = 0;
	private final short Button_XPos = ((Game.WINDOW_LENGTH / 2) - (buttonLength / 2)); 	//all buttons share same x coord
	private final short ResumeButton_YPos = Game.WINDOW_HEIGHT / 2 - 20; 					//50 pixels below center
	private final short OptionsButton_YPos = ResumeButton_YPos + buttonHeight + 20; 		//20 pixel spacing
	private final short QuitButton_YPos = OptionsButton_YPos + buttonHeight + 20; 		//20 pixel spacing
	
	//controls the filter effect
	private static float alpha = 0;
	
	
	
	//sets ID
	public EscMenu(int state){
		ID = state;
	}
	
	
	
	//controls everything about entering this state
	public static void setStatePrerequisites(int stateCallingFrom, Image screenshot) throws SlickException{
		IDOfPreviousGameState = stateCallingFrom;
		screenshotOfPreviousGameState = screenshot;
		alpha = 0;
	}
	//controls everything about entering this state (overloading, can enter an alpha value)
	public static void setStatePrerequisites(float alpha) throws SlickException{
		EscMenu.alpha = alpha;
	}


	
	//this method is called only once to initialize variables
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//initializing images
		resume = new Image("res/Menu/ResumeDefault.png");
		options = new Image("res/Menu/OptionsDefault.png");
		quitGame = new Image("res/Menu/QuitDefault.png");
		
	}


	//this method is called on after update() is called on, used to display things on screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		//drawing the screenshot first
		g.drawImage(screenshotOfPreviousGameState, 0, 0);
		
		//drawing the filter 
		g.setColor(new Color(0.2f, 0.2f, 0.2f, alpha));
		g.fillRect(0, 0, Game.WINDOW_LENGTH, Game.WINDOW_HEIGHT);
		
		//drawing the buttons
		g.drawImage(resume, Button_XPos, ResumeButton_YPos);
		g.drawImage(options, Button_XPos, OptionsButton_YPos);
		g.drawImage(quitGame, Button_XPos, QuitButton_YPos);
			
	}

	
	//this method is called on every frame, the "delta" parameter deals with unwanted lag and helps smooth gameflow
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Mouse_XPos = Mouse.getX();
		Mouse_YPos = Mouse.getY();
		Input input = gc.getInput();
		
		
		
		//if hitting escape again, return to game
		if(input.isKeyPressed(Game.keybindEscape)){
			sbg.enterState(IDOfPreviousGameState);
			
			return;
		}else{
			
			//controls filtering effect
			if(alpha < 0.5f){
				alpha += 0.01 + 0.01 * (double) delta / 1000;
				
				if(alpha > 0.5f){
					alpha = 0.5f;
				}
			}
			
			
			
			if(input.isMousePressed(0) || input.isMousePressed(1)){
				if(Mouse_XPos >= Button_XPos && Mouse_XPos <= (Button_XPos + buttonLength)){
					//if "Resume" button, start LevelOne
					if(Mouse_YPos <= (Game.WINDOW_HEIGHT - ResumeButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - ResumeButton_YPos - buttonHeight)){
						resume = new Image("res/Menu/ResumeClick.png");
						sbg.enterState(IDOfPreviousGameState);
					}
					
					//if "Options" button
					if(Mouse_YPos <= (Game.WINDOW_HEIGHT - OptionsButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - OptionsButton_YPos - buttonHeight)){
						options = new Image("res/Menu/OptionsClick.png");
						OptionMenu.setStatePrerequisites(ID, screenshotOfPreviousGameState, alpha);
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
					if(Mouse_YPos <= (Game.WINDOW_HEIGHT - ResumeButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - ResumeButton_YPos - buttonHeight)){
						resume = new Image("res/Menu/ResumeHover.png");
						options = new Image("res/Menu/OptionsDefault.png");
						quitGame = new Image("res/Menu/QuitDefault.png");
					}else if(Mouse_YPos <= (Game.WINDOW_HEIGHT - OptionsButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - OptionsButton_YPos - buttonHeight)){
						resume = new Image("res/Menu/ResumeDefault.png");
						options = new Image("res/Menu/OptionsHover.png");
						quitGame = new Image("res/Menu/QuitDefault.png");
					}else if(Mouse_YPos <= (Game.WINDOW_HEIGHT - QuitButton_YPos) && Mouse_YPos >= (Game.WINDOW_HEIGHT - QuitButton_YPos - buttonHeight)){
						resume = new Image("res/Menu/ResumeDefault.png");
						options = new Image("res/Menu/OptionsDefault.png");
						quitGame = new Image("res/Menu/QuitHover.png");
					}else{
						resume = new Image("res/Menu/ResumeDefault.png");
						options = new Image("res/Menu/OptionsDefault.png");
						quitGame = new Image("res/Menu/QuitDefault.png");
					}
				}else{
					resume = new Image("res/Menu/ResumeDefault.png");
					options = new Image("res/Menu/OptionsDefault.png");
					quitGame = new Image("res/Menu/QuitDefault.png");
				}
			}
		}
	}

	
	//returns ID when referenced
	@Override
	public int getID() {
		return ID;
	}
	
}