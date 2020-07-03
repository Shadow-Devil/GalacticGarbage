import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Vector;

public class VectorTests{
	
	private Vector v1;
	private Vector v2;
	
	@Before
	public void setup() {
		v1 = new Vector(0, 0);
		v2 = new Vector(0, 0);
		
	}
	
	@Test
	public void testLength() {
		//wird verwendet um den collisionvektor auszurechnen
		v1.setXY(100, 200);
		v2.setXY(100, 300);
		double length = v2.sub(v1).getLength();
		
		assertEquals(100.0, length, 0);
	}
	
	@Test
	public void testLengthDiagonal() {
		v1.setXY(0, 3);
		v2.setXY(4, 0);
		double length = v2.sub(v1).getLength();
		
		assertEquals(5, length, 0);
	}
	
	@Test
	public void testDegree() {
		
	}
}
