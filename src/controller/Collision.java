package controller;

import model.SpaceObject;

public class Collision{

	private boolean collide;
	private final SpaceObject one, two;
	
	
	public Collision(SpaceObject one, SpaceObject two){
		this.one = one;
		this.two = two;
	}

	/**
	 * Detects a collsion between two spaceObjects
	 * @return true if there is a collision
	 */
	public boolean detectCollision() {
		//TODO Collision - detectCollision()
		return false;
	}
	
	/*
	 * 
	 */
	public void collide() {
		//TODO Collision - collide()
	}
}
