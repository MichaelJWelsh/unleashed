package unleashed;

import org.newdawn.slick.state.*;

import unleashed.Game;
import unleashed.entities.EntityCollection;
import unleashed.entities.Player;
import unleashed.terrain.Background;
import unleashed.terrain.Block;
import unleashed.terrain.Terrain;
import unleashed.util.Info;
import unleashed.util.Time;

import org.newdawn.slick.*;


@Info(
	dateLastEdited = "12/4/2015",
	purpose = "Is level THREE. Play through this to win"
	)


public class LevelThree extends BasicGameState{
	//ID is needed to identify what state this class represents
	private final int ID;
	
	//creating player
	private Player player;
	
	//creating terrain
	Terrain terrain;
	
	//creating a timer object, which keeps track of time passed.  300 seconds in this level
	Time timer;
	
	//creating background
	Background background;
	
	//creating EntityCollection
	EntityCollection entityCollection;
	
	//did you win?
	private boolean win;
	
	//images for winning
	Image win1, win2;
	
	
	
	//sets ID
	public LevelThree(int state){
		ID = state;
	}


	//this method is called only once to initialize variables
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		//timer is made
		timer = new Time(300);
		
		//player is made (level, Time_object, initialXPosition, speedPerFrame, initialAnimation)
		player = new Player(getID(), timer, 100, 4, null);
		
		//terrain is made (Player_object, txt file location)
		terrain = new Terrain(player, "world/Level Three.txt");
		
		//background is made
		background = new Background(terrain);
		
		//setting the entities terrain that can be referenced
		player.setReferenceTerrain(terrain);
		
		//entityCollection is made
		entityCollection = new EntityCollection(ID, timer, player, terrain);
		
		//setting the entityCollection to player which will be referenced
		player.setReferenceEntityCollection(entityCollection);
		
		//haven't won yet
		win = false;
		
