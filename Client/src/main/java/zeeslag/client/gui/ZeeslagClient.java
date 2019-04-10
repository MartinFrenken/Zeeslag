/*
 * Sea Battle Start project.
 */
package zeeslag.client.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeeslag.client.game.SpectatorGame;
import zeeslag.client.game.ZeeslagGame;
import zeeslag.client.game.ZeeslagGameImpl;
import zeeslag.shared.HitType;
import zeeslag.shared.ShipType;

import java.util.Arrays;
import java.util.Objects;

/**
 * Main application of the sea battle game.
 *
 * @author Nico Kuijpers
 */
@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "SpellCheckingInspection", "Duplicates"})
public class ZeeslagClient extends Application implements ZeeslagGui {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagClient.class);
    private int playerNr = 0;
    private Button buttonPlaceAllShips;
    private Button buttonRemoveAllShips;
    private Button buttonReadyToPlay;
    private Button buttonStartNewGame;
    private Button buttonPlaceAircraftCarrier;
    private Button buttonPlaceBattleShip;
    private Button buttonPlaceCruiser;
    private Button buttonPlaceSubmarine;
    private Button buttonPlaceMineSweeper;
    private Button buttonRemoveShip;
    private String opponentName;
    private Rectangle[][] squaresTargetArea;
    @Nullable
    private String playerName = null;
    private int playerTurn = 0;
    private Label labelPlayerName;
    private Label labelYourName;
    private TextField textFieldPlayerName;
    private Label labelYourPassword;
    private PasswordField passwordFieldPlayerPassword;
    private Rectangle[][] squaresOceanArea;
    private ZeeslagGame game;
    private boolean singlePlayerMode = true;
    private RadioButton radioSinglePlayer;
    private RadioButton radioMultiPlayer;
    private boolean playingMode = false;
    private boolean gameEnded = false;
    private boolean horizontal = true;
    private Label labelHorizontalVertical;
    private RadioButton radioHorizontal;
    private RadioButton radioVertical;
    private Button buttonLoginPlayer;
    private boolean squareSelectedInOceanArea = false;
    private int selectedSquareX;
    private int selectedSquareY;
    private static boolean isSpectatorMode;
    private static String host;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var list = Arrays.asList(args);
        isSpectatorMode = list.contains("-s");

        var index = list.indexOf("-h");
        if (index == -1 || index > list.size())
            host = "localhost:3000";
        else
            host = list.get(index + 1);
        
        launch(args);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {

        log.info("Seabattle started");

        // Define grid pane
        GridPane grid;
        grid = new GridPane();
        int BORDERSIZE = 10;
        grid.setHgap(BORDERSIZE);
        grid.setVgap(BORDERSIZE);
        grid.setPadding(new Insets(BORDERSIZE, BORDERSIZE, BORDERSIZE, BORDERSIZE));

        // For debug purposes
        // Make de grid lines visible
        // grid.setGridLinesVisible(true);

        Group root = new Group();
        int BUTTONWIDTH = 180;
        int AREAWIDTH = 400;
        Scene scene = new Scene(root, AREAWIDTH + BUTTONWIDTH + 3 * BORDERSIZE, 2 * AREAWIDTH + 2 * BORDERSIZE + 65);
        root.getChildren().add(grid);

        // Label for opponent's name
        opponentName = "Opponent";
        Label labelOpponentName = new Label(opponentName + "\'s grid");
        labelOpponentName.setMinWidth(AREAWIDTH);
        grid.add(labelOpponentName, 0, 0, 1, 2);

        // Target area, a 10 x 10 grid where the opponent's ships are placed
        Rectangle targetArea = new Rectangle(BORDERSIZE, 3 * BORDERSIZE, AREAWIDTH, AREAWIDTH);
        targetArea.setFill(Color.WHITE);
        root.getChildren().add(targetArea);

        // Create 10 x 10 squares for the target area
        int NRSQUARESVERTICAL = 10;
        int NRSQUARESHORIZONTAL = 10;
        squaresTargetArea = new Rectangle[NRSQUARESHORIZONTAL][NRSQUARESVERTICAL];
        int SQUAREHEIGHT = 36;
        int SQUAREWIDTH = 36;
        for (int i = 0; i < NRSQUARESHORIZONTAL; i++) {
            for (int j = 0; j < NRSQUARESVERTICAL; j++) {
                double x = targetArea.getX() + i * (AREAWIDTH / NRSQUARESHORIZONTAL) + 2;
                double y = targetArea.getY() + j * (AREAWIDTH / NRSQUARESVERTICAL) + 2;
                Rectangle rectangle = new Rectangle(x, y, SQUAREWIDTH, SQUAREHEIGHT);
                rectangle.setArcWidth(10.0);
                rectangle.setArcHeight(10.0);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setVisible(true);
                final int xpos = i;
                final int ypos = j;
                rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        event -> rectangleTargetAreaMousePressed(xpos, ypos));
                squaresTargetArea[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }

        // Label for player's name
        playerName = "";
        labelPlayerName = new Label("Your grid");
        labelPlayerName.setMinWidth(AREAWIDTH);
        grid.add(labelPlayerName, 0, 33, 1, 2);

        // Ocean area, a 10 x 10 grid where the player's ships are placed
        Rectangle oceanArea = new Rectangle(BORDERSIZE, 46 * BORDERSIZE, AREAWIDTH, AREAWIDTH);
        oceanArea.setFill(Color.WHITE);
        root.getChildren().add(oceanArea);

        // Create 10 x 10 squares for the ocean area
        squaresOceanArea = new Rectangle[NRSQUARESHORIZONTAL][NRSQUARESVERTICAL];
        for (int i = 0; i < NRSQUARESHORIZONTAL; i++) {
            for (int j = 0; j < NRSQUARESVERTICAL; j++) {
                double x = oceanArea.getX() + i * (AREAWIDTH / NRSQUARESHORIZONTAL) + 2;
                double y = oceanArea.getY() + j * (AREAWIDTH / NRSQUARESVERTICAL) + 2;
                Rectangle rectangle = new Rectangle(x, y, SQUAREWIDTH, SQUAREHEIGHT);
                rectangle.setArcWidth(10.0);
                rectangle.setArcHeight(10.0);
                rectangle.setStroke(Color.BLACK);
                rectangle.setFill(Color.LIGHTBLUE);
                rectangle.setVisible(true);
                final int xpos = i;
                final int ypos = j;
                rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED,
                        event -> rectangleOceanAreaMousePressed(xpos, ypos));
                squaresOceanArea[i][j] = rectangle;
                root.getChildren().add(rectangle);
            }
        }

        // Text field to set the player's name
        labelYourName = new Label("Your name:");
        grid.add(labelYourName, 1, 2, 1, 2);
        textFieldPlayerName = new TextField();
        textFieldPlayerName.setMinWidth(BUTTONWIDTH);
        textFieldPlayerName.setOnAction(event -> {
            playerName = textFieldPlayerName.getText();
            labelPlayerName.setText(playerName + "\'s grid");
        });
        grid.add(textFieldPlayerName, 1, 4, 1, 2);

        // Text field to set the player's password
        labelYourPassword = new Label("Your password:");
        grid.add(labelYourPassword, 1, 6, 1, 2);
        passwordFieldPlayerPassword = new PasswordField();
        passwordFieldPlayerPassword.setMinWidth(BUTTONWIDTH);
        grid.add(passwordFieldPlayerPassword, 1, 8, 1, 2);

        // Radio buttons to choose between single-player and multi-player mode
        radioSinglePlayer = new RadioButton("single-player mode");
        Tooltip tooltipSinglePlayer = new Tooltip("Play game in single-player mode");
        radioSinglePlayer.setTooltip(tooltipSinglePlayer);
        radioSinglePlayer.setOnAction((EventHandler) event -> singlePlayerMode = true);
        radioMultiPlayer = new RadioButton("multi-player mode");
        Tooltip tooltipMultiPlayer = new Tooltip("Play game in multi-player mode");
        radioMultiPlayer.setTooltip(tooltipMultiPlayer);
        radioMultiPlayer.setOnAction((EventHandler) event -> singlePlayerMode = false);
        ToggleGroup tgSingleMultiPlayer = new ToggleGroup();
        radioSinglePlayer.setToggleGroup(tgSingleMultiPlayer);
        radioMultiPlayer.setToggleGroup(tgSingleMultiPlayer);
        radioSinglePlayer.setSelected(true);
        grid.add(radioSinglePlayer, 1, 10, 1, 2);
        grid.add(radioMultiPlayer, 1, 12, 1, 2);

        // Button to login the player
        buttonLoginPlayer = new Button("Login");
        buttonLoginPlayer.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipLoginParticipant =
                new Tooltip("Press this button to login as player");
        buttonLoginPlayer.setTooltip(tooltipLoginParticipant);
        buttonLoginPlayer.setOnAction(
                (EventHandler) event -> {
                    try {
                        loginPlayer();
                    } catch (Exception e) {
                        log.error("Login Player error: {}", e.getMessage());
                    }
                });
        grid.add(buttonLoginPlayer, 1, 14, 1, 3);

        // Button to place the player's ships automatically
        buttonPlaceAllShips = new Button("Place ships for me");
        buttonPlaceAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceShips =
                new Tooltip("Press this button to let the computer place your ships");
        buttonPlaceAllShips.setTooltip(tooltipPlaceShips);
        buttonPlaceAllShips.setOnAction((EventHandler) event -> placeShipsAutomatically());
        buttonPlaceAllShips.setDisable(true);
        grid.add(buttonPlaceAllShips, 1, 18, 1, 3);

        // Button to remove the player's ships that are already placed
        buttonRemoveAllShips = new Button("Remove all my ships");
        buttonRemoveAllShips.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveAllShips =
                new Tooltip("Press this button to remove all your ships");
        buttonRemoveAllShips.setTooltip(tooltipRemoveAllShips);
        buttonRemoveAllShips.setOnAction((EventHandler) event -> removeAllShips());
        buttonRemoveAllShips.setDisable(true);
        grid.add(buttonRemoveAllShips, 1, 22, 1, 3);

        // Button to notify that the player is ready to start playing
        buttonReadyToPlay = new Button("Ready to play");
        buttonReadyToPlay.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipReadyToPlay =
                new Tooltip("Press this button when you are ready to play");
        buttonReadyToPlay.setTooltip(tooltipReadyToPlay);
        buttonReadyToPlay.setOnAction((EventHandler) event -> notifyWhenReady());
        buttonReadyToPlay.setDisable(true);
        grid.add(buttonReadyToPlay, 1, 26, 1, 3);

        // Button to start a new game
        buttonStartNewGame = new Button("Start new game");
        buttonStartNewGame.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipStartNewGame =
                new Tooltip("Press this button to start a new game");
        buttonStartNewGame.setTooltip(tooltipStartNewGame);
        buttonStartNewGame.setOnAction((EventHandler) event -> startNewGame());
        buttonStartNewGame.setDisable(true);
        grid.add(buttonStartNewGame, 1, 30, 1, 3);

        // Radio buttons to place ships horizontally or vertically
        labelHorizontalVertical = new Label("Place next ship: ");
        radioHorizontal = new RadioButton("horizontally");
        Tooltip tooltipHorizontal = new Tooltip("Place next ship horizontally");
        radioHorizontal.setTooltip(tooltipHorizontal);
        radioHorizontal.setOnAction((EventHandler) event -> horizontal = true);
        radioVertical = new RadioButton("vertically");
        Tooltip tooltipVertical = new Tooltip("Place next ship vertically");
        radioVertical.setTooltip(tooltipVertical);
        radioVertical.setOnAction((EventHandler) event -> horizontal = false);
        ToggleGroup tgHorizontalVertical = new ToggleGroup();
        radioHorizontal.setToggleGroup(tgHorizontalVertical);
        radioVertical.setToggleGroup(tgHorizontalVertical);
        radioHorizontal.setSelected(true);
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        grid.add(labelHorizontalVertical, 1, 36, 1, 2);
        grid.add(radioHorizontal, 1, 38, 1, 2);
        grid.add(radioVertical, 1, 40, 1, 2);

        // Button to place aircraft carrier on selected square
        buttonPlaceAircraftCarrier = new Button("Place aircraft carrier (5)");
        buttonPlaceAircraftCarrier.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceAircraftCarrier =
                new Tooltip("Press this button to place the aircraft carrier on the selected square");
        buttonPlaceAircraftCarrier.setTooltip(tooltipPlaceAircraftCarrier);
        buttonPlaceAircraftCarrier.setOnAction((EventHandler) event -> placeShipAtSelectedSquare(ShipType.AIRCRAFT_CARRIER, horizontal));
        buttonPlaceAircraftCarrier.setDisable(true);
        grid.add(buttonPlaceAircraftCarrier, 1, 42, 1, 3);

        // Button to place battle ship on selected square
        buttonPlaceBattleShip = new Button("Place battle ship (4)");
        buttonPlaceBattleShip.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceBattleShip =
                new Tooltip("Press this button to place the battle ship on the selected square");
        buttonPlaceBattleShip.setTooltip(tooltipPlaceBattleShip);
        buttonPlaceBattleShip.setOnAction((EventHandler) event -> placeShipAtSelectedSquare(ShipType.BATTLESHIP, horizontal));
        buttonPlaceBattleShip.setDisable(true);
        grid.add(buttonPlaceBattleShip, 1, 46, 1, 3);

        // Button to place battle ship on selected square
        buttonPlaceCruiser = new Button("Place cruiser (3)");
        buttonPlaceCruiser.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceCruiser =
                new Tooltip("Press this button to place the cruiser on the selected square");
        buttonPlaceCruiser.setTooltip(tooltipPlaceCruiser);
        buttonPlaceCruiser.setOnAction((EventHandler) event -> placeShipAtSelectedSquare(ShipType.CRUISER, horizontal));
        buttonPlaceCruiser.setDisable(true);
        grid.add(buttonPlaceCruiser, 1, 50, 1, 3);

        // Button to place mine sweeper on selected square
        buttonPlaceSubmarine = new Button("Place submarine (3)");
        buttonPlaceSubmarine.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceSubmarine =
                new Tooltip("Press this button to place the submarine on the selected square");
        buttonPlaceSubmarine.setTooltip(tooltipPlaceSubmarine);
        buttonPlaceSubmarine.setOnAction((EventHandler) event -> placeShipAtSelectedSquare(ShipType.SUBMARINE, horizontal));
        buttonPlaceSubmarine.setDisable(true);
        grid.add(buttonPlaceSubmarine, 1, 54, 1, 3);

        // Button to place mine sweeper on selected square
        buttonPlaceMineSweeper = new Button("Place mine sweeper (2)");
        buttonPlaceMineSweeper.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipPlaceMineSweeper =
                new Tooltip("Press this button to place the mine sweeper on the selected square");
        buttonPlaceMineSweeper.setTooltip(tooltipPlaceMineSweeper);
        buttonPlaceMineSweeper.setOnAction((EventHandler) event -> placeShipAtSelectedSquare(ShipType.MINESWEEPER, horizontal));
        buttonPlaceMineSweeper.setDisable(true);
        grid.add(buttonPlaceMineSweeper, 1, 58, 1, 3);

        // Button to remove ship that is positioned at selected square
        buttonRemoveShip = new Button("Remove ship");
        buttonRemoveShip.setMinWidth(BUTTONWIDTH);
        Tooltip tooltipRemoveShip =
                new Tooltip("Press this button to remove ship that is "
                        + "positioned on the selected square");
        buttonRemoveShip.setTooltip(tooltipRemoveShip);
        buttonRemoveShip.setOnAction((EventHandler) event -> removeShipAtSelectedSquare());
        buttonRemoveShip.setDisable(true);
        grid.add(buttonRemoveShip, 1, 62, 1, 3);

        // Set font for all labeled objects
        for (Node n : grid.getChildren()) {
            if (n instanceof Labeled) {
                ((Labeled) n).setFont(new Font("Arial", 13));
            }
        }

        // Define title and assign the scene for main window
        primaryStage.setTitle("Sea battle: the game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create instance of class that implements java interface ZeeslagGame.
        // The class ZeeslagGameImpl is not implemented yet.
        // When invoking methods of class ZeeslagGameImpl an
        // UnsupportedOperationException will be thrown

        game = isSpectatorMode ? new SpectatorGame(this) : new ZeeslagGameImpl(this);
    }


    /**
     * Set player number.
     *
     * @param playerNr identification of player
     * @param name     player's name
     */
    @Override
    public void setPlayerNumber(int playerNr, String name) {
        // Check identification of player
        if (!Objects.requireNonNull(this.playerName).equals(name)) {
            showMessage("ERROR: Wrong player name method setPlayerNumber()");
            return;
        }
        this.playerNr = playerNr;
        Platform.runLater(() -> {
            labelPlayerName.setText(playerName + "\'s grid");
            labelYourName.setDisable(true);
            textFieldPlayerName.setDisable(true);
            labelYourPassword.setDisable(true);
            passwordFieldPlayerPassword.setDisable(true);
            radioSinglePlayer.setDisable(true);
            radioMultiPlayer.setDisable(true);
            buttonLoginPlayer.setDisable(true);
            labelHorizontalVertical.setDisable(false);
            radioHorizontal.setDisable(false);
            radioVertical.setDisable(false);
            buttonPlaceAllShips.setDisable(false);
            buttonRemoveAllShips.setDisable(false);
            buttonReadyToPlay.setDisable(false);
            buttonPlaceAircraftCarrier.setDisable(false);
            buttonPlaceBattleShip.setDisable(false);
            buttonPlaceCruiser.setDisable(false);
            buttonPlaceSubmarine.setDisable(false);
            buttonPlaceMineSweeper.setDisable(false);
            buttonRemoveShip.setDisable(false);
        });
        showMessage("Player " + name + " logined");
    }


    /**
     * Notification that the game has started.
     *
     * @param playerNr identification of player
     */
    @Override
    public void notifyStartGame(int playerNr) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method notifyStartGame()");
            return;
        }

        // Set playing mode and disable placing/removing of ships
        playingMode = true;
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        buttonPlaceAllShips.setDisable(true);
        buttonRemoveAllShips.setDisable(true);
        buttonReadyToPlay.setDisable(true);
        buttonStartNewGame.setDisable(true);
        buttonPlaceAircraftCarrier.setDisable(true);
        buttonPlaceBattleShip.setDisable(true);
        buttonPlaceCruiser.setDisable(true);
        buttonPlaceSubmarine.setDisable(true);
        buttonPlaceMineSweeper.setDisable(true);
        buttonRemoveShip.setDisable(true);
        showMessage("Start playing by selecting a square in " + opponentName + "\'s grid");
    }


    @Override
    public void waitForOtherPlayerToBeReady() {
        labelHorizontalVertical.setDisable(true);
        radioHorizontal.setDisable(true);
        radioVertical.setDisable(true);
        buttonPlaceAllShips.setDisable(true);
        buttonRemoveAllShips.setDisable(true);
        buttonReadyToPlay.setDisable(true);
        buttonStartNewGame.setDisable(true);
        buttonPlaceAircraftCarrier.setDisable(true);
        buttonPlaceBattleShip.setDisable(true);
        buttonPlaceCruiser.setDisable(true);
        buttonPlaceSubmarine.setDisable(true);
        buttonPlaceMineSweeper.setDisable(true);
        buttonRemoveShip.setDisable(true);
        showMessage("Waiting for " + opponentName + " to be ready");
    }


    /**
     * Communicate the result of a shot fired by the player.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALL_SUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param hitType  result of shot fired by player
     */
    @Override
    public void playerFiresShot(int playerNr, HitType hitType) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method playerFiresShot()");
            return;
        }
        if (hitType.equals(HitType.SUNK)) {
            showMessage("Ship of " + opponentName + " is sunk");
        }
        if (hitType.equals(HitType.ALL_SUNK)) {
            showMessage("Winner: " + playerName + ".\nPress Start new game to continue");
            buttonStartNewGame.setDisable(false);
            gameEnded = true;
        }
    }


    /**
     * Communicate the result of a shot fired by the opponent.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALL_SUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param shotType result of shot fired by opponent
     */
    @Override
    public void opponentFiresShot(int playerNr, HitType shotType) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method opponentFiresShot()");
            return;
        }
        if (shotType.equals(HitType.SUNK)) {
            showMessage("Ship of " + playerName + " is sunk");
        }
        if (shotType.equals(HitType.ALL_SUNK)) {
            showMessage("Winner: " + opponentName + ".\nPress Start new game to continue");
            buttonStartNewGame.setDisable(false);
            gameEnded = true;
        }
        // Player's turn
        switchTurn();
    }


    /**
     * Show state of a square in the ocean area.
     * The color of the square depends on the state of the square.
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    @Override
    public void showSquarePlayer(int playerNr, final int posX, final int posY, final SquareState squareState) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method showSquarePlayer()");
            return;
        }
        Platform.runLater(() -> {
            Rectangle square = squaresOceanArea[posX][posY];
            setSquareColor(square, squareState);
        });
    }


    /**
     * Show state of a square in the target area.
     * The color of the square depends on the state of the square.
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    @Override
    public void showSquareOpponent(int playerNr, final int posX, final int posY, final SquareState squareState) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            showMessage("ERROR: Wrong player number method showSquareOpponent()");
            return;
        }
        Platform.runLater(() -> {
            Rectangle square = squaresTargetArea[posX][posY];
            setSquareColor(square, squareState);
        });
    }


    /**
     * Show error message.
     *
     * @param errorMessage error message
     */
    @Override
    public void showErrorMessage(String errorMessage) {
        // Show the error message as an alert message
        showMessage(errorMessage);
    }


    /**
     * Set the color of the square according to position type.
     * Setting the color will be performed by the JavaFX Application Thread.
     *
     * @param square      the square of which the color should be changed.
     * @param squareState position type to determine the color.
     */
    private void setSquareColor(@NotNull final Rectangle square, @NotNull final SquareState squareState) {
        // Ensure that changing the color of the square is performed by
        // the JavaFX Application Thread.
        Platform.runLater(() -> {
            switch (squareState) {
                case WATER:
                    square.setFill(Color.LIGHTBLUE);
                    break;
                case SHIP:
                    square.setFill(Color.DARKGRAY);
                    break;
                case SHOT_MISSED:
                    square.setFill(Color.BLUE);
                    break;
                case SHOT_HIT:
                    square.setFill(Color.RED);
                    break;
                case SHIP_SUNK:
                    square.setFill(Color.GREEN);
                    break;
                default:
                    square.setFill(Color.LIGHTBLUE);
                    break;
            }
        });
    }


    /**
     * Login the player at the game server.
     */
    private void loginPlayer() {
        playerName = textFieldPlayerName.getText();
        if ("".equals(playerName) || playerName == null) {
            showMessage("Enter your name before logining");
            return;
        }
        String playerPassword = passwordFieldPlayerPassword.getText();
        if ("".equals(playerPassword) || playerPassword == null) {
            showMessage("Enter your password before logining");
            return;
        }
        game.loginPlayer(playerName, playerPassword, singlePlayerMode);
    }


    /**
     * Place the player's ships automatically.
     */
    private void placeShipsAutomatically() {
        // Place the player's ships automatically.
        game.placeShipsAutomatically();
    }


    /**
     * Remove the player's ships.
     */
    private void removeAllShips() {
        // Remove the player's ships
        game.removeAllShips();
    }


    /**
     * Notify that the player is ready to start the game.
     */
    private void notifyWhenReady() {
        // Notify that the player is ready is start the game.
        game.notifyWhenReady();
    }


    @Override
    public void stop() {
        game.stop();
    }


    /**
     * Start a new game.
     */
    private void startNewGame() {
        // The player wants to start a new game.
        game.resetGame();
        playingMode = false;
        gameEnded = false;
        labelYourName.setDisable(false);
        textFieldPlayerName.setDisable(false);
        labelYourPassword.setDisable(false);
        passwordFieldPlayerPassword.setDisable(false);
        radioSinglePlayer.setDisable(false);
        radioMultiPlayer.setDisable(false);
        buttonLoginPlayer.setDisable(false);
    }


    /**
     * Place a ship of a certain ship type. The bow of the ship will
     * be placed at the selected square in the ocean area. The stern is
     * placed to the right of the bow when the ship should be placed
     * horizontally and below of the bow when the ship should be placed
     * vertically. The exact position of the stern depends on the size
     * of the ship.
     *
     * @param shipType   type of the ship to be placed
     * @param horizontal indicates whether ship should be placed horizontally or
     *                   vertically.
     */
    private void placeShipAtSelectedSquare(ShipType shipType, boolean horizontal) {
        if (squareSelectedInOceanArea) {
            int bowX = selectedSquareX;
            int bowY = selectedSquareY;
            game.placeShip(shipType, bowX, bowY, horizontal);
        } else {
            showMessage("Select square in " + playerName + "\'s grid to place ship");
        }
    }


    /**
     * Remove ship that is positioned at selected square in ocean area.
     */
    private void removeShipAtSelectedSquare() {
        if (squareSelectedInOceanArea) {
            int posX = selectedSquareX;
            int posY = selectedSquareY;
            game.removeShip(posX, posY);
        } else {
            showMessage("Select square in " + playerName + "\'s grid to remove ship");
        }
    }


    /**
     * Show an alert message.
     * The message will disappear when the user presses ok.
     */
    private void showMessage(final String message) {
        // Use Platform.runLater() to ensure that code concerning 
        // the Alert message is executed by the JavaFX Application Thread
        log.debug("Show Message for {} - {}", playerName, message);

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sea battle");
            alert.setHeaderText("Message for " + playerName);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    /**
     * Event handler when mouse button is pressed in rectangle in target area.
     * A shot will be fired at the selected square when in playing mode.
     * A message will be shown otherwise.
     *
     * @param x x-coordinate of selected square
     * @param y y-coordinate of selected square
     */
    private void rectangleTargetAreaMousePressed(int x, int y) {
        if (playingMode && !gameEnded) {
            // Game is in playing modev
            if (playersTurn()) {
                // It is this player's turn
                // Player fires a shot at the selected target area
                game.fireShotGui(x, y);
                squaresTargetArea[x][y].setFill(Color.YELLOW);
                // Opponent's turn
                switchTurn();
            } else {
                // It is not this player's turn yet
                showMessage("Wait till " + opponentName + " has fired a shot");
            }
        } else {
            if (gameEnded) {
                showMessage("Press Start new game");
            } else {
                showMessage("Select square in " + playerName + "\'s grid to place ships");
            }
        }
    }


    /**
     * Event handler when mouse button is pressed in rectangle in ocean area.
     * When not in playing mode: the square that was selected before will
     * become light blue and the the selected square will become yellow.
     * A message will be shown when in playing mode.
     *
     * @param x x-coordinate of selected square
     * @param y y-coordinate of selected square
     */
    private Paint selectedSquareColor;


    private void rectangleOceanAreaMousePressed(int x, int y) {
        if (!playingMode) {
            // Game is not in playing mode: select square to place a ship
            if (squareSelectedInOceanArea) {
                Rectangle square = squaresOceanArea[selectedSquareX][selectedSquareY];
                if (square.getFill().equals(Color.YELLOW)) {
                    square.setFill(selectedSquareColor);
                }
            }
            selectedSquareX = x;
            selectedSquareY = y;
            selectedSquareColor = squaresOceanArea[x][y].getFill();
            squaresOceanArea[x][y].setFill(Color.YELLOW);
            squareSelectedInOceanArea = true;
        } else {
            showMessage("Select square in " + opponentName + "\'s grid to fire");
        }
    }


    /**
     * Method to switch player's turn.
     * This method is synchronized because switchTurn() may be
     * called by the Java FX Application thread or by another thread
     * handling communication with the game server.
     */
    private synchronized void switchTurn() {
        playerTurn = 1 - playerTurn;
    }


    /**
     * Method to check whether it is this player's turn.
     * This method is synchronized because switchTurn() may be
     * called by the Java FX Application thread or another thread
     * handling communication with the game server.
     */
    private synchronized boolean playersTurn() {
        return playerNr == playerTurn;
    }

}
