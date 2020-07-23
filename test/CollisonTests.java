import static org.junit.Assert.*;

import org.junit.Test;

import controller.collision.*;
import model.*;

public class CollisonTests {
	
	@Test
	public void testCollisionSelection() {
		SpaceObject one = new Debris(2, new Vector(100, 100), new Vector(1, 0));
		SpaceObject two = new Player(30, 30);
		Collision collision = new Collision();
		collision.setSObjects(one, two);
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		
		collision.selectCollisionType();
		
		assertTrue(collision.getCollisionType() instanceof CollisionPlayer_Debris);
		
//		assertEquals(CollisionPlayer_Debris.class, collision.getCollisionType().getClass());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullCollisionSelection() {
		SpaceObject one = new Debris(2, new Vector(0, 0), new Vector(1, 0));
		SpaceObject two = null;
		Collision collision = new Collision();
		collision.setSObjects(one, two);
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		
		collision.selectCollisionType();
		
		assertTrue(collision.getCollisionType() instanceof CollisionPlayer_Debris);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupportedCollisionSelection() {		
		SpaceObject one = new Planet(100, 0, 700, 300);
		SpaceObject two = new Moon(10, 0, new Vector(0,200), -3.0, one.getPositionVector().copy());
		Collision collision = new Collision();
		collision.setSObjects(one, two);
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		
		collision.selectCollisionType();
		
		assertTrue(collision.getCollisionType() instanceof CollisionPlayer_Debris);
	}
	
	@Test
	public void testPlayerPlayerCollisionSelection() {
		SpaceObject one = new Player(30, 30);
		SpaceObject two = new Player(30, 30);
		Collision collision = new Collision();
		collision.setSObjects(one, two);
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		
		collision.selectCollisionType();
		
		assertTrue(collision.getCollisionType() instanceof CollisionPlayer_Player);
	}
}