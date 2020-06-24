package model;

public class Projectile extends SpaceObject{
	
	private static final String ICONNAME = "playerIcon.gif";
	private int lifeTime = 50;

	public Projectile(Vector positionVector, Vector directionVector){
		//TODO radius, iconName, speed
		super(5, ICONNAME, positionVector, directionVector, 10);
	}
	
	@Override
	public void move(){
		super.move();
		if(lifeTime-- < 0)
			die();
	}
}
