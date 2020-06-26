package controller.collision;

import model.*;

public class Collision{

	//private boolean collide;
	private SpaceObject one;
	private SpaceObject two;
	private Vector collisionVector;
	private CollisionType collisionType;
	
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
	
	public void collide() {
		executeCollision();
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
	
	public void selectCollisionType(){
		if (one instanceof Projectile){
			if (two instanceof Projectile){
				this.collisionType = new CollisionProjectile_Projectile();
			} else if (two instanceof Debris){
				this.collisionType = new CollisionProjectile_Debris();
			} else {
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}
		} else if(one instanceof Player){
			if (two instanceof Debris){
				this.collisionType = new CollisionPlayer_Debris();
			}else if(two instanceof Projectile){
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}else {//Planet oder Moon
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}
		} else if(one instanceof Debris){
			if(two instanceof Debris){
				this.collisionType = new CollisionDebris_Debris();
			}else if(two instanceof Projectile) {
				switchSpaceObjects();
				this.collisionType = new CollisionProjectile_Debris();
			}else if(two instanceof Player) {
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_Debris();
			}else {//Planet oder Moon
				this.collisionType = new CollisionDebris_MoonPlanet();
			}
			
		} else{//Planet oder Moon
			if(two instanceof Player || two instanceof Projectile) {
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}else if(two instanceof Debris) {
				switchSpaceObjects();
				this.collisionType = new CollisionDebris_MoonPlanet();
			}
		} 
		
	}
	
	private void switchSpaceObjects() {
		SpaceObject tmp = two;
		two = one;
		one = tmp;
	}
	
	public void executeCollision(){
		collisionType.collide(one, two, collisionVector);
	}

	
	public static void repel(SpaceObject one, SpaceObject two, Vector collisionVector){
		//System.out.println("repel");
		System.out.println(collisionVector.getDegree());
		double degree = collisionVector.getDegree() - two.getDirectionVector().getDegree();
		System.out.println(degree);
		double diff = Math.signum(degree);
		System.out.println(diff);
		two.getDirectionVector().turn(2*degree*diff).multiply(-1).toUnit();

		moveAppart(one, two, false);
	}
	
	public static void bounce(SpaceObject one, SpaceObject two, Vector collisionVector){
		//System.out.println("Bounce");
			
		double degree = collisionVector.getDegree() - 90;//TODO kontrollieren
		//System.out.println(collisionVector.getDegree() + " " + degree);
		//System.out.println(one + " speed " + one.getSpeed());
		Vector v1 = one.getDirectionVector();
		Vector v2 = two.getDirectionVector();
		if(one instanceof Player) {
			v1 = v1.copy();
		}
		v1.turn(degree);
		if(!(one instanceof Player)) {
			v1.multiply(one.getSpeed());
		}
		v2.turn(degree).multiply(two.getSpeed());
		
		double temp = v1.getY();
		v1.setY(v2.getY());
		v2.setY(temp);
		
		if(!(one instanceof Player)) {
			one.setSpeed(v1.getLength());
		}
		two.setSpeed(v2.getLength());
		v1.turn(-degree);
		if(!(one instanceof Player)) {
			v1.toUnit();
		} else {
			((Player) one).getAccelerationVector().add(v1);
		}
		v2.turn(-degree).toUnit();
		Collision.moveAppart(one, two, true);
	}

	public static void moveAppart(SpaceObject one, SpaceObject two, boolean both) {
		Collision collision = new Collision(one, two);
		while(collision.detectCollision()) {
			if(both) {
				if(one instanceof Player) {
					((Player) one).moveBasic();
				} else {
					one.move();
				}
			}
			two.move();
		}
	}

	@Override
	public String toString(){
		return "Collision [one=" +
			one +
			", two=" +
			two +
			", collisionVector=" +
			collisionVector +
			"]";
	}
}
