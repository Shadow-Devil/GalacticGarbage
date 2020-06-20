package model;

public class Moon extends SpaceObject{
	
	private static final String[] ICONNAME = {"playerIcon.gif", "playerIcon.gif", "playerIcon.gif"};
	private Vector planet;
	private double orbit;
	
	public Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet){
		super(radius, ICONNAME[icon], planet.copy().add(planetToMoonVector), planetToMoonVector, turnSpeed);
		this.planet= planet;
		orbit = planetToMoonVector.getLength();
	}

	@Override
	public void move(int maxX, int maxY){
		//System.out.println(directionVector);
		this.getDirectionVector().toUnit().turn(this.getSpeed());
		directionVector.multiply(orbit);
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
