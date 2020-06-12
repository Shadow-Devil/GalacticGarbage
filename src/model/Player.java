package model;

public class Player extends SpaceObject{

	private int health;
	
	public Player(Vector positionVector, Vector directionVector){
		//TODO
		super(10, "playerIcon", positionVector, directionVector, 0);
	}
	
	public void shoot() {
		
	}
	
	@Override
	public void move(){
		// TODO Auto-generated method stub
	}
	
	public void loseHealth(int damage) {

	}
}
