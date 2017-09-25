package unleashed.entities;

import java.awt.Rectangle;
import java.util.Iterator;

import org.newdawn.slick.*;
import unleashed.util.*;
import unleashed.Game;
import unleashed.terrain.Block;


@Info(
		dateLastEdited = "12/2/2015",
		purpose = "This class revolves around all the workings of the player",
		otherInfo = "components of class: animation manipulation, ability manipulation"
		)

final public class Player extends Entity{
	//is the player width and height (32 x 64 pixels)
	public static final int PLAYER_WIDTH = 32;
	public static final int PLAYER_HEIGHT = 64;
	
	//controls everything about how the player accelerates due to gravity
	private int timeCounterForVerticalMovement = 0;	
	private double verticalVelocity = 0;
	
	//controls how gravity is affected
	private final double maximumAllowedVerticalVelocity = 8; //pixels per frame & HAS TO BE LESS THAN Block.BLOCK_SIDE_LENGTH
	
	//controls which abilities the Player has access to based on level
	private boolean ability_gravityChange = true;
	
	//numbers which can be referenced when changing ability_abilityName
	public static final int ABILITY_GRAVITY_CHANGE = 0;
	
	//controls how long of a cooldown the ability Gravity_Switch is
	public static final int ABILITY_GRAVITY_CHANGE_COOLDOWN = 2; 	//in seconds
	
	//controls how long of a time player cannot take damage upon receiving damage
	public static final int TIME_PLAYER_IS_GRANTED_HEALTH_IMMUNITY = 1;  //in seconds
	
	//is the player's health (2 = full heart, 1 = half a heart, 0 = no hearts = death
	private short health = 2;
	
	//used in the 'setMovingAnimation()' method, sets 'moving' to the specified animation
	public static final int WALKING_RIGHT = 0;
	public static final int WALKING_LEFT = 1;
	public static final int JUMPING_RIGHT = 2;
	public static final int JUMPING_LEFT = 3;
	public static final int FLIPPED_WALKING_RIGHT = 12;
	public static final int FLIPPED_WALKING_LEFT = 13;
	public static final int FLIPPED_JUMPING_RIGHT = 14;
	public static final int FLIPPED_JUMPING_LEFT = 15;
	
	//is the entity collection
	EntityCollection entityCollection;
	
	
	//Health display
	private Image healthDisplay, youDiedText, deathFrame, outOfBoundsText, timeIsUpText;
	
	
	
