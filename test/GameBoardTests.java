import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.*;
import controller.collision.Collision;
import model.*;

public class GameBoardTests {
	GameBoard gameboard;
	
	
	@Before
	public void setup() {
		gameboard = new GameBoard(1000, 1000, 0, new Collision(), false);	//difficulty easy
		GameBoard.setSpaceObjects(new ArrayList<SpaceObject>());
		gameboard.addSpaceObjects();
	}
	
	@Test
	public void dieTest() {
		List<SpaceObject> obj = new ArrayList<SpaceObject>();
		Debris debris1 = new Debris(0, new Vector(100, 100), new Vector(1, 0));
		Debris debris2 = new Debris(0, new Vector(200, 200), new Vector(1, 0));
		obj.add(debris1);
		obj.add(debris2);
		GameBoard.setSpaceObjects(obj);
		int x = gameboard.getSpaceObjects().size();
		
		debris1.die(new Vector(1, 0));
		gameboard.updateSpaceObjects();
		assertEquals(x-1, gameboard.getSpaceObjects().size());
		
		debris2.die(new Vector(1, 0));
		gameboard.updateSpaceObjects();
		assertEquals(x-2, gameboard.getSpaceObjects().size());
	}
	
	@Test
	public void splitTest() {
		List<SpaceObject> obj = new ArrayList<SpaceObject>();
		Debris debris1 = new Debris(2, new Vector(200, 200), new Vector(1, 0));
		Debris debris2 = new Debris(2, new Vector(500, 500), new Vector(1, 0));
		obj.add(debris1);
		obj.add(debris2);
		GameBoard.setSpaceObjects(obj);
		int x = gameboard.getSpaceObjects().size();
		
		debris1.die(new Vector(1, 0));
		gameboard.updateSpaceObjects();
		assertEquals(x+1, gameboard.getSpaceObjects().size());
		
		debris2.die(new Vector(1, 0));
		gameboard.updateSpaceObjects();
		assertEquals(x+2, gameboard.getSpaceObjects().size());
	}
	
	@Test
	public void respawnTest() {
		gameboard.addSpaceObjects();
		
		gameboard.getSpaceObjects().removeIf(obj -> obj instanceof Debris);
		int x = gameboard.getSpaceObjects().size();
		
		gameboard.updateSpaceObjects();
		
		assertNotEquals(x, gameboard.getSpaceObjects().size());
	}
}
