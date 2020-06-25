package controller;

import model.Debris;
import model.Moon;
import model.Planet;
import model.SpaceObject;
import model.Vector;

import java.util.ArrayList;
import java.util.List;


public enum Maps{	//TODO bei getObj immer neue Liste erstellen!!
	EASY(1) {
		@Override
		public List<SpaceObject> getObjects() {	//alles außer Player
			List<SpaceObject> map = new ArrayList<>();
			
			Planet p1 = new Planet(100, 0, new Vector(700,300));
			map.add(p1);
			//Moon(int radius, int icon, Vector planetToMoonVector, double turnSpeed, Vector planet)
			map.add(new Moon(10, 0, new Vector(0,200), -3.0, p1.getPositionVector().copy()));


			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			//TODO Maps
			List<Debris> baseDebris = new ArrayList<>();
			baseDebris.add(new Debris(2, new Vector(500, 500), new Vector(-1, 0) , 2));


			return baseDebris;
		}
	}, MEDIUM(3) {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Maps
			List<SpaceObject> map = new ArrayList<>();
			
			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			// TODO Maps
			List<Debris> baseDebris = new ArrayList<>();
			
			return baseDebris;
		}
	}, HARD(4) {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Maps
			List<SpaceObject> map = new ArrayList<>();
			
			return map;
		}

		@Override
		public List<Debris> getBaseDebris(){
			// TODO Maps
			List<Debris> baseDebris = new ArrayList<>();
			
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
