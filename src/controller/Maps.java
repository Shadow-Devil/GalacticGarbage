package controller;

import model.Debris;
import model.Moon;
import model.Planet;
import model.Player;
import model.SpaceObject;
import model.Vector;

import java.util.ArrayList;
import java.util.List;


public enum Maps{	//TODO bei getObj immer neue Liste erstellen!!
	//in controller da es nur als rexxourve für GameBoard benutzt wird
	EASY(1) {
		@Override
		public List<SpaceObject> getObjects() {	//alles außer Player
			List<SpaceObject> map = new ArrayList<>();
			map.add(new Player(30, 30));
			
			Planet p1 = new Planet(100, 0, 700, 300);
			map.add(p1);
			//Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet)
			map.add(new Moon(10, 0, new Vector(0,200), -2, p1.getPositionVector().copy()));
			
			
			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			List<Debris> baseDebris = new ArrayList<>();
			baseDebris.add(new Debris(200, 500));
			baseDebris.add(new Debris(200, 200));

			return baseDebris;
		}
	}, MEDIUM(2) {
		@Override
		public List<SpaceObject> getObjects() {	//alles außer Player
			List<SpaceObject> map = new ArrayList<>();
			map.add(new Player(30, 30));
			
			Planet p1 = new Planet(80, 0, 400, 300);
			map.add(p1);
			//Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet)
			map.add(new Moon(20, 0, new Vector(0,-270), -3.0, p1.getPositionVector().copy()));
			map.add(new Moon(10, 0, new Vector(150,0), -3.4, p1.getPositionVector().copy()));
			
			map.add(new Planet(30, 1, 800, 500));

			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			List<Debris> baseDebris = new ArrayList<>();
			baseDebris.add(new Debris(900, 200));
			baseDebris.add(new Debris(300, 500));
			baseDebris.add(new Debris(20, 20));

			return baseDebris;
		}
	}, HARD(4) {
		@Override
		public List<SpaceObject> getObjects(){
			List<SpaceObject> map = new ArrayList<>();
			map.add(new Player(0, 500));
			
			map.add(new Planet(40, 0, 900, 100));
			map.add(new Planet(40, 1, 900, 500));
			map.add(new Planet(50, 2, 400, 400));
			
			
			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			List<Debris> baseDebris = new ArrayList<>();
			baseDebris.add(new Debris(850, 300));
			baseDebris.add(new Debris(300, 200));
			baseDebris.add(new Debris(20, 20));
			
			
			return baseDebris;
		}
	};

	
	Maps(int maxDebris) {
		this.maxDebris = maxDebris*4;
	}
	
	public abstract List<SpaceObject> getObjects(); 
	public abstract List<Debris> getBaseDebris();
	private final int maxDebris;
	
	/**
	 * @return amount of (initial) maxSpaceObjects, diese wird erst im GameBoard incremented.
	 */
	public int getMaxDebris() {
		return maxDebris;
	}
	
	/**
	 * Sets the default number of maximum spaceObjects on the GameBoard.
	 * @return the current Map
	 */
	public static Maps chooseMap(int difficulty){
		switch (difficulty) {
			case 0:
				return Maps.EASY;
			case 1:
				return Maps.MEDIUM;
			case 2:
				return Maps.HARD;
			default:
				throw new IllegalArgumentException("Ausgewählte Difficulty gibt es nicht");
			}
		
	}
}
