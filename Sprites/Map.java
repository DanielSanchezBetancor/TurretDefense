package Sprites;

import java.awt.Color;
import java.awt.Rectangle;

public class Map extends Sprite {
	private Rectangle[] areas;
	private int infoBoardWidth;
	public Map (int x, int y) {
		super(x, y);
		initStatus();
	}
	public void initStatus() {
		loadImage("/images/map_test_0.png");
		getImageDimensions();
	}
	public void createVectorsNotConstructible() {
		Rectangle blockInfo = new Rectangle(getImage().getWidth(null), 0, infoBoardWidth, getImage().getHeight(null));
		Rectangle block1 = new Rectangle(61, 0, 61, 38);
		areas = new Rectangle[]{blockInfo, block1};
	}
	public Rectangle[] getAreas() {
		createVectorsNotConstructible();
		return areas;
	}
	public void setInfoBoard(int infoBoardWidth) {
		this.infoBoardWidth = infoBoardWidth;
	}
}
