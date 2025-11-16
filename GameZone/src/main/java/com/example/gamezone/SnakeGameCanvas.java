package com.example.gamezone;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;

public class SnakeGameCanvas extends Canvas {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 20;

    private LinkedList<int[]> snake = new LinkedList<>();
    private int[] food = new int[2];

    private int dx = 1, dy = 0;
    private final Deque<int[]> directionQueue = new ArrayDeque<>();

    private Timeline timeline;
    private boolean gameOver = false;
    private int score = 0;

    private SnakeGameController.ScoreNotifier scoreNotifier;

    public SnakeGameCanvas(SnakeGameController.ScoreNotifier notifier) {
        this.scoreNotifier = notifier;
        setWidth(WIDTH * TILE_SIZE);
        setHeight(HEIGHT * TILE_SIZE);
        initGame();
    }

    private void initGame() {
        resetSnake();
        placeFood();

        timeline = new Timeline(new KeyFrame(Duration.millis(300), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (gameOver) return;

            KeyCode code = e.getCode();
            int[] lastDir = directionQueue.peekLast();
            int currentDx = (lastDir != null ? lastDir[0] : dx);
            int currentDy = (lastDir != null ? lastDir[1] : dy);

            if (code == KeyCode.UP && currentDy != 1) {
                directionQueue.add(new int[]{0, -1});
            } else if (code == KeyCode.DOWN && currentDy != -1) {
                directionQueue.add(new int[]{0, 1});
            } else if (code == KeyCode.LEFT && currentDx != 1) {
                directionQueue.add(new int[]{-1, 0});
            } else if (code == KeyCode.RIGHT && currentDx != -1) {
                directionQueue.add(new int[]{1, 0});
            }
        });
    }

    private void update() {
        if (gameOver) return;

        if (!directionQueue.isEmpty()) {
            int[] newDir = directionQueue.poll();
            dx = newDir[0];
            dy = newDir[1];
        }

        int[] head = snake.peekFirst();
        int[] newHead = new int[]{head[0] + dx, head[1] + dy};

        if (newHead[0] < 0 || newHead[0] >= WIDTH ||
                newHead[1] < 0 || newHead[1] >= HEIGHT ||
                snake.stream().anyMatch(p -> p[0] == newHead[0] && p[1] == newHead[1])) {
            gameOver = true;
            draw();
            javafx.application.Platform.runLater(() -> scoreNotifier.onGameOver(score + 1));
            return;
        }

        snake.addFirst(newHead);

        if (newHead[0] == food[0] && newHead[1] == food[1]) {
            score++;
            scoreNotifier.onScoreChange(score + 1);
            placeFood();
        } else {
            snake.removeLast();
        }

        draw();
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // Food
        gc.setFill(Color.RED);
        gc.fillRect(food[0] * TILE_SIZE, food[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Snake
        gc.setFill(Color.LIMEGREEN);
        for (int[] p : snake) {
            gc.fillRect(p[0] * TILE_SIZE, p[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Game Over Text
        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font(20));
            gc.fillText("Game Over", getWidth() / 2 - 50, getHeight() / 2);
        }

        // Border
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, getWidth(), getHeight());
    }

    public void resetGame() {
        resetSnake();
        placeFood();
        timeline.play();
        draw();
    }

    private void resetSnake() {
        snake.clear();
        snake.add(new int[]{WIDTH / 2, HEIGHT / 2});
        dx = 1;
        dy = 0;
        directionQueue.clear();
        score = 0;
        gameOver = false;
        scoreNotifier.onScoreChange(1);
    }

    private void placeFood() {
        Random rand = new Random();
        boolean valid;
        do {
            food[0] = rand.nextInt(WIDTH);
            food[1] = rand.nextInt(HEIGHT);
            valid = snake.stream().noneMatch(p -> p[0] == food[0] && p[1] == food[1]);
        } while (!valid);
    }

    public interface ScoreNotifier {
        void onScoreChange(int score);
        void onGameOver(int score);
    }
}
