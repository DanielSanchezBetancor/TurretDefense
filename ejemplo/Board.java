package ejemplo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Board extends JComponent implements ActionListener {
	//Variables primitivas
	private int x, y, carCounter = -1, heightBackground, rabbitCounter = -1;
	private boolean fire = false;
	private Rectangle[] areas;
	private Thread repaintThread = new Thread() {
		public void run() {
			while (true) {
				repaint();
				try {
					sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private Thread checkWinStatus = new Thread() {
		public void run() {
			while (true) {
				for (int i = 0;i<=rabbitCounter;i++) {
					if (rabbits[i] != null)
						if (!rabbits[i].isDead())
							if (rabbits[i].getWin()) {
								rabbits[i].setWin(false);
								gs.takeALife();
								textHp.setText(gs.getHpText());
							}
				}
			}
		}
	};
	private Thread carRunning;
	//Componentes JFrame
	private Image background, infoBoardImage;
	private Sprites.Map backgroundMap;
	private MouseListener ml = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			setNewCoords(e.getX(), e.getY());
			boolean canConstruct = true;
			addCar();
			for (int i = 0;i<areas.length;i++) {
				if (areas[i].intersects(car.getBounds()))
					canConstruct = false;
			}
			if (!canConstruct)
				eraseCar();
			if (!repaintThread.isAlive())
				repaint();
		}
	};
	private JButton play;
	private JLabel[] texto = new JLabel[15];
	private JLabel textRound, textHp;
	
	//Extensiones de clase
	private Sprites.car[] cars = new Sprites.car[10];
	private Sprites.car car;
	private Sprites.Rabbit rabbit;
	private Sprites.Rabbit[] rabbits = new Sprites.Rabbit[15];
	private Thread newRabbitThread, carsThreads[] = new Thread[10];
	private GameStats gs;
	private Sprites.InfoBoard infoBoard;
	
	public Board() {
		initUI();
	}
	public void initUI() {
		setLayout(null);
		loadBackgroundStatus();
		setPreferredSize(new Dimension(background.getWidth(null) + infoBoardImage.getWidth(null), background.getHeight(null)));
		addMouseListener(ml);
		
		play = new JButton("Empezar");
		play.addActionListener(this);
		play.setLocation(new Point(0, 0));
		play.setBorder(null);
		play.setSize(new Dimension(60, 30));
		add(play);
		textRound = new JLabel("Ronda: ");
		textRound.setForeground(Color.WHITE);
		textRound.setLocation(new Point(background.getWidth(null) + 32, 15));
		textRound.setSize(new Dimension(80, 30));
		add(textRound);		
		carRunning = threadCars();
		gs = new GameStats(100);
		gs.resetState();
		textHp = new JLabel(gs.getHpText());
		textHp.setForeground(Color.WHITE);
		textHp.setLocation(new Point(background.getWidth(null) + 32, 35));
		textHp.setSize(new Dimension(80, 30));
		add(textHp);
	}
	public void loadBackgroundStatus() { 
		backgroundMap = new Sprites.Map(0, 0);
		background = backgroundMap.getImage();
		if (newRabbitThread == null)
			newRabbitThread = newRabbitThread();
		heightBackground = background.getHeight(this);
		infoBoard = new Sprites.InfoBoard(0, background.getHeight(null));
		infoBoardImage = infoBoard.getImage();
		backgroundMap.setInfoBoard(infoBoardImage.getWidth(null));
		areas = backgroundMap.getAreas();
	}
	/*
	 * Empezamos a pintar
	 * 
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
		g.drawImage(infoBoardImage, background.getWidth(null), 0, null);
		if (carCounter != -1)
			paintBullet(g);
		if (rabbit != null)
			paintRabbit(g);
		textHp.setText(gs.getHpText());
	}
	public void paintBullet(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (cars[carCounter].isVisible()) {
            for (int i = 0;i<=carCounter;i++) {
		        g2d.drawImage(cars[i].getImage(), cars[i].getX(), cars[i].getY(), this);
		        ArrayList<Sprites.tankBullet> ms = cars[i].getBullets();
		        for (Object m1 : ms) {
		            Sprites.tankBullet m = (Sprites.tankBullet) m1;
		            if (m.isVisible()) {
		            	g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
		            }
		       }
			}
		}
	}
	private void paintRabbit(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	        for (int i = 0;i<=rabbit.getCount();i++) {
	            if (rabbits[i] != null && rabbits[i].isVisible()) {
	            	g2d.drawImage(rabbits[i].getImage(), rabbits[i].getX(), rabbits[i].getY(), this);
		            if (texto[i] != null) {
		            	texto[i].setLocation(new Point(rabbits[i].getX(), rabbits[i].getY()-50));
		            	texto[i].setText(rabbits[i].getText());
		            }
	            } else {
	            	texto[i].setVisible(false);
	            }
	        }
	}
	/*
	 * PAINT TERMINADO
	 * 
	 */
	
	public void addCar() {
		carCounter++;
		cars[carCounter] = new Sprites.car(x, y, carCounter);
		car = cars[carCounter];
		
	}	
	public void eraseCar() {
		carCounter--;
		
	}
	public void addRabbit() {
		rabbitCounter++;
		rabbits[rabbitCounter] = new Sprites.Rabbit(81, 0, heightBackground, rabbitCounter, gs, infoBoard);
		rabbit = rabbits[rabbitCounter];
		texto[rabbitCounter] = new JLabel(rabbit.getText());
		texto[rabbitCounter].setSize(new Dimension(100, 50));
		add(texto[rabbitCounter]);
		if (fire)
			rabbit.resume();
		else
			rabbit.interrupt();
		new Thread(rabbit).start();
	}
	public void setNewCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Thread threadCars() {
		return new Thread() {
			public void run() {
				long beforeTime, timeDiff, sleep;
				int countBullets = 0;
				while (fire) {
		            try {
					beforeTime = System.currentTimeMillis();
					for (int i = 0;i<=carCounter;i++) {
						Sprites.Rabbit rabbit = swapTarget();
						if (countBullets%50 == 0) 
							cars[i].fire();
						if (rabbit != null && cars[i] != null)
							updateBullet(cars[i], i, rabbit);
			            if (rabbits[14] != null && !rabbits[14].isVisible()) {
			            	fire = false;
							play.setText("Siguiente ronda");
							play.setSize(new Dimension(120, 30));
							play.setVisible(true);
			            }
					}
					countBullets++;
					timeDiff = System.currentTimeMillis() - beforeTime;
		            sleep = 25 - timeDiff;

		            if (sleep < 0) {
		                sleep = 2;
		            }
		            	
		                Thread.sleep(sleep);
		            } catch (InterruptedException e) {
		                System.out.println("Interrupted: " + e.getMessage());
		            }

		            beforeTime = System.currentTimeMillis();
		            } 
				cleanBullets();
				System.out.println("He muerto: Hilo de coches");
            }
            
            
		};
	}
	//Crea el vector de Rabbits por cada wave
	public Thread newRabbitThread() {
		return new Thread() {
			public void run() {
				boolean newWave = true;
				while (newWave) {
					if (rabbits[14] != null) {
						newWave = false;
					} else {
						addRabbit();
					}
					 try {
			                Thread.sleep(500);
			            } catch (InterruptedException e) {
			                System.out.println("Interrupted: " + e.getMessage());
			        }
				}
				System.out.println("He muerto: Hilo de creador de conejos");
			}
		};
	}
	private void updateBullet(Sprites.car car, int i, Sprites.Rabbit rabbit) {
			ArrayList<Sprites.tankBullet> al = car.getBullets();
			for (int j = 0;j < al.size();j++) {
				Sprites.tankBullet tb = (Sprites.tankBullet) al.get(j);
				if (tb.isVisible()) {
					boolean dealDamage = tb.move(rabbit.getBounds());
					if (dealDamage)
						rabbit.setHp(car.getDamage());
					} else
						al.remove(j);
					}
				} 
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == play) {
			if (fire) {
				killThreads();
					for (int i = 0;i<=car.getCount();i++) {
						if (carsThreads[i] != null)
							carsThreads[i].interrupt();
					}
					for (int i = 0;i<=rabbitCounter;i++)
						rabbits[i].interrupt();
					repaintThread.interrupt();
			}
			else {
				if (!newRabbitThread.isAlive())
					startNewRound();
				play.setVisible(false);
			}
		}
	}
	private Sprites.Rabbit swapTarget() {
		Sprites.Rabbit rabbitReturn = null;
		try {
			for (int i = 0;i<=rabbitCounter;i++) {
				if (rabbits[i].isVisible()) {
					rabbitReturn = rabbits[i];
					break;
				}
			}
		} catch (NullPointerException e) {
		
		}
		return rabbitReturn;
	}
	private void cleanBullets() {
		for (int i = 0;i<cars.length;i++) {
			ArrayList<Sprites.tankBullet> al = cars[i].getBullets();
			for (int j = 0;j < al.size();j++) {
				al.remove(j);
			}
		}
	}
	private void startNewRound() {
		killThreads();
		if (gs.getLevel() <= 20) {
			try {
				gs.nextLevel();
				fire = true;
				rabbits = new Sprites.Rabbit[15];
				textRound.setText(gs.getRoundText());
				rabbitCounter = -1;
				newRabbitThread().start();
				Thread.sleep(100);
				carRunning = threadCars();
				carRunning.start();
				if (!repaintThread.isAlive())
					repaintThread.start();
				if (!checkWinStatus.isAlive())
					checkWinStatus.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		} 
	}
	private void killThreads() {
		try {
			fire = false;
			if (newRabbitThread.isAlive()) {
				newRabbitThread.interrupt();
				newRabbitThread.join();
				System.out.println(newRabbitThread.isInterrupted());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
/*private void updateRabbit() {
System.out.println("1: " + isUsed);
if (!isUsed) {
    System.out.println("2: " + isUsed);
	isUsed = true;
	if (rabbit.getX() == background.getWidth(this))
		rabbit.swapDirections("Left");
	else if (rabbit.getX() == 0)
		rabbit.swapDirections("Right");
	rabbit.move(this);
} 
}*/

/*public Thread newThread() {
		return new Thread(this);
	}
	*/
/*@Override
public void run() {
	 long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        int countBullets = 0;
        int carCounter = this.carCounter;
        while(true) {
        	if (fire) {
	        	if (countBullets%10 == 0) {
	    			cars[carCounter].fire();
	    			beforeTime = System.currentTimeMillis();
	        	}
		            updateBullet(carCounter);
		            //updateRabbit();
		            countBullets++;
		            
            }
        	if (rabbitThread.getHp() <= 0) {
    			fire = false;
    			cars[carCounter].cleanBullets();
    		}
            repaint();
            
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = 125 - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }
            
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
            } 
}*/