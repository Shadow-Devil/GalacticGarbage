package model;

public class Moon extends SpaceObject{
	
	private static final String[] ICONNAME = {"moon1Icon.gif", "moon2Icon.gif", "moon3Icon.gif"};
	private final Vector planet;
	private final double orbit;
	
	public Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet){
		super(radius, ICONNAME[icon], planet.copy().add(planetToMoonVector), planetToMoonVector, turnSpeed);
		this.planet= planet;
		orbit = planetToMoonVector.getLength();
	}

	@Override
	public void move(){
		//System.out.println(directionVector);
		directionVector.toUnit().turn(speed);
		directionVector.multiply(orbit);
		Vector newPos = planet.copy().add(directionVector);
		
		positionVector.setXY(newPos.getX(), newPos.getY());
	}

}