	/*	ANIMATIONS */
	int[] duration = {120,120,120,120};		//100 milliseconds per animation change
	int[] durationSingle = {1000};
	// TODO finish these
	private Animation walkingRight, walkingLeft, jumpRight, jumpLeft;
	private Animation flippedWalkingRight, flippedWalkingLeft, flippedJumpRight, flippedJumpLeft;
	
	
	//constructor, initializes many members of this class
	public <T extends Number> Player(int level, Time timer, int initialXPos, T speed, Integer initialMotion) throws SlickException{
		super(level, timer, initialXPos, Game.WINDOW_HEIGHT / 2 - PLAYER_HEIGHT / 2, speed);
		
		if(initialXPos > Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2 || initialXPos < 0){
			System.err.println("Player is not spawned in a proper coordinate location!");
		}
		
		
		
		// TODO finish these
		//creating Image arrays to hold images that will be used for each corresponding animation
		Image[] walkingRightImages = {new Image("res/Character Sprites/Sprite Standard Right.png"), new Image("res/Character Sprites/WalkingRight.png"),
				new Image("res/Character Sprites/Sprite Standard Walking2 Right.png"),
				new Image("res/Character Sprites/Sprite Standard Walking3 Right.png")};
		Image[] walkingLeftImages = {new Image("res/Character Sprites/Sprite Standard Left.png"), new Image("res/Character Sprites/WalkingLeft.png"),
				new Image("res/Character Sprites/Sprite Standard Walking2 Left.png"),
				new Image("res/Character Sprites/Sprite Standard Walking3 Left.png")};
		Image[] jumpingRightImages = {new Image("res/Character Sprites/JumpRight.png")};
		Image[] jumpingLeftImages = {new Image("res/Character Sprites/JumpingLeft.png")};
		
		//initializing the animations
		walkingRight = new Animation(walkingRightImages, duration, false);
		walkingLeft = new Animation(walkingLeftImages, duration, false);
		jumpRight = new Animation(jumpingRightImages, durationSingle, false);
		jumpLeft = new Animation(jumpingLeftImages, durationSingle, false);
		
		
		
		//creating Image arrays that are flipped vertically, for when the gravity switches
		Image[] flippedWalkingRightImages = {new Image("res/Character Sprites/Sprite Standard Right.png").getFlippedCopy(false, true), 
				new Image("res/Character Sprites/WalkingRight.png").getFlippedCopy(false, true),
				new Image("res/Character Sprites/Sprite Standard Walking2 Right.png").getFlippedCopy(false, true),
				new Image("res/Character Sprites/Sprite Standard Walking3 Right.png").getFlippedCopy(false, true)};
		Image[] flippedWalkingLeftImages = {new Image("res/Character Sprites/Sprite Standard Left.png").getFlippedCopy(false, true), 
				new Image("res/Character Sprites/WalkingLeft.png").getFlippedCopy(false, true),
				new Image("res/Character Sprites/Sprite Standard Walking2 Left.png").getFlippedCopy(false, true),
				new Image("res/Character Sprites/Sprite Standard Walking3 Left.png").getFlippedCopy(false, true)};
		Image[] flippedJumpingRightImages = {new Image("res/Character Sprites/JumpRight.png").getFlippedCopy(false, true)};
		Image[] flippedJumpingLeftImages = {new Image("res/Character Sprites/JumpingLeft.png").getFlippedCopy(false, true)};
		
		
		
		//initializing the flipped animations
		flippedWalkingRight = new Animation(flippedWalkingRightImages, duration, false);
		flippedWalkingLeft = new Animation(flippedWalkingLeftImages, duration, false);
		flippedJumpRight = new Animation(flippedJumpingRightImages, durationSingle, false);
		flippedJumpLeft = new Animation(flippedJumpingLeftImages, durationSingle, false);
		
		
		
		setMovingAnimation(initialMotion);
		
		
		
		//creating health display & Text's
		healthDisplay = new Image("res/Health Display/HeartFull.png");
		healthDisplay.setFilter(Image.FILTER_NEAREST);
		youDiedText = new Image("res/Text/YouDied.png");
		outOfBoundsText = new Image("res/Text/OutOfBounds.png");
		timeIsUpText = new Image("res/Text/TimeIsUp.png");
	}
	
	
	
	//sets the entityCollection
	public void setReferenceEntityCollection(EntityCollection ec){
		entityCollection = ec;
	}
	
	
	
	//removes health if there is health to be removed, otherwise, triggers the death animation
	private int timePassedSinceLastRemoveHealth = -1;
	public void removeHealth() throws SlickException{
		if(timePassedSinceLastRemoveHealth == -1){
			timePassedSinceLastRemoveHealth = timer.getTimePassed();
		}else if(timer.getTimePassed() - timePassedSinceLastRemoveHealth <= TIME_PLAYER_IS_GRANTED_HEALTH_IMMUNITY){
			return;
		}else{
			timePassedSinceLastRemoveHealth = timer.getTimePassed();
		}
		
		
		if(health == 2){
			health--;
			healthDisplay = new Image("res/Health Display/HeartHalf.png");
		}else if(health == 1){
			health--;
			healthDisplay = new Image("res/Health Display/HeartEmpty.png");
			deathFrame = getMovingAnimation().getCurrentFrame();
			deathFrame.setFilter(Image.FILTER_NEAREST);
		}else if(health == 0){
			deathFrame = getMovingAnimation().getCurrentFrame();
			deathFrame.setFilter(Image.FILTER_NEAREST);
		}
	}
	public void removeHealth(boolean ignoreTemporaryInvincibility) throws SlickException{ //this method is overloaded.
		if(health == 2){																  //this method simply ignores temporary immunity
			health--;
			healthDisplay = new Image("res/Health Display/HeartHalf.png");
		}else if(health == 1){
			health--;
			healthDisplay = new Image("res/Health Display/HeartEmpty.png");
			deathFrame = getMovingAnimation().getCurrentFrame();
			deathFrame.setFilter(Image.FILTER_NEAREST);
		}else if(health == 0){
			deathFrame = getMovingAnimation().getCurrentFrame();
			deathFrame.setFilter(Image.FILTER_NEAREST);
		}
	}
	
