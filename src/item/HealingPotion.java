package item;

import javax.imageio.ImageIO;
import java.io.IOException;
import src.main.GamePanel;
import src.entity.Player;

public class HealingPotion extends Item {
    public int healAmount;

    public HealingPotion(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.name = "Healing Potion";
        this.itemType = TYPE_CONSUMABLE;
        this.healAmount = 50 + gp.player.intelligence * 2;

        try {

            image = ImageIO.read(getClass().getResourceAsStream("/res/items/potion_red.png"));
            if (image == null) System.err.println("HealingPotion image not found!");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading HealingPotion image: " + e.getMessage());
        }
    }

    @Override
    public void pickUp() {
        super.pickUp();
        gp.player.healingPotions++;

    }

    @Override
    public boolean useItem() {
        if (gp.player.hit_point < gp.player.max_hit_point) {
            int actualHeal = this.healAmount;
            gp.player.hit_point += actualHeal;
            if (gp.player.hit_point > gp.player.max_hit_point) {
                gp.player.hit_point = gp.player.max_hit_point;
            }
            System.out.println("Used Healing Potion. Healed " + actualHeal + " HP.");

            return true;
        } else {
            System.out.println("Player HP is full. Potion not used.");
            return false;
        }
    }
}