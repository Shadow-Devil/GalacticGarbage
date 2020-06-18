package controller;

import model.Planet;
import model.SpaceObject;
import model.Vector;

import java.util.ArrayList;
import java.util.List;

public enum Maps{
	EASY {
		List<SpaceObject> map = new ArrayList<>();
		@Override
		public List<SpaceObject> getObjects(){

			
			map.add(new Planet(20, 0, new Vector(30, 30)));
			
			
			return map;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Auto-generated method stub
			List<Vector> spawnPoints = new ArrayList<>();



			return spawnPoints;
		}
	}, MEDIUM {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Auto-generated method stub
			return null;
		}
	}, HARD {
		@Override
		public List<SpaceObject> getObjects(){
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Auto-generated method stub
			return null;
		}
	};


	public abstract List<SpaceObject> getObjects();
	public abstract List<Vector> getSpawnPoints();
}
