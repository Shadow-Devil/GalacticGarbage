import static org.junit.Assert.*;

import org.junit.Test;

import controller.collision.*;
import model.*;

public class CollisonTests {
	
	@Test
	public void testCollisionSelection() {
		SpaceObject one = new Debris(0, null, null, 0);
		SpaceObject two = new Player();
		Collision collision = new Collision(one, two);
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		
		collision.selectCollisionType();
		
		assertTrue(collision.getCollisionType() instanceof CollisionPlayer_Debris);
		
//		assertEquals(CollisionPlayer_Debris.class, collision.getCollisionType().getClass());
	}
}