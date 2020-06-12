package model;

public class Projectile extends SpaceObject{
	
	

	public Projectile(Vector positionVector, Vector directionVector){
		//TODO radius, iconName, speed
		super(5, "ProjectileString", positionVector, directionVector, 5);
	}

	@Override
	public void move(){
		// TODO Auto-generated method stub
		
	}

}
