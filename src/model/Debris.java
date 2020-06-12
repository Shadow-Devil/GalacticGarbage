package model;

public class Debris extends SpaceObject{
	
	private int size;
	
	public Debris(int radius, String icon, Vector positionVector, Vector directionVector, double speed){
		super(radius, icon, positionVector, directionVector, speed);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void move(){
		// TODO Auto-generated method stub
	}

	public void split() {
		
	}
	
	public int getSize(){
		return size;
	}
}
