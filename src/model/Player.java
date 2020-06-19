package model;

import controller.GameBoard;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "playerIcon";
	private int health = 100;
	private static final double DEGREE_ON_TURN = 3.0;//TODO wert
	private Vector facingVector;
	
	public Player(){
		//TODO
		super(10, ICONNAME, new Vector(250, 30), new Vector(1, 0), 0);
		facingVector = new Vector(1, 0);
	}
	
	/**
	 * Spawns a projectile in the direction the player is facing.
	 */
	public void shoot() {
		
	}
	
	@Override
	public void move(int maxX, int maxY) {
		// TODO Turn
		boolean a = GameBoard.input.isaPressed();
		boolean b = GameBoard.input.isdPressed();
		if (a && b) {
		} else if (a) {
			this.getDirectionVector().turn(DEGREE_ON_TURN);
		} else if (b) {
			this.getDirectionVector().turn(-DEGREE_ON_TURN);
		}
		super.move(maxX, maxY);
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
