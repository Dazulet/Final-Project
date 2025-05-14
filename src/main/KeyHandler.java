package src.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	public boolean upPressed, downPressed, leftPressed, rightPressed, rPressed;
	public boolean spell1Pressed;
	public boolean spell2Pressed;
	public boolean spell3Pressed;
	public boolean usePotionPressed;
	GamePanel gp;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if(gp == null) return;

		if(code == KeyEvent.VK_ESCAPE) {
			gp.gameThread = null;
		}

		if(code == KeyEvent.VK_M) {
			gp.toggle_sound();
		}

		if(gp.panelState == gp.START) {
			if (gp.ss == null) return;
			if(code == KeyEvent.VK_W) {
				if(gp.ss.commandNum == 0) gp.ss.commandNum = 2; else gp.ss.commandNum--;
			}
			else if(code == KeyEvent.VK_S) {
				if(gp.ss.commandNum == 2) gp.ss.commandNum = 0; else gp.ss.commandNum++;
			}
			if(code == KeyEvent.VK_ENTER) {
				switch(gp.ss.commandNum) {
					case 0: gp.panelState = gp.GAME; break;
					case 1: gp.panelState = gp.LEVEL; break;
					case 2: gp.gameThread = null; break;
				}
			}
		}
		else if(gp.panelState == gp.GAME) {
			if (gp.player == null) return;
			if(code == KeyEvent.VK_W) upPressed = true;
			else if(code == KeyEvent.VK_S) downPressed = true;
			else if(code == KeyEvent.VK_D) rightPressed = true;
			else if(code == KeyEvent.VK_A) leftPressed = true;
			else if(code == KeyEvent.VK_R) {

				if (!rPressed) rPressed = true;
			}
			else if (code == KeyEvent.VK_1) {
				if (!spell1Pressed) spell1Pressed = true;
			}
			else if (code == KeyEvent.VK_2) {
				if (!spell2Pressed) spell2Pressed = true;
			}
			else if (code == KeyEvent.VK_3) {
				if (!spell3Pressed) spell3Pressed = true;
			}
			else if (code == KeyEvent.VK_Q) {
				if (!usePotionPressed) usePotionPressed = true;
			}
		}
		else if(gp.panelState == gp.LEVEL) {
			if (gp.ls == null) return;
			if(code == KeyEvent.VK_A ) {
				if(gp.ls.commandNum == 0) gp.ls.commandNum = 2; else gp.ls.commandNum--;
				gp.ls.level = 400 - (2 - gp.ls.commandNum) * 100;
			}
			else if(code == KeyEvent.VK_D ) {
				if(gp.ls.commandNum == 2) gp.ls.commandNum = 0; else gp.ls.commandNum++;
				gp.ls.level = 400 - (2-gp.ls.commandNum) * 100;
			}
			if(code == KeyEvent.VK_ENTER) {

				gp.panelState = gp.START;
			}
		}
		else if(gp.panelState == gp.END) {
			if (gp.es == null) return;
			if(code == KeyEvent.VK_W) {
				if(gp.es.commandNum == 0) gp.es.commandNum = 1; else gp.es.commandNum--;
			}
			else if(code == KeyEvent.VK_S) {
				if(gp.es.commandNum == 1) gp.es.commandNum = 0; else gp.es.commandNum++;
			}
			if(code == KeyEvent.VK_ENTER) {
				switch(gp.es.commandNum) {
					case 0: gp.panelState = gp.START; gp.restart(); break;
					case 1: gp.gameThread = null; break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_W) upPressed = false;
		else if(code == KeyEvent.VK_S) downPressed = false;
		else if(code == KeyEvent.VK_D) rightPressed = false;
		else if(code == KeyEvent.VK_A) leftPressed = false;
		else if(code == KeyEvent.VK_R) rPressed = false;
		else if (code == KeyEvent.VK_1) spell1Pressed = false;
		else if (code == KeyEvent.VK_2) spell2Pressed = false;
		else if (code == KeyEvent.VK_3) spell3Pressed = false;

		else if (code == KeyEvent.VK_Q) usePotionPressed = false;
	}
}