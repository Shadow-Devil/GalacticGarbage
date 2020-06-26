package model;

import controller.GameBoard;
import controller.collision.Collision;

public abstract class SpaceObject{
	protected final int radius;
	protected final String icon;
	protected final Vector positionVector, directionVector;
	
	protected double speed;
	protected boolean alive;
	
	public SpaceObject(int radius, String icon, Vector positionVector, Vector directionVector, double speed) {
		this.radius = radius;
		this.icon = icon;
		this.positionVector = positionVector;
		this.directionVector = directionVector;
		this.speed = speed;
		this.alive = true;
	}

	public void move() { 
		positionVector.add(directionVector.copy().multiply(speed));
		//System.out.println(speed);
		//TODO Beschleunigung
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

	public double getSpeed(){
		return speed;
	}

	public void setSpeed(double speed){
		this.speed = speed;
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

}
