import controller.Main;

public class GalacticGarbage {
	
	/**
	 * Bis jetzt implementiert:
	 * 	Bewegung des Players mit W,A,S,D
	 * 	Shoot mit E (Da Space StopButton aufruft)
	 * 	Große und mittlere Debris gehen durch shoot kaputt, 
	 * 		beim colliden mit Player bekommt dieser Schaden
	 * 	Kleine Debris gehen durch Monde/Planeten kaputt
	 * 	Alle Maps implementiert
	 * 	Score zählt kleinen Debris, die in Mode/Planeten geschoben wurden
	 *  Spielzeit ausgegeben
	 *  Maps verbessert
	 *  Highscore wird in neu kreiertem(wenn nicht existent) scores.txt abgespeichert
	 *    ansonsten in dem existierenden
	 *  
	 * 
	 * TODO:
	 * 	Multiplayer fixen.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//This is a workaround for a known issue when starting JavaFX applications
		Main.startApp(args);
	}
}
