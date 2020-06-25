package controller.collision;

import model.SpaceObject;
import model.Vector;

public interface CollisionType{
	void collide(SpaceObject one, SpaceObject two, Vector collisionVector);
}
