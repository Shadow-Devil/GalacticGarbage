package model;

public class Planet extends SpaceObject{
	private static final String[] ICONNAME = {"PlanetIcon1", "PlanetIcon2", "PlanetIcon3"};

	public Planet(int radius, int icon, Vector positionVector){
		
		super(radius, ICONNAME[icon], positionVector, new Vector(0, 0), 0);
	}

	@Override
	public void move(int maxX, int maxY){}
	
	public static int numberOfDiffrentPlanets() {
		return ICONNAME.length;
	}

	@Override
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Player) {
			two.die();
		}else if(two instanceof Debris) {
			if(((Debris) two).getSize() == 0) 
				two.die();
			else {
				//TODO eventuell GameLost
				//TODO Planet keine auswirkung durch die Collision
				repel(two, collisionVector);
			}
		}else if(two instanceof Projectile) {
			two.die();
		}else {
			throw new IllegalArgumentException("Planet ist mit einem Mond/Planeten collided");
		}
		
	}
}