	//adds to health (cannot go above 2 health, cannot add from 0)
	public void addHealth() throws SlickException{
		if(health == 1){
			health++;
			healthDisplay = new Image("res/Health Display/HeartFull.png");
		}
	}
	
	//gets health
	public int getHealth(){
		return health;
	}
	
	//draws the health to screen
	public void drawHealth() throws SlickException{
		healthDisplay.draw(Game.WINDOW_LENGTH / 20 - 35, Game.WINDOW_HEIGHT / 54, 2);
		
		if(timer.getTimeRemaining() == 0){
			while(getHealth() > 0){
				removeHealth(true);
			}
			
			timeIsUpText.draw(0, 0);
			
			deathFrame = deathFrame.getScaledCopy((float) 0.92);
			Image[] deathArray = {deathFrame};
			
			moving = new Animation(deathArray, durationSingle, false);
		}else if(Math.abs(terrain.getTerrainSet().iterator().next().getTranslatedDistanceVertical()) > Game.WINDOW_HEIGHT * 3){
			while(getHealth() > 0){
				removeHealth(true);
			}
			
			outOfBoundsText.draw(0, 0);
			
			deathFrame = deathFrame.getScaledCopy((float) 0.92);
			Image[] deathArray = {deathFrame};
			
			moving = new Animation(deathArray, durationSingle, false);
		}else if(getHealth() == 0){
			youDiedText.draw(0, 0);
			
			deathFrame = deathFrame.getScaledCopy((float) 0.92);
			Image[] deathArray = {deathFrame};
			
			moving = new Animation(deathArray, durationSingle, false);
		}
	}
	
	
	
	
	//checks to see if ability is active
	public boolean isAbilityActive(int ability){
		switch(ability){
			case ABILITY_GRAVITY_CHANGE:
				if(ability_gravityChange){
					return true;
				}else{
					return false;
				}
			default:
				System.err.println("Error in input, try using Player.ABILITY_ABILITYNAME!");
				return false;
		}
	}
	
	//sets an ability permanitely active/inactive
	public void setAbilityLiveliness(int ability, boolean active){
		switch(ability){
			case ABILITY_GRAVITY_CHANGE:
				ability_gravityChange = active;
				break;
			default:
				System.err.println("Wrong input, make sure to call setAbilityActivity(Player.ABILITYNAME, true/false");
				break;
		}
	}
	
	
	
	//changes the gravity and vertically flips player
	private int timeSinceLastGravityChange = -1;
	@Override
	public void switchGravity() throws SlickException{
		if(timeSinceLastGravityChange == -1){
			timeSinceLastGravityChange = timer.getTimePassed();
		}else if(timer.getTimePassed() - timeSinceLastGravityChange <= ABILITY_GRAVITY_CHANGE_COOLDOWN){
			return;
		}else{
			timeSinceLastGravityChange = timer.getTimePassed();
		}
		
		
		
		if(ability_gravityChange){
			gravityDownward = !gravityDownward;
			timeCounterForVerticalMovement = 0;
			
			if(this.isGravityDownward()){
				if(isFacingRight()){
					walkingRight.restart();
					moving = walkingRight;
				}else{
					walkingLeft.restart();
					moving = walkingLeft;
				}
			}else{	//gravity upward
				if(isFacingRight()){
					flippedWalkingRight.restart();
					moving = flippedWalkingRight;
				}else{
					flippedWalkingLeft.restart();
					moving = flippedWalkingLeft;
				}
			}
		}
	}
	
	
	
