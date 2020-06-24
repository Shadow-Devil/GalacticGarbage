package model;

public class Planet extends SpaceObject{
	private static final String[] ICONNAME = {"planetIcon.gif", "debrisIcon.gif", "debrisIcon.gif"};

	public Planet(int radius, int icon, Vector positionVector){
		super(radius, ICONNAME[icon], positionVector, new Vector(1, 0), 0);
	}

	@Override
	public void move(){}
	
	public static int numberOfDiffrentPlanets() {
		return ICONNAME.length;
	}
}
