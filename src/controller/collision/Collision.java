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
	
	/*
	 * 
	 */
	public void collide() {
		//TODO Collision - collide()
		selectCollisionType();
		executeCollision();
		
		//one.collide(two, collisionVector);

		
		
		
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
	
	private void selectCollisionType(){
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
	
	private void executeCollision(){
		collisionType.collide(one, two, collisionVector);
	}

	
	public static void repel(SpaceObject one, SpaceObject two, Vector collisionVector){
		//System.out.println("repel");
		double degree = collisionVector.getDegree() - two.getDirectionVector().getDegree();
		double diff = Math.signum(degree);
		if (diff == 0){
			two.getDirectionVector().multiply(-1);
		} else {
			two.getDirectionVector().turn(2*degree*diff);
		}
		moveAppart(one, two, false);
	}
	
	public static void bounce(SpaceObject one, SpaceObject two, Vector collisionVector){
		//System.out.println("Bounce");
		double degree = collisionVector.getDegree() - 90;//TODO kontrollieren
		//System.out.println(collisionVector.getDegree() + " " + degree);
		//System.out.println(one + " speed " + one.getSpeed());
		Vector v1 = one.getDirectionVector().turn(degree).multiply(one.getSpeed());
		Vector v2 = two.getDirectionVector().turn(degree).multiply(two.getSpeed());
		
		double temp = v1.getY();
		v1.setY(v2.getY());
		v2.setY(temp);
		
		one.setSpeed(v1.getLength());
		two.setSpeed(v2.getLength());
		//System.out.println(v1);
		v1.turn(-degree).toUnit();
		v2.turn(-degree).toUnit();
		//System.out.println(v1);
		Collision.moveAppart(one, two, true);
	}

	public static void moveAppart(SpaceObject one, SpaceObject two, boolean both) {
		Collision collision = new Collision(one, two);
		while(collision.detectCollision()) {
			if(both)
				one.move2();
			two.move2();
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
