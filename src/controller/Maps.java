package controller;

import model.Planet;
import model.SpaceObject;
import model.Vector;

import java.util.ArrayList;
import java.util.List;


public enum Maps{	//TODO bei getObj immer neue Liste erstellen!!
	EASY(6) {
		@Override
		public List<SpaceObject> getObjects(){
			List<SpaceObject> map = new ArrayList<SpaceObject>();
			
			map.add(new Planet(20, 0, new Vector(30, 30)));
			
			
			return map;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			//TODO Maps
			List<Vector> spawnPoints = new ArrayList<>();



			return spawnPoints;

		}
	}, MEDIUM(8) {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Maps
			return null;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Maps
			return null;
		}
	}, HARD(10) {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Maps
			return null;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Maps
			return null;
		}
	};

	
	Maps(int maxSpaceObjects) {
		this.maxSpaceObjects = maxSpaceObjects;
	}
	
	public abstract List<SpaceObject> getObjects();
	public abstract List<Vector> getSpawnPoints();
	private final int maxSpaceObjects;
	
	/**
	 * @return amount of (initial) maxSpaceObjects, diese wird erst im GameBoard incremented.
	 */
	public int getMaxSpaceObjects() {
		return maxSpaceObjects;
	}
}
