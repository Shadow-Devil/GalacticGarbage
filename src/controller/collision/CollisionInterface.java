package controller.collision;

import model.SpaceObject;

public interface CollisionInterface {
	public boolean detectCollision();
	public void collide();
	public void selectCollisionType();
	public void executeCollision();
	public CollisionType getCollisionType();
	public void setSObjects(SpaceObject one, SpaceObject two);
}
