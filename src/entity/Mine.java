package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import src.main.GamePanel;

public class Mine extends Entity {
    GamePanel gp;
    public static final int number = 5;
    final int damage = 100;

    public Mine(GamePanel gp, int col, int row) {
        this.gp = gp;
        Objects.requireNonNull(gp);
        this.x = col * 48;
        Objects.requireNonNull(gp);
        this.y = row * 48;
        this.symbol = 'm';
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.black);
        int var10001 = this.x;
        int var10002 = this.y;
        Objects.requireNonNull(this.gp);
        Objects.requireNonNull(this.gp);
        g2.fillRect(var10001, var10002, 48, 48);
    }
}
