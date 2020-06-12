package model;

public abstract class SpaceObject{
	protected final int radius;
	protected final String icon;
	protected final Vector positionVector, directionVector;
	
	protected double speed;
	protected boolean alive;

	


	public SpaceObject(int radius, String icon, Vector positionVector, Vector directionVector, double speed){
		this.radius = radius;
		this.icon = icon;
		this.positionVector = positionVector;
		this.directionVector = directionVector;
		this.speed = speed;
		this.alive = true;
	}



	public abstract void move();
	
	public void die() {
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
}
