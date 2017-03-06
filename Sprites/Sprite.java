package Sprites;

import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.ImageIcon;

import ejemplo.Board;

public class Sprite {
	protected int x;
	protected int y;
	protected int width, height;
	protected boolean isVisible;
	protected Image sprite;
	protected long speed = 500;
	public Sprite(int x, int y) {
		this.x = x;
		this.y = y;
		isVisible = true;
	}
	protected void loadImage(String path) {
		URL url = Sprite.class.getResource(path);
		ImageIcon ii = new ImageIcon(url);
		sprite = ii.getImage();
	}
	protected void getImageDimensions() {
		height = sprite.getHeight(null);
		width = sprite.getWidth(null);
	}
	public Image getImage() {
        return sprite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
    	isVisible = visible;
    }
    public Rectangle getBounds() {
    	return new Rectangle(x, y, width, height);
    }
    public long getSpeed() {
    	return speed;
    }
}
