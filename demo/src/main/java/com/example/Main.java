package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX application entry point for Tic-Tac-Toe. */
public class Main extends Application {
    private final Board board = new Board();
    private final HumanPlayer human = new HumanPlayer('X', new Scanner(""));
    private final CpuPlayer cpu = new CpuPlayer('O');
    private final List<Button> cells = new ArrayList<>();

    private Label status;
    private boolean gameOver;

    /** Creates and displays the game window. */
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.getStyleClass().addAll("root", "content");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(36));

        Label eyebrow = new Label("HUMAN VS CPU");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Tic-Tac-Toe");
        title.getStyleClass().add("title");

        status = new Label();
        status.getStyleClass().add("status");

        GridPane grid = createBoard();
        grid.getStyleClass().add("board");

        Label hint = new Label("Choose an empty square to make your move.");
        hint.getStyleClass().add("hint");

        Button restart = new Button("New game");
        restart.getStyleClass().add("restart-button");
        restart.setOnAction(event -> restartGame());

        root.getChildren().addAll(eyebrow, title, status, grid, hint, restart);
        updateBoard();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private GridPane createBoard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(12);
        grid.setVgap(12);

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                final int cellRow = row;
                final int cellColumn = column;
                Button cell = new Button();
                cell.getStyleClass().add("cell");
                cell.setOnAction(event -> playHumanMove(cellRow, cellColumn));
                cells.add(cell);
                grid.add(cell, column, row);
            }
        }
        return grid;
    }

    private void playHumanMove(int row, int column) {
        if (gameOver) {
            return;
        }
        if (!human.makeMove(board, row, column)) {
            return;
        }
        if (finishIfGameOver(human)) {
            updateBoard();
            return;
        }

        cpu.makeMove(board);
        if (!finishIfGameOver(cpu)) {
            status.setText("Your turn, " + human.getName() + " (X)");
        }
        updateBoard();
    }

    private boolean finishIfGameOver(Player player) {
        if (board.checkWin()) {
            gameOver = true;
            status.setText(player.getName() + " wins!");
            return true;
        }
        if (board.isFull()) {
            gameOver = true;
            status.setText("It's a draw!");
            return true;
        }
        return false;
    }

    private void updateBoard() {
        for (int index = 0; index < cells.size(); index++) {
            int row = index / 3;
            int column = index % 3;
            char marker = board.getCell(row, column);
            Button cell = cells.get(index);
            cell.setText(marker == ' ' ? "" : Character.toString(marker));
            cell.setDisable(gameOver || marker != ' ');
            cell.getStyleClass().removeAll("x-cell", "o-cell");
            if (marker == 'X') {
                cell.getStyleClass().add("x-cell");
            } else if (marker == 'O') {
                cell.getStyleClass().add("o-cell");
            }
        }
        if (!gameOver) {
            status.setText("Your turn, " + human.getName() + " (X)");
        }
    }

    private void restartGame() {
        board.initialize();
        gameOver = false;
        updateBoard();
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }
}
