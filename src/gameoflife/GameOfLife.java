package gameoflife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameoflife.config.GameOfLifeConfig;
import gameoflife.data.Cell;
import gameoflife.data.Position;
import gameoflife.rules.*;
import gameoflife.rules.text.TextBirthRule;
import gameoflife.rules.text.TextSurvivalRule;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GameOfLife extends PApplet {
    public static final int cellSize = 20;
    public static File file;

    private List<IRule> rules;
    private Cell[][] grid;
    private GameOfLifeConfig config;

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
        addRule(new SurviveRule());
        addRule(new ReproductionRule());
        
        loadOrCreateConfig();

        surface.setSize(1200, 800);
        xLength = width / cellSize;
        yLength = height / cellSize;
        grid = createGrid();

        paused = true;
        nextGen = false;
        
        frameRate(240);
    }

    @Override
    public void draw() {
        background(20);

        renderCells();
        renderHud();

        if (nextGen || (!paused && elapsed % config.delayBetweenUpdates() == 0)) {
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
        Cell[][] nextGeneration = createGrid();

        forEach((i, j) -> {
            boolean alive = grid[i][j].isAlive();
            int aliveNeighbors = getAliveNeighborCount(i, j);
            boolean anyMatch = false;

            for (IRule rule : rules) {
                if (rule.match(alive, aliveNeighbors)) {
                    nextGeneration[i][j].setAlive(rule.canLive());
                    anyMatch = true;
                    break;
                }
            }

            if (!anyMatch) {
                nextGeneration[i][j].setAlive(false);
            }
        });

        grid = nextGeneration;
    }

    private void renderCells() {
        forEach((i, j) -> {
            grid[i][j].render(this);
        });
    }

    private void renderHud() {
        textSize(20);
        fill(255, 255, 255, 255);

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
            if (grid[pos.x()][pos.y()].isAlive()) {
                count++;
            }
        }

        return count;
    }

    private void modifyCell() {
        int x = mouseX / cellSize;
        int y = mouseY / cellSize;
        if (outOfRange(x, y)) return;
        grid[x][y].setAlive(mouseButton != RIGHT);
    }

    private boolean outOfRange(int x, int y) {
        return x >= xLength || y >= yLength || x < 0 || y < 0;
    }

    private void forEach(BiConsumer<Integer, Integer> action) {
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                action.accept(i, j);
            }
        }
    }

    private Cell[][] createGrid() {
        Cell[][] g = new Cell[xLength + 1][yLength + 1];
        for (int j = 0; j < g.length; j++) {
            Cell[] arr = g[j];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = new Cell(j, i);
            }
        }
        return g;
    }

    private void loadOrCreateConfig() {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

        try {
            config = gson.fromJson(Files.readString(file.toPath()), GameOfLifeConfig.class);
        } catch (IOException ignored) {
            config = new GameOfLifeConfig(0.05f, new String[0], false, 8);
        }

        if (config.removeDefaultRules()) {
            rules.clear();
        }

        for (String rule : config.rules()) {
            String[] rules = rule.split("/");
            addRule(new TextSurvivalRule(rules[0]));
            addRule(new TextBirthRule(rules[1]));
        }
    }

    public GameOfLifeConfig getConfig() {
        return config;
    }
}
