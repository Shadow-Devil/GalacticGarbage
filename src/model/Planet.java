package model;

public class Planet extends SpaceObject{

	public Planet(int radius, String icon, Vector positionVector){
		
		super(radius, icon, positionVector, new Vector(0, 0), 0);
	}

	@Override
	public void move(){}
	
}
