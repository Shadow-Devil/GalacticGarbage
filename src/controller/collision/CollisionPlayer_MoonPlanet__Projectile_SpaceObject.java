package controller.collision;

import model.SpaceObject;
import model.Vector;

public class CollisionPlayer_MoonPlanet__Projectile_SpaceObject implements CollisionType {

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector) {
		one.die();
	}
}