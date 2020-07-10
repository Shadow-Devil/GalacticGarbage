package model;

import controller.GameBoard;
import controller.collision.Collision;

public class Debris extends SpaceObject{
	
	private static final String ICONNAME = "debrisIcon.gif";
	public static final int damagePerSize = 10;
	private static final int radiusPerSize = 10;
	
	private final int size;//0, 1, 2
	private Vector projectileDirectionVector;
	
	public Debris(double x, double y){
		super(3 * radiusPerSize, ICONNAME, new Vector(x, y), new Vector(0, 0));
		this.size = 2;
	}
	
	public Debris(int size, Vector positionVector, Vector directionVector) {
		super((size + 1) * radiusPerSize, ICONNAME, positionVector, directionVector);
		this.size = size;
	}

	/**
	 * Is invoked when debris is split into smaller parts.
	 */
	public void split() {

		Debris deb1 = new Debris(size - 1, positionVector.copy(), projectileDirectionVector.copy().turn(30.0));
		Debris deb2 = new Debris(size - 1, positionVector.copy(), projectileDirectionVector.copy().turn(-30.0));
		Collision.moveAppart(deb1, deb2, true);
		
		GameBoard.spaceObjects.add(deb1);
		GameBoard.spaceObjects.add(deb2);
		
		GameBoard.debrisCount += size*2;
	}
	
	/**
	 * @return size
	 */
	public int getSize(){
		return size;
	}

	@Override
	public void die() {
		super.die();
		GameBoard.debrisCount -= (size>0 ? size*2 : 1);
	}
	
	/**
	 * Saves the vector of the killer projectile for direction purposes after being split
	 * @param projdir = the new projectileDirectionVector
	 */
	public void die(Vector projdir) {
		projectileDirectionVector = projdir;
		die();
	}
	
	/**
	 * 
	 * @return a new copy of this debris
	 */
	public Debris getCopy() {
		GameBoard.debrisCount += (size>0 ? size*2 : 1);
		return new Debris(size, positionVector.copy(), directionVector.copy());
	}
}
