package controller.collision;

import model.SpaceObject;
import model.Vector;

public class CollisionProjectile_Projectile implements CollisionType {

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector) {
		one.die();
		two.die();
	}
}