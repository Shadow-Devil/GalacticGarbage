package controller.collision;

import model.*;

public class Collision implements CollisionInterface {

	private SpaceObject one;
	private SpaceObject two;
	private Vector collisionVector;
	private CollisionType collisionType;

	public void setSObjects(SpaceObject one, SpaceObject two) {
		this.one = one;
		this.two = two;
	}

	/**
	 * Detects a collision between two spaceObjects
	 * 
	 * @return true if there is a collision
	 */
	public boolean detectCollision() {
		// zeigt von two nach one
		collisionVector = one.getPositionVector().copy();
		double distance = collisionVector.sub(two.getPositionVector()).getLength();
		return one.getRadius() + two.getRadius() >= distance;
	}

	/**
	 * Initializes the CollisionType based on the two SpaceObjects
	 */
	public void selectCollisionType() {
		if (one == null || two == null)
			throw new IllegalArgumentException("A SpaceObject isn't instantiated.");
		if (one instanceof Projectile) {
			if (two instanceof Projectile) {
				this.collisionType = new CollisionProjectile_Projectile();
			} else if (two instanceof Debris) {
				this.collisionType = new CollisionProjectile_Debris();
			} else {
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}
		} else if (one instanceof Player) {
			if (two instanceof Debris) {
				this.collisionType = new CollisionPlayer_Debris();
			} else if (two instanceof Projectile) {
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			} else if (two instanceof Player) {
				this.collisionType = new CollisionPlayer_Player();
			} else {// Planet oder Moon
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			}
		} else if (one instanceof Debris) {
			if (two instanceof Debris) {
				this.collisionType = new CollisionDebris_Debris();
			} else if (two instanceof Projectile) {
				switchSpaceObjects();
				this.collisionType = new CollisionProjectile_Debris();
			} else if (two instanceof Player) {
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_Debris();
			} else {// Planet oder Moon
				this.collisionType = new CollisionDebris_MoonPlanet();
			}
		} else {// Planet oder Moon
			if (two instanceof Player || two instanceof Projectile) {
				switchSpaceObjects();
				this.collisionType = new CollisionPlayer_MoonPlanet__Projectile_SpaceObject();
			} else if (two instanceof Debris) {
				switchSpaceObjects();
				this.collisionType = new CollisionDebris_MoonPlanet();
			} else if (two instanceof Moon || two instanceof Planet) {
				throw new UnsupportedOperationException("Planets & moons are not allowed to collide.");
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
	public void executeCollision() {
		collisionType.collide(one, two, collisionVector);
	}

	/**
	 * Repels the movable SpaceObject from the stationary one
	 * 
	 * @param one             = stationary SpaceObject
	 * @param two             = movable SpaceObject
	 * @param collisionVector = the vector between the two SpaceObjects
	 */
	public static void repel(SpaceObject one, SpaceObject two, Vector collisionVector) {
		double degree = collisionVector.getDegree() - two.getDirectionVector().getDegree();
		double diff = Math.signum(degree);
		Vector v = two.getDirectionVector().copy().turn(2 * degree * diff).multiply(-1);
		two.getAccelerationVector().add(v);
		moveAppart(one, two, false);
	}

	/**
	 * Bounces the SpaceObjects from each other
	 * 
	 * @param one             = first movable SpaceObject
	 * @param two             = second movable SpaceObject
	 * @param collisionVector = the vector between the two SpaceObjects
	 */
	public static void bounce(SpaceObject one, SpaceObject two, Vector collisionVector) {
		double degree = collisionVector.getDegree() - 90;
		Vector v1 = one.getDirectionVector();
		Vector v2 = two.getDirectionVector();

		v1.turn(degree);
		v2.turn(degree);

		double temp = v1.getY();
		v1.setY(v2.getY());
		v2.setY(temp);

		v1.turn(-degree).multiply(1.1);
		v2.turn(-degree).multiply(1.1);

		Collision.moveAppart(one, two, true);
	}

	/**
	 * 
	 * @param one  stationary, if it was just repelled
	 * @param two  always moving spaceObject
	 * @param both if repelled or bounced
	 */
	public static void moveAppart(SpaceObject one, SpaceObject two, boolean both) {
		Collision collision = new Collision();
		collision.setSObjects(one, two);
		collision.detectCollision();

		Vector v = new Vector(0, 0).turnUnitVector(collision.collisionVector.getDegree())
				.multiply((one.getRadius() + two.getRadius() - collision.collisionVector.getLength() + 0.5));
		if (both) {
			one.getPositionVector().add(v.copy().multiply(0.5));
			two.getPositionVector().sub(v.copy().multiply(0.5));
		} else {
			two.getPositionVector().sub(v);
		}
	}

	@Override
	public String toString() {
		return "Collision [one=" + one + ", two=" + two + ", collisionVector=" + collisionVector + "]";
	}

	public CollisionType getCollisionType() {
		return collisionType;
	}
}