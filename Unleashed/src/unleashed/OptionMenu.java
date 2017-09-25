package unleashed;

import org.newdawn.slick.state.*;

import unleashed.Game;
import unleashed.util.Info;

import org.newdawn.slick.*;
import org.lwjgl.input.*;


@Info(
	dateLastEdited = "12/2/2015",	
	purpose = "Controls options, this will be a screen that pauses the current state when entered to set options",
	otherInfo = "This menu will use a default background unless the player is currently accessing this from within a level, in which case, "
			   +" a screenshot will be used"
	)

@SuppressWarnings("unused")
public class OptionMenu extends BasicGameState{
	//ID is needed to identify what state this class represents
	private final int ID;
	
	//is the default background image
	private Image background;
	
	//is the screenshot of the previous gamestate
	private static Image screenshotOfPreviousGameState = null;
		
	//is the ID of the previous gamestate
	private static int IDOfPreviousGameState;
	
	//controls the filter effect
	private static float alpha;
	
	//coordinates of everything in this class that appears on screen
	private int Mouse_XPos = 0;
	private int Mouse_YPos = 0;
	
	Image tutorial;
	
	
	
	//sets ID
	public OptionMenu(int state){
		ID = state;
	}
	
	
	
	//controls everything about entering this state
	public static void setStatePrerequisites(int stateCallingFrom, Image screenshot, float alpha) throws SlickException{
		IDOfPreviousGameState = stateCallingFrom;
		screenshotOfPreviousGameState = screenshot;
		OptionMenu.alpha = alpha;
	}
	//controls everything about entering this state (overloading, can enter an alpha value)
	public static void setStatePrerequisites(float alpha) throws SlickException{
		OptionMenu.alpha = alpha;
	}
	


	//this method is called only once to initialize variables
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//initializing default background
		background = new Image("res/Background/Main Menu.png");
		
		tutorial = new Image("res/Background/Tutorial.png");
	}


	//this method is called on after update() is called on, used to display things on screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		//if this menu is being accessed outside a level, show the default background image, otherwise, use the screenshot
		if(screenshotOfPreviousGameState == null){
			g.drawImage(background, 0, 0);
			g.drawImage(tutorial, 0, 0);
			
		}else{
			//drawing the screenshot first
			g.drawImage(screenshotOfPreviousGameState, 0, 0);
			
			//drawing the filter 
			g.setColor(new Color(0.2f, 0.2f, 0.2f, alpha));
			g.fillRect(0, 0, Game.WINDOW_LENGTH, Game.WINDOW_HEIGHT);
			
			g.drawImage(tutorial, 0, 0);
			
		}
		
	}

	
	//this method is called on every frame, the "delta" parameter deals with unwanted lag and helps smooth gameflow
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Mouse_XPos = Mouse.getX();
		Mouse_YPos = Mouse.getY();
		Input input = gc.getInput();
		
		
		
		//if hitting escape again, return to game
		if(input.isKeyPressed(Game.keybindEscape)){
			if(screenshotOfPreviousGameState == null){
				sbg.enterState(Game.MAIN_MENU);
				
			}else{
				EscMenu.setStatePrerequisites(alpha);
				sbg.enterState(Game.ESC_MENU);
				
			}
			
			return;
		}else{
			if(screenshotOfPreviousGameState != null){
				//controls filtering effect
				if(alpha < 0.5f){
					alpha += 0.01 + 0.01 * (double) delta / 1000;
					
					if(alpha > 0.5f){
						alpha = 0.5f;
					}
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
