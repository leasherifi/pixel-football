/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MyfirstJavaFX;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
public class minigame extends Application {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;
    private static final double GRAVITY = 0.5;
    private static final double POWER = 22.0;
    private double ballVX = 0, ballVY = 0;
    private boolean isBallMoving = false;
    private boolean isGameOver = false;
    private int score = 0;
    private static int highScore = 0;
    private int timeLeft = 30;
    private Pane root = new Pane();
    private Label scoreLabel, timeLabel, endScoreLabel, endHighScoreLabel;
    private VBox gameOverBox;
    private ImageView player, ball, goal, background;
    private Group arrowGroup;
    private Rotate arrowRotate = new Rotate();
    
    private double arrowAngle = 0;
    private double arrowDir = 1.0; 
    @Override
    public void start(Stage stage) {
        try {
            background = new ImageView(new Image("file:///C:/a/background1.jpg"));
            background.setFitWidth(WIDTH);
            background.setFitHeight(HEIGHT);
            root.getChildren().add(background);
        } catch (Exception e) {
            root.setStyle("-fx-background-color: lightblue;");
        }
        createGameObjects();
        createUI();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                if (isGameOver) resetGame();
                else shoot();
            }
        });
        new AnimationTimer() {
            public void handle(long now) {
                if (!isGameOver) update();
            }
        }.start();
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (!isGameOver) {
                timeLeft--;
                timeLabel.setText("TIME: " + timeLeft);
                if (timeLeft <= 0) endGame();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        stage.setTitle("Pixel Football");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    private void createGameObjects() {
        player = new ImageView(new Image("file:///C:/a/minigame.png"));
        player.setFitHeight(180);
        player.setPreserveRatio(true);
        player.setSmooth(false);
        player.setX(50);
        player.setY(HEIGHT - 200);
        goal = new ImageView(new Image("file:///C:/a/goal.png"));
        goal.setFitHeight(200);
        goal.setPreserveRatio(true);
        goal.setSmooth(false);
        goal.setX(WIDTH - 220);
        goal.setY(HEIGHT - 220);
        ball = new ImageView(new Image("file:///C:/a/ballgame.png"));
        ball.setFitHeight(40);
        ball.setFitWidth(40);
        ball.setSmooth(false);
        Line shaft = new Line(0, 0, 100, 0);
        shaft.setStroke(Color.YELLOW);
        shaft.setStrokeWidth(2);
        Polygon head = new Polygon(100, -5, 115, 0, 100, 5);
        head.setFill(Color.YELLOW);
        
        arrowGroup = new Group(shaft, head);
        arrowGroup.setEffect(new DropShadow(2, Color.BLACK));
        arrowGroup.getTransforms().add(arrowRotate);
        root.getChildren().addAll(player, goal, ball, arrowGroup);
        resetBall();
    }
    private void createUI() {
        Font font = Font.font("Verdana", FontWeight.BOLD, 18);
        String style = "-fx-background-color: rgba(0,0,0,0.5); -fx-text-fill: white; -fx-padding: 5 15; -fx-background-radius: 15;";
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setFont(font);
        scoreLabel.setStyle(style);
        scoreLabel.setLayoutX(20);
        scoreLabel.setLayoutY(20);
        timeLabel = new Label("TIME: 30");
        timeLabel.setFont(font);
        timeLabel.setStyle(style);
        timeLabel.setLayoutX(WIDTH - 150);
        timeLabel.setLayoutY(20);
        gameOverBox = new VBox(10);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 30; -fx-background-radius: 20; -fx-border-color: black; -fx-border-width: 3;");
        
        Label title = new Label("TIME'S UP!");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        
        endScoreLabel = new Label();
        endScoreLabel.setFont(Font.font("Verdana", 20));
        
        endHighScoreLabel = new Label();
        endHighScoreLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        endHighScoreLabel.setTextFill(Color.GOLD);
        
        Label hint = new Label("Press SPACE to Retry");
        
        gameOverBox.getChildren().addAll(title, endScoreLabel, endHighScoreLabel, hint);
        gameOverBox.setLayoutX(WIDTH / 2.0 - 125);
        gameOverBox.setLayoutY(HEIGHT / 2.0 - 100);
        gameOverBox.setVisible(false);
        root.getChildren().addAll(scoreLabel, timeLabel, gameOverBox);
    }
    private void update() {
        if (!isBallMoving) {
            arrowAngle += arrowDir;
            if (arrowAngle < -80 || arrowAngle > 10) arrowDir *= -1;
            arrowRotate.setAngle(arrowAngle);
        }
        if (isBallMoving) {
            ball.setX(ball.getX() + ballVX);
            ball.setY(ball.getY() + ballVY);
            ballVY += GRAVITY;
            if (ball.getY() > HEIGHT - 40 || ball.getX() > WIDTH) {
                resetBall();
            }
            
            if (checkGoal()) {
                score += 10;
                scoreLabel.setText("SCORE: " + score);
                resetBall();
            }
        }
    }
    private void shoot() {
        if (!isBallMoving) {
            double rad = Math.toRadians(arrowAngle);
            ballVX = Math.cos(rad) * POWER;
            ballVY = Math.sin(rad) * POWER;
            isBallMoving = true;
            arrowGroup.setVisible(false);
        }
    }
    private void resetBall() {
        isBallMoving = false;
        ballVX = 0; 
        ballVY = 0;
        arrowGroup.setVisible(true);
        double startX = 130, startY = HEIGHT - 80;
        ball.setX(startX);
        ball.setY(startY);
        arrowGroup.setLayoutX(startX + 20);
        arrowGroup.setLayoutY(startY + 20);
        arrowRotate.setPivotX(0); 
        arrowRotate.setPivotY(0);
    }
    private boolean checkGoal() {
        double gx = WIDTH - 200, gy = HEIGHT - 220;
        double bx = ball.getX(), by = ball.getY();
        
        boolean underCrossbar = (by > gy + 30); 
        boolean insidePosts = (bx > gx && bx < gx + 180);
        boolean aboveGround = (by < gy + 200);
        return insidePosts && underCrossbar && aboveGround;
    }
    private void endGame() {
        isGameOver = true;
        if (score > highScore) highScore = score;
        
        endScoreLabel.setText("Your Score: " + score);
        endHighScoreLabel.setText("High Score: " + highScore);
        
        gameOverBox.setVisible(true);
        gameOverBox.toFront();
    }
    private void resetGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("SCORE: 0");
        timeLabel.setText("TIME: 30");
        gameOverBox.setVisible(false);
        isGameOver = false;
        resetBall();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
