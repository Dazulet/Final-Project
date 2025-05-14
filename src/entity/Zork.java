package src.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import src.main.GamePanel;

public class Zork extends Entity{
    GamePanel gp;


    private int initialCol, initialRow;

    public Zork(GamePanel gp, int col, int row) {
        this.gp = gp;
        this.initialCol = col;
        this.initialRow = row;
        setDefaultValues();
        getImage();
    }

    @Override
    public void setDefaultValues() {
        super.setDefaultValues();

        this.x = initialCol * gp.tileSize;
        this.y = initialRow * gp.tileSize;

        max_hit_point = 500;
        hit_point = max_hit_point;
        visable = false;

        symbol = ZORK;

        experienceDropped = 500;
        critChance = 0.15;
        critDamageMultiplier = 1.75;

    }

    public void getImage() {
        try {
            standingUp = new BufferedImage[2];
            standingUp[0] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing up 1.png"));
            standingUp[1] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing up 2.png"));

            standingDown = new BufferedImage[2];
            standingDown[0] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing down 1.png"));
            standingDown[1] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing down 2.png"));

            standingLeft = new BufferedImage[2];
            standingLeft[0] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing left 1.png"));
            standingLeft[1] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing left 2.png"));

            standingRight = new BufferedImage[2];
            standingRight[0] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing right 1.png"));
            standingRight[1] = ImageIO.read(getClass().getResourceAsStream("/res/zork/standing right 2.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }catch(IllegalArgumentException e) {
            System.err.println("Error loading zork images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if(!visable) return;

        if (gp != null && gp.player != null) {
            int entityTileCol = this.x / gp.tileSize;
            int entityTileRow = this.y / gp.tileSize;

            if (!gp.player.isMapRevealedTemporarily && !gp.player.isTileCurrentlyVisible(entityTileCol, entityTileRow)) {

                return;
            }
        } else { return; }


        spriteCounter++;
        if(spriteCounter > 12) {
            spriteNum = (spriteNum + 1) % 2;
            spriteCounter = 0;
        }

        BufferedImage image = null;
        switch(direction) {
            case UP:    image = standingUp[spriteNum];    break;
            case DOWN:  image = standingDown[spriteNum];  break;
            case LEFT:  image = standingLeft[spriteNum];  break;
            case RIGHT: image = standingRight[spriteNum]; break;
            default:    image = standingDown[spriteNum];  break;
        }

        if (image != null) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }
}