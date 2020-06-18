package model;

public class Debris extends SpaceObject{
	
	private static final String ICONNAME = "debrisIcon";
	private int size;
	
	public Debris(int radius, Vector positionVector, Vector directionVector, double speed){
		super(radius, ICONNAME, positionVector, directionVector, speed);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Is invoked when debris is split into smaller parts.
	 */
	public void split() {
		//TODO Debris - split()
	}
	
	/**
	 * @return size
	 */
	public int getSize(){
		return size;
	}
}
