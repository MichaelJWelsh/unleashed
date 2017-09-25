package unleashed.entities;


import java.util.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import unleashed.Game;
import unleashed.terrain.Block;
import unleashed.terrain.Terrain;
import unleashed.util.*;


@Info(
		dateLastEdited = "12/4/2015",
		purpose = "This class serves to bring all entites (but the player) together to easily be manipulated.",
		otherInfo = "This class itself is NOT an entity, but merely a way to organize entities!"
		)


public class EntityCollection {
	/* ENTITY SETS */
	private Set<Coin> coins = new HashSet<Coin>();
	private Set<Heart> hearts = new HashSet<Heart>();
	private Set<JuggernautStationary> juggernauts = new HashSet<JuggernautStationary>();
	private Set<BlackHole> blackHoles = new HashSet<BlackHole>();
	private Set<Spike> spikes = new HashSet<Spike>();
	private Set<Fireball> fireballs = new HashSet<Fireball>();
	
	/* REFERENCE VARIABLES/OBJECTS */
	int level;
	double speed;
	Time timer;
	Player player;
	Terrain terrain;
	
	
	
	//constructor
	public EntityCollection(int level, Time timer, Player player, Terrain terrain) throws SlickException{
		this.level = level;
		this.timer = timer;
		this.player = player;
		this.terrain = terrain;
		this.speed = player.getSpeedPerFrame();
		
		
		//making coins/hearts/juggernauts
		for(Iterator<Block> iterator = terrain.getTerrainSet().iterator(); iterator.hasNext();){
			double random = Math.random();
			double random2 = Math.random();
			Block referenceBlock = iterator.next();
			
			if(random < 0.03 && random > 0.02){
				if(random2 <= 0.5){
					makeHeart(referenceBlock.getXPos(), referenceBlock.getYPos() + Block.BLOCK_SIDE_LENGTH + 5);
				}else{
					makeHeart(referenceBlock.getXPos(), referenceBlock.getYPos() - (Block.BLOCK_SIDE_LENGTH + 5));
				}
			}
			
			if(random < 0.07 && random > 0.03){
				if(random2 < 0.5){
					makeCoin(referenceBlock.getXPos(), referenceBlock.getYPos() + Block.BLOCK_SIDE_LENGTH + 5);
				}else{
					makeCoin(referenceBlock.getXPos(), referenceBlock.getYPos() - (Block.BLOCK_SIDE_LENGTH + 5));
				}
			}
			
			if(random < 0.11 && random > 0.07){
				if(random2 < 0.5){
					makeJuggernaut(referenceBlock.getXPos(), referenceBlock.getYPos() + Block.BLOCK_SIDE_LENGTH + 5);
				}else{
					makeJuggernaut(referenceBlock.getXPos(), referenceBlock.getYPos() - (Block.BLOCK_SIDE_LENGTH + 5));
				}
			}
			
			
		}
	}
	
	
	
	//makes a coin/heart/juggernaut
	public void makeCoin(int initialXPos, int initialYPos) throws SlickException{
		coins.add(new Coin(level, timer, initialXPos, initialYPos, speed, player, Game.score));
	}
	
	public void makeHeart(int initialXPos, int initialYPos) throws SlickException{
		hearts.add(new Heart(level, timer, initialXPos, initialYPos, speed, player));
	}
	
	public void makeJuggernaut(int initialXPos, int initialYPos) throws SlickException{
		juggernauts.add(new JuggernautStationary(level, timer, initialXPos, initialYPos, speed, player));
	}
	
	
	
	//draws the entities to the screen
	public void drawEntities(Graphics g){
		juggernauts.forEach(e -> {
			e.drawJuggernautStationary(g);
		});
		
		coins.forEach(e -> {
			e.drawCoin(g);
		});
		
		hearts.forEach(e -> {
			e.drawHeart(g);
		});
		
		blackHoles.forEach(e -> {
			e.drawBlackHole(g);
		});
		
		fireballs.forEach(e -> {
			e.drawFireball(g);
		});
		
		spikes.forEach(e -> {
			e.drawSpike(g);
		});
		
	}
	
	
	
