import static org.junit.Assert.*;

import org.junit.Test;

import controller.GameBoard;
import controller.collision.Collision;
import controller.collision.Policy;
import model.Debris;
import model.Player;
import model.SpaceObject;
import model.Vector;

public class BouceTest{

	@Test
	public void test(){
		GameBoard.newGameborders(400, 400);
		
//		SpaceObject one = new Player();
//		one.getPositionVector().setXY(100, 100);
		SpaceObject one = new Debris(0, new Vector(100, 100), new Vector(1, 0), 2);
		
		SpaceObject two = new Debris(0, new Vector(110, 100), new Vector(-1, 0), 2);
		

		Collision collision = new Collision(one, two);
		
		assertTrue(collision.detectCollision());
		Policy policy = new Policy(collision);
		policy.selectStrategy();
		collision.collide();
		
		System.out.println(one.getPositionVector());
		System.out.println(two.getPositionVector());
	}

}