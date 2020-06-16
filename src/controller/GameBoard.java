package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import model.Planet;
import model.Player;
import model.SpaceObject;
import model.Vector;

public class GameBoard{

	// the player object with player car object
	private Player player;

	private Input input;
	//sollte static sein um vom Player darauf zuzugreifen, 
	//lässt sich für den Rest auch so argumentieren --> alles static?
	
	private final int difficulty;

	private int width, height;
	private boolean isRunning;
	private boolean gameEnded;

	public List<SpaceObject> spaceObjects = new ArrayList<>();
	public static List<SpaceObject> deadSpaceObjects = new ArrayList<>();

	private static int maxSpaceObject;

	/**
	 * Constructor, creates the gameboard based on size
	 * 
	 * @param size of the gameboard
	 * @param input checker
	 * @param difficulty from 0(EASY) to 2(HARD)
	 */
	public GameBoard(int width, int height, Input input, int difficulty){
		gameEnded = false;
		this.player = new Player();
		this.width = width;
		this.height = height;
		this.input = input;
		this.addSpaceObjects();
		this.difficulty = difficulty;
	}

	/**
	 * Removes all existing spaceObjects from the spaceObjects list. <br>
	 * Adds specified number of spaceObjects to the spaceObjects list. The number of spaceObjects depends on the chosen map
	 * and therefore difficulty.
	 */
	public void addSpaceObjects(){
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
		return this.isRunning;
	}

	/**
	 * Used for testing only
	 */
	public void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}

	/**
	 * 
	 * @return null if the game is running; true if the player has won; false if the player
	 *         has lost
	 */
	public Boolean hasEnded(){
		return this.gameEnded;
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

		// maximum x and y values a car can have depending on the size of the game board
		int maxX = width;
		int maxY = height;

		// update the positions of the player car and the autonomous cars
		for (SpaceObject so: spaceObjects){
			so.move(maxX, maxY);
		}

		player.move(maxX, maxY);

		// iterate through all cars (except player car) and check if it is crunched
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

					collision.collide();

					// : The player gets notified when he looses or wins the game
					// Hint: you should set the attribute gameWon accordingly, use 'isWinner()' below for your
					// implementation
					if (!player.isAlive())
						gameEnded = true;

				}
			}
		}

		for (SpaceObject spaceObject: deadSpaceObjects){
			spaceObjects.remove(spaceObject);
		}

		input.updateLoop();
	}
	
	/**
	 * Sets the default number of maximum spaceObjects on the GameBoard.
	 * @return the current Map
	 */
	public Maps chooseMap(){
		switch (difficulty) {
		case 0: {
			maxSpaceObject = Maps.EASY.getMaxSpaceObjects();
			return Maps.EASY;
		}
		case 1: {
			maxSpaceObject = Maps.MEDIUM.getMaxSpaceObjects();
			return Maps.MEDIUM;
		}
		case 2: {
			maxSpaceObject = Maps.HARD.getMaxSpaceObjects();
			return Maps.HARD;
		}
		default:
			maxSpaceObject = Maps.EASY.getMaxSpaceObjects();
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
