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
}
