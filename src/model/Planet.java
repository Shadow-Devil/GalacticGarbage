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
}