	//sets the 'moving' animation to an animation
	@Override
	public void setMovingAnimation(Integer motion) throws SlickException{
		
		//if null, set animation 'moving' to walkingRight, and exit this method
		if(motion == null){
			moving = walkingRight;
			facingRight = true;
			facingLeft = false;
			return;
		}
		
		switch(motion){
		case WALKING_RIGHT:
			moving = walkingRight;
			facingRight = true;
			facingLeft = false;
			break;
		case WALKING_LEFT:
			moving = walkingLeft;
			facingLeft = true;
			facingRight = false;
			break;
		case JUMPING_RIGHT:
			moving = jumpRight;
			facingRight = true;
			facingLeft = false;
			break;
		case JUMPING_LEFT:
			moving = jumpLeft;
			facingLeft = true;
			facingRight = false;
			break;
		case FLIPPED_WALKING_RIGHT:
			moving = flippedWalkingRight;
			facingRight = true;
			facingLeft = false;
			break;
		case FLIPPED_WALKING_LEFT:
			moving = flippedWalkingLeft;
			facingLeft = true;
			facingRight = false;
			break;
		case FLIPPED_JUMPING_RIGHT:
			moving = flippedJumpRight;
			facingRight = true;
			facingLeft = false;
			break;
		case FLIPPED_JUMPING_LEFT:
			moving = flippedJumpLeft;
			facingLeft = true;
			facingRight = false;
			break;
		default:	//if the switch has to resolve to the default code, an error has occurred (you probably miss-typed the "motion")
			System.err.println("getAnimation:  Had to resolve to 'walkingRight' animation, check for mistype in calling");
			moving = walkingRight;
			facingRight = true;
			facingLeft = false;
			break;
		}
	}

	
	
	/*
	 * These following incrementing/decrementing methods
	 *  contain algorithms that make up collision detection for the Player
	 */
	//controls movement of player
	@Override
	public void incrementXPos(double delta) throws SlickException{
		if(terrain == null){
			System.err.println("Never set a reference terrain to entity!");
			return;
		}
		
		
		
		Rectangle playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(playerHitBox.intersects(referenceBlockHitBox)){
				return;
			}
		}
		
		
		xPos += getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		
		
		if(xPos > Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2){
			terrain.getTerrainSet().forEach(e -> {
				e.addToXPosDirectly(-( xPos - (Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2)));
			});
			entityCollection.addToXPosDirectly(-( xPos - (Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2)));
			
			xPos = Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2;
		}
		
		
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
			boolean intersecting = false;
			while(playerHitBox.intersects(referenceBlockHitBox)){
				intersecting = true;
				
				
				
				if(getXPos() == Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2){
					terrain.getTerrainSet().forEach(e -> {
						e.addToXPosDirectly(1);
					});
					entityCollection.addToXPosDirectly(1);
				}else{
					xPos = getXPos() - 1;
				}
				
				referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			}
			
