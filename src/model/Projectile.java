package model;

public abstract class Projectile extends SpaceObject {
	
	public Projectile(int radius, String icon, Vector positionVector, Vector directionVector){
		//TODO radius, iconName, speed
		super(radius, icon, positionVector, directionVector);
	}
	
	@Override
	public abstract void move();
}