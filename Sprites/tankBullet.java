package Sprites;

import java.awt.Rectangle;

public class tankBullet extends Sprite {
    public tankBullet(int x, int y) {
    	super(x, y);
    	initBullet();
    }
    public void initBullet() {
    	loadImage("/images/tank_bullet_test.png");
    	getImageDimensions();
    }
    public boolean move(Rectangle posRabbit) {
    	if (!getBounds().intersects(posRabbit)) {
    		if (x > posRabbit.getX())
    			x -= 15;
    		if (x < posRabbit.getX())
    			x += 15;
    		if (y > posRabbit.getY())
    			y -= 15;
    		if (y < posRabbit.getY())
        		y += 15;
    		return false;
    	} else {
    		setVisible(false);
    		return true;
    	}
    }
}
