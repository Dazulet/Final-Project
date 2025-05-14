package src.main;

import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static JFrame frame;

	public static void main(String[] args) {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Dungeon Survivor");

		GamePanel gamePanel = new GamePanel();
		frame.add(gamePanel);

		frame.setUndecorated(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitFullScreen();
				System.exit(0);
			}
		});

		if (device.isFullScreenSupported()) {
			try {
				device.setFullScreenWindow(frame);
				System.out.println("Fullscreen mode enabled.");
			} catch (Exception e) {
				System.err.println("Error setting fullscreen mode: " + e.getMessage());
				device.setFullScreenWindow(null);
				setupWindowedMode();
			}
		} else {
			System.out.println("Fullscreen mode not supported by the system. Using windowed mode.");
			setupWindowedMode();
		}

		frame.setVisible(true);

		gamePanel.startGameThread();
	}

	private static void setupWindowedMode() {
		frame.setUndecorated(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for (WindowAdapter wl : frame.getListeners(WindowAdapter.class)) {
			frame.removeWindowListener(wl);
		}
	}

	public static void exitFullScreen() {
		if (device.getFullScreenWindow() != null) {
			device.setFullScreenWindow(null);
			System.out.println("Exited fullscreen mode.");
		}
	}
}