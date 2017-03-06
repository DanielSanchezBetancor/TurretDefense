package Sprites;

import java.util.ArrayList;

public class car extends Sprite{
	private ArrayList<tankBullet> bullets;
	private int getCount, damage = 10;
	public car(int x, int y, int getCount) {
		super(x, y);
		this.getCount = getCount;
		initCar();
	}
	public void initCar() {
		bullets = new ArrayList<tankBullet>();
		loadImage("/images/car_test.png");
		getImageDimensions();
	}
	public void fire() {
		bullets.add(new tankBullet(x, y));
	}
	public ArrayList<tankBullet> getBullets() {
		return bullets;
	}
	public void cleanBullets() {
		bullets.clear();
	}
	public int getCount() {
		return getCount;
	}
	public int getDamage() {
		return damage;
	}

}
