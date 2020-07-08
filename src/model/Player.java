package model;

import controller.GameBoard;
import controller.Input;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "spaceshipIcon.gif";
	private static final double DEGREE_ON_TURN = 4.0;//TODO wert
	private static final double projectileSpawnDiff = 30;
	private static final double maxSpeed = 1.8;
	
	private int health = 100;
	

	public Player(){
		super(20, ICONNAME, new Vector(30, 30), new Vector(0, 0));
	}
	
	/**
	 * Spawns a projectile in the direction the player is facing.
	 */
	public void shoot() {
		Projectile laser = new baseProjectile(positionVector.copy().add(facingVector.copy().
			multiply(projectileSpawnDiff)), facingVector.copy());
		Input.resetSpacePressed();		
		GameBoard.eventSpaceObjects.add(laser);
	}
	

	@Override
	public void move() {
		super.move();

		if (Input.isaPressed() && !Input.isdPressed())
			facingVector.turn(DEGREE_ON_TURN).toUnit();
		if (Input.isdPressed() && !Input.isaPressed())
			facingVector.turn(-DEGREE_ON_TURN).toUnit();
		


		if (Input.iswPressed() && !Input.issPressed()) {
			Vector n = facingVector.copy().multiply(maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
		}
		
		if (Input.issPressed() && !Input.iswPressed()) {
			Vector n = facingVector.copy().multiply(-1*maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
		}
		
		if (Input.isSpacePressed())
			shoot();
	}
	
	

	/**
	 * Reduces the players amount of health by the incoming damage. <br>
	 * Kills the player if health drops to 0.
	 * 
	 * @param damage received
	 * @exception IllegalArgumentException if the damage is <= 0
	 */
	public void loseHealth(int damage) {
		if(damage <= 0)
			throw new IllegalArgumentException("received damage must be over 0");
		health -= damage;
		if(health <= 0)
			die();
	}
}
