package ejemplo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Sprites.Sprite;

@SuppressWarnings("serial")
public class MainMenu extends JPanel{
	protected Image sprite;
	protected boolean startAnimation;
	protected int x, heightBackground;
	public MainMenu() {
		initUI();
	}
	public void initUI() {
		loadImage();
		setPreferredSize(new Dimension(sprite.getWidth(null), sprite.getHeight(null)));
		startThreads();
		
	}
	private Thread startAnimation() {
		return new Thread(){
			public void run() {
				x = heightBackground;
				startAnimation = true;
				while (x != 0) {
					x--;
					try {
						sleep(2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				startAnimation = false;
			};
		};
	}
	private void loadImage() {
		URL url = Sprite.class.getResource("/images/map_test_0.png");
		ImageIcon ii = new ImageIcon(url);
		sprite = ii.getImage();
		heightBackground = sprite.getHeight(null);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (startAnimation)
			paintAnimation(g);
		else 
			g.drawImage(sprite, 0, 0, null);
			
	}
	private void paintAnimation(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(sprite, x, 0, this);
	}
	private Thread threadRepaint() {
		return new Thread() {
			public void run() {
				while (true) {
					repaint();
				}
			}
		};
	}
	private void startThreads() {
		Thread xd = startAnimation();
		xd.start();
		Thread repaint = threadRepaint();
		repaint.start();
	}
}
