package controller.collision;

import model.SpaceObject;
import model.Vector;

public class CollisionDebris_Debris implements CollisionType{

	@Override
	public void collide(SpaceObject one, SpaceObject two, Vector collisionVector){
		Collision.bounce(one, two, collisionVector);
	}

}
