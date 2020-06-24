import controller.Main;

public class GalacticGarbage {
	
	/**
	 * Bis jetzt implementiert:
	 * 	Bewegung des Players mit W,A,S,D
	 * 	Shoot mit E (Da Space Button aufruft)
	 * 	Große und mittlere Debris gehen durch shoot kaputt, 
	 * 		beim colliden mit Player bekommt dieser Schaden
	 * 	Kleine Debris gehen durch Monde/Planeten kaputt
	 * 	Nur Easy Map implementiert
	 * 	Score zählt kleinen Debris, die in Mode/Planeten geschoben wurden
	 * 
	 * TODO:
	 * 	Collisionen fixen
	 * 	Verschiedene Maps erstellen
	 * 	Beschleunigung des Players
	 * 	Input durchgehend shooten aus
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//This is a workaround for a known issue when starting JavaFX applications
		Main.startApp(args);
	}
}
