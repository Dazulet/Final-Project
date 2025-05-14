package src.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.entity.Entity;
import src.entity.Player;

public class UI{

	GamePanel gp;
	Font font;
	Font smallFont;
	Font largeFont;
	BufferedImage[] redDice;
	BufferedImage[] blueDice;

	public UI(GamePanel gp) {
		this.gp = gp;
		this.font = new Font("Arial", Font.PLAIN, 16);
		this.smallFont = new Font("Arial", Font.BOLD, 12);
		this.largeFont = new Font("Arial", Font.BOLD, 50);
		getDiceImage();
	}

	public void getDiceImage() {
		redDice = new BufferedImage[6];
		blueDice = new BufferedImage[6];
		try {
			for(int i=0; i<6; i++) {
				if (getClass().getResourceAsStream("/res/dice/red " + (i+1) + ".png") != null)
					redDice[i] = ImageIO.read(getClass().getResourceAsStream("/res/dice/red " + (i+1) + ".png"));
				if (getClass().getResourceAsStream("/res/dice/blue " + (i+1) + ".png") != null)
					blueDice[i] = ImageIO.read(getClass().getResourceAsStream("/res/dice/blue " + (i+1) + ".png"));
			}
		}catch(IOException e) { e.printStackTrace(); }
		catch(IllegalArgumentException e) {System.err.println("UI: Dice images not found! Check paths /res/dice/"); e.printStackTrace();}
	}

