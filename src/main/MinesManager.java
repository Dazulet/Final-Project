package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import src.entity.Mine;

public class MinesManager {
    GamePanel gp;
    public Mine[] mines;
    int total_num;
    boolean explosion;
    int explosionX;
    int explosionY;
    int explosionCounter;
    BufferedImage[] images;
  
    public MinesManager(GamePanel gp) {
        this.gp = gp;
        this.explosionCounter = 0;
        this.setMines();
        this.setImages();
    }
    
    void setImages() {
        this.images = new BufferedImage[13];

        try {
            for(int i = 0; i < 13; ++i) {
                String path = "Dungeon Survivor/res/explosion/" + (i + 1) + ".png";
                this.images[i] = ImageIO.read(new File(path));
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

}
