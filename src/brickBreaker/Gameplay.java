package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
//import java.util.Timer;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

	private boolean play = false;
	private int score = 0;
	private Timer timer;
	private int delay = 3;
	private int playerX = 310;
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXDir = -1;
	private int ballYDir = -1;
	private int bRow = 4;
	private int bCol = 8;
	private int totalBricks = bRow*bCol;
	private MapGenerator map;

	public Gameplay() {
		addKeyListener(this);
		map = new MapGenerator(bRow, bCol);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		// bricks

		map.draw((Graphics2D) g);

		// border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(692, 0, 3, 592);

		// scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);

		// paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);

		// ball
		g.setColor(Color.yellow);
		g.fillOval(ballPosX, ballPosY, 20, 20);

		if (ballPosY > 570 || totalBricks == 0) {
			play = false;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press enter to restart the game", 230, 350);
		}

		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (play) {
			if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYDir = -ballYDir;
			}

			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;

						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						Rectangle brickRect = rect;

						if (ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks = totalBricks - 1;
							score = score + 5;

							if (ballPosX + 19 <= brickRect.x || ballPosX + 1 > brickRect.x + brickRect.width) {
								ballXDir = -ballXDir;
							} else {
								ballYDir = -ballYDir;
							}
							break A;
						}
					}
				}
			}

			ballPosX = ballPosX + ballXDir;
			ballPosY = ballPosY + ballYDir;
			if (ballPosX < 0) {
				ballXDir = -ballXDir;
			}
			if (ballPosY < 0) {
				ballYDir = -ballYDir;
			}
			if (ballPosX > 670) {
				ballXDir = -ballXDir;
			}
		}
		repaint();

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}// not required in this game

	@Override
	public void keyTyped(KeyEvent arg0) {
	}// not required in this game

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 590) {
				playerX = 590;
			} else {
				moveRight();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX <= 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (play == false) {
				play = true;
				defaultValues();
				repaint();
			}
		}
	}

	public void defaultValues() {
		ballPosX = 120;
		ballPosY = 300;
		totalBricks = bRow*bCol;
		playerX = 310;
		score = 0;
		totalBricks = 21;
		map = new MapGenerator(bRow, bCol);
	}
	
	public void moveRight() {
		play = true;
		playerX = playerX + 20;
	}

	public void moveLeft() {
		play = true;
		playerX = playerX - 20;
	}

}
