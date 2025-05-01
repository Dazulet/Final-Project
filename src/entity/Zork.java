package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import src.main.GamePanel;

public class Zork extends Entity{
    GamePanel gp;
    static final int number = 1;
    
    public Zork(GamePanel gp, int col, int row) {
        this.gp = gp;
        setDefaultValues(col, row);
        getImage();
    }
    
    public void setDefaultValues(int col, int row) {
        this.x = col*gp.tileSize;
        this.y = row*gp.tileSize;
        hit_point = 500;
        visable = false;
        direction = DOWN;
        symbol = ZORK;
    }
    
    

}
