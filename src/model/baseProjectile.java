package model;

import controller.GameBoard;

public class baseProjectile extends Projectile {
	
	private static final String ICONNAME = "projectileIcon.gif";
	private int lifeTime = 50;

	public baseProjectile(Vector positionVector, Vector directionVector){
		super(5, ICONNAME, positionVector, directionVector.toUnit().multiply(10));
	}
	
	@Override
	public void move(){
		positionVector.add(directionVector);
		GameBoard.keepInFrame(positionVector);
		
		if(lifeTime-- < 0)
			die();
	}
}