package src.main;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import src.item.Item;
import src.item.HealingPotion;
import src.item.Weapon;
import src.item.Armor;
import src.entity.Entity;

public class ItemManager {
    GamePanel gp;
    public List<Item> itemsOnMap;

    public ItemManager(GamePanel gp) {
        this.gp = gp;
        itemsOnMap = new ArrayList<>();
        placeInitialItems();
    }

    public void placeInitialItems() {
        if (gp == null) return;

        itemsOnMap.clear();

        if (gp.currentLevel == 1) {

            if (isValidPlacement(15, 2)) itemsOnMap.add(new HealingPotion(gp, 15 * gp.tileSize, 2 * gp.tileSize));
            if (isValidPlacement(3, 3)) itemsOnMap.add(new Weapon(gp, 3 * gp.tileSize, 3 * gp.tileSize, "Short Sword", "/res/items/sword_short.png", 5));
        } else if (gp.currentLevel == 2) {
            if (isValidPlacement(8, 8)) itemsOnMap.add(new HealingPotion(gp, 8 * gp.tileSize, 8 * gp.tileSize));
            if (isValidPlacement(12, 5)) itemsOnMap.add(new Armor(gp, 12 * gp.tileSize, 5 * gp.tileSize, "Chain Mail", "/res/items/armor_chain.png", 5));
            if (isValidPlacement(1, 1)) itemsOnMap.add(new Weapon(gp, 1 * gp.tileSize, 1 * gp.tileSize, "Long Sword", "/res/items/sword_long.png", 8));
        } else if (gp.currentLevel == 3) {

            if (isValidPlacement(5, 10)) itemsOnMap.add(new HealingPotion(gp, 5 * gp.tileSize, 10 * gp.tileSize));
            if (isValidPlacement(10, 12)) itemsOnMap.add(new Weapon(gp, 10 * gp.tileSize, 12 * gp.tileSize, "Great Sword", "/res/items/sword_great.png", 12));
            if (isValidPlacement(1, 14)) itemsOnMap.add(new Armor(gp, 1 * gp.tileSize, 14 * gp.tileSize, "Plate Armor", "/res/items/armor_plate.png", 8));
        }

        System.out.println("ItemManager: Placed " + itemsOnMap.size() + " items for level " + gp.currentLevel);
    }

        private boolean isValidPlacement(int col, int row) {
        if (gp == null || gp.tileM == null || gp.tileM.mapTileNum == null || gp.tileM.tiles == null) return false;
        if (col < 0 || col >= gp.maxScreenCol || row < 0 || row >= gp.maxScreenRow) return false;

        int tileNum = gp.tileM.mapTileNum[col][row];
        if (tileNum < 0 || tileNum >= gp.tileM.tiles.length || gp.tileM.tiles[tileNum] == null) return false;

        return gp.tileM.tiles[tileNum].symbol == gp.tileM.map_tile && !gp.tileM.tiles[tileNum].collision;
    }


    public void update() {

    }

    public void draw(Graphics2D g2) {
        if (itemsOnMap == null || gp == null) return;
        for (Item item : itemsOnMap) {
            if (item != null && item.onMap) {
                item.draw(g2);
            }
        }
    }

    public void checkAndPickUpItem(Entity player) {
        if (itemsOnMap == null || player == null || gp == null) return;

        int playerCenterX = player.getX() + gp.tileSize / 2;
        int playerCenterY = player.getY() + gp.tileSize / 2;

        for (int i = itemsOnMap.size() - 1; i >= 0; i--) {
            Item item = itemsOnMap.get(i);
            if (item != null && item.onMap) {
                int itemCenterX = item.worldX + gp.tileSize / 2;
                int itemCenterY = item.worldY + gp.tileSize / 2;

                if (Math.abs(playerCenterX - itemCenterX) < gp.tileSize / 2 &&
                        Math.abs(playerCenterY - itemCenterY) < gp.tileSize / 2) {

                    item.pickUp();

                    break;
                }
            }
        }
    }
}