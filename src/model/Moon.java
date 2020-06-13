package model;

public class Moon extends SpaceObject{
	
	private static final String[] ICONNAME = {"MoonIcon1", "MoonIcon2", "MoonIcon3"};
	private Planet base;
	
	public Moon(int radius, int icon, Vector positionVector, Vector directionVector, double speed, Planet base){
		super(radius, ICONNAME[icon], positionVector, directionVector, speed);
		this.base = base;
	}

	@Override
	public void move(int maxX, int maxY){
		// TODO Auto-generated method stub
	}
}
