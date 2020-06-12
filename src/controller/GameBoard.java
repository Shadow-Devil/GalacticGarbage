package controller;

import java.util.List;
import java.util.ArrayList;

import model.Planet;
import model.Player;
import model.SpaceObject;
import model.Vector;

public class GameBoard {
	
	// the player object with player car object
	private Player player;
	private Input input;
	
	private int width, height;
	private boolean isRunning;
	private Boolean gameWon;
	
	private static int maxSpaceObject;
	public List<SpaceObject> spaceObjects = new ArrayList<>();
	public static List<SpaceObject> tmpspaceObjects = new ArrayList<>();
	
	//constants
	public static int NUMBER_OF_PLANETS = 5;
	public static int NUMBER_OF_MOONS = 2;
	
	
	
	/**
	 * Constructor, creates the gameboard based on size 
	 * @param size of the gameboard
	 */
	public GameBoard(int width, int height, Input input) {
		this.player = new Player(new Vector(250, 30), new Vector(1, 0));
		this.width = width;
		this.height = height;
		this.input = input;
		this.addSpaceObjects();
	}
	
	/**
	 * Adds specified number of cars to the cars list, creates new object for each car
	 */
	public void addSpaceObjects() {
		for (int i = 0; i < NUMBER_OF_PLANETS; i++) {
			spaceObjects.add(new Planet(width(), height()));
		}
        for (int i = 0; i < NUMBER_OF_TESLA_CARS; i++) {
        	spaceObjects.add(new FastCar(width(), height()));
        }
        
        spaceObjects.add(new CrazyCar(width(), height()));
		
	}
	
	/**
	 * Removes all existing cars from the car list, resets the position of the
	 * player car Invokes the creation of new car objects by calling addCars()
	 */
	public void resetSpaceObjects() {
		this.player = new Player(new Vector(250, 30), new Vector(1, 0));
		spaceObjects.clear();
		addSpaceObjects();
	}
	
	/**
	 * Checks if game is currently running by checking if the thread is running
	 * @return boolean isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Used for testing only
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	/**
	 * 
	 * @return null if the game is running; true if the player has won; false if the player has lost
	 */
	public Boolean hasWon() {
		return this.gameWon;
	}	
	
	/**
	 * @return list of cars
	 */
	public List<SpaceObject> getspaceObjects() {
		return spaceObjects;
	}
	
	/**
	 * Starts the game. Cars start to move and background music starts to play.
	 */
	public void startGame() {
		this.isRunning = true;
	}
	
	/**
	 * Stops the game. Cars stop moving and background music stops playing.
	 */
	public void stopGame() {
		this.isRunning = false;
	}
	
	public void updateSpaceObjects() {
		

		List<SpaceObject> spaceObjects = getspaceObjects();

		// maximum x and y values a car can have depending on the size of the game board
		int maxX = width;
		int maxY = height;

		// update the positions of the player car and the autonomous cars
		for (SpaceObject so : spaceObjects) {
			so.updatePosition(maxX, maxY);
		}

		player.updatePosition(maxX, maxY);

		// iterate through all cars (except player car) and check if it is crunched
		for (SpaceObject so : spaceObjects) {
			if (so.isCrunched()) {
				continue; // because there is no need to check for a collision
			}
			
			// : Add a new collision type! 
			// Hint: Make sure to create a subclass of the class Collision 
			// and store it in the new Collision package.
			// Create a new collision object 
			// and check if the collision between player car and autonomous car evaluates as expected

			Collision collision = new Collision(player, so);

			if (collision.detectCollision()) {
			
				
				collision.collide();
				
				if(winner != null) {
					// : The loser car is crunched and stops driving
					Car loser = collision.evaluateLoser();
					loserCars.add(loser);
					loser.setCrunched();
				}
				
				// : The player gets notified when he looses or wins the game
				// Hint: you should set the attribute gameWon accordingly, use 'isWinner()' below for your implementation
				if(isWinner())
					gameWon = true;
				if(player.isAlive())
					gameWon = false;
				
			}
		}
		
		input.updateLoop();
	}
	
	
	public void chooseMap() {
		
	}
	
//	/**
//	 * @return list of loser cars
//	 */
//	public List<Car> getLoserCars() {
//		return this.loserCars;
//	}
	
//	/**
//	 * If all cars are crunched, the player wins.
//	 * 
//	 * @return true if game is won
//	 * @return false if game is not (yet) won
//	 */
//	private boolean isWinner() {
//		for (SpaceObject so : spaceObjects) {
//			if (!so.isCrunched()) {
//				return false;
//			}
//		}
//		return true;
//	}
	
}
