package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import model.SpaceObject;
import model.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import controller.GameBoard;
import controller.Input;
import controller.Input2;
import controller.collision.Collision;

public class GameBoardUI extends Canvas implements Runnable {

	private static final int DEFAULT_WIDTH = 1000;
	private static final int DEFAULT_HEIGHT = 600;

	private static final Color backgroundColor = Color.BLACK;
	private static final int SLEEP_TIME = 1000 / 25; // this gives us 25fps

	private static final HashMap<SpaceObject, Image> spaceImages = new HashMap<>();

	// attribute inherited by the JavaFX Canvas class
	private final GraphicsContext graphicsContext = this.getGraphicsContext2D();

	// user interface objects
	private GameBoard gameBoard;
	private int width, height;
	private final Toolbar toolBar;

	private long starttime;

	private Socket sock;
	public ServerSocket serverSock;
	public boolean waitForCon;
	public boolean isServer;
	public boolean multiplayer;

	/**
	 * Sets up all attributes, starts the mouse steering and sets up all graphics
	 * 
	 * @param toolBar used to start and stop the game
	 */
	public GameBoardUI(Toolbar toolBar) {
		this.toolBar = toolBar;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		gameSetup(0);
	}

	public boolean setConnectionInHost() {
		try {
			int port = 25566;
			serverSock = new ServerSocket(port);
			sock = serverSock.accept();
			serverSock.close();
			isServer = true;
			this.multiplayer = true;
			System.out.println("Server gestartet, Player2 verbunden");
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	public boolean setConnectionInClient(String ip) {
		try {
			sock = new Socket(ip, 25566);
			isServer = true;
			this.multiplayer = true;
			return true;
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.INFORMATION, "Connection failed.");
			alert.setTitle("Fail");
			alert.setHeaderText("");

			alert.show();
			return false;
		}
	}
	
	private boolean exchangeInfoInHost(boolean toClose) {
		boolean end = false;
		try (BufferedReader in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
                PrintWriter out = new PrintWriter(sock.getOutputStream(),
                        true)) {
			out.println("start");
			String gState = in.readLine();
			String inp = in.readLine();
			
			end = getGameStateFromString(gState);
			getInputFromString(inp);
			
			out.println(buildSpaceObjectsString());
			out.println(buildGameStateString(toClose));
		} catch (IOException e1) {
		}
		if (toClose || end) {
			try {
				sock.close();
			} catch (IOException e) {
			}
		}
		return end;
	}
	
	private boolean exchangeInfoInClient(boolean toClose) {
		boolean end = false;
		try (BufferedReader in = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
                PrintWriter out = new PrintWriter(sock.getOutputStream(),
                        true)) {
			
			in.readLine();
			out.println(buildGameStateString(toClose));
			out.println(buildInputString());
			String sObj = in.readLine();
			String gState = in.readLine();
			
			getSpaceObjectsFromString(sObj);
			end = getGameStateFromString(gState);
		} catch (IOException e1) {
		}
		if (toClose || end) {
			try {
				sock.close();
			} catch (IOException e) {
			}
		}
		return end || toClose;
	}
	
	private String buildSpaceObjectsString() {
		List<SpaceObject> list = gameBoard.getSpaceObjects();
		StringBuilder ret = new StringBuilder("");
		list.forEach(sO -> {
			ret.append("" + sO.getRadius() + ";" + 
					sO.getIcon() + ";" + 
					sO.getPositionVector().getX() + ";" +
					sO.getPositionVector().getY() + ";" +
					sO.getFacingVector().getX() + ";" +
					sO.getFacingVector().getY() + "|"
					);
		});
		return ret.toString();
	}
	
	private void getSpaceObjectsFromString(String sObjects) {
		List<SpaceObject> list = new ArrayList<SpaceObject>();
		String[] arr = sObjects.split("|");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("|"))
				break;
			String[] arr2 = arr[i].split(";");
			
			int count = 0;
			for (int j = 0; j < arr2.length; j++) {
				if (arr2[j].equals(";"))
					break;
				count++;
			}
			String[] newArr2 = new String[count];
			int count2 = 0;
			for (int j = 0; j < arr2.length; j++) {
				if (arr2[j].equals(";"))
					break;
				newArr2[count2++] = arr2[j];
			}
			
			list.add(new SpaceObject(
					Integer.parseInt(newArr2[0]),
					newArr2[1],
					Double.parseDouble(newArr2[2]),
					Double.parseDouble(newArr2[3]),
					Double.parseDouble(newArr2[4]),
					Double.parseDouble(newArr2[5])
					));
			GameBoard.setSpaceObjects(list);
			gameBoard.getSpaceObjects().forEach((so -> spaceImages.put(so, getImage(so.getIcon()))));
		}
	}
	//wPressed, aPressed, sPressed, dPressed, spacePressed

	private String buildInputString() {
		return "" + Input.iswPressed() + "|" +
				Input.isaPressed() + "|" +
				Input.issPressed() + "|" +
				Input.isdPressed() + "|" +
				Input.isSpacePressed()
				;
	}
	
	private void getInputFromString(String inp) {
		String[] arr = inp.split("|");
		int count = 0;
		for (int j = 0; j < arr.length; j++) {
			if (arr[j].equals("|"))
				break;
			count++;
		}
		String[] newArr = new String[count];
		int count2 = 0;
		for (int j = 0; j < arr.length; j++) {
			if (arr[j].equals("|"))
				break;
			newArr[count2++] = arr[j];
		}
		
		Input2.setwPressed(Boolean.parseBoolean(newArr[0]));
		Input2.setwPressed(Boolean.parseBoolean(newArr[1]));
		Input2.setwPressed(Boolean.parseBoolean(newArr[2]));
		Input2.setwPressed(Boolean.parseBoolean(newArr[3]));
		Input2.setwPressed(Boolean.parseBoolean(newArr[4]));
	}
	
	private String buildGameStateString(boolean endGame) {
		return "" + gameBoard.isRunning() + "|" +
				gameBoard.hasEnded() + "|" +
				gameBoard.getScore() + "|" +
				endGame
				;
	}
	
	private boolean getGameStateFromString(String inp) {
		String[] arr = inp.split("|");
		int count = 0;
		for (int j = 0; j < arr.length; j++) {
			if (arr[j].equals("|"))
				break;
			count++;
		}
		String[] newArr = new String[count];
		int count2 = 0;
		for (int j = 0; j < arr.length; j++) {
			if (arr[j].equals("|"))
				break;
			newArr[count2++] = arr[j];
		}
		boolean isR = Boolean.parseBoolean(newArr[0]);
		boolean hasE = Boolean.parseBoolean(newArr[1]);
		String sc = newArr[2];
		boolean endG = Boolean.parseBoolean(newArr[3]);
		
		if (gameBoard.isRunning() && !isR)
			stopGame();
		if (!gameBoard.isRunning() && isR)
			startGame();
		if (hasE && !isServer) {
			showAsyncAlert("Oh.. you lost.\n" +
					"Your Score: " + sc);
			stopGame();
		}
		return endG;
	}

	/**
	 * Called after starting the game thread Constantly updates the game board and renders
	 * graphics
	 * 
	 * @see Runnable#run()
	 */
	@Override
	public void run(){
		starttime = System.currentTimeMillis();
		while (gameBoard.isRunning()){
			if (multiplayer && isServer) {
				boolean end = exchangeInfoInHost(false);
				if (end) {
					gameSetup(0);
					stopGame();
					return;
				}
			}
			// updates Spaceobjects positions and re-renders graphics
			gameBoard.updateSpaceObjects();
			// when this.gameBoard.hasWon() is null, do nothing
			if (gameBoard.hasEnded()){
				int sek = (int) ((System.currentTimeMillis() - starttime)/1000);
				int min = sek /60;
				sek = sek % 60;
				String time = "Time: " + min + " Min, " + sek + " Sek";
				
				FileWriter writer;
				File target = new File(GameBoardUI.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
				Path targetPath = target.toPath();
				Path scoresPath = targetPath.resolve("scores.txt");
				File scores = scoresPath.toFile();
				System.out.println(scores.getName());
				System.out.println(Files.isRegularFile(scoresPath));
				
				try {
					System.out.println("trying");
					/*
					writer = new FileWriter(scores, true);
					writer.write(gameBoard.getScore()+ "|" + time);
					writer.write(System.getProperty("line.seperator"));
					
					writer.flush();
					writer.close();*/
					List<String> stringList = new ArrayList<String>();
					stringList.add(""+gameBoard.getScore()+"|" + time);
					Files.write(scoresPath, stringList);
				} catch (IOException e) {
					System.out.println("Catch");
				} 
				
				showAsyncAlert("Oh.. you lost.\n" +
						"Your Score: " + gameBoard.getScore() + "\n" + 
						time);
				stopGame();
				exchangeInfoInHost(true);
			} 
			paint(graphicsContext);
			try{
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	} // end run()
	
	public void clientLoop() {
		while (true) {
			if (exchangeInfoInClient(false))
				break;
		}
	}

	/**
	 * Removes all existing spaceObjects from the game board and re-adds them.
	 * Status bar is set to default value. Player spaceship is reset to default
	 * starting position. Renders graphics.
	 */
	public void gameSetup(int difficulty) {
		if(multiplayer && !isServer) {
			exchangeInfoInClient(true);
			return;
		}
		gameBoard = new GameBoard(width, height, difficulty, new Collision(), multiplayer);
		widthProperty().set(width);
		heightProperty().set(height);
		width = (int) getWidth();
		height = (int) getHeight();
		spaceImages.clear();

		gameBoard.resetSpaceObjects();
		gameBoard.getSpaceObjects().forEach((so -> spaceImages.put(so, getImage(so.getIcon()))));
		paint(graphicsContext);
		toolBar.resetToolBarButtonStatus(false);
	}

	/**
	 * Sets the Spaceobjects image
	 *
	 * @param soImageFilePath: an image file path that needs to be available in the
	 *                         resources folder of the project
	 */
	private Image getImage(String soImageFilePath) { // TODO GameBoardUI - getImage()
		try {
			URL soImageUrl = getClass().getClassLoader().getResource(soImageFilePath);
			if (soImageUrl == null)
				throw new RuntimeException("Resource not found: " + soImageFilePath);

			InputStream inputStream = soImageUrl.openStream();
			return new Image(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
	 * which causes the spaceObjects to change their positions (i.e. move). Renders
	 * graphics and updates tool bar status.
	 */
	public void startGame() {
		if (!gameBoard.isRunning()) {
			gameBoard.startGame();

			// thread responsible for starting game
			if (!multiplayer || (multiplayer && isServer)) {
				Thread theThread = new Thread(this);
				theThread.start();
			}
			paint(graphicsContext);
			toolBar.resetToolBarButtonStatus(true);
		}
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame() {
		if (gameBoard.isRunning()) {
			gameBoard.stopGame();
			toolBar.resetToolBarButtonStatus(false);
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the spaceObjects
	 * of the game board at render each of them individually.
	 * 
	 * @param graphics used to draw changes
	 */
	private void paint(GraphicsContext graphics) {
		graphics.setFill(backgroundColor);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		for (SpaceObject so : gameBoard.getSpaceObjects())
			paintSpaceObject(so, graphics);

	}

	/**
	 * Show image of a spaceObject at the current position of the spaceObject.
	 * 
	 * @param so       SpaceObject to be drawn
	 * @param graphics used to draw changes
	 */
	private void paintSpaceObject(SpaceObject so, GraphicsContext graphics) {
		Vector canvasPosition = convertPosition(so.getPositionVector());
		// System.out.println(so);
		// TODO richtige drehung, vll. invertieren

		graphics.save(); // saves the current state on stack, including the current transform
		// graphics.rotate((double) (so.getDirectionVector().getDegree()));
		rotate(graphics, -(so.getFacingVector().getDegree()), canvasPosition.getX(), canvasPosition.getY());

		if (spaceImages.get(so) == null)
			spaceImages.put(so, getImage(so.getIcon()));

		graphics.drawImage(spaceImages.get(so), canvasPosition.getX() - so.getRadius(),
				canvasPosition.getY() - so.getRadius(), so.getRadius() * 2, so.getRadius() * 2);
		graphics.restore(); // back to original state (before rotation)
	}

	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	/**
	 * Converts position of spaceObject to position on the canvas
	 * 
	 * @param toConvert the point to be converted
	 */
	private Vector convertPosition(Vector toConvert) {
		return new Vector(toConvert.getX(), (int) (getHeight() - toConvert.getY()));
	}

	/**
	 * Method used to display alerts in updateSpaceObjects() Java 8 Lambda
	 * Functions: java 8 lambda function without arguments Platform.runLater
	 * Function:
	 * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html
	 *
	 * @param message you want to display as a String
	 */
	public void showAsyncAlert(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(message);
			alert.showAndWait();
			gameSetup(0);
		});
	}
	
	public static void main(String[] args) {
		Path path = FileSystems.getDefault().getPath("target", "scoresA.txt");
		try {
			Files.writeString(path, "\nNeuer next für scoresA.txt", StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("fertig geschrieben:");
		try {
			Files.readAllLines(path).stream().forEach(line -> System.out.println(line));;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
