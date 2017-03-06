package ejemplo;

import java.awt.EventQueue;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Application extends JFrame {
	
	public Application() {
		InitUI();
	}
	private void InitUI() {
		add(new MainMenu());
		setResizable(false);
		pack();
		setTitle("Turret Defense 0.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application a = new Application();
				a.setVisible(true);
			}
		});
	}
}
