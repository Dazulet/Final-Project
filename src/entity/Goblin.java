package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import src.main.GamePanel;

public class Goblin extends Entity{
    
    GamePanel gp;
    public static final int number = 10;
    
    public Goblin(GamePanel gp, int col, int row) {
        this.gp = gp;
        setDefaultValues(col, row);
        getImage();
    }
    
    public void setDefaultValues(int col, int row) {
        this.x = col*gp.tileSize;
        this.y = row*gp.tileSize;
        hit_point = 100;
        visable = false;
        direction = UP;
        symbol = GOBLIN;
    }
  
  //public void getImage() 
  
  //public void draw(Graphics2D g2)
}
}
