package src.item;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import src.main.GamePanel;


public class Item {
    public int worldX, worldY;

    public BufferedImage image;
    public String name;
    public boolean collision = false;

    public boolean onMap = true;

    public GamePanel gp;


    public static final int TYPE_CONSUMABLE = 0;
    public static final int TYPE_WEAPON = 1;
    public static final int TYPE_ARMOR = 2;
    public int itemType;

    public Item(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        if (onMap && image != null && gp != null && gp.player != null && gp.player.revealedTiles != null) {
            int itemTileCol = worldX / gp.tileSize;
            int itemTileRow = worldY / gp.tileSize;

            boolean tileInBounds = itemTileCol >= 0 && itemTileCol < gp.maxScreenCol &&
                    itemTileRow >= 0 && itemTileRow < gp.maxScreenRow;

            boolean tileIsRevealed = false;
            if (tileInBounds) {
                tileIsRevealed = gp.player.revealedTiles[itemTileCol][itemTileRow];
            }


            if (!gp.player.isMapRevealedTemporarily && !tileIsRevealed) {
                return;

            }


            g2.drawImage(image, worldX, worldY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void pickUp() {
        onMap = false;
        System.out.println("Picked up: " + name);
    }

    public boolean useItem() {

        System.out.println("Trying to use item: " + name + " (no effect by default)");
        return false;

    }
}