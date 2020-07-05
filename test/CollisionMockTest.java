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
import model.SpaceObject;
import model.Vector;

@RunWith(EasyMockRunner.class)
public class CollisionMockTest {
	@Mock
	private CollisionInterface collisionMock;
	
	@TestSubject
	private GameBoard gameboard = new GameBoard(100, 100, 0, collisionMock);
	
	

    @Test
    public void testUpdateSpaceObjects(){
    	gameboard.addSpaceObjects();
    	List<SpaceObject> list = new ArrayList<SpaceObject>();
    	
    	
    	GameBoard.setSpaceObjects(list);
    	
		expect(collisionMock.detectCollision()).andReturn(true);
		replay(collisionMock);

		gameboard.updateSpaceObjects();
    }

}


