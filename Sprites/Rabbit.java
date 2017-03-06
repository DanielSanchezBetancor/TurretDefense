package Sprites;

import java.awt.Point;

import ejemplo.GameStats;

public class Rabbit extends Sprite implements Runnable {
	private int count, heightBackground;
	private double moveX, moveY, hp, doublex, doubley;
	private boolean win = false, isDead = false, keepGoing, firstPart, secondPart, thirdPart, fourthPart, fifthPart, sixthPart, seventhPart, eighthPart, ninethPart, tenthPart, eleventhPart, twelfthPart, thirdteenthPart;
	private GameStats gs;
	private Point[] points;
	private InfoBoard ib;
	/*
	 * Base stats
	 *  HP: 30
	 */
	public Rabbit(int x, int y, int heightBackground, int count, GameStats gs, InfoBoard ib) {
		super(x, y);
		doublex = x;
		doubley = y;
		this.gs = gs;
		initUI();
		hp = 30 * gs.levelMultiplicator();
		this.count = count;
		this.heightBackground = heightBackground;
		keepGoing = true;
		points = new Point[]{new Point(81, 38), new Point(125, 77), new Point(455, 77), new Point(455, 461), new Point(369, 506), new Point(155, 512), new Point(90, 421), new Point(95, 389), new Point(210, 283), new Point(319, 390), new Point(375, 390), new Point(375, 133), new Point(17, 133), new Point(17, heightBackground)};
		this.ib = ib;
	}
	public void initUI() {
		loadImage("/images/rabbit_test.png");
		getImageDimensions();
	}
	public int getCount() {
		return count;
	}
	public void move() {
		doublex += moveX ;
		doubley += moveY ;
		x = (int)(doublex);
		y = (int)doubley;
	}
	public int checkPath(int pos) {
		double x = points[pos].getX() - this.x;
		double y = points[pos].getY() - this.y;
		double tempx = x;
		if (x < 0)
			tempx = - x;
		if (tempx > y) {
			if (points[pos].getX() > this.x)
				moveX = 1;
			else if (points[pos].getX() < this.x)
				moveX = -1;
			else
				moveX = 0;
			if (points[pos].getY() != this.y) 
				moveY = y / tempx;
			else
				moveY = 0;
		}
		else if (tempx < y) {
			if (points[pos].getX() !=  this.x) {
				moveX = tempx / y;
				if (moveX < 0.5 && moveX > 0)
					moveX = 0.5;
				else if (moveX > -0.5 && moveX < 0)
					moveX = -0.5;
			} else
				moveX = 0;
			if (points[pos].getY() >  this.y)
				moveY = 1;
			else if (points[pos].getY() <  this.y)
				moveY = - 1;
			else
				moveY = 0;
		}
		else if (y == tempx) {
			if (points[pos].getX() >  this.x)
				moveX = 1;
			else if (points[pos].getX() <  this.x)
				moveX = -1;
			else
				moveX = 0;
			if (points[pos].getY() >  this.y)
				moveY = 1;
			else if (points[pos].getY() <  this.y)
				moveY = - 1;
			else
				moveY = 0;
		}
		if (moveY > 1)
			moveY = 1;
		else if (moveY < -1)
			moveY = -1;
		if (moveX == 0 && moveY == 0)
			pos++;
		return pos;
	}
	public double getHp() {
		return hp;
	}
	public void setHp(int hpDealed) {
		hp -= hpDealed;
		if (getHp() <= 0) {
			setVisible(false);
			isDead = true;
		}
	}
	public void run() {
		int pos = 0;
		while (isVisible) {
			if (keepGoing) {
				pos = checkPath(pos);
				move();
				if (y == heightBackground) {
					setVisible(false);
					keepGoing = false;
					gs.takeALife();
				}
		        try {
		        	Thread.sleep(getSpeed()/50);
		        } catch (InterruptedException e) {
		        	System.out.println("Interrupted: " + e.getMessage());
		        }
			}
		}
		}
	public void interrupt() {
		keepGoing = false;
	}
	public void resume() {
		keepGoing = true;
	}
	public boolean getWin() {
		return win;
	}
	public void setWin(boolean win) {
		this.win = win;
	}
	public boolean isDead() {
		return isDead;
	}
	public String getText() {
		return Double.toString(hp);
	}
}
