import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.* ;
import static org.junit.Assert.* ;

import org.easymock.* ;
import org.junit.Test;
import org.junit.runner.RunWith;

import controller.GameBoard;
import controller.collision.Collision;
import controller.collision.CollisionInterface;
import controller.collision.Policy;
import model.Debris;
import model.Projectile;
import model.SpaceObject;
import model.Vector;
import model.baseProjectile;

@RunWith(EasyMockRunner.class)
public class CollisionMockTest {
	@Mock
	private CollisionInterface collisionMock;
	
	@TestSubject
	private GameBoard gameboard = new GameBoard(100, 100, 0, collisionMock);
	
	

    @Test
    public void testUpdateSpaceObjects(){
    	gameboard.addSpaceObjects();
    	GameBoard.maxDebris = 0;
    	List<SpaceObject> list = new ArrayList<SpaceObject>();
    	
    	SpaceObject one = new Debris(0, new Vector(500, 500), new Vector(-1, 0) , 2);
    	SpaceObject two = new baseProjectile(new Vector(30, 30), new Vector(70, 70));
    	
    	list.add(one);
    	list.add(two);
    	
    	GameBoard.setSpaceObjects(list);
    	
		expect(collisionMock.detectCollision()).andReturn(false);
		
		
		replay(collisionMock);
		
		assertEquals(2, gameboard.getSpaceObjects().size());
		// Fehler bei executeCollision und setSObjects
		gameboard.updateSpaceObjects();
		expectLastCall().andAnswer(new IAnswer() {
		    public Object answer() {
//		        //supply your mock implementation here...
//		        SomeClass arg1 = (SomeClass) getCurrentArguments()[0];
//		        AnotherClass arg2 = (AnotherClass) getCurrentArguments()[1];
//		        arg1.doSomething(blah);
//		        //return the value to be returned by the method (null for void)
		        return null;
		    }
		});
		
		assertEquals(0, gameboard.getSpaceObjects().size());
    }

}


