package entity;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import src.main.GamePanel;

public class Gargoyle extends Entity{
    
    GamePanel gp;
    public static final int number = 5;
    
    public Gargoyle(GamePanel gp, int col, int row) {
        this.gp = gp;
        setDefaultValues(col, row);
        getImage();
    }


public void setDefaultValues(int col, int row) {
        this.x = col*gp.tileSize;
        this.y = row*gp.tileSize;
        hit_point = 150;
        visable = false;
        direction = LEFT;
        symbol = GARGOYLE;
    }
