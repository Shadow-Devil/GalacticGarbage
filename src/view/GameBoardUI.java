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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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
	private ServerSocket serverSock;
	private boolean isServer;
	private boolean multiplayer;
	private boolean clientRunning;

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

	/**
	 * Attempts to establish a connection with the host
	 * 
	 * @return true if successful
	 */
	public boolean setConnectionInHost() {
		try {
			int port = 25566;
			serverSock = new ServerSocket(port);
			sock = serverSock.accept();
			serverSock.close();
			isServer = true;
			this.multiplayer = true;
			// System.out.println("Server gestartet, Player2 verbunden");
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * Attempts to establish a connection with the client
	 * 
	 * @param ip IP-address in form of a String
	 * @return true if successful
	 */
	public boolean setConnectionInClient(String ip) {
		try {
			sock = new Socket(ip, 25566);
			isServer = false;
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

	/**
	 * Reads and writes over socket with client
	 * 
	 * @param toClose boolean that states if connections should be closed
	 * @return true if successful
	 */
	private boolean exchangeInfoInHost(boolean toClose) {
		boolean end = false;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {

			out.println("start");
			String gState = in.readLine();
			String inp = in.readLine();

			end = getGameStateFromString(gState);
			getInputFromString(inp);

			out.println(buildSpaceObjectsString());
			out.println(buildGameStateString(toClose));
			// System.out.println("end host");
		} catch (IOException e1) {
			// System.out.println("catchHost");
		}
		if (toClose || end) {
			try {
				sock.close();
			} catch (IOException e) {
			}
		}
		return end;
	}

	/**
	 * Reads and writes over socket with host
	 * 
	 * @param toClose boolean that states if connections should be closed
	 * @return true if successful
	 */
	private synchronized boolean exchangeInfoInClient(boolean toClose) {
		boolean end = false;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {

			// System.out.println("client begin");
			in.readLine();
			out.println(buildGameStateString(toClose));
			out.println(buildInputString());

			String sObj = in.readLine();
			String gState = in.readLine();

			getSpaceObjectsFromString(sObj);
			end = getGameStateFromString(gState);
			// System.out.println("end client");
		} catch (IOException e1) {
			// System.out.println("catchClient");
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
		StringBuilder ret = new StringBuilder();
		list.forEach(sO -> {
			ret.append(sO.getRadius() + ";" + sO.getIcon() + ";" + sO.getPositionVector().getX() + ";"
					+ sO.getPositionVector().getY() + ";" + sO.getFacingVector().getX() + ";"
					+ sO.getFacingVector().getY() + "!");
		});
		return ret.toString();
	}

	private void getSpaceObjectsFromString(String sObjects) {
		List<SpaceObject> list = new ArrayList<SpaceObject>();
		String[] arr = sObjects.split("!");
		for (int i = 0; i < arr.length; i++) {
			String[] arr2 = arr[i].split(";");
			list.add(new SpaceObject(Integer.parseInt(arr2[0]), arr2[1], Double.parseDouble(arr2[2]),
					Double.parseDouble(arr2[3]), Double.parseDouble(arr2[4]), Double.parseDouble(arr2[5])));
		}
		GameBoard.setSpaceObjects(list);
		gameBoard.getSpaceObjects().forEach((so -> spaceImages.put(so, getImage(so.getIcon()))));
	}

	private String buildInputString() {
		return Input.iswPressed() + "!" + Input.isaPressed() + "!" + Input.issPressed() + "!" + Input.isdPressed() + "!"
				+ Input.isSpacePressed();
	}

	private void getInputFromString(String inp) {
		String[] arr = inp.split("!");
		Input2.setwPressed(Boolean.parseBoolean(arr[0]));
		Input2.setwPressed(Boolean.parseBoolean(arr[1]));
		Input2.setwPressed(Boolean.parseBoolean(arr[2]));
		Input2.setwPressed(Boolean.parseBoolean(arr[3]));
		Input2.setwPressed(Boolean.parseBoolean(arr[4]));
	}

	/**
	 * Builds a String with current game information
	 * 
	 * @param endGame boolean
	 * @return GameState in Form of a String (also contains endGame)
	 */
	private String buildGameStateString(boolean endGame) {
		return gameBoard.isRunning() + "!" + gameBoard.hasEnded() + "!" + gameBoard.getScore() + "!" + endGame;
	}

	/**
	 * Sets the GameState attributes in GameBoard from the input String
	 * 
	 * @param inp input String
	 * @return boolean endG, if the game should end
	 */
	private boolean getGameStateFromString(String inp) {
		String[] arr = inp.split("!");
		boolean isR = Boolean.parseBoolean(arr[0]);
		boolean hasE = Boolean.parseBoolean(arr[1]);
		int sc = Integer.parseInt(arr[2]);
		boolean endG = Boolean.parseBoolean(arr[3]);

		if (gameBoard.isRunning() && !isR)
			stopGame();
		if (!gameBoard.isRunning() && isR)
			startGame();
		if (hasE && !isServer) {
			showAsyncAlert("Oh.. you lost.\n" + "Your Score: " + sc);
			stopGame();
		}
		return endG;
	}

	/**
	 * Called after starting the game thread Constantly updates the game board and
	 * renders graphics
	 * 
	 * @see Runnable#run()
	 */
	@Override
	public void run() {
		starttime = System.currentTimeMillis();
		while (gameBoard.isRunning()) {
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
			// when this.gameBoard.hasEnded() is null, do nothing
			if (gameBoard.hasEnded()) {
				int sek = (int) ((System.currentTimeMillis() - starttime) / 1000);
				int min = sek / 60;
				sek = sek % 60;
				String time = "Time: " + min + " Min, " + sek + " Sek";

				try {
					updateScore(gameBoard.getScore(), time);
				} catch (Exception e) {
					System.out.println("Saving score unsuccessfull //outer");
				}

				showAsyncAlert("Oh.. you lost.\n" + "Your Score: " + gameBoard.getScore() + "\n" + time);
				stopGame();
				if (multiplayer && isServer)
					exchangeInfoInHost(true);
			}
			paint(graphicsContext);
			try {
				Thread.sleep(SLEEP_TIME); // milliseconds to sleep
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets clientRunning to true, clears spaceImages, resets toolBar, calls
	 * startGame() <br>
	 * As long as clientRunning, this method will try to paint the graphicsContext
	 */
	public void clientLoop() {
		clientRunning = true;
		spaceImages.clear();
		toolBar.resetToolBarButtonStatus(false);
		startGame();
		while (clientRunning) {
			if (exchangeInfoInClient(false)) {
				break;
			}
			paint(graphicsContext);
			// clientRunning = false;
		}
	}

	/**
	 * Checks score file, updates if necessary, score is saved in format "score |
	 * time"
	 * 
	 * @param score int value
	 * @param time  String
	 */
	public static void updateScore(int score, String time) {
		Path path = getScoresPath();
		if (path == null)
			return;
		try {
			List<String> lines = Files.readAllLines(path);
			lines.add(score + " | " + time);
			String msg = lines.stream().sorted((l1, l2) -> {
				int val1 = Integer.parseInt(l1.split(" \\| ")[0]);
				int val2 = Integer.parseInt(l2.split(" \\| ")[0]);
				return (int) Math.signum(val1 - val2) * (-1);
			}).limit(3).collect(Collectors.joining("\n"));
			Files.writeString(path, msg, StandardOpenOption.WRITE);
		} catch (IOException e) {
		}
	}

	/**
	 * @return Path of the scores.txt file, will attempt to create new file if it
	 *         cant find scores.txt
	 */
	public static Path getScoresPath() {
		Path path = Path.of("resources", "scores.txt");
		if (!Files.isRegularFile(path)) {
			path = Path.of("scores.txt");
		}
		if (!Files.isRegularFile(path)) {
			try {
				path.toFile().createNewFile();
			} catch (IOException e1) {
			}
			if (!Files.isRegularFile(path)) {
				return null;
			}
		}
		return path;
	}

	/**
	 * Removes all existing spaceObjects from the game board and re-adds them.
	 * Status bar is set to default value. Player spaceship is reset to default
	 * starting position. Renders graphics.
	 */
	public void gameSetup(int difficulty) {
		if (multiplayer && !isServer) {
			clientRunning = false;
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
	private Image getImage(String soImageFilePath) {
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

		graphics.save(); // saves the current state on stack, including the current transform
		rotate(graphics, -(so.getFacingVector().getDegree()), canvasPosition.getX(), canvasPosition.getY());

		if (spaceImages.get(so) == null)
			spaceImages.put(so, getImage(so.getIcon()));

		graphics.drawImage(spaceImages.get(so), canvasPosition.getX() - so.getRadius(),
				canvasPosition.getY() - so.getRadius(), so.getRadius() * 2, so.getRadius() * 2);
		graphics.restore(); // back to original state (before rotation)
	}

	/**
	 * Rotiert die Grafik gc um double angle gegen den Uhrzeigersinn
	 * 
	 * @param GraphicsContext gc
	 * @param double          angle
	 * @param double          px
	 * @param double          py
	 */
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

	public boolean isMultiplayer() {
		return multiplayer;
	}

	public void setMultiplayer(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}

	public ServerSocket getServerSock() {
		return serverSock;
	}
}