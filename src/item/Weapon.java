package item;

import javax.imageio.ImageIO;
import java.io.IOException;
import src.main.GamePanel;

public class Weapon extends Item {
    public int attackBonus;

    public Weapon(GamePanel gp, int worldX, int worldY, String name, String imagePath, int attackBonus) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.name = name;
        this.itemType = TYPE_WEAPON;
        this.attackBonus = attackBonus;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            if (image == null) System.err.println("Weapon image not found: " + imagePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading Weapon image (" + name + "): " + e.getMessage());
        }
    }

    @Override
    public void pickUp() {
        super.pickUp();

        if (gp.player.equippedWeapon == null || this.attackBonus > gp.player.equippedWeapon.attackBonus) {
            gp.player.equipWeapon(this);
            System.out.println("Equipped: " + this.name);
        } else {
            System.out.println(this.name + " is not better than current weapon. (Not picked up/added to inventory yet)");

            onMap = true;

        }


    }


    @Override
    public boolean useItem() {
        System.out.println("Cannot 'use' a weapon like this. Equip it.");
        return false;
    }
}