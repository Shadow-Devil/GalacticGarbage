package model;

import controller.GameBoard;
import controller.collision.Collision;

public class Debris extends SpaceObject{
	
	private static final String ICONNAME = "debrisIcon.gif";
	private final int size;//0, 1, 2
	public static final int damagePerSize = 10;
	private static final int radiusPerSize = 10;
	private final double baseSpeed;
	
	public Debris(int size, Vector positionVector, Vector directionVector, double speed){
		super((size+1) * radiusPerSize, ICONNAME, positionVector, directionVector, speed);
		this.size = size;
		this.baseSpeed = speed;
	}

	/**
	 * Is invoked when debris is split into smaller parts.
	 */
	public void split() { 
		//TODO Debris - split()
		//System.out.println("Split");

		Debris deb1 = new Debris(size - 1, positionVector.copy(), directionVector.copy().turn(30.0), baseSpeed);
		Debris deb2 = new Debris(size - 1, positionVector.copy(), directionVector.copy().turn(-30.0), baseSpeed);
		Collision.moveAppart(deb1, deb2, true);
		
		GameBoard.spaceObjects.add(deb1);
		GameBoard.spaceObjects.add(deb2);
		
		System.out.println("Splited");
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

	public Debris getCopy() {
		GameBoard.debrisCount += (size>0 ? size*2 : 1);
		return new Debris(size, positionVector, directionVector, baseSpeed);
	}
}
