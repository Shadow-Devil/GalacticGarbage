package controller;

import java.util.List;
import java.util.stream.Collectors;

import controller.collision.CollisionInterface;
import controller.collision.Policy;

import java.util.ArrayList;

import model.Debris;
import model.Planet;
import model.Player;
import model.Player2;
import model.SpaceObject;
import model.Vector;

public class GameBoard {

	private Player player;
	private Player player2;

	private final int difficulty;

	private static int width, height;
	private boolean isRunning;
	private boolean gameEnded;
	private int updateCounter;

	private static List<SpaceObject> spaceObjects = new ArrayList<>();
	private static List<SpaceObject> eventSpaceObjects = new ArrayList<>();
	private List<Debris> spawn = new ArrayList<>();
	private int spawncounter;

	private int score;

	private static int maxDebris;
	private static int debrisCount;

	private CollisionInterface collision;

	private static List<Planet> planetList;

	private boolean multiplayer;

	/**
	 * Constructor, creates the gameboard based on size
	 * 
	 * @param w          of the gameboard
	 * @param h          of the gameboard
	 * @param difficulty from 0(EASY) to 2(HARD)
	 */
	public GameBoard(int w, int h, int difficulty, CollisionInterface collision, boolean multiplayer) {
		this.multiplayer = multiplayer;
		gameEnded = false;
		width = w;
		height = h;
		spawncounter = 0;
		addSpaceObjects();
		this.difficulty = difficulty;
		updateCounter = 0;
		score = 0;
		this.collision = collision;
	}

	public GameBoard() {
		this.difficulty = 0;
	}

	/**
	 * Removes all existing spaceObjects from the spaceObjects list. <br>
	 * Adds specified number of spaceObjects to the spaceObjects list. The number of
	 * spaceObjects depends on the chosen map and therefore difficulty.
	 */
	public void addSpaceObjects() {
		Maps.setMultiplayer(multiplayer);
		Maps map = Maps.chooseMap(difficulty);
		maxDebris = map.getMaxDebris();
		spawn = map.getBaseDebris();
		spaceObjects = map.getObjects();
		planetList = spaceObjects.stream().filter(so -> so instanceof Planet).map(so -> (Planet) so)
				.collect(Collectors.toList());
		player = spaceObjects.stream().filter(so -> so instanceof Player && !(so instanceof Player2))
				.map(so -> (Player) so).findFirst().get();
		if (multiplayer) {
			player2 = spaceObjects.stream().filter(so -> so instanceof Player2).map(so -> (Player) so).findFirst()
					.get();
		}
	}

	/**
	 * Resets the position of the player. <br>
	 * Invokes the creation of new spaceObjects.
	 */
	public void resetSpaceObjects() {
		debrisCount = 0;
		addSpaceObjects();
	}

	/**
	 * Checks if game is currently running by checking if the thread is running
	 * 
	 * @return boolean isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @return null if the game is running; true if the player has won; false if the
	 *         player has lost
	 */
	public boolean hasEnded() {
		return gameEnded;
	}

	/**
	 * @return list of spaceObjects
	 */
	public List<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

	public static List<SpaceObject> getEventSpaceObjects() {
		return eventSpaceObjects;
	}

	public static int getDebrisCount() {
		return debrisCount;
	}

	public static void setDebrisCount(int debrisCount) {
		GameBoard.debrisCount = debrisCount;
	}

	public static void setSpaceObjects(List<SpaceObject> sObj) {
		spaceObjects = sObj;
	}

	public static void setMaxDebris(int maxDebris) {
		GameBoard.maxDebris = maxDebris;
	}

	/**
	 * Starts the game. SpaceObjects start to move.
	 */
	public void startGame() {
		this.isRunning = true;
	}

	/**
	 * Stops the game. SpaceObjects stop moving.
	 */
	public void stopGame() {
		Input.resetAllPressed();
		this.isRunning = false;
	}

	/**
	 * Updates positions of all spaceObjects, checks collisions, handles
	 * eventSpaceObjects, increases maximum Number of Debris
	 */
	public void updateSpaceObjects() {
		List<SpaceObject> spaceObjects = getSpaceObjects();

		// update the positions of the player and the other spaceObjects
		for (SpaceObject so : spaceObjects) {
			so.move();
		}

		// iterate through all spaceObjects (except player) and checks for collision
		for (SpaceObject so1 : spaceObjects) {
			if (!so1.isAlive())
				continue; // because there is no need to check for a collision

			for (int i = spaceObjects.indexOf(so1) + 1; i < spaceObjects.size(); i++) {
				SpaceObject so2 = spaceObjects.get(i);
				if (so1.equals(so2))
					continue;

				collision.setSObjects(so1, so2);

				if (collision.detectCollision()) {
					Policy policy = new Policy(collision);
					policy.selectStrategy();
					collision.executeCollision();

					if (!player.isAlive() && !multiplayer || !player.isAlive() && multiplayer && !player2.isAlive())
						gameEnded = true;
				}
			}
		}

		for (SpaceObject spaceObject : eventSpaceObjects) {
			if (spaceObject.isAlive()) {
				spaceObjects.add(spaceObject);
			} else {
				if (spaceObject instanceof Debris && ((Debris) spaceObject).getSize() > 0) {
					((Debris) spaceObject).split();
				} else if (spaceObject instanceof Debris)
					increaseScore();
				spaceObjects.remove(spaceObject);
			}
		}
		eventSpaceObjects.clear();

		if (updateCounter >= 1500) { // over time increase
			maxDebris += 4;
			updateCounter %= 10;
		}
		if (debrisCount + 4 <= maxDebris) { // neuer Spawn possible
			Debris debris = spawn.get(spawncounter++ % spawn.size()).getCopy();
			GameBoard.spaceObjects.add(debris);
		}
		updateCounter++;
	}

	/**
	 * Checks if a Vector is outside of the game borders, updates it if necessary
	 * 
	 * @param v Vector that needs to be updated
	 */
	public static void keepInFrame(Vector v) {
		v.setXY(((v.getX() % width) + width) % width, ((v.getY() % height) + height) % height);
	}

	public Player getPlayer() {
		return player;
	}

	public static List<Planet> getPlanetList() {
		return planetList;
	}

	public void increaseScore() {
		score++;
	}

	public int getScore() {
		return score;
	}

	public static void newGameborders(int w, int h) {
		width = w;
		height = h;
	}
}