package model;

public class Planet extends SpaceObject {
	private static final String[] ICONNAME = { "planet1Icon.gif", "planet2Icon.gif", "planet3Icon.gif" };

	public Planet(int radius, int icon, double x, double y) {
		super(radius, ICONNAME[icon], new Vector(x, y), new Vector(1, 0));
	}

	@Override
	public void move() {
	}
}