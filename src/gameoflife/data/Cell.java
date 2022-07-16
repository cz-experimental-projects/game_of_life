package gameoflife.data;

import gameoflife.GameOfLife;
import processing.core.PApplet;

import static gameoflife.GameOfLife.cellSize;

public class Cell {
    private boolean alive;
    private boolean wasAlive;
    private boolean wasDead;
    private float alpha;
    
    private final int x;
    private final int y;
    
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = false;
        this.wasAlive = false; 
        this.wasDead = false;
        this.alpha = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.wasAlive = true;
        this.wasDead = false;
    }
    
    public void resurrect() {
        this.alive = true;
        this.wasAlive = false;
        this.wasDead = true;
    }
    
    public void setAlive(boolean alive) {
        if (alive) {
            resurrect();
            return;
        }
        
        die();
    }
    
    public void render(GameOfLife applet) {
        if (wasDead) {
            alpha += applet.getConfig().fadeSpeed();
            if (alpha >= 1) {
                alpha = 1;
                wasDead = false;
            }
        }

        if (wasAlive) {
            alpha -= applet.getConfig().fadeSpeed();
            if (alpha <= 0) {
                alpha = 0;
                wasAlive = false;
                alive = false;
            }
        }
        
        if (isAlive()) {
            applet.fill(255, 255, 255, 255 * alpha);
            applet.stroke(100);
            applet.rect(x * cellSize, y * cellSize, cellSize - 1, cellSize - 1);
        }
    }
}
