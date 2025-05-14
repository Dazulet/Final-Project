package src.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.main.GamePanel;
import src.item.Weapon;
import src.item.Armor;

public class Player extends Entity{

	GamePanel gp;
	public int movingState;
	public int gameState;
	public final int MOVE = 0;
	public final int BATTLE = 1;
	public boolean forcedMove;

	public int level;
	public int experience;
	public int experienceToNextLevel;
	public int strength;
	public int dexterity;
	public int vitality;
	public int intelligence;
	public int mana;
	public int maxMana;

	public int hit_point;
	public int max_hit_point;

	public int healingPotions = 0;
	public Weapon equippedWeapon = null;
	public Armor equippedArmor = null;

	private int animationTick = 0;
	private final int ANIMATION_SPEED_WALK = 12;
	private final int ANIMATION_SPEED_IDLE = 15;

	public int visibilityRadius = 1;

	public boolean[][] revealedTiles;
	public static final int REVEAL_MAP_MANA_COST = 25;
	public int revealMapTimer = 0;
	public boolean isMapRevealedTemporarily = false;


	public Player(GamePanel gp) {
		this.gp = gp;
		setDefaultValues();
		getPlayerImage();
	}

	@Override
	public void setDefaultValues() {
		super.setDefaultValues();

		x = 17 * gp.tileSize;
		y = 1 * gp.tileSize;

		speed = 1;
		steps = 0;
		facing = true;
		direction = DOWN;
		symbol = HERO;
		gameState = MOVE;
		movingState = 0;
		forcedMove = false;
		spriteNum = 0;
		animationTick = 0;

		level = 1;
		experience = 0;
		experienceToNextLevel = 100;
		strength = 5;
		dexterity = 5;
		vitality = 50;
		intelligence = 5;
		healingPotions = 1;
		equippedWeapon = null;
		equippedArmor = null;

		updateMaxStatsAndCapCurrent();

		hit_point = max_hit_point;
		mana = maxMana;

		resetMapVisibility();
	}

	public void resetForNewLevel() {

		x = 17 * gp.tileSize;
		y = 1 * gp.tileSize;

		speed = 1;

		facing = true;
		direction = DOWN;
		gameState = MOVE;
		movingState = 0;
		forcedMove = false;
		moving = false;
		collisionOn = false;
		spriteNum = 0;
		animationTick = 0;

		updateMaxStatsAndCapCurrent();

		hit_point = max_hit_point;
		mana = maxMana;

		resetMapVisibility();
	}

	private void updateMaxStatsAndCapCurrent() {

		max_hit_point = 50 + (vitality * 10) + getArmorBonusHP();
		maxMana = 20 + (intelligence * 5);

		if (hit_point > max_hit_point) {
			hit_point = max_hit_point;
		}
		if (mana > maxMana) {
			mana = maxMana;
		}

		if (hit_point < 0) hit_point = 0;
		if (mana < 0) mana = 0;
	}

	private void resetMapVisibility() {
		if (gp != null) {

			if (revealedTiles == null || revealedTiles.length != gp.maxScreenCol || revealedTiles[0].length != gp.maxScreenRow) {
				revealedTiles = new boolean[gp.maxScreenCol][gp.maxScreenRow];
			}

			for (int i = 0; i < revealedTiles.length; i++) {
				for (int j = 0; j < revealedTiles[i].length; j++) {
					revealedTiles[i][j] = false;
				}
			}

			updateRevealedTiles();
		}

		revealMapTimer = 0;
		isMapRevealedTemporarily = false;
	}


	public int getWeaponAttackBonus() {
		return (equippedWeapon != null) ? equippedWeapon.attackBonus : 0;
	}

	public int getArmorDefenseBonus() {

		return (equippedArmor != null) ? equippedArmor.defenseBonus : 0;
	}

	public int getArmorBonusHP() {

		return 0;
	}

	public int getTotalStrength() {
		return strength + getWeaponAttackBonus();
	}



	public void gainExperience(int amount) {
		if (amount <= 0 || gp == null) return;
		experience += amount;
		System.out.println("Player gained " + amount + " XP. Total XP: " + experience + "/" + experienceToNextLevel);
		while (experience >= experienceToNextLevel) {
			levelUp();
		}
	}

