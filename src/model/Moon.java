package model;

public class Moon extends SpaceObject{

	private Planet base;
	
	public Moon(int radius, String icon, Vector positionVector, Vector directionVector, double speed, Planet base){
		super(radius, icon, positionVector, directionVector, speed);
		this.base = base;
	}

	@Override
	public void move(){
		// TODO Auto-generated method stub
	}
}
