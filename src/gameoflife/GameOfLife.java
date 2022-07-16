package gameoflife;

import gameoflife.data.Position;
import gameoflife.rules.*;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GameOfLife extends PApplet {
    private static final int cellSize = 40;

    private List<IRule> rules;

    private boolean[][] grid;
    private int xLength;
    private int yLength;

    private boolean paused;
    private boolean nextGen;
    private int elapsed;
    private int generation;

    @Override
    public void setup() {
        rules = new ArrayList<>();
        addRule(new UnderPopulationRule());
        addRule(new OverPopulationRule());
        addRule(new LiveRule());
        addRule(new ReproductionRule());

        surface.setSize(1200, 800);
        xLength = width / cellSize;
        yLength = height / cellSize;
        grid = createGrid();

        paused = true;
        nextGen = false;
    }

    @Override
    public void draw() {
        background(60);

        renderCells();
        renderHud();

        if (nextGen || (!paused && elapsed % 10 == 0)) {
            updateCells();
            generation++;
            nextGen = false;
        }

        if (mousePressed) {
            modifyCell();
        }

        elapsed++;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKey() == ' ') {
            paused = !paused;
        }

        if (event.getKey() == '\t') {
            nextGen = true;
        }
    }

    private void addRule(IRule rule) {
        rules.add(rule);
        rules.sort((ruleA, ruleB) -> ruleB.priority() - ruleA.priority());
    }

    private void updateCells() {
        boolean[][] nextGeneration = createGrid();

        forEach((i, j) -> {
            boolean alive = grid[i][j];
            int aliveNeighbors = getAliveNeighborCount(i, j);
            
            for (IRule rule : rules) {
                if (rule.match(alive, aliveNeighbors)) {
                    nextGeneration[i][j] = rule.canLive();
                    break;
                }
            }
        });

        grid = nextGeneration;
    }

    private void renderCells() {
        forEach((i, j) -> {
            if (grid[i][j]) {
                fill(255);
                stroke(100);
                rect(i * cellSize, j * cellSize, cellSize - 1, cellSize - 1);
            }
        });
    }

    private void renderHud() {
        textSize(20);

        if (paused) {
            text("Paused", 40, 40);
        }

        text("Generation: " + generation, 40, 80);
    }
    
    
    
    
    private void printInfo(int x, int y) {
        System.out.println("Pos {" + x + ", " + y + "} | Alive neighbors: " + getAliveNeighborCount(x, y) + " | Is alive: " + grid[x][y]);
    }
    
    private List<Position> getNeighboringPositions(int x, int y) {
        List<Position> pos = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int k = x + i;
                int l = y + j;
                if (outOfRange(k, l)) continue;
                if (i == 0 && j == 0) continue;
                pos.add(new Position(k, l));
            }
        }

        return pos;
    }

    private int getAliveNeighborCount(int x, int y) {
        int count = 0;
        List<Position> poses = getNeighboringPositions(x, y);

        for (Position pos : poses) {
            if (grid[pos.x()][pos.y()]) {
                count++;
            }
        }

        return count;
    }

    private void modifyCell() {
        int x = mouseX / cellSize;
        int y = mouseY / cellSize;
        if (outOfRange(x, y)) return;
        grid[x][y] = mouseButton != RIGHT;
    }

    private boolean outOfRange(int x, int y) {
        return x >= xLength || y >= yLength || x < 0 || y < 0;
    }

    private boolean[][] createGrid() {
        return new boolean[xLength + 1][yLength + 1];
    }

    private void forEach(BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                action.accept(i, j);
            }
        }
    }
}
