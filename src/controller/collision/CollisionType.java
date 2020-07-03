package controller.collision;

import model.SpaceObject;
import model.Vector;

public interface CollisionType{
	/**
	 * Calls bounce/repel in Collision or similar functionality (die() etc.)
	 * @param one = first SpaceObject
	 * @param two = second SpaceObject
	 * @param collisionVector = the vector between the two SpaceObjects
	 */
	void collide(SpaceObject one, SpaceObject two, Vector collisionVector);
}
