package model;

public class Moon extends SpaceObject {

	private static final String[] ICONNAME = { "moon1Icon.gif", "moon2Icon.gif", "moon3Icon.gif" };
	private final Vector planet;
	private final double orbit;
	private final double turnSpeed;

	public Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet) {
		super(radius, ICONNAME[icon], planet.copy().add(planetToMoonVector), planetToMoonVector);
		this.turnSpeed = turnSpeed;
		this.planet = planet;
		orbit = planetToMoonVector.getLength();
	}

	@Override
	public void move() {
		directionVector.toUnit().turn(turnSpeed);
		directionVector.multiply(orbit);
		Vector newPos = planet.copy().add(directionVector);

		positionVector.setXY(newPos.getX(), newPos.getY());
	}
}