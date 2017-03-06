package Sprites;

public class InfoBoard extends Sprite {
	public InfoBoard(int x, int y) {
		super(x, y);
		initStatus();
	}
	private void initStatus() {
		loadImage("/images/board_test.png");
		getImageDimensions();
	}
}
