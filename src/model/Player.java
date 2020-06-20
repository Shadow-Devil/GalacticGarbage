package model;

import controller.GameBoard;
import controller.Input;
import view.GameBoardUI;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "playerIcon.gif";
	private static final double DEGREE_ON_TURN = 4.0;//TODO wert
	private int health = 100;
	private Vector facingVector;
	private double maxSpeed = 2;
	private static double projectileSpawnDiff = 20;	
	public Player(){
		//TODO
		super(10, ICONNAME, new Vector(30, 30), new Vector(1, 0), 0);
		facingVector = new Vector(1, 0);
	}
	
	/**
	 * Spawns a projectile in the direction the player is facing.
	 */
	public void shoot() {
		Projectile laser = new Projectile(this.positionVector.copy().add(this.facingVector.copy().
			multiply(projectileSpawnDiff)), this.facingVector);
		
		GameBoard.eventSpaceObjects.add(laser);
		GameBoardUI.addNew(laser); 
		
	}
	
	public Vector getFacingVector(){
		return facingVector;
	}

	@Override
	public void move(int maxX, int maxY) {
		// TODO Turn
		boolean a = Input.isaPressed();
		boolean d = Input.isdPressed();
		//System.out.println(a);
		if (a && d) {
		} else if (a) { //facingVector
			this.getFacingVector().turn(DEGREE_ON_TURN);
			//System.out.println("Turn " + facingVector);
			//System.out.println(facingVector.getDegree());
		} else if (d) {
			this.getFacingVector().turn(-DEGREE_ON_TURN);
			//System.out.println(facingVector);
		}
		//change movement
		boolean w = Input.iswPressed();
		boolean s = Input.issPressed();
		if (w && s) {
		} else if (w) { //TODO speed increase

			this.getPositionVector().add(directionVector.copy()
			.multiply(speed).add(this.facingVector.copy().multiply(maxSpeed)));
			//System.out.println(speed);
			//TODO Beschleunigung
		
			positionVector.setXY(((positionVector.getX() % maxX) + maxX) % maxX, 
							 ((positionVector.getY() % maxY) + maxY) % maxY);

			//System.out.println("pressed w");
			//this.directionVector.multiply(speed).add(this.facingVector.copy().multiply(maxSpeed));
			//System.out.println(directionVector);
			//System.out.println(positionVector.getDegree());
			//speed = this.directionVector.getLength();
			//this.directionVector.toUnit();
			//System.out.println(directionVector);
		} else if (s) { 
			this.getPositionVector().add(directionVector.copy()
			.multiply(speed).add(this.facingVector.copy().multiply(-1*maxSpeed)));
			//System.out.println(speed);
			//TODO Beschleunigung
		
			positionVector.setXY(((positionVector.getX() % maxX) + maxX) % maxX, 
							 ((positionVector.getY() % maxY) + maxY) % maxY);
		} else{
			super.move(maxX, maxY);
		}
		if (Input.isSpacePressed()){
			this.shoot();
		}
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

	@Override
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Debris) {
			if(((Debris) two).getSize() > 0) 
				loseHealth(((Debris) two).getSize() * Debris.damagePerSize);
				
//			}else {
//				two.repel(this, collisionVector);
//				speed /= 3;
//			}
			bounce(two, collisionVector);
			
		}else if(two instanceof Projectile) {
			two.die();
		}else if(two instanceof Moon || two instanceof Planet){
			die();
		}else {
			bounce(two, collisionVector);
		}
	}
}
