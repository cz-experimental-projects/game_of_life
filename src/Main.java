import gameoflife.GameOfLife;
import processing.core.PApplet;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        GameOfLife.file = new File(Main.class.getResource("config.json").getPath());
        PApplet.main("gameoflife.GameOfLife");
    }
}