	private void levelUp() {
		if (gp == null) return;
		level++;
		experience -= experienceToNextLevel;
		experienceToNextLevel = (int) (experienceToNextLevel * 1.5) + 50;

		strength += 1;
		dexterity += 1;
		vitality += 2;
		intelligence += 1;

		updateMaxStatsAndCapCurrent();

		hit_point = max_hit_point;
		mana = maxMana;

		critChance = 0.05 + (dexterity * 0.005);

		System.out.println("LEVEL UP! Player is now level " + level + "!");
		System.out.println("HP: " + hit_point + "/" + max_hit_point + " MP: " + mana + "/" + maxMana +
				" Str: " + strength + " Dex: " + dexterity + " Vit: " + vitality + " Int: " + intelligence);
		if (gp != null) gp.playSE(2);
	}

	public boolean canCastHeal(int manaCost) {
		return mana >= manaCost;
	}

	public void castHeal(int manaCost, int healAmountBase) {
		if (canCastHeal(manaCost)) {
			mana -= manaCost;
			int actualHeal = healAmountBase + intelligence * 2;

			hit_point += actualHeal;
			if (hit_point > max_hit_point) {
				hit_point = max_hit_point;
			}
			System.out.println("Player casts Heal! Restored " + actualHeal + " HP. Mana left: " + mana);

		} else {

			System.out.println("Player: Attempted to cast Heal, but not enough mana (should be checked by BattleManager).");
		}
	}

	public boolean canCastStrongAttack(int manaCost) {
		return mana >= manaCost;
	}

	public void consumeManaForStrongAttack(int manaCost) {
		if (canCastStrongAttack(manaCost)) {
			mana -= manaCost;
			System.out.println("Player uses mana for Strong Attack! Mana left: " + mana);
		}
	}

	public boolean canCastRevealMap() {
		return mana >= REVEAL_MAP_MANA_COST;
	}

	public void castRevealMap() {
		if (canCastRevealMap()) {
			mana -= REVEAL_MAP_MANA_COST;
			isMapRevealedTemporarily = true;
			revealMapTimer = 3 * gp.FPS;
			System.out.println("Player casts Reveal Map! Mana left: " + mana);

		} else {
			System.out.println("Not enough mana for Reveal Map!");

		}
	}

	public void useHealingPotion() {
		if (healingPotions > 0) {

			int potionHealAmount = 50 + intelligence;

			if (hit_point < max_hit_point) {
				int oldHp = hit_point;
				hit_point += potionHealAmount;
				if (hit_point > max_hit_point) {
					hit_point = max_hit_point;
				}
				healingPotions--;
				System.out.println("Used Healing Potion. Healed " + (hit_point - oldHp) + " HP. Potions left: " + healingPotions);
				if(gp != null) gp.playSE(1);
			} else {
				System.out.println("Player HP is full. Potion not used.");

			}
		} else {
			System.out.println("No healing potions left!");
			if(gp != null) gp.playSE(0);
		}
	}

	public void equipWeapon(Weapon newWeapon) {
		if (newWeapon == null) {
			System.out.println("Tried to equip a null weapon.");
			return;
		}

		this.equippedWeapon = newWeapon;
		System.out.println("Equipped: " + newWeapon.name + " (Attack Bonus: +" + newWeapon.attackBonus + ")");

	}

	public void equipArmor(Armor newArmor) {
		if (newArmor == null) {
			System.out.println("Tried to equip null armor.");
			return;
		}

		this.equippedArmor = newArmor;
		System.out.println("Equipped: " + newArmor.name + " (Defense Bonus: +" + newArmor.defenseBonus + ")");

		updateMaxStatsAndCapCurrent();
	}

	public void updateRevealedTiles() {
		if (revealedTiles == null || gp == null) return;

		int playerTileCol = getX() / gp.tileSize;
		int playerTileRow = getY() / gp.tileSize;

		for (int r_offset = -visibilityRadius; r_offset <= visibilityRadius; r_offset++) {
			for (int c_offset = -visibilityRadius; c_offset <= visibilityRadius; c_offset++) {


				int checkCol = playerTileCol + c_offset;
				int checkRow = playerTileRow + r_offset;

				if (checkCol >= 0 && checkCol < gp.maxScreenCol && checkRow >= 0 && checkRow < gp.maxScreenRow) {
					revealedTiles[checkCol][checkRow] = true;
				}
			}
		}
	}

