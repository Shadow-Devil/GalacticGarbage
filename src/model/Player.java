package model;

public class Player extends SpaceObject{
	
	private static final String ICONNAME = "playerIcon";
	private int health = 100;
	
	public Player(){
		//TODO
		super(10, ICONNAME, new Vector(250, 30), new Vector(1, 0), 0);
	}
	
	public void shoot() {
		
	}
	
	@Override
	public void move(int maxX, int maxY){
		// TODO Auto-generated method stub
	}
	
	public void loseHealth(int damage) {
		health -= damage;
		if(health <= 0)
			die();
	}
	
	
}
