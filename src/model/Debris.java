package model;

import controller.GameBoard;
import view.GameBoardUI;

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
		Vector d1 = this.directionVector.copy().turn(30.0);
		Vector d2 = this.directionVector.copy().turn(-30.0);
		Debris deb1 = new Debris(this.size - 1, this.positionVector, d1, this.baseSpeed);
		Debris deb2 = new Debris(this.size - 1, this.positionVector, d2, this.baseSpeed);

		GameBoardUI.addNew(deb1);
		GameBoardUI.addNew(deb2);
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
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Player) {
			if(size > 0)
				((Player) two).loseHealth(size * damagePerSize);
			
			bounce(two, collisionVector);
		}else if(two instanceof Projectile) {
			if (size > 0){
				die();
			}
			two.die();
		}else if(two instanceof Moon || two instanceof Planet){
			if(size == 0)
				die();
			else {
				//TODO eventuell GameLost
				two.repel(this, collisionVector);
			}
		}else {
			bounce(two, collisionVector);
		}
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
