package controller.collision;

import model.SpaceObject;
import model.Vector;

public class CollisionPlayer_Player implements CollisionType{

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector){
		Collision.bounce(one, two, collisionVector);
	}
}