			if(intersecting){
				return;
			}
		}
		
		
		
		boolean isFalling = true;
		for(Iterator<Block> iteratedBlock = terrain.getTerrainSet().iterator(); iteratedBlock.hasNext();){
			Block referenceBlock = iteratedBlock.next();
			Rectangle block = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(isGravityDownward()){
				if(new Rectangle(getXPos(), getYPos() + 1, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(block)){
					isFalling = false;
				}
			}else{
				if(new Rectangle(getXPos(), getYPos() - 1, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(block)){
					isFalling = false;
				}
			}
		}
		if(isFalling){
			return;
		}
		
		
		
		getMovingAnimation().update((int) delta); 
	}
	
	@Override
	public void decrementXPos(double delta) throws SlickException{
		if(terrain == null){
			System.err.println("Never set a reference terrain to entity!");
			return;
		}
		
		Rectangle playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(playerHitBox.intersects(referenceBlockHitBox)){
				return;
			}
		}
		
		
		
		if(terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal() < 0){
			if(getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000 + terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal() < 0){
				terrain.getTerrainSet().forEach(e -> {
					e.addToXPosDirectly(getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000);
				});
				entityCollection.addToXPosDirectly(getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000);
			}else{
				xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000 + terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal();
				
				double horizontalTranslationOfTerrain = terrain.getTerrainSet().iterator().next().getTranslatedDistanceHorizontal();
				terrain.getTerrainSet().forEach(e -> {
					e.addToXPosDirectly( -(horizontalTranslationOfTerrain));
				});
				entityCollection.addToXPosDirectly(-(horizontalTranslationOfTerrain));
			}
		}else{
			xPos -= getSpeedPerFrame() + getSpeedPerFrame() * delta / 1000;
		}
		
	
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
			boolean intersecting = false;
			while(playerHitBox.intersects(referenceBlockHitBox)){
				intersecting = true;
				
				
				
				if(getXPos() == Game.WINDOW_LENGTH / 2 - PLAYER_WIDTH / 2){
					terrain.getTerrainSet().forEach(e -> {
						e.addToXPosDirectly(-1);
					});
					entityCollection.addToXPosDirectly(-1);
				}else{
					xPos = getXPos() + 1;
				}
				
				referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			}
			
			if(intersecting){
				return;
			}
			
			
			
		}
		
		
		
		if(xPos < 0){
			xPos = 0;
			return;
		}
		
		
		
		boolean isFalling = true;
		for(Iterator<Block> iteratedBlock = terrain.getTerrainSet().iterator(); iteratedBlock.hasNext();){
			Block referenceBlock = iteratedBlock.next();
			Rectangle block = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(isGravityDownward()){
				if(new Rectangle(getXPos(), getYPos() + 1, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(block)){
					isFalling = false;
				}
			}else{
				if(new Rectangle(getXPos(), getYPos() - 1, PLAYER_WIDTH, PLAYER_HEIGHT).intersects(block)){
					isFalling = false;
				}
			}
		}
		if(isFalling){
			return;
		}
		
		
		
		getMovingAnimation().update((int) delta); 
	}

	
	
	//controls movement of player
	@Override
	public void incrementYPos(double delta) throws SlickException {
		if(terrain == null){
			System.err.println("Never set a reference terrain to entity!");
			return;
		}
		
		
		
		if(timer.getTimePassed() == 0){
			decrementYPos(delta);
			return;
		}
		
		
		
		Rectangle playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
		
		
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(playerHitBox.intersects(referenceBlockHitBox)){
				if(jumping){
					verticalVelocity = 0;
					timeCounterForVerticalMovement = 0;
				}
				
				
				jumping = false;
				
				
				decrementYPos(delta);
				return;
			}
		}
		
		
		
		if(!isJumping()){
			if((int) Math.round(verticalVelocity) == 0 && timeCounterForVerticalMovement == 0){
				jumping = true;
				if(isGravityDownward()){
					verticalVelocity = maximumAllowedVerticalVelocity;
				}else{
					verticalVelocity = -maximumAllowedVerticalVelocity;
				}
				
			}else{
				jumping = false;
				decrementYPos(delta);
				return;
			}
		}else{
			if(isGravityDownward()){
				if(verticalVelocity <= 0){
					jumping = false;
					
					if(isFacingRight() && isGravityDownward()){
						setMovingAnimation(Player.WALKING_RIGHT);
					}else if(isFacingRight() && !isGravityDownward()){
						setMovingAnimation(Player.FLIPPED_WALKING_RIGHT);
					}else if(isFacingLeft() && isGravityDownward()){
						setMovingAnimation(Player.WALKING_LEFT);
					}else if(isFacingLeft() && !isGravityDownward()){
						setMovingAnimation(Player.FLIPPED_WALKING_LEFT);
					}
					
					getMovingAnimation().restart();
					
					decrementYPos(delta);
					return;
				}
			}else{
				if(verticalVelocity >= 0){
					jumping = false;
					
					if(isFacingRight() && isGravityDownward()){
						setMovingAnimation(Player.WALKING_RIGHT);
					}else if(isFacingRight() && !isGravityDownward()){
						setMovingAnimation(Player.FLIPPED_WALKING_RIGHT);
					}else if(isFacingLeft() && isGravityDownward()){
						setMovingAnimation(Player.WALKING_LEFT);
					}else if(isFacingLeft() && !isGravityDownward()){
						setMovingAnimation(Player.FLIPPED_WALKING_LEFT);
					}
					
					getMovingAnimation().restart();
					
					decrementYPos(delta);
					return;
				}
			} 
		} 
		
		
		
		
		
		
		if(isGravityDownward()){
			if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == -(maximumAllowedVerticalVelocity)){
				timeCounterForVerticalMovement = Game.FPS;
			}else if(timeCounterForVerticalMovement >= Game.FPS){
				verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(-verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = -maximumAllowedVerticalVelocity;
				}
			}else{
				timeCounterForVerticalMovement++;
				verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(-verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = -(maximumAllowedVerticalVelocity);
				}
			}
			
			
			
			terrain.getTerrainSet().forEach(e -> {
				e.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			});
			entityCollection.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
		
	
		
			for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
				Block referenceBlock = iterator.next();
				Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
				boolean intersecting = false;
				while(playerHitBox.intersects(referenceBlockHitBox)){
					intersecting = true;
					timeCounterForVerticalMovement = 0;
					
				
					
					if(verticalVelocity <= 0){
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(1);
						});
						entityCollection.addToYPosDirectly(1);
					}else{
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(-1);
						});
						entityCollection.addToYPosDirectly(-1);
					}
					
					
				
					referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
				}
			
				if(intersecting){
					verticalVelocity = 0;
					return;
				}
				
			}
			
			
			
		}else{ //else gravity is upward
			
			if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == maximumAllowedVerticalVelocity){
				timeCounterForVerticalMovement = Game.FPS;
			}else if(timeCounterForVerticalMovement >= Game.FPS){
				verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS 
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = maximumAllowedVerticalVelocity;
				}	
			}else{
				timeCounterForVerticalMovement++;
				verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = maximumAllowedVerticalVelocity;
				}
			}
			
			
			
			terrain.getTerrainSet().forEach(e -> {
				e.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			});
			entityCollection.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
		
	
		
			for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
				Block referenceBlock = iterator.next();
				Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
				boolean intersecting = false;
				while(playerHitBox.intersects(referenceBlockHitBox)){
					intersecting = true;
					timeCounterForVerticalMovement = 0;
					
				
					
					if(verticalVelocity >= 0){
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(-1);
						});
						entityCollection.addToYPosDirectly(-1);
					}else{
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(1);
						});
						entityCollection.addToYPosDirectly(1);
					}
					
					
				
					referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
				}
			
				if(intersecting){
					verticalVelocity = 0;
					return;
				}
				

			}
		}
		
		if(isFacingRight() && isGravityDownward()){
			setMovingAnimation(Player.JUMPING_RIGHT);
		}else if(isFacingRight() && !isGravityDownward()){
			setMovingAnimation(Player.FLIPPED_JUMPING_RIGHT);
		}else if(isFacingLeft() && isGravityDownward()){
			setMovingAnimation(Player.JUMPING_LEFT);
		}else if(isFacingLeft() && !isGravityDownward()){
			setMovingAnimation(Player.FLIPPED_JUMPING_LEFT);
		}
		
	}
	
	@Override
	public void decrementYPos(double delta) throws SlickException{
		
		if(terrain == null){
			System.err.print("Never set a reference terrain to entity!");
			return;
		}
		

		
		Rectangle playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
		
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			Block referenceBlock = iterator.next();
			Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
			
			if(playerHitBox.intersects(referenceBlockHitBox)){
				if(isGravityDownward()){
					if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == -(maximumAllowedVerticalVelocity)){
						timeCounterForVerticalMovement = Game.FPS;
					}else if(timeCounterForVerticalMovement >= Game.FPS){
						verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
								+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
						
						if(-verticalVelocity > maximumAllowedVerticalVelocity){
							verticalVelocity = -maximumAllowedVerticalVelocity;
						}
					}else{
						timeCounterForVerticalMovement++;
						verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
								+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
						
						if(-verticalVelocity > maximumAllowedVerticalVelocity){
							verticalVelocity = -(maximumAllowedVerticalVelocity);
						}
					}
					
					
					
				}else{
					if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == maximumAllowedVerticalVelocity){
						timeCounterForVerticalMovement = Game.FPS;
					}else if(timeCounterForVerticalMovement >= Game.FPS){
						verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS 
								+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
						
						if(verticalVelocity > maximumAllowedVerticalVelocity){
							verticalVelocity = maximumAllowedVerticalVelocity;
						}	
					}else{
						timeCounterForVerticalMovement++;
						verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
								+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
						
						if(verticalVelocity > maximumAllowedVerticalVelocity){
							verticalVelocity = maximumAllowedVerticalVelocity;
						}
					}
					
					

				}
				
				
				
				if(verticalVelocity >= 0){	
					
					int spaceCounter = -1;
					for(int counter = 0; counter <= verticalVelocity + verticalVelocity * delta / 1000; counter++){
						boolean intersecting = false;
						
						for(Iterator<Block> spaceIterator = terrain.getTerrainSet().iterator(); spaceIterator.hasNext();){
							Block spaceReferenceBlock = spaceIterator.next();
							Rectangle spaceReferenceBlockHitBox = new Rectangle(spaceReferenceBlock.getXPos() + counter, spaceReferenceBlock.getYPos() + counter, Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
							
							if(playerHitBox.intersects(spaceReferenceBlockHitBox)){
								intersecting = true;
							}
						}
						
						if(!intersecting && counter + 1 <= verticalVelocity + verticalVelocity * delta / 1000){
							spaceCounter = counter;
						}else if(counter + 1 > verticalVelocity + verticalVelocity * delta / 1000 && spaceCounter != -1){
							for(Iterator<Block> tempIterator = terrain.getTerrainSet().iterator(); tempIterator.hasNext();){
								tempIterator.next().addToYPosDirectly(spaceCounter);
								
							}
							entityCollection.addToYPosDirectly(spaceCounter);
							
							timeCounterForVerticalMovement = 0;
							
							return;
						}
					}
				}else{
					
					int spaceCounter = 1;
					for(int counter = 0; counter >= verticalVelocity + verticalVelocity * delta / 1000; counter--){
						boolean intersecting = false;
						
						for(Iterator<Block> spaceIterator = terrain.getTerrainSet().iterator(); spaceIterator.hasNext();){
							Block spaceReferenceBlock = spaceIterator.next();
							Rectangle spaceReferenceBlockHitBox = new Rectangle(spaceReferenceBlock.getXPos() + counter, spaceReferenceBlock.getYPos() + counter, Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
							
							if(playerHitBox.intersects(spaceReferenceBlockHitBox)){
								intersecting = true;
							}
						}
						
						if(!intersecting && counter - 1 >= verticalVelocity + verticalVelocity * delta / 1000){
							spaceCounter = counter;
						}else if(counter - 1 < verticalVelocity + verticalVelocity * delta / 1000 && spaceCounter != 1){
							for(Iterator<Block> tempIterator = terrain.getTerrainSet().iterator(); tempIterator.hasNext();){
								tempIterator.next().addToYPosDirectly(spaceCounter);
								
							}
							entityCollection.addToYPosDirectly(spaceCounter);
							
							timeCounterForVerticalMovement = 0;
							
							return;
						}
					}
				}
				
				
					
				terrain.getTerrainSet().forEach(e -> {
					e.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
				});
				entityCollection.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
				
				
				
				return;
			}
		}
		
		
		

		
		
		
		if(isGravityDownward()){
			if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == -(maximumAllowedVerticalVelocity)){
				timeCounterForVerticalMovement = Game.FPS;
			}else if(timeCounterForVerticalMovement >= Game.FPS){
				verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(-verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = -maximumAllowedVerticalVelocity;
				}
			}else{
				timeCounterForVerticalMovement++;
				verticalVelocity -= ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(-verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = -(maximumAllowedVerticalVelocity);
				}
			}
			
			
			
			Block blockToCompareTo = terrain.getTerrainSet().iterator().next();
			double yPosBefore = blockToCompareTo.getYPos();
			
			terrain.getTerrainSet().forEach(e -> {
				e.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			});
			entityCollection.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			
			double yPosAfter = blockToCompareTo.getYPos();
			
			if(yPosBefore == yPosAfter){
				for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
					int incrementTester = 0;
					
					if(verticalVelocity >= 0){
						incrementTester = 1;
					}else{
						incrementTester = -1;
					}
					
					Block referenceBlock = iterator.next();
					Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos() + incrementTester, Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
					
					boolean intersecting = false;
					while(playerHitBox.intersects(referenceBlockHitBox)){
						intersecting = true;
						timeCounterForVerticalMovement = 0;
					
						referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
						playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
						
						break;
					}
				
					if(intersecting){
						verticalVelocity = 0;
						return;
					}
					

				}
			}
		
	
		
			for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
				Block referenceBlock = iterator.next();
				Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
				boolean intersecting = false;
				while(playerHitBox.intersects(referenceBlockHitBox)){
					intersecting = true;
					timeCounterForVerticalMovement = 0;
					
				
					
					if(verticalVelocity <= 0){
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(1);
						});
						entityCollection.addToYPosDirectly(1);
					}else{
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(-1);
						});
						entityCollection.addToYPosDirectly(-1);
					}
					
					
				
					referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
				}
			
				if(intersecting){
					verticalVelocity = 0;
					return;
				}
				
			}
			
			
			
		}else{ //else gravity is upward
			
			if(timeCounterForVerticalMovement >= Game.FPS && verticalVelocity == maximumAllowedVerticalVelocity){
				timeCounterForVerticalMovement = Game.FPS;
			}else if(timeCounterForVerticalMovement >= Game.FPS){
				verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS 
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = maximumAllowedVerticalVelocity;
				}	
			}else{
				timeCounterForVerticalMovement++;
				verticalVelocity += ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS
						+ ACCELERATION_DUE_TO_GRAVITY * (double) timeCounterForVerticalMovement / (double) Game.FPS * delta / 1000;
				
				if(verticalVelocity > maximumAllowedVerticalVelocity){
					verticalVelocity = maximumAllowedVerticalVelocity;
				}
			}
			
			
			
			Block blockToCompareTo = terrain.getTerrainSet().iterator().next();
			double yPosBefore = blockToCompareTo.getYPos();
			
			terrain.getTerrainSet().forEach(e -> {
				e.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			});
			entityCollection.addToYPosDirectly(verticalVelocity + verticalVelocity * delta / 1000);
			
			double yPosAfter = blockToCompareTo.getYPos();
			
			if(yPosBefore == yPosAfter){
				for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
					int incrementTester = 0;
					
					if(verticalVelocity >= 0){
						incrementTester = 1;
					}else{
						incrementTester = -1;
					}
					
					Block referenceBlock = iterator.next();
					Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos() + incrementTester, Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
					
					boolean intersecting = false;
					while(playerHitBox.intersects(referenceBlockHitBox)){
						intersecting = true;
						timeCounterForVerticalMovement = 0;
					
						referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
						playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
						
						break;
					}
				
					if(intersecting){
						verticalVelocity = 0;
						return;
					}
					
				}
			}
		
	
		
			for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
				Block referenceBlock = iterator.next();
				Rectangle referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
				playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
				boolean intersecting = false;
				while(playerHitBox.intersects(referenceBlockHitBox)){
					intersecting = true;
					timeCounterForVerticalMovement = 0;
					
				
					
					if(verticalVelocity >= 0){
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(-1);
						});
						entityCollection.addToYPosDirectly(-1);
					}else{
						terrain.getTerrainSet().forEach(e -> {
							e.addToYPosDirectly(1);
						});
						entityCollection.addToYPosDirectly(1);
					}
					
					
				
					referenceBlockHitBox = new Rectangle(referenceBlock.getXPos(), referenceBlock.getYPos(), Block.BLOCK_SIDE_LENGTH, Block.BLOCK_SIDE_LENGTH);
					playerHitBox = new Rectangle(getXPos(), getYPos(), PLAYER_WIDTH, PLAYER_HEIGHT);
				}
			
				if(intersecting){
					verticalVelocity = 0;
					return;
				}
				
			}
		}
		
	}
	
	
}
