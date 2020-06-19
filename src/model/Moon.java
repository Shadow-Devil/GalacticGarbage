package model;

public class Moon extends SpaceObject{
	
	private static final String[] ICONNAME = {"MoonIcon1", "MoonIcon2", "MoonIcon3"};
	private Vector planet;
	
	public Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet){
		super(radius, ICONNAME[icon], planet.copy().add(planetToMoonVector), planetToMoonVector, turnSpeed);
		this.planet= planet;
	}

	@Override
	public void move(int maxX, int maxY){
		this.getDirectionVector().turn(this.getSpeed());
		Vector newPos = planet.copy().add(this.getDirectionVector());
		this.getPositionVector().setXY(newPos.getX(), newPos.getY());
	}

	@Override
	public void collide(SpaceObject two, Vector collisionVector){
		if(two instanceof Player) {
			two.die();
		}else if(two instanceof Debris) {
			if(((Debris) two).getSize() == 0) 
				two.die();
			else {
				//TODO eventuell GameLost/fancy methode(split?)
				//TODO mond keine auswirkung durch die Collision
				repel(two, collisionVector);
			}
		}else if(two instanceof Projectile) {
			two.die();
		}else {
			throw new IllegalArgumentException("Mond ist mit einem Mond/Planeten collided");
		}
	}
}
