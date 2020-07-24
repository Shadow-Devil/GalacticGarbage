package controller.collision;

import model.Debris;
import model.SpaceObject;
import model.Vector;

public class CollisionDebris_MoonPlanet implements CollisionType {

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector) {
		internCollide((Debris) one, two, collisionVector);
	}

	private void internCollide(Debris one, SpaceObject two, Vector collisionVector) {
		if (one.getSize() == 0)
			one.die();
		else
			Collision.repel(two, one, collisionVector);
	}
}