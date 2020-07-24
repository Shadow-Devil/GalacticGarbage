package controller.collision;

import model.Debris;
import model.Player;
import model.SpaceObject;
import model.Vector;

public class CollisionPlayer_Debris implements CollisionType {

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector) {
		internCollide((Player) one, (Debris) two, collisionVector);
	}

	private void internCollide(Player one, Debris two, Vector collisionVector) {
		if (two.getSize() > 0) {
			one.loseHealth(two.getSize() * Debris.damagePerSize);
			Collision.bounce(one, two, collisionVector);
		} else
			Collision.bounce(one, two, collisionVector);
	}
}