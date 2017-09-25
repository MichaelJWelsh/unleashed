package unleashed;

import org.newdawn.slick.state.*;
import org.newdawn.slick.*;

import unleashed.util.Info;
import unleashed.util.Score;




@Info(
	dateLastEdited = "12/2/2015",
	purpose = "This is the main class, the states are created and the gui is made here"
	)


/*
 * BY:
 * 
 * 	MICHAEL J. WELSH
 * 
 * 	BENJAMIN SITI
 * 
 */


public final class Game extends StateBasedGame{
	
	//dimensions for gui window (width,height), (1024x608)
	//32 pixels should be a factor for length and height, as the characters' size and block size for terrain
	//are all divisible by 32 without a remainder
	public static final int WINDOW_LENGTH = 1024;
	public static final int WINDOW_HEIGHT = 608;
	
	//fps for overall game, should be 30
	public static final int FPS = 30;
	
	//game name, gui window title will display this name
	private static final String GAME_NAME = "Unleashed";
	
	//setting states to corresponding numbers to reference
	public static final int MAIN_MENU = 1;
	public static final int OPTION_MENU = 2;
	public static final int ESC_MENU = 3;
	public static final int LEVEL_ONE = 4;
	public static final int LEVEL_TWO = 5;
	public static final int LEVEL_THREE = 6;
	
	//keybinds which are relevant to the game
	public static int keybindJump = Input.KEY_W;
	public static int keybindLeft = Input.KEY_A;
	public static int keybindRight = Input.KEY_D;
	public static int keybindChangeGravity = Input.KEY_SPACE;
	public static int keybindRestart = Input.KEY_R;
	public static int keybindEscape = Input.KEY_ESCAPE;
	
	//Is the score for game
	public static Score score = new Score();
	
	
	
	//setting game title, adding states
	public Game(String gameName){
		super(gameName);
		
		addState(new MainMenu(MAIN_MENU));
		addState(new OptionMenu(OPTION_MENU));
		addState(new EscMenu(ESC_MENU));
		addState(new LevelOne(LEVEL_ONE));
		addState(new LevelTwo(LEVEL_TWO));
		addState(new LevelThree(LEVEL_THREE));

	}
	
	
	//runs when (AppGameContainer_Object).start(), begins by entering the MainMenu
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		enterState(MAIN_MENU);
	}
	
	
	//main method, creates the game and runs it
	public static void main(String[] args){
		AppGameContainer appGameContainer;
		/*when the game is made, it can throw a SlickException which is specified in the Slick2D library.
		*Essentially, this exception is thrown only if something prevents the "appGameContainer" from
		*being initialized or starting.
		*/
		try{
			appGameContainer = new AppGameContainer(new Game(GAME_NAME));
			appGameContainer.setDisplayMode(WINDOW_LENGTH, WINDOW_HEIGHT, false);
			appGameContainer.setTargetFrameRate(FPS); // sets requested number of frames per second
			appGameContainer.setVSync(true);
			appGameContainer.setShowFPS(false);
			String[] icon = {"res/Background/Face32.png", "res/Background/Face16.png"};
			appGameContainer.setIcons(icon);
			appGameContainer.start();
		}catch(SlickException e){
			//print error out to console to diagnose
			e.printStackTrace();
		}
	}
}