	public boolean isTileCurrentlyVisible(int tileCol, int tileRow) {
		if (isMapRevealedTemporarily) return true;

		int playerTileCol = getX() / gp.tileSize;
		int playerTileRow = getY() / gp.tileSize;

		int dCol = Math.abs(tileCol - playerTileCol);
		int dRow = Math.abs(tileRow - playerTileRow);

		return dCol <= visibilityRadius && dRow <= visibilityRadius;

	}



	public void getPlayerImage() {
		try {
			runningUp = new BufferedImage[4];
			runningUp[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running up 1.png"));
			runningUp[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running up 2.png"));
			runningUp[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running up 3.png"));
			runningUp[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running up 4.png"));
			runningDown = new BufferedImage[4];
			runningDown[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running down 1.png"));
			runningDown[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running down 2.png"));
			runningDown[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running down 3.png"));
			runningDown[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running down 4.png"));
			runningLeft = new BufferedImage[4];
			runningLeft[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running left 1.png"));
			runningLeft[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running left 2.png"));
			runningLeft[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running left 3.png"));
			runningLeft[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running left 4.png"));
			runningRight = new BufferedImage[4];
			runningRight[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running right 1.png"));
			runningRight[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running right 2.png"));
			runningRight[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running right 3.png"));
			runningRight[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/running right 4.png"));
			standingUp = new BufferedImage[2];
			standingUp[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing up 1.png"));
			standingUp[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing up 2.png"));
			standingDown = new BufferedImage[2];
			standingDown[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing down 1.png"));
			standingDown[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing down 2.png"));
			standingLeft = new BufferedImage[6];
			standingLeft[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 1.png"));
			standingLeft[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 2.png"));
			standingLeft[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 3.png"));
			standingLeft[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 4.png"));
			standingLeft[4] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 5.png"));
			standingLeft[5] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing left 6.png"));
			standingRight = new BufferedImage[6];
			standingRight[0] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 1.png"));
			standingRight[1] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 2.png"));
			standingRight[2] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 3.png"));
			standingRight[3] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 4.png"));
			standingRight[4] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 5.png"));
			standingRight[5] = ImageIO.read(getClass().getResourceAsStream("/res/hero/standing right 6.png"));
		}catch(IOException e) {
			e.printStackTrace();
		}catch(IllegalArgumentException e) {
			System.err.println("Error loading player images: Resource not found. Check paths in /res/hero/. " + e.getMessage());

			e.printStackTrace();
		}
	}

	public void update() {
		if(gp == null || gp.keyH == null || gp.cChecker == null) return;

		if (isMapRevealedTemporarily) {
			revealMapTimer--;
			if (revealMapTimer <= 0) {
				isMapRevealedTemporarily = false;
				revealMapTimer = 0;
				System.out.println("Reveal Map effect wore off.");
			}
		}

		if (gameState == MOVE) {
			if (gp.keyH.usePotionPressed) {
				useHealingPotion();
				gp.keyH.usePotionPressed = false;
			}
			if (gp.keyH.spell3Pressed) {
				castRevealMap();
				gp.keyH.spell3Pressed = false;
			}
		}

		if(movingCounter == 0) { moving = false; }

		if(forcedMove) {
			if (!moving) { moving = true; movingCounter = 0; spriteNum = 0; }
			forcedMove = false;
		}

		if(!moving) {
			if((gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed)
					&& movingState == 0 && gameState == MOVE) {

				if(gp.keyH.upPressed) direction = UP;
				else if(gp.keyH.downPressed) direction = DOWN;
				else if(gp.keyH.rightPressed) direction = RIGHT;
				else if(gp.keyH.leftPressed) direction = LEFT;

				gp.cChecker.checkTile();
				if(!collisionOn) {
					if (gp!=null) gp.playSE(2);
					moving = true;
					movingCounter = 0;
					spriteNum = 0;
				}
			}
		}

		animationTick++;

		if(moving) {

			switch(direction) {
				case UP: y -= speed; break;
				case DOWN: y += speed; break;
				case RIGHT: x += speed; break;
				case LEFT: x -= speed; break;
			}
			movingCounter += speed;

			if(movingCounter >= gp.tileSize) {
				movingCounter = 0;


				steps++;
				updateRevealedTiles();

				gp.cChecker.checkVillages();
				gp.cChecker.checkMines();
				gp.cChecker.checkItems();

				if(movingState == 0 && gameState == BATTLE)
					movingState = 1;
			}

			if (animationTick >= ANIMATION_SPEED_WALK) {
				spriteNum = (spriteNum + 1) % 4;
				animationTick = 0;
			}
		} else {
			if (animationTick >= ANIMATION_SPEED_IDLE) {

				if (direction != null && (direction.equals(LEFT) || direction.equals(RIGHT))) {
					spriteNum = (spriteNum + 1) % 6;
				} else {
					spriteNum = (spriteNum + 1) % 2;
				}
				animationTick = 0;
			}
		}

		updateMaxStatsAndCapCurrent();
	}

	@Override
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int currentFrame = spriteNum;

		if(moving) {
			currentFrame = spriteNum % 4;
			BufferedImage[] activeArray = null;
			if(facing) {
				switch(direction) {
					case UP:    activeArray = runningUp;    break;
					case DOWN:  activeArray = runningDown;  break;
					case LEFT:  activeArray = runningLeft;  break;
					case RIGHT: activeArray = runningRight; break;
					default:    activeArray = runningDown;  break;
				}
			} else {
				switch(direction) {
					case DOWN:  activeArray = runningUp;    break;
					case UP:    activeArray = runningDown;  break;
					case RIGHT: activeArray = runningLeft;  break;
					case LEFT:  activeArray = runningRight; break;
					default:    activeArray = runningUp;    break;
				}
			}

			if (activeArray != null && activeArray.length > currentFrame && activeArray[currentFrame] != null) {
				image = activeArray[currentFrame];
			} else if (activeArray != null && activeArray.length > 0 && activeArray[0] != null) {
				image = activeArray[0];
			} else if (runningDown != null && runningDown.length > 0) {
				image = runningDown[0];
			}

		} else {
			BufferedImage[] activeArray = null;
			int frameCount = 2;
			if (direction != null && (direction.equals(LEFT) || direction.equals(RIGHT))) {
				frameCount = 6;
			}
			currentFrame = spriteNum % frameCount;

			if(facing) {
				switch(direction) {
					case UP:    activeArray = standingUp;    frameCount = 2; break;
					case DOWN:  activeArray = standingDown;  frameCount = 2; break;
					case LEFT:  activeArray = standingLeft;  frameCount = 6; break;
					case RIGHT: activeArray = standingRight; frameCount = 6; break;
					default:    activeArray = standingDown;  frameCount = 2; break;
				}
			} else {
				switch(direction) {
					case DOWN:  activeArray = standingUp;    frameCount = 2; break;
					case UP:    activeArray = standingDown;  frameCount = 2; break;
					case RIGHT: activeArray = standingLeft;  frameCount = 6; break;
					case LEFT:  activeArray = standingRight; frameCount = 6; break;
					default:    activeArray = standingUp;    frameCount = 2; break;
				}
			}

			currentFrame = spriteNum % frameCount;

			if (activeArray != null && activeArray.length > currentFrame && activeArray[currentFrame] != null) {
				image = activeArray[currentFrame];
			} else if (activeArray != null && activeArray.length > 0 && activeArray[0] != null) {
				image = activeArray[0];
			} else if (standingDown != null && standingDown.length > 0) {
				image = standingDown[0];
			}
		}

		if (image != null && gp != null) {
			g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
		} else if (gp != null) {

			System.err.println("Player image is null for direction: " + direction + ", moving: " + moving + ", frame: " + currentFrame);
		}
	}

	public void changeFacing() {
		facing = !facing;

		if (direction != null) {
			switch (direction){
				case UP:    direction = Entity.DOWN;  break;
				case DOWN:  direction = Entity.UP;    break;
				case LEFT:  direction = Entity.RIGHT; break;
				case RIGHT: direction = Entity.LEFT;  break;
			}
		} else {

			direction = facing ? Entity.DOWN : Entity.UP;
		}

		spriteNum = 0;
		animationTick = 0;
	}
}