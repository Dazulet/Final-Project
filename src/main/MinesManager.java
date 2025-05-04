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
    void setMines() {
        this.total_num = 5;
        this.mines = new Mine[this.total_num];
        Random random = new Random();

        while(true) {
            int var10000;
            int col;
            int row;
            int tileNum;
            do {
                do {
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        do {
                                            if (this.total_num <= 0) {
                                                return;
                                            }

                                            Objects.requireNonNull(this.gp);
                                            col = random.nextInt(30);
                                            Objects.requireNonNull(this.gp);
                                            row = random.nextInt(16);
                                            tileNum = this.gp.tileM.mapTileNum[col][row];
                                            Objects.requireNonNull(this.gp);
                                            var10000 = col * 48;
                                            Objects.requireNonNull(this.gp);
                                            if (var10000 != 17 * 48) {
                                                break;
                                            }

                                            Objects.requireNonNull(this.gp);
                                            var10000 = row * 48;
                                            Objects.requireNonNull(this.gp);
                                        } while(var10000 == 1 * 48);

                                        Objects.requireNonNull(this.gp);
                                        var10000 = col * 48;
                                        Objects.requireNonNull(this.gp);
                                        if (var10000 != 16 * 48) {
                                            break;
                                        }

                                        Objects.requireNonNull(this.gp);
                                        var10000 = row * 48;
                                        Objects.requireNonNull(this.gp);
                                    } while(var10000 == 1 * 48);

                                    Objects.requireNonNull(this.gp);
                                    var10000 = col * 48;
                                    Objects.requireNonNull(this.gp);
                                    if (var10000 != 17 * 48) {
                                        break;
                                    }
                                    
                                    Objects.requireNonNull(this.gp);
                                    var10000 = row * 48;
                                    Objects.requireNonNull(this.gp);
                                } while(var10000 == 2 * 48);

                                Objects.requireNonNull(this.gp);
                                var10000 = col * 48;
                                Objects.requireNonNull(this.gp);
                                if (var10000 != 16 * 48) {
                                    break;
                                }

                                Objects.requireNonNull(this.gp);
                                var10000 = row * 48;
                                Objects.requireNonNull(this.gp);
                            } while(var10000 == 2 * 48);

                            Objects.requireNonNull(this.gp);
                            var10000 = col * 48;
                            Objects.requireNonNull(this.gp);
                            if (var10000 != 1 * 48) {
                                break;
                            }

                            
                            Objects.requireNonNull(this.gp);
                            var10000 = row * 48;
                            Objects.requireNonNull(this.gp);
                        } while(var10000 == 10 * 48);

                        Objects.requireNonNull(this.gp);
                        var10000 = col * 48;
                        Objects.requireNonNull(this.gp);
                        if (var10000 != 1 * 48) {
                            break;
                        }

                        Objects.requireNonNull(this.gp);
                        var10000 = row * 48;
                        Objects.requireNonNull(this.gp);
                    } while(var10000 == 11 * 48);

                    Objects.requireNonNull(this.gp);
                    var10000 = col * 48;
                    Objects.requireNonNull(this.gp);
                    if (var10000 != 2 * 48) {
                        break;
                    }

                    Objects.requireNonNull(this.gp);
                    var10000 = row * 48;
                    Objects.requireNonNull(this.gp);
                } while(var10000 == 10 * 48);
                Objects.requireNonNull(this.gp);
                var10000 = col * 48;
                Objects.requireNonNull(this.gp);
                if (var10000 != 2 * 48) {
                    break;
                }

                Objects.requireNonNull(this.gp);
                var10000 = row * 48;
                Objects.requireNonNull(this.gp);
            } while(var10000 == 11 * 48);

            boolean allowedPlacement = true;

            int i;
            for(i = this.gp.monstersM.monsters.length - 1; i >= 0 && allowedPlacement && this.gp.monstersM.monsters[i] != null; --i) {
                var10000 = this.gp.monstersM.monsters[i].x;
                Objects.requireNonNull(this.gp);
                if (var10000 == col * 48) {
                    var10000 = this.gp.monstersM.monsters[i].y;
                    Objects.requireNonNull(this.gp);
                    if (var10000 == row * 48) {
                        allowedPlacement = false;
                    }
                }
            }

            for(i = this.mines.length - 1; i >= 0 && allowedPlacement && this.mines[i] != null; --i) {
                var10000 = this.mines[i].x;
                Objects.requireNonNull(this.gp);
                if (var10000 == col * 48) {
                    var10000 = this.mines[i].y;
                    Objects.requireNonNull(this.gp);
                    if (var10000 == row * 48) {
                        allowedPlacement = false;
                    }
                }
            }

            if (this.gp.tileM.tiles[tileNum].symbol == 'm' && allowedPlacement) {
                this.mines[--this.total_num] = new Mine(this.gp, col, row);
            }
        }
    }


}
