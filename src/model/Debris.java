package model;

public class Debris extends SpaceObject{
	
	private static final String ICONNAME = "debrisIcon";
	private int size;
	
	public Debris(int radius, Vector positionVector, Vector directionVector, double speed){
		super(radius, ICONNAME, positionVector, directionVector, speed);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void move(int maxX, int maxY){
		// TODO Auto-generated method stub
	}

	public void split() {
		
	}
	
	public int getSize(){
		return size;
	}
}
