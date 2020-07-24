package view;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.Main;

public class Toolbar extends ToolBar {
	private final Main gameWindow;
	private final Button start;
	private final Button stop;

	private final Button scores;

	public Toolbar(Main gameWindow) {
		this.start = new Button("Start");
		this.stop = new Button("Stop");
		this.scores = new Button("Scores");

		initActions();
		this.getItems().addAll(start, new Separator(), stop, new Separator(), scores);
		this.gameWindow = gameWindow;
	}

	/**
	 * Initialises the actions
	 */
	@SuppressWarnings("deprecation")
	private void initActions() {
		this.start.setOnAction(event -> {
			gameWindow.gameBoardUI.stopGame();
			gameWindow.gameBoardUI.setMultiplayer(false);

			ButtonType SINGLEPLAYER = new ButtonType("Singleplayer", ButtonBar.ButtonData.OK_DONE);
			ButtonType MULTIPLAYER = new ButtonType("Multiplayer", ButtonBar.ButtonData.OK_DONE);
			ButtonType CANCELMODE = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

			Alert alertMode = new Alert(AlertType.INFORMATION, "Mode?", SINGLEPLAYER, MULTIPLAYER, CANCELMODE);
			alertMode.setTitle("Mode");
			alertMode.setHeaderText("");

			Optional<ButtonType> resultMode = alertMode.showAndWait();
			if (resultMode.get() == SINGLEPLAYER) {
				chooseMap();
			} else if (resultMode.get() == MULTIPLAYER) {
				ButtonType HOST = new ButtonType("Host", ButtonBar.ButtonData.OK_DONE);
				ButtonType CLIENT = new ButtonType("Client", ButtonBar.ButtonData.OK_DONE);
				ButtonType CANCELCONNECTION = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

				Alert alertConnection = new Alert(AlertType.INFORMATION, "Connection?", HOST, CLIENT, CANCELCONNECTION);
				alertConnection.setTitle("Connection");
				alertConnection.setHeaderText("");

				Optional<ButtonType> resultConnection = alertConnection.showAndWait();
				if (resultConnection.get() == HOST) {
					ButtonType CANCELWAIT = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);

					Alert alertWait = new Alert(AlertType.INFORMATION, "Wait for Connection.", CANCELWAIT);
					alertWait.setTitle("Wait");
					alertWait.setHeaderText("");

					Runnable task = () -> {
						gameWindow.gameBoardUI.setConnectionInHost();
						Platform.runLater(() -> {
							alertWait.close();
						});
					};

					Thread thread = new Thread(task);
					thread.start();
					Optional<ButtonType> resultWait = alertWait.showAndWait();
					if (resultWait.isPresent() && resultWait.get() == CANCELWAIT) {
						thread.stop();
						try {
							ServerSocket s = gameWindow.gameBoardUI.getServerSock();
							if (s != null && !s.isClosed()) {
								s.close();
							}
						} catch (IOException e) {
						}
					} else if (gameWindow.gameBoardUI.isMultiplayer()) {
						chooseMap();
					} else {
						thread.stop();
						try {
							ServerSocket s = gameWindow.gameBoardUI.getServerSock();
							if (s != null && !s.isClosed()) {
								s.close();
							}
						} catch (IOException e) {
						}
					}
				} else if (resultConnection.get() == CLIENT) {
					TextInputDialog txt = new TextInputDialog();
					txt.setTitle("Adress");
					txt.setHeaderText("IP:");

					Optional<String> resultText = txt.showAndWait();
					if (resultText.isPresent()) {
						boolean connected = gameWindow.gameBoardUI.setConnectionInClient(resultText.get());
						if (connected) {
							gameWindow.gameBoardUI.clientLoop();
						}
					}
				}
			}
		});

		// start without extra multiplayer button:
//        this.start.setOnAction(event -> {
//        	gameWindow.gameBoardUI.stopGame();
//        	chooseMap();
//        });

		this.stop.setOnAction(event -> {
			gameWindow.gameBoardUI.stopGame();

			ButtonType YES = new ButtonType("Yes", ButtonBar.ButtonData.YES);
			ButtonType NO = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

			Alert alertStop = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game?", YES, NO);
			alertStop.setTitle("Stop Game Confirmation");
			alertStop.setHeaderText("");

			Optional<ButtonType> resultStop = alertStop.showAndWait();
			if (resultStop.get() == YES) {
				gameWindow.gameBoardUI.gameSetup(0);
			} else {
				gameWindow.gameBoardUI.startGame();
			}
		});

		this.scores.setOnAction(event -> {
			Path path = GameBoardUI.getScoresPath();
			if (path == null)
				return;
			List<String> lines = new ArrayList<String>();
			try {
				lines = Files.readAllLines(path);
			} catch (IOException e) {
				return;
			}
			Alert alertScores = new Alert(AlertType.INFORMATION, lines.stream().collect(Collectors.joining("\n")));
			alertScores.setTitle("Scores");
			alertScores.setHeaderText("");
			alertScores.showAndWait();
		});
	}

	private void chooseMap() {
		ButtonType EASY = new ButtonType("Easy", ButtonBar.ButtonData.OK_DONE);
		ButtonType MEDIUM = new ButtonType("Medium", ButtonBar.ButtonData.OK_DONE);
		ButtonType HARD = new ButtonType("Hard", ButtonBar.ButtonData.OK_DONE);
		ButtonType CANCELCHOOSE = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

		Alert alertChooseDifficulty = new Alert(AlertType.INFORMATION, "Which difficulty do you want to play?", EASY,
				MEDIUM, HARD, CANCELCHOOSE);
		alertChooseDifficulty.setTitle("Choose Difficulty");
		alertChooseDifficulty.setHeaderText("");

		Optional<ButtonType> resultChooseDifficulty = alertChooseDifficulty.showAndWait();
		if (resultChooseDifficulty.get() == CANCELCHOOSE) {
			gameWindow.gameBoardUI.stopGame();
			return;
		}
		if (resultChooseDifficulty.get() == MEDIUM) {
			gameWindow.gameBoardUI.gameSetup(1);
		} else if (resultChooseDifficulty.get() == HARD) {
			gameWindow.gameBoardUI.gameSetup(2);
		} else {
			gameWindow.gameBoardUI.gameSetup(0);
		}
		gameWindow.gameBoardUI.startGame();
	}

	/**
	 * Resets the toolbar button status
	 * 
	 * @param running Used to disable/enable buttons
	 */
	public void resetToolBarButtonStatus(boolean running) {
		this.start.setDisable(running);
		this.stop.setDisable(!running);
		this.scores.setDisable(running);
	}

}