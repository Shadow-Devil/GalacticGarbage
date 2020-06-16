package model;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "playerIcon";
	private int health = 100;
	
	public Player(){
		//TODO
		super(10, ICONNAME, new Vector(250, 30), new Vector(1, 0), 0);
	}
	
	/**
	 * Spawns a projectile in the direction the player is facing.
	 */
	public void shoot() {
		
	}
	
	@Override
	public void move(int maxX, int maxY) {
		// TODO Player - move()
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
