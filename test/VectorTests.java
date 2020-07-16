import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
		//1,0 - rechts - deg=0
		//0,1 - oben - deg=90
		//-1,0 - links - deg=180
		//0,-1 - unten - deg=270
		
		v1.setXY(1, 0);
		assertEquals(0, v1.getDegree(), 0);
		
		v1.setXY(0, 1);
		assertEquals(90, v1.getDegree(), 0);
		
		v1.setXY(-1, 0);
		assertEquals(180, v1.getDegree(), 0);
		
		v1.setXY(0, -1);
		assertEquals(270, v1.getDegree(), 0);
	}
	
	@Test
	public void testTurn() {
		v1.setXY(0, 1);
		//System.out.println(v1);
		assertEquals(90, v1.getDegree(), 0.01);
		
		v1.turnUnitVector(0);
		//System.out.println(v1);
		assertEquals(90, v1.getDegree(), 0.01);
		
		v1.turnUnitVector(90);
		//System.out.println(v1);
		assertEquals(180, v1.getDegree(), 0.01);
	}
	
	
}
