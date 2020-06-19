package model;

public class Projectile extends SpaceObject{
	
	private static final String ICONNAME = "ProjectileIcon";

	public Projectile(Vector positionVector, Vector directionVector){
		//TODO radius, iconName, speed
		super(5, ICONNAME, positionVector, directionVector, 5);
	}

	@Override
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Debris) {
			if(((Debris) two).getSize() > 0)
				((Debris) two).split();
			
		}else if(two instanceof Projectile) {
			two.die();
		}
		die();
	}
}
