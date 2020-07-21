package model;

import controller.GameBoard;

public class SpaceObject{
	protected final int radius;
	protected final String icon;
	protected final Vector positionVector, directionVector;
	protected Vector accelerationVector;
	protected Vector facingVector;
	
//	protected double speed;
	protected boolean alive;
	
	public SpaceObject(int radius, String icon, Vector positionVector, Vector directionVector) {
		this.radius = radius;
		this.icon = icon;
		this.positionVector = positionVector;
		this.directionVector = directionVector;
		this.accelerationVector = new Vector(0, 0);
		this.facingVector = new Vector(1, 0);
		this.alive = true;
	}
	
	public SpaceObject(int radius, String icon, double pX, double pY, double fX, double fY) {
		this(radius, icon, new Vector(pX, pY), null);
		this.facingVector = new Vector(fX, fY);
	}

	public void move() { 
		GameBoard.getPlanetList().forEach(p -> {
			Vector c = positionVector.copy().sub(p.positionVector).multiply(-0.000002 * p.getRadius());
			accelerationVector.add(c);
		});
		
		//moveWithAcceleration
		double y = -0.2;
		Vector dir = directionVector.copy().multiply(y);
		accelerationVector.add(dir);
		directionVector.add(accelerationVector);
		
		if(!(this instanceof Player) && directionVector.getLength() > 0.0001)
			facingVector = directionVector.copy().toUnit();
		
		moveBasic();
		
		accelerationVector = new Vector(0, 0);
	}
	
	public void moveBasic() {
		positionVector.add(directionVector);
		GameBoard.keepInFrame(positionVector);
	}
	
	
	/** 
	 * Adds this spaceObject to GameBoard.deadSpaceObjects. <br>
	 * Sets the boolean alive to false.
	 */
	public void die() {
		GameBoard.eventSpaceObjects.add(this);
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public int getRadius(){
		return radius;
	}

	public String getIcon(){
		return icon;
	}

	public Vector getPositionVector(){
		return positionVector;
	}

	public Vector getDirectionVector(){
		return directionVector;
	}
	
	public Vector getAccelerationVector(){
		return accelerationVector;
	}
	
	public Vector getFacingVector(){
		return facingVector;
	}

}
