package controller;

import model.Player;
import model.Projectile;
import model.SpaceObject;
import model.Vector;

public class Collision{

	private boolean collide;
	private final SpaceObject one, two;
	private Vector collisionVector;
	
	
	public Collision(SpaceObject one, SpaceObject two){
		this.one = one;
		this.two = two;
	}

	/**
	 * Detects a collsion between two spaceObjects
	 * @return true if there is a collision
	 */
	public boolean detectCollision() {
		collisionVector = one.getPositionVector().copy();
		Vector positionTwo = two.getPositionVector().copy().multiply(-1);
		double distance = collisionVector.add(positionTwo).getLength();
		//System.out.println(distance);
		
		return one.getRadius() + two.getRadius() >= distance;
	}
	
	/*
	 * 
	 */
	public void collide() {
		//TODO Collision - collide()
		
		//System.out.println();
		one.collide(two, collisionVector);
		
		
		/*
		 * Player:
		 * 		debris -> eventuell damage	
		 * 		moon/planet -> die	
		 * 		
		 * Projectile:
		 * 		wird ausgelöscht
		 * 		debris -> eventuell split
		 * 
		 * debris:
		 * 		moon/planet(/debris) -> die (wenn klein)
		 * 	
		 * 
		 * bounce sonst
		 */
		
	}

	@Override
	public String toString(){
		return "Collision [collide=" + collide +
			", one=" +
			one +
			", two=" +
			two +
			", collisionVector=" +
			collisionVector +
			"]";
	}
}
