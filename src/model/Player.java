package model;

import controller.GameBoard;
import controller.Input;
import view.GameBoardUI;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "spaceshipIcon.gif";
	private static final double DEGREE_ON_TURN = 4.0;//TODO wert
	private static final double projectileSpawnDiff = 20;	
	
	private int health = 100;
	private double maxSpeed = 4;	
	private Vector facingVector;
	private Vector accelerationVector;
	private boolean firstCollision = false;
	

	public Player(){
		super(20, ICONNAME, new Vector(30, 30), new Vector(1, 0), 5);
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
		// TODO Turn
		//System.out.println(a);

		if (Input.isaPressed() && !Input.isdPressed())  //facingVector
			facingVector.turn(DEGREE_ON_TURN).toUnit();
			
			//System.out.println("Turn " + facingVector);
			//System.out.println(facingVector.getDegree());

		if (Input.isdPressed() && !Input.isaPressed())
			facingVector.turn(-DEGREE_ON_TURN).toUnit();
		
		//change movement
		boolean w = Input.iswPressed();
		boolean s = Input.issPressed();
		
		if (w && s) {
		} else if (w) { //TODO speed increase
			Vector n = facingVector.copy().multiply(maxSpeed);
			if (this.firstCollision){
				n.add(directionVector.copy().multiply(speed));
			}
			positionVector.add(n);
			//speed = m.getLength();
			//System.out.println(speed);
			//TODO Beschleunigung
			
			GameBoard.keepInFrame(positionVector);

			//System.out.println("pressed w");
			//this.directionVector.multiply(speed).add(this.facingVector.copy().multiply(maxSpeed));
			//System.out.println(directionVector);
			//System.out.println(positionVector.getDegree());
			//speed = this.directionVector.getLength();
			//this.directionVector.toUnit();
			//System.out.println(directionVector);
		} else if (s) { 
			Vector n = facingVector.copy().multiply(-1*maxSpeed);
			if (this.firstCollision){
				n.add(directionVector.copy().multiply(speed));
			}
			positionVector.add(n);
			
			//speed = m.getLength();
			//System.out.println(speed);
			//TODO Beschleunigung
		
			GameBoard.keepInFrame(positionVector);
		} else{
			if (this.firstCollision){
				super.move();
			}
		}
		//Speed 
		speed *= 0.99;
		
		if (Input.isSpacePressed()){
			//System.out.println("Shoot");
			this.shoot();
		}
		
		
		accelerationVector = new Vector(0, 0);
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