	//maintains the entities movement
	private int timeSinceLastSpawnBlackHole = 0;
	private int timeSinceLastSpawnFireball = 0;
	private int timeSinceLastSpawnSpike = 0;
	public void maintainEntities(int delta) throws SlickException{
		
		/* REMOVING DEAD ENTITIES */
		Set<BlackHole> blackHolesToRemove = new HashSet<BlackHole>();
		blackHoles.forEach(e -> {
			if(e.isRemove()){
				blackHolesToRemove.add(e);
			}
		});
		blackHolesToRemove.forEach(e -> {
			blackHoles.remove(e);
		});
		
		Set<Spike> spikesToRemove = new HashSet<Spike>();
		spikes.forEach(e -> {
			if(e.isRemove()){
				spikesToRemove.add(e);
			}
		});
		spikesToRemove.forEach(e -> {
			spikes.remove(e);
		});
		
		Set<Fireball> fireballsToRemove = new HashSet<Fireball>();
		fireballs.forEach(e -> {
			if(e.isRemove()){
				fireballsToRemove.add(e);
			}
		});
		fireballsToRemove.forEach(e -> {
			fireballs.remove(e);
		});
		
		Set<Coin> coinsToRemove = new HashSet<Coin>();
		coins.forEach(e -> {
			if(e.isRemove()){
				coinsToRemove.add(e);
			}
		});
		coinsToRemove.forEach(e -> {
			coins.remove(e);
		});
		
		Set<Heart> heartsToRemove = new HashSet<Heart>();
		hearts.forEach(e -> {
			if(e.isRemove()){
				heartsToRemove.add(e);
			}
		});
		heartsToRemove.forEach(e -> {
			hearts.remove(e);
		});
		
		
		
		/* LETTING ENTITIES OPERATE */
		blackHoles.forEach(e -> {
			try {
				e.decrementXPosWithEffect(delta);
			} catch (SlickException v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (SlickException v) {
				v.printStackTrace();
			}
		});
		
		spikes.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		
		
		/* UPDATING ANIMATIONS */
		coins.forEach(e -> {
			e.updateAnimation(delta);
		});
		
		blackHoles.forEach(e -> {
			e.updateAnimation(delta);
		});
		
		
		
		/* SPAWNING IN NEW ENTITIES */
		if(timer.getTimePassed() > 5){
			if(timer.getTimePassed() % 3 == 0){
				if(timer.getTimePassed() - timeSinceLastSpawnFireball > 0){
					fireballs.add(new Fireball(level, timer, speed, player, terrain));
					timeSinceLastSpawnFireball = timer.getTimePassed();
				}
			}
			
			if(timer.getTimePassed() % 2 == 0){
				if(timer.getTimePassed() - timeSinceLastSpawnBlackHole > 0){
					blackHoles.add(new BlackHole(level, timer, speed, terrain));
					timeSinceLastSpawnBlackHole = timer.getTimePassed();
				}
				
			}
			
			if(timer.getTimePassed() % 3 == 0){
				if(timer.getTimePassed() - timeSinceLastSpawnSpike > 0){
					spikes.add(new Spike(level, timer, speed, player));
					timeSinceLastSpawnSpike = timer.getTimePassed();
				}
			}
		}
		
		
	}
	
	
	
	//coordinate manipulation
	public void incrementXPos(double delta) throws SlickException{
		spikes.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.incrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	public void decrementXPos(double delta) throws SlickException{
		spikes.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.decrementXPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	public void incrementYPos(double delta) throws SlickException{
		spikes.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.incrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	public void decrementYPos(double delta) throws SlickException{
		spikes.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.decrementYPos(delta);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	public void addToXPosDirectly(double increment){
		spikes.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.addToXPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	public void addToYPosDirectly(double increment){
		spikes.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		hearts.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		coins.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		juggernauts.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		fireballs.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
		
		blackHoles.forEach(e -> {
			try {
				e.addToYPosDirectly(increment);
			} catch (Exception v) {
				v.printStackTrace();
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
