package item;

import javax.imageio.ImageIO;
import java.io.IOException;
import src.main.GamePanel;

public class Armor extends Item {
    public int defenseBonus;

    public Armor(GamePanel gp, int worldX, int worldY, String name, String imagePath, int defenseBonus) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.name = name;
        this.itemType = TYPE_ARMOR;
        this.defenseBonus = defenseBonus;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            if (image == null) System.err.println("Armor image not found: " + imagePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading Armor image (" + name + "): " + e.getMessage());
        }
    }

    @Override
    public void pickUp() {
        super.pickUp();
        if (gp.player.equippedArmor == null || this.defenseBonus > gp.player.equippedArmor.defenseBonus) {
            gp.player.equipArmor(this);
            System.out.println("Equipped: " + this.name);
        } else {
            System.out.println(this.name + " is not better than current armor. (Not picked up/added to inventory yet)");
            onMap = true;
        }

    }

    @Override
    public boolean useItem() {
        System.out.println("Cannot 'use' armor like this. Equip it.");
        return false;
    }
}