import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.* ;
import org.easymock.* ;
import org.junit.Test;
import org.junit.runner.RunWith;

import controller.GameBoard;
import controller.collision.CollisionInterface;
import model.Debris;
import model.Projectile;
import model.SpaceObject;
import model.Vector;

@RunWith(EasyMockRunner.class)
public class CollisionMockTest {
	@Mock
	private CollisionInterface collisionMock;
	
	@TestSubject
	private GameBoard gameboard = new GameBoard(100, 100, 0, collisionMock);
	
	

    @Test
    public void testCollision(){
    	gameboard.addSpaceObjects();
    	GameBoard.maxDebris = 0;
    	List<SpaceObject> list = new ArrayList<SpaceObject>();
    	
    	SpaceObject one = new Debris(0, new Vector(30, 30), new Vector(-1, 0) , 2);
    	SpaceObject two = new Projectile(new Vector(30, 30), new Vector(70, 70));
    	
    	list.add(one);
    	list.add(two);
    	
    	GameBoard.setSpaceObjects(list);
    	
    	//Es gibt immer eine Collision
		expect(collisionMock.detectCollision()).andReturn(true);
		
		//Erzeugt ein Stub, welches die SelectCollisionType Methode als leere Methode simuliert
		collisionMock.selectCollisionType();
		expectLastCall().asStub();
		
		//Erzeugt ein Stub, welches die setSObjects Methode als leere Methode simuliert
		collisionMock.setSObjects(one, two);
		expectLastCall().asStub();
		
		//Simuliert die executeCollision Methode, dass beide Spaceobjecte sterben
		collisionMock.executeCollision();
		expectLastCall().andAnswer(() -> {
		    one.die();
			two.die();
			return null;
		});
		
		
		replay(collisionMock);
		
		
		assertEquals(2, gameboard.getSpaceObjects().size());
		
		gameboard.updateSpaceObjects();
		assertEquals(0, gameboard.getSpaceObjects().size());
    }
    
    @Test
    public void testNoCollision(){
    	gameboard.addSpaceObjects();
    	GameBoard.maxDebris = 0;
    	List<SpaceObject> list = new ArrayList<SpaceObject>();
    	
    	SpaceObject one = new Debris(0, new Vector(30, 30), new Vector(-1, 0) , 2);
<<<<<<< HEAD
    	SpaceObject two = new baseProjectile(new Vector(300, 300), new Vector(70, 70));
=======
    	SpaceObject two = new Projectile(new Vector(30, 30), new Vector(70, 70));
>>>>>>> origin/master
    	
    	list.add(one);
    	list.add(two);
    	
    	GameBoard.setSpaceObjects(list);
    	
    	//Es gibt keine Collision
		expect(collisionMock.detectCollision()).andReturn(false);
		
		//Erzeugt ein Stub, welches die SelectCollisionType Methode als leere Methode simuliert
		collisionMock.selectCollisionType();
		expectLastCall().asStub();
		
		//Erzeugt ein Stub, welches die setSObjects Methode als leere Methode simuliert
		collisionMock.setSObjects(one, two);
		expectLastCall().asStub();
		
		//Simuliert die executeCollision Methode, dass beide Spaceobjecte sterben
		collisionMock.executeCollision();
		expectLastCall().andAnswer(() -> {
		    one.die();
			two.die();
			return null;
		});
		
		
		replay(collisionMock);
		
		
		assertEquals(2, gameboard.getSpaceObjects().size());
		
		gameboard.updateSpaceObjects();
		assertEquals(2, gameboard.getSpaceObjects().size());
    }

}


