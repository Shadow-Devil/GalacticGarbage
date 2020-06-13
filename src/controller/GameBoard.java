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
	 */
	public GameBoard(int width, int height, Input input){
		gameEnded = false;
		this.player = new Player();
		this.width = width;
		this.height = height;
		this.input = input;
		this.addSpaceObjects();
	}

	/**
	 * Adds specified number of cars to the cars list, creates new object for each car
	 */
	public void addSpaceObjects(){
		spaceObjects = chooseMap().getObjects();
		spaceObjects.add(player);
	}  

	/**
	 * Removes all existing cars from the car list, resets the position of the player car
	 * Invokes the creation of new car objects by calling addCars()
	 */
	public void resetSpaceObjects(){
		this.player = new Player();
		spaceObjects.clear();
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
	 * @return list of cars
	 */
	public List<SpaceObject> getspaceObjects(){
		return spaceObjects;
	}

	/**
	 * Starts the game. Cars start to move and background music starts to play.
	 */
	public void startGame(){
		this.isRunning = true;
	}

	/**
	 * Stops the game. Cars stop moving and background music stops playing.
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

	public Maps chooseMap(){

		return Maps.EASY;
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
