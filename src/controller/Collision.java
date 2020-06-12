package controller;

import model.SpaceObject;

public class Collision{

	private boolean collide;
	private final SpaceObject one, two;
	
	
	public Collision(SpaceObject one, SpaceObject two){
		this.one = one;
		this.two = two;
	}

	public boolean detectCollision() {
		return false;
	}
	
	public void collide() {
		
	}
}