	public void draw(Graphics2D g2) {
		if (gp == null || gp.player == null || gp.monstersM == null || gp.battleM == null || redDice == null || blueDice == null) {
			return;
		}

		g2.setFont(font);
		g2.setColor(Color.white);

		int yPos = 18;
		int xLeftCol = 10;
		int lineHeight = 18;

		g2.drawString("HP: " + gp.player.hit_point + "/" + gp.player.max_hit_point, xLeftCol, yPos);
		yPos += lineHeight;
		g2.drawString("MP: " + gp.player.mana + "/" + gp.player.maxMana, xLeftCol, yPos);
		yPos += lineHeight;
		g2.drawString("Lvl: " + gp.player.level + " (XP: " + gp.player.experience + "/" + gp.player.experienceToNextLevel + ")", xLeftCol, yPos);
		yPos += lineHeight;
		g2.drawString("Potions: " + gp.player.healingPotions, xLeftCol, yPos);
		yPos += lineHeight;
		String weaponInfo = "Weapon: None";
		if (gp.player.equippedWeapon != null) {
			weaponInfo = gp.player.equippedWeapon.name + " (+" + gp.player.equippedWeapon.attackBonus + " Atk)";
		}
		g2.drawString(weaponInfo, xLeftCol, yPos);
		yPos += lineHeight;
		String armorInfo = "Armor: None";
		if (gp.player.equippedArmor != null) {
			armorInfo = gp.player.equippedArmor.name + " (+" + gp.player.equippedArmor.defenseBonus + " Def)";
		}
		g2.drawString(armorInfo, xLeftCol, yPos);

		yPos = 18;
		int xRightCol = gp.screenWidth - 10;

		String stepsText = "Steps: " + gp.player.steps;
		g2.drawString(stepsText, xRightCol - getXoffsetForRightAlignedText(stepsText, g2, font), yPos);
		yPos += lineHeight;
		String monstersText = "Monsters: " + gp.monstersM.monster_remaning;
		g2.drawString(monstersText, xRightCol - getXoffsetForRightAlignedText(monstersText, g2, font), yPos);
		yPos += lineHeight;
		String mapLvlText = "Map Lvl: " + gp.currentLevel;
		g2.drawString(mapLvlText, xRightCol - getXoffsetForRightAlignedText(mapLvlText, g2, font), yPos);

		if(gp.player.gameState == gp.player.BATTLE && gp.battleM.monsterIndex >= 0 && gp.battleM.monsterIndex < gp.monstersM.monsters.length && gp.monstersM.monsters[gp.battleM.monsterIndex] != null) {
			Entity currentMonster = gp.monstersM.monsters[gp.battleM.monsterIndex];
			int currentBattleState = gp.battleM.battleState;

			int playerDiceX = gp.screenWidth - gp.tileSize - 10;
			int playerDiceY = gp.screenHeight - gp.tileSize*2 - 50;
			if (currentBattleState == 0 || currentBattleState == 11 || currentBattleState == 13 || currentBattleState == 3 || currentBattleState == 4 ) {
				if (currentBattleState == 0) {
					if(gp.player.dice[0] > 0 && gp.player.dice[0] <= redDice.length && redDice[gp.player.dice[0]-1] != null)
						g2.drawImage(redDice[gp.player.dice[0]-1], playerDiceX - gp.tileSize -5, playerDiceY, gp.tileSize, gp.tileSize, null);
				} else {
					if(gp.player.dice[0] > 0 && gp.player.dice[0] <= redDice.length && redDice[gp.player.dice[0]-1] != null)
						g2.drawImage(redDice[gp.player.dice[0]-1], playerDiceX - gp.tileSize -5, playerDiceY, gp.tileSize, gp.tileSize, null);
					if(gp.player.dice[1] > 0 && gp.player.dice[1] <= blueDice.length && blueDice[gp.player.dice[1]-1] != null)
						g2.drawImage(blueDice[gp.player.dice[1]-1], playerDiceX, playerDiceY, gp.tileSize, gp.tileSize, null);
				}
			}

			String monsterHPText = currentMonster.symbol + " HP: " + currentMonster.hit_point + "/" + currentMonster.max_hit_point;
			g2.drawString(monsterHPText, 20, gp.screenHeight - 60);

			int monsterDiceX = 20;
			int monsterDiceY = gp.screenHeight - gp.tileSize*2 - 75;
			if (currentBattleState == 0 || currentBattleState == 2 || currentBattleState == 3) {
				if (currentBattleState == 0) {
					if(currentMonster.dice[0]>0 && currentMonster.dice[0] <= redDice.length && redDice[currentMonster.dice[0]-1] != null)
						g2.drawImage(redDice[currentMonster.dice[0]-1], monsterDiceX, monsterDiceY, gp.tileSize, gp.tileSize, null);
				} else {
					if(currentMonster.dice[0]>0 && currentMonster.dice[0] <= redDice.length && redDice[currentMonster.dice[0]-1] != null)
						g2.drawImage(redDice[currentMonster.dice[0]-1], monsterDiceX, monsterDiceY, gp.tileSize, gp.tileSize, null);
					if(currentMonster.dice[1]>0 && currentMonster.dice[1] <= blueDice.length && blueDice[currentMonster.dice[1]-1] != null)
						g2.drawImage(blueDice[currentMonster.dice[1]-1], monsterDiceX + gp.tileSize + 5, monsterDiceY, gp.tileSize, gp.tileSize, null);
				}
			}

			g2.setFont(smallFont);
			int messageY = gp.screenHeight - gp.tileSize - 90;
			String playerActionMsg = gp.battleM.getLastPlayerActionInfo();
			if (!playerActionMsg.isEmpty()) {
				g2.drawString(playerActionMsg, gp.screenWidth/2 - getCenteredTextXOffset(playerActionMsg, g2, smallFont), messageY);
				messageY -= 20;
			}
			String monsterAttackMsg = gp.battleM.getLastMonsterAttackInfo();
			if (!monsterAttackMsg.isEmpty()) {
				g2.drawString(monsterAttackMsg, gp.screenWidth/2 - getCenteredTextXOffset(monsterAttackMsg, g2, smallFont), messageY);
			}

			g2.setFont(font);
			String battlePrompt = "";
			if (currentBattleState == 0) battlePrompt = "Roll Initiative! (R)";
			else if (currentBattleState == 1) battlePrompt = "ACTION: (R)Attack | (1)Heal:"+BattleManager.HEAL_MANA_COST+"MP | (2)S.Atk:"+BattleManager.STRONG_ATTACK_MANA_COST+"MP | (Q)Potion";
			else if (currentBattleState == 2 && !gp.battleM.rolling) battlePrompt = currentMonster.symbol + "'s Turn...";
			else if (currentBattleState == 3) battlePrompt = "Simultaneous Roll! (R)";
			else if (currentBattleState == 4 && !gp.battleM.rolling && (gp.player.dice[0] == 0 && gp.player.dice[1] == 0) ) battlePrompt = "Roll to Heal HP! (R)";
			else if (gp.battleM.rolling && currentBattleState != 2 ) battlePrompt = "Player Rolling...";
			else if (gp.battleM.rolling && currentBattleState == 2) battlePrompt = currentMonster.symbol + " Rolling...";


			if (!battlePrompt.isEmpty()){
				g2.drawString(battlePrompt, gp.screenWidth/2 - getCenteredTextXOffset(battlePrompt, g2, font), gp.screenHeight - 30);
			}

		} else if (gp.player.gameState == gp.player.MOVE) {
			String movePrompt = "Press Q to use Potion";
			if (gp.player.canCastRevealMap()) {
				movePrompt += " | 3: Reveal (" + Player.REVEAL_MAP_MANA_COST + "MP)";
			}
			g2.drawString(movePrompt, gp.screenWidth/2 - getCenteredTextXOffset(movePrompt, g2, font), gp.screenHeight - 30);
		}
	}

	private int getCenteredTextXOffset(String text, Graphics2D g2, Font currentFont) {
		if (text == null || g2 == null || currentFont == null) return 0;
		Font oldFont = g2.getFont(); g2.setFont(currentFont);
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		g2.setFont(oldFont); return length / 2;
	}
	private int getXoffsetForRightAlignedText(String text, Graphics2D g2, Font currentFont) {
		if (text == null || g2 == null || currentFont == null) return 0;
		Font oldFont = g2.getFont(); g2.setFont(currentFont);
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		g2.setFont(oldFont); return length;
	}
	public void drawTransitionMessage(Graphics2D g2, String message) {
		if (message == null || g2 == null || largeFont == null || gp == null) return;
		g2.setFont(largeFont); g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);
		g2.setColor(Color.WHITE);
		int x = getXforCenteredText(message, g2, largeFont); int y = gp.screenHeight / 2;
		g2.drawString(message, x, y);
	}
	public int getXforCenteredText(String text, Graphics2D g2, Font customFont) {
		if (text == null || g2 == null || customFont == null || gp == null) return 0;
		Font oldFont = g2.getFont(); g2.setFont(customFont);
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		g2.setFont(oldFont); int x = (gp.screenWidth - length) / 2; return x;
	}
}