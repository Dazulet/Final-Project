package src.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import src.entity.Mine;
import src.entity.Entity;
import src.tile.TileManager;

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
        explosionCounter = 0;
        explosion = false;
        loadExplosionImages();
        setMines();
    }

        void loadExplosionImages() {
        images = new BufferedImage[13];
        try {

            for (int i = 0; i < 13; i++) {
                String path = "/res/explosion/" + (i + 1) + ".png";
                images[i] = ImageIO.read(getClass().getResourceAsStream(path));
                if (images[i] == null) {
                    System.err.println("Could not load explosion image: " + path);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading explosion images: " + e.getMessage());
            e.printStackTrace();
            images = null;
        }
    }

        void setMines() {
        if (gp == null || gp.tileM == null || gp.monstersM == null) {
            System.err.println("MinesManager: Cannot set mines, critical components missing (gp, tileM, or monstersM).");
            mines = new Mine[0];
            total_num = 0;
            return;
        }

        total_num = Mine.number;
        mines = new Mine[total_num];
        if (total_num == 0) {
            System.out.println("MinesManager: Number of mines is set to 0.");
            return;
        }

        Random random = new Random();
        int placedCount = 0;
        int attempts = 0;
        final int MAX_ATTEMPTS_PER_MINE = 100;

        while(placedCount < total_num && attempts < MAX_ATTEMPTS_PER_MINE * total_num) {
            attempts++;
            int col = random.nextInt(gp.maxScreenCol);
            int row = random.nextInt(gp.maxScreenRow);

            if (gp.tileM.mapTileNum == null || col < 0 || col >= gp.maxScreenCol || row < 0 || row >= gp.maxScreenRow) continue;
            int tileNumOnMap = gp.tileM.mapTileNum[col][row];
            if (tileNumOnMap < 0 || tileNumOnMap >= gp.tileM.tiles.length || gp.tileM.tiles[tileNumOnMap] == null) continue;

            if (gp.tileM.tiles[tileNumOnMap].symbol != TileManager.map_tile) {
                continue;
            }

            boolean isForbiddenZone = checkForbiddenZones(col, row);
            if (isForbiddenZone) continue;

            boolean overlapsMonster = checkMonsterOverlap(col, row);
            if (overlapsMonster) continue;

            boolean overlapsMine = checkMineOverlap(col, row, placedCount);
            if (overlapsMine) continue;

            mines[placedCount] = new Mine(gp, col, row);

            placedCount++;
            attempts = 0;
        }

        if (placedCount < total_num) {
            System.err.println("MinesManager: Could not place all " + total_num + " mines. Placed: " + placedCount);

            Mine[] actualMines = new Mine[placedCount];
            System.arraycopy(mines, 0, actualMines, 0, placedCount);
            mines = actualMines;
            total_num = placedCount;
        } else {

        }
    }

        private boolean checkForbiddenZones(int col, int row) {

        if ((col == 17 && row == 1) || (col == 16 && row == 1) ||
                (col == 17 && row == 2) || (col == 16 && row == 2)) {
            return true;
        }

        Entity boss = (gp.monstersM != null && gp.monstersM.monsters != null && gp.monstersM.monsters.length > 0) ? gp.monstersM.monsters[0] : null;
        if (boss != null && boss.symbol == Entity.ZORK) {
            int bossCol = boss.getX() / gp.tileSize;
            int bossRow = boss.getY() / gp.tileSize;

            if (Math.abs(col - bossCol) <= 1 && Math.abs(row - bossRow) <= 1) {
                return true;
            }
        }
        return false;
    }

        private boolean checkMonsterOverlap(int col, int row) {
        if (gp.monstersM != null && gp.monstersM.monsters != null) {
            for(Entity monster : gp.monstersM.monsters) {
                if(monster != null) {
                    if(monster.getX() == col*gp.tileSize && monster.getY() == row*gp.tileSize) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

        private boolean checkMineOverlap(int col, int row, int currentPlacedCount) {
        if (mines != null) {
            for(int i = 0; i < currentPlacedCount; i++) {
                if(mines[i] != null) {
                    if(mines[i].getX() == col*gp.tileSize && mines[i].getY() == row*gp.tileSize) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

        public void setExplosion(Mine mine) {
        if (mine == null) return;
        explosion = true;
        explosionX = mine.getX();
        explosionY = mine.getY();
        explosionCounter = 0;
        if(gp != null) gp.playSE(3);
    }

        public void explode(Graphics2D g2) {
        if (!explosion || images == null || images.length == 0 || gp == null) {
            return;
        }

        if (images[0] == null) {
            System.err.println("Explosion images not loaded, cannot draw explosion.");
            explosion = false;
            return;
        }

        int frameIndex = explosionCounter % images.length;

        if (images[frameIndex] != null) {
            g2.drawImage(images[frameIndex], explosionX, explosionY, gp.tileSize, gp.tileSize, null);
        } else {

        }

        explosionCounter++;

        if(explosionCounter >= images.length * 3 + 5) {
            explosionCounter = 0;
            explosion = false;

            if (gp.player != null && gp.player.hit_point <= 0) {
                if (gp.panelState != gp.END) {
                    gp.player.hit_point = 0;
                    gp.es.setTitleTexts("You Died on a Mine! :(\nSteps: " + gp.player.steps + " Lvl: " + gp.player.level);
                    gp.panelState = gp.END;
                }
            }
        }
    }
}