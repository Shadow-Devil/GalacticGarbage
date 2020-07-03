package controller.collision;

import model.*;

public class Collision{

	//private boolean collide;
	private SpaceObject one;
	private SpaceObject two;
	private Vector collisionVector;
	private CollisionType collisionType;
	
	public Collision(SpaceObject one, SpaceObject two) {
		this.one = one;
		this.two = two;
	}
	

	/**
	 * Detects a collsion between two spaceObjects
	 * @return true if there is a collision
	 */
	public boolean detectCollision() {
		//zeigt von two nach one
		collisionVector = one.getPositionVector().copy();
		double distance = collisionVector.sub(two.getPositionVector()).getLength();
		//System.out.println(distance);
		
		return one.getRadius() + two.getRadius() >= distance;
	}
	
	/**
	 * Calls the executeCollision method
	 */
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
	
	/**
	 * Initializes the CollisionType based on the two SpaceObjects
	 */
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
	
	/**
	 * Switches the references for one & two
	 */
	private void switchSpaceObjects() {
		SpaceObject tmp = two;
		two = one;
		one = tmp;
	}
	
	/**
	 * Calls the collide method of the (previously chosen) collisionType
	 */
	public void executeCollision(){
		collisionType.collide(one, two, collisionVector);
	}

	/**
	 * Repels the movable SpaceObject from the stationary one
	 * @param one = stationary SpaceObject
	 * @param two = movable SpaceObject
	 * @param collisionVector = the vector between the two SpaceObjects
	 */
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
	
	/**
	 * Bounces the SpaceObjects from each other
	 * @param one = first movable SpaceObject
	 * @param two = second movable SpaceObject
	 * @param collisionVector = the vector between the two SpaceObjects
	 */
	public static void bounce(SpaceObject one, SpaceObject two, Vector collisionVector){
		//System.out.println("Bounce");
			
		double degree = collisionVector.getDegree() - 90;//TODO kontrollieren
		//System.out.println(collisionVector.getDegree() + " " + degree);
		//System.out.println(one + " speed " + one.getSpeed());
		Vector v1 = one.getDirectionVector();
		Vector v2 = two.getDirectionVector();
		
		//System.out.println("Player: " + v1 + "\nDebris: " + v2 + "\nCollision: " + collisionVector);
		
		//Wenn one Player, directionVector wird nicht geändert (kopiert)

		v1 = v1.copy();
		v2 = v2.copy();
		
		//
		v1.turn(degree);
		
		//System.out.println("Turned DirectionVector Player: " + v1);
		v2.turn(degree);
		//System.out.println("Turned DirectionVector Debris: " + v2);
		
		double temp = v1.getY();
		v1.setY(v2.getY());
		v2.setY(temp);
		
		
//		two.setSpeed(v2.getLength());
		v1.turn(-degree);
		v2.turn(-degree);
		
		one.getAccelerationVector().add(v1);
		two.getAccelerationVector().add(v1);
		
		
		
		//System.out.println("Player: " + one.getAccelerationVector() + "\nDebris: " + two.getAccelerationVector());
		Collision.moveAppart(one, two, true);
	}

	
	/**
	 * 
	 * @param one Stationary, wenn nur repellt wurde
	 * @param two Sich immer bewegende Spaceobject
	 * @param both Variable, ob repellt oder gebounced wird
	 */
	public static void moveAppart(SpaceObject one, SpaceObject two, boolean both) {
		Collision collision = new Collision(one, two);
		collision.detectCollision();
		
		//collisionVector von two nach one
//		if(both) {
//			
//		}else {
//			Vector v = collision.collisionVector;
//			double x = v.getLength() - one.getRadius() - two.getRadius();
//			x*=1.5;
//			x = x / v.getLength();
//			
//			two.getPositionVector().add(v.multiply(x));
//			
//			
//		}
		
		Vector v = new Vector(0,0)
		.turnUnitVector(collision.collisionVector.getDegree())
		.multiply((one.getRadius() + two.getRadius()));
		if(both) {
			
			one.getPositionVector().sub(v.copy().multiply(0.5));
			two.getPositionVector().sub(v.multiply(0.5));
			
		System.out.println(two);
		}else {
			two.getPositionVector().sub(v);
			
		}
		
		
//		while(collision.detectCollision()) {
//			if(both) 
//				one.moveBasic();
//			//System.out.println("moveapart");
//			two.moveBasic();
//		}
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
	
	public CollisionType getCollisionType() {
		return collisionType;
	}
}