		//initializing images
		win1 = new Image("res/Background/Winning.png");
		win2 = new Image("res/Background/YouWin.png");
		
	}


	//this method is called on after update() is called on, used to display things on screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		/* PREREQUISITES */
		g.setColor(Color.white); //sets the color of text to white if it isn't already
		
		
		/* BACKGROUND */
		background.drawBackground(g);
		 
		
		/* TERRAIN */
		terrain.drawTerrain(g);
		
		
		/* Entities */
		entityCollection.drawEntities(g);
		
		
		/* PLAYER */
		g.drawAnimation(player.getMovingAnimation(), player.getXPos(), player.getYPos());
		
		
		/* TIME REMAINING */
		g.drawString(String.valueOf(timer.getTimeRemaining()) + "s", Game.WINDOW_LENGTH - Game.WINDOW_LENGTH / 20, Game.WINDOW_HEIGHT / 54);
		
		
		/* SCORE OF LEVEL */
		g.drawString("Score:" + String.valueOf(Game.score.getScoreOfLevel(ID)), Game.WINDOW_LENGTH / 20 - 40, Game.WINDOW_HEIGHT - 25);
		
		
		/* HEALTH DISPLAY */
		player.drawHealth();

		/* WIN? */
		if(win){
			g.drawImage(win1, 0, 0);
			g.drawImage(win2, 0, 0);
			g.drawString("TOTAL SCORE: " + String.valueOf(Game.score.getTotalScore()), Game.WINDOW_LENGTH / 20 - 40, Game.WINDOW_HEIGHT - 25);
		}
	}

	
	@SuppressWarnings("unused")
	//this method is called on every frame, the "delta" parameter deals with unwanted lag and helps smooth gameflow
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		/* 
		 * MANDATORY ~ helps with major lag compensation
		 * (i.e: At most in a single frame, everything can move across an entire tile)
		 */
		if(player.getSpeedPerFrame() + player.getSpeedPerFrame() * delta / 1000 > Block.BLOCK_SIDE_LENGTH){
			delta = (int) (250 * (Block.BLOCK_SIDE_LENGTH - player.getSpeedPerFrame()));
		}
		
		
		
		//gets the input from outside source
		Input input = gc.getInput();
		
		
		
		//if pressing down the escape button, then pause the game a bring up the EscMenu
		if(input.isKeyPressed(Game.keybindEscape)){
			Image screenshot = new Image(gc.getWidth(), gc.getHeight());
			gc.getGraphics().copyArea(screenshot, 0, 0);
			
			EscMenu.setStatePrerequisites(ID, screenshot);
			sbg.enterState(Game.ESC_MENU);
			
			input.clearKeyPressedRecord();
			input.clearMousePressedRecord();
			
			return;
		}else if(player.getHealth() == 0){
			if(input.isMouseButtonDown(0) || input.isMouseButtonDown(1) || 
					input.isKeyDown(Game.keybindRestart)){ //if a key is pressed upon death (restart,or any mouse buttons), restart level
				
				gc.reinit();
				sbg.enterState(ID);
				
				input.clearKeyPressedRecord();
				input.clearMousePressedRecord();
				
				Game.score.resetScoreOfLevel(ID);
			}
		}else if(input.isKeyDown(Game.keybindRestart)){		//if pressing down the restart button, then completely restart the level
			if(win){
				gc.reinit();
				sbg.enterState(Game.MAIN_MENU);
				Game.score.resetAllScores();
				
				input.clearKeyPressedRecord();
				input.clearMousePressedRecord();
			}else{
				gc.reinit();
				sbg.enterState(ID);
				
				input.clearKeyPressedRecord();
				input.clearMousePressedRecord();
				
				Game.score.resetScoreOfLevel(ID);
			}
		}else if(!win){		//else: let the game commence
			
			
			
			/*
			 * THE CODE THAT FOLLOWS MAKES UP THE LEVEL's FUNCTIONALITY
			 * 
			 * THIS CODE ONLY RUNS IF THERE ARE NO INTERRUPTIONS (LIKE PAUSING/RESTARTING)
			 * 
			 */
			
			
			
			Player_Movement:
				//if changing gravity
				if(input.isKeyDown(Game.keybindChangeGravity)){
					player.switchGravity();
				}
		
				//if up then jump, else fall
				if(input.isKeyDown(Game.keybindJump) && !(player.isJumping())){
					player.incrementYPos(delta);
				}else if(!(player.isJumping())){
					player.decrementYPos(delta);
				}else{	//if player is jumping
					player.incrementYPos(delta);
				}
				
				//if moving left or right
				if(input.isKeyDown(Game.keybindLeft) && input.isKeyDown(Game.keybindRight)){
					if(player.isFacingRight()){
						player.incrementXPos(delta);
					}
					if(player.isFacingLeft()){
						player.decrementXPos(delta);
					}
				}else if(input.isKeyDown(Game.keybindLeft)){
					if(player.isGravityDownward() && !player.isJumping()){
						player.setMovingAnimation(Player.WALKING_LEFT);
					}else if(!player.isGravityDownward() && !player.isJumping()){
						player.setMovingAnimation(Player.FLIPPED_WALKING_LEFT);
					}else if(player.isGravityDownward() && player.isJumping()){
						player.setMovingAnimation(Player.JUMPING_LEFT);
					}else if(!player.isGravityDownward() && player.isJumping()){
						player.setMovingAnimation(Player.FLIPPED_JUMPING_LEFT);
					}
					
					player.decrementXPos(delta);
				}else if(input.isKeyDown(Game.keybindRight)){
					if(player.isGravityDownward() && !player.isJumping()){
						player.setMovingAnimation(Player.WALKING_RIGHT);
					}else if(!player.isGravityDownward() && !player.isJumping()){
						player.setMovingAnimation(Player.FLIPPED_WALKING_RIGHT);
					}else if(player.isGravityDownward() && player.isJumping()){
						player.setMovingAnimation(Player.JUMPING_RIGHT);
					}else if(!player.isGravityDownward() && player.isJumping()){
						player.setMovingAnimation(Player.FLIPPED_JUMPING_RIGHT);
					}
					
					player.incrementXPos(delta);
				}else{
					if(!player.isJumping()){
						player.getMovingAnimation().restart();
					}
				}
			
				if(terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal() < -6060){
					win = true;
					Game.score.addToScore(ID, timer.getTimeRemaining());
				}
			
			Background_Tracker:
				background.maintainBackground(delta);
				
				
				
			EntityCollection_Tracker:
				entityCollection.maintainEntities(delta);
				
				
				
			Time_Tracker:
				timer.maintainTime(delta);
				
				
		}
	}

	
	//returns ID when referenced
	@Override
	public int getID() {
		return ID;
	}
	
}
