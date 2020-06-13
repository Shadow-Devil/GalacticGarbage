package controller;

import java.util.ArrayList;
import java.util.List;

import model.Planet;
import model.SpaceObject;
import model.Vector;

public enum Maps{
	EASY {
		@Override
		public List<SpaceObject> getObjects(){
			List<SpaceObject> map = new ArrayList<>();
			
			map.add(new Planet(20, 0, new Vector(30, 30)));
			
			
			return null;
		}

		@Override
		public List<Vector> getSpawnPoints(){
			// TODO Auto-generated method stub
			return null;
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
