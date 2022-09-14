package snake_game;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;


public class GamePanel extends JPanel implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();
		
	}
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int BOX = SCREEN_WIDTH/UNIT_SIZE;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static int DELAY = 150;
	// position of snake's head
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char dir = 'R'; //for moving r
	boolean running = false;
	Timer timer; //using javax.swing.Timer
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.DARK_GRAY);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple(); 
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();;
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		//creating a matrix graphic
		if(running) {
			/**
			for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			//Draw apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	
			//Draw snake
			for (int i = 0; i < bodyParts; i++) {
				if(i == 0){
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else {
					g.setColor(new Color(45,180,100));
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			//draw current score
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH -metrics.stringWidth("Score" + applesEaten))/2, g.getFont().getSize());
			
		} else {
			gameOver(g);
			timer.start();
		}
	}
	public void move() {
		for (int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(dir) {
		case 'U':	//up
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':	//down
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':	//left
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':	//right
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)){
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}	
	public void checkCollision() {
		//collide with the body
		for(int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && y[0]== y[i]) {
				running = false;
			}
		}
		// collide with the border
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		if(y[0] < 0) {
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) timer.stop();
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH -metrics2.stringWidth("Score" + applesEaten))/2, g.getFont().getSize());
		//Game Over Text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH -metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

		//Play Again
		g.setColor(Color.green);
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press R to replay", (SCREEN_WIDTH - metrics3.stringWidth("Press R to replay"))/2, (SCREEN_HEIGHT/2 + 2*UNIT_SIZE));
		
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public  void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(dir != 'R' ) {
					dir = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(dir != 'L' ) {
					dir = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(dir != 'D' ) {
					dir = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(dir != 'U' ) {
					dir = 'D';
				}
				break;
			}
			if (e.getKeyCode() == KeyEvent.VK_R) startGame();
		}
		
	}
}
