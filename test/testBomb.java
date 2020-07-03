import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import controller.GameBoard;
import controller.collision.*;
import model.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;

@RunWith(EasyMockRunner.class) 
public class testBomb {
	
	@TestSubject
	private GameBoard game = new GameBoard(1000, 800, 0);
    
    @Mock
    private Collision collision;
    
    @Mock
    private Projectile bomb;
    
    @Test
    public void testBomb() {
    	List<SpaceObject> obj = new ArrayList<SpaceObject>();
    	Debris debris = new Debris(2, new Vector(500, 500), new Vector(-1, 0) , 2);
    	obj.add(debris);
    	obj.add(new Planet(100, 0, new Vector(700,300)));
    	GameBoard.setSpaceObjects(obj);
    	
    	expect(game.getSpaceObjects().contains(debris)).andReturn(false);
    }
    
}