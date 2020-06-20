package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import model.Debris;
import model.Planet;
import model.Player;
import model.SpaceObject;
import model.Vector;
import view.GameBoardUI;

public class GameBoard{

	// the player object with player car object
	private Player player;

	//public static Input input;
	//sollte static sein um vom Player darauf zuzugreifen, 
	//lässt sich für den Rest auch so argumentieren --> alles static?
	
	private final int difficulty;

	private int width, height;
	private boolean isRunning;
	private boolean gameEnded;
	private int updateCounter;

	public static List<SpaceObject> spaceObjects = new ArrayList<>();
	public static List<SpaceObject> eventSpaceObjects = new ArrayList<>();
	public List<Debris> spawn = new ArrayList<>();
	private int spawncounter;

	private static int maxDebris;
	public static int debrisCount;

	/**
	 * Constructor, creates the gameboard based on size
	 * 
	 * @param width of the gameboard
	 * @param height of the gameboard
	 * @param difficulty from 0(EASY) to 2(HARD)
	 */
	public GameBoard(int width, int height, int difficulty) { 
		gameEnded = false;
		player = new Player();
		this.width = width;
		this.height = height;
		spawncounter = 0;
		addSpaceObjects();
		this.difficulty = difficulty;
		updateCounter = 0;
	}

	/**
	 * Removes all existing spaceObjects from the spaceObjects list. <br>
	 * Adds specified number of spaceObjects to the spaceObjects list. The number of spaceObjects depends on the chosen map
	 * and therefore difficulty.
	 */
	public void addSpaceObjects(){
		spawn = chooseMap().getBaseDebris();
		spaceObjects = chooseMap().getObjects();
		spaceObjects.add(player); 
	}  

	/**
	 * Resets the position of the player. <br>
	 * Invokes the creation of new spaceObjects.
	 */
	public void resetSpaceObjects(){
		this.player = new Player();
//		spaceObjects.clear();	//eig nicht nötig
		addSpaceObjects();
	}

	/**
	 * Checks if game is currently running by checking if the thread is running
	 * 
	 * @return boolean isRunning
	 */
	public boolean isRunning(){
		return isRunning;
	}

	/**
	 * Used for testing only
	 */
	private void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}

	/**
	 * 
	 * @return null if the game is running; true if the player has won; false if the player
	 *         has lost
	 */
	public boolean hasEnded(){
		return gameEnded;
	}

	/**
	 * @return list of spaceObjects
	 */
	public List<SpaceObject> getspaceObjects(){
		return spaceObjects;
	}

	/**
	 * Starts the game. SpaceObjects start to move.
	 */
	public void startGame(){
		this.isRunning = true;
	}

	/**
	 * Stops the game. SpaceObjects stop moving.
	 */
	public void stopGame(){
		this.isRunning = false;
	}

	public void updateSpaceObjects(){
		List<SpaceObject> spaceObjects = getspaceObjects();

		// maximum x and y values a spaceObjects can have depending on the size of the game board
		int maxX = width;
		int maxY = height;

		// update the positions of the player and the other spaceObjects
		for (SpaceObject so: spaceObjects){
			so.move(maxX, maxY);
		}

		player.move(maxX, maxY);

		// iterate through all spaceObjects (except player) and check if it is crunched
		for (SpaceObject so1: spaceObjects){
			if (!so1.isAlive())
				continue; // because there is no need to check for a collision
			
			for (SpaceObject so2: spaceObjects){
				
				//TODO equals überschreiben
				if(so1.equals(so2))
					continue;
				
				// : Add a new collision type!
				// Hint: Make sure to create a subclass of the class Collision
				// and store it in the new Collision package.
				// Create a new collision object
				// and check if the collision between player car and autonomous car evaluates as expected

				Collision collision = new Collision(so1, so2);

				if (collision.detectCollision()){
					//System.out.println("collision:" + collision);
					collision.collide();

					// : The player gets notified when he looses or wins the game
					// Hint: you should set the attribute gameWon accordingly, use 'isWinner()' below for your
					// implementation
					if (!player.isAlive())
						gameEnded = true;

				}
			}
		}

		for (SpaceObject spaceObject: eventSpaceObjects){ 
			if (spaceObject.isAlive()){
				
				GameBoardUI.addNew(spaceObject);
			} else {
				if (spaceObject instanceof Debris && ((Debris) spaceObject).getSize() > 0){
					((Debris) spaceObject).split();
				}
		
			}
			spaceObjects.remove(spaceObject);
		}

		if(updateCounter >= 15000) {	//over time increase
			maxDebris += 4;
			updateCounter %= 10;
		}
		if(debrisCount+4 <= maxDebris) {	//neuer Spawn possible
			System.out.println("new Spawn");
			Debris debris = spawn.get(spawncounter++ % spawn.size()).getCopy();
			GameBoard.spaceObjects.add(debris);
			GameBoardUI.addNew(debris);
			
		}

		Input.updateLoop();
		updateCounter++;
	}
	
	/**
	 * Sets the default number of maximum spaceObjects on the GameBoard.
	 * @return the current Map
	 */
	public Maps chooseMap(){
		switch (difficulty) {
		case 0: {
			maxDebris = Maps.EASY.getMaxDebris();
			return Maps.EASY;
		}
		case 1: {
			maxDebris = Maps.MEDIUM.getMaxDebris();
			return Maps.MEDIUM;
		}
		case 2: {
			maxDebris = Maps.HARD.getMaxDebris();
			return Maps.HARD;
		}
		default:
			maxDebris = Maps.EASY.getMaxDebris();
			return Maps.EASY;
		}
	}

	public Player getPlayer(){
		return player;
	}

	// /**
	// * @return list of loser cars
	// */
	// public List<Car> getLoserCars() {
	// return this.loserCars;
	// }

	// /**
	// * If all cars are crunched, the player wins.
	// *
	// * @return true if game is won
	// * @return false if game is not (yet) won
	// */
	// private boolean isWinner() {
	// for (SpaceObject so : spaceObjects) {
	// if (!so.isCrunched()) {
	// return false;
	// }
	// }
	// return true;
	// }

}
