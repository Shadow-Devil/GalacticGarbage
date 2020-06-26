package model;

import controller.GameBoard;
import controller.Input;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "spaceshipIcon.gif";
	private static final double DEGREE_ON_TURN = 4.0;//TODO wert
	private static final double projectileSpawnDiff = 20;	
	
	private int health = 100;
	private double maxSpeed = 2;	
	private Vector facingVector;
	private Vector accelerationVector;
	private boolean firstCollision = false;
	

	public Player(){
		super(20, ICONNAME, new Vector(30, 30), new Vector(0, 0), 0);
		facingVector = new Vector(1, 0);
		accelerationVector = new Vector(0, 0);
	}
	
	/**
	 * Spawns a projectile in the direction the player is facing.
	 */
	public void shoot() {
		Projectile laser = new Projectile(positionVector.copy().add(facingVector.copy().
			multiply(projectileSpawnDiff)), facingVector.copy());
		Input.resetSpacePressed();		
		GameBoard.eventSpaceObjects.add(laser);
		//System.out.println("Shoot");
	}
	
	public Vector getFacingVector(){
		return facingVector;
	}

	@Override
	public void move() {
		moveWithAcc();
		accelerationVector = new Vector(0, 0);

		if (Input.isaPressed() && !Input.isdPressed())
			facingVector.turn(DEGREE_ON_TURN).toUnit();
		if (Input.isdPressed() && !Input.isaPressed())
			facingVector.turn(-DEGREE_ON_TURN).toUnit();
		
		//change movement
		boolean w = Input.iswPressed();
		boolean s = Input.issPressed();
		if (w && s) {
		} else if (w) { 
			Vector n = facingVector.copy().multiply(maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
			//System.out.println("pressed w");
		} else if (s) { 
			Vector n = facingVector.copy().multiply(-1*maxSpeed);
			accelerationVector.add(n);
			GameBoard.keepInFrame(positionVector);
		}
		
		if (Input.isSpacePressed()){
			//System.out.println("Shoot");
			this.shoot();
		}
	}
	
	public void moveWithAcc() {
		double y = -0.2;
		Vector dir = directionVector.copy().multiply(y);
		accelerationVector.add(dir);
		directionVector.add(accelerationVector);
		moveBasic();
	}

	public void moveBasic() { 
		positionVector.add(directionVector.copy());
		GameBoard.keepInFrame(positionVector);
	}
	
	public Vector getAccelerationVector(){
		return accelerationVector;
	}

	public void setFirstCollision(){
		this.firstCollision = true;
	}
	
	public boolean isFirstCollision(){
		return firstCollision;
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
