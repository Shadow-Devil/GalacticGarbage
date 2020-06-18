package model;

public class Projectile extends SpaceObject{
	
	private static final String ICONNAME = "ProjectileIcon";

	public Projectile(Vector positionVector, Vector directionVector){
		//TODO radius, iconName, speed
		super(5, ICONNAME, positionVector, directionVector, 5);
	}
}
