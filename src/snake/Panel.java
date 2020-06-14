package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Panel extends JPanel implements KeyListener, ActionListener {
    // import all images
    private Image title = new ImageIcon("title.jpeg").getImage();
    private Image snakeRight = new ImageIcon("right.png").getImage();
    private Image snakeLeft = new ImageIcon("left.png").getImage();
    private Image snakeUp = new ImageIcon("up.png").getImage();
    private Image snakeDown = new ImageIcon("down.png").getImage();
    private Image body = new ImageIcon("body.png").getImage();
    private Image food = new ImageIcon("food.png").getImage();

    // record snake's length
    private int snakeLen;
    // record snake's length by coordinate
    private int[] snakeX = new int[700];
    private int[] snakeY = new int[700];

    // detect snake direction - R, L, U, D
    private String direction;

    // game start
    private boolean isStarted;

    // timer for movement
    private Timer timer;

    // coordinate of food
    private int foodX;
    private int foodY;

    // random number generator
    private Random rand = new Random();

    // game over
    private boolean isFailed;

    // score
    private int score;

    // constructor will execute paintComponent automatically
    public Panel() {
        initSnake();
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(200, this);
        timer.start();

    }

    public void initSnake() {
        isStarted = false;
        isFailed = false;
        direction = "R";
        snakeLen = 3;
        snakeX[0] = 100;
        snakeY[0] = 100;
        snakeX[1] = 75;
        snakeY[1] = 100;
        snakeX[2] = 50;
        snakeY[2] = 100;
        score = 0;
        generateFood();
    }

    public void generateFood() {
        foodX = 25 + 25 * rand.nextInt(34);
        foodY = 75 + 25 * rand.nextInt(24);
    }

    public Image getDirection() {
        Image currentImage = snakeRight;
        switch (direction) {
            case "L":
                currentImage = snakeLeft;
                break;
            case "D":
                currentImage = snakeDown;
                break;
            case "U":
                currentImage = snakeUp;
                break;
            default:
                break;
        }
        return currentImage;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw title
        g.drawImage(title, 25, 11, null);
        // draw game area
        g.fillRect(25, 75, 850, 600);
        // draw score and length
        g.setFont(new Font("safari", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString("Length: " + snakeLen, 750, 30);
        g.drawString("Score: " + score, 750, 50);

        // draw snake head
        g.drawImage(getDirection(), snakeX[0], snakeY[0], null);

        // draw snake body
        for (int i = 1; i < snakeLen; i++) {
            g.drawImage(body, snakeX[i], snakeY[i], null);
        }

        // draw food
        g.drawImage(food, foodX, foodY, null);

        if (!isStarted) {
            // hint to user for game start
            var startMessage = "Press Space to Start!";
            var f = new Font("TimesRoman", Font.ITALIC, 30);
            g.setFont(f);
            // set the pen color to white in order to show the message
            g.setColor(Color.WHITE);
            g.drawString(startMessage, 350, 330);
        } else {
            if (isFailed) {
                var failedMessage = "Failed! Press Space to Restart!";
                var f = new Font("TimesRoman", Font.ITALIC, 30);
                g.setFont(f);
                // set the pen color to white in order to show the message
                g.setColor(Color.WHITE);
                g.drawString(failedMessage, 320, 330);
            }
        }

    }

    // check whether the snake hits the border
    public boolean hitBorder() {
        boolean hitted = false;
        switch (direction) {
            case "L":
                if (snakeX[0] - 25 < 25)
                    hitted = true;
                break;
            case "D":
                if (snakeY[0] + 25 > 650)
                    hitted = true;
                break;
            case "U":
                if (snakeY[0] - 25 < 75)
                    hitted = true;
                break;
            case "R":
                if (snakeX[0] + 25 > 850)
                    hitted = true;
                break;
            default:
                break;
        }
        isFailed = hitted ? true : false;
        return hitted;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        int keyCode = keyEvent.getKeyCode();

        // detect keystroke
        if (keyCode == keyEvent.VK_SPACE) {
            if (isFailed) {
                isFailed = false;
                initSnake();
            }
            isStarted = !isStarted;

            repaint();
        } else if (keyCode == keyEvent.VK_LEFT) {
            direction = "L";
        } else if (keyCode == keyEvent.VK_RIGHT) {
            direction = "R";
        } else if (keyCode == keyEvent.VK_DOWN) {
            direction = "D";
        } else if (keyCode == keyEvent.VK_UP) {
            direction = "U";
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (isStarted && !isFailed) {
            if (!hitBorder()) {
                // move the snake body
                for (int i = snakeLen - 1; i > 0; i--) {
                    snakeX[i] = snakeX[i - 1];
                    snakeY[i] = snakeY[i - 1];
                }
                // move the snake head
                switch (direction) {
                    case "L":
                        snakeX[0] = snakeX[0] - 25;
                        break;
                    case "D":
                        snakeY[0] = snakeY[0] + 25;
                        break;
                    case "U":
                        snakeY[0] = snakeY[0] - 25;
                        break;
                    case "R":
                        snakeX[0] = snakeX[0] + 25;
                        break;
                    default:
                        break;
                }
            }
            if (!isFailed) {
                // Eat food
                if (snakeX[0] == foodX && snakeY[0] == foodY) {
                    // increment snake body
                    snakeLen++;
                    // increment score
                    score += 10;
                    // generate food
                    generateFood();
                }

                // head collides with body - game over
                for (int i = 1; i < snakeLen; i++) {
                    if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                        isFailed = true;
                    }
                }

            }
            repaint();
            timer.restart();
        }
    }
}
