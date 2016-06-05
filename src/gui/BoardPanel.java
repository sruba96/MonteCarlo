package gui;

import data.Data;
import domain.Germ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pawel on 08.05.16.
 */
public class BoardPanel extends JPanel {

    private static final int PANELSIZE = Data.SIZE;
    private static final int CELLSIZE = Data.CELLSIZE;
    public Timer timer;

    public int[][] grid;
    public int[][] checkInCycle;
    public int[][] notOnEdge;

    private int width = PANELSIZE;
    private int height = PANELSIZE;

    private List<Germ> germList = new ArrayList<Germ>();
    private Random random = new Random();
    //    public Rules rules = new Rules(this);
    private int SIZE;
    private int boardSize;

    Random rand = new Random();

    public BoardPanel() {

        Germ germ = new Germ(Color.DARK_GRAY);
        germList.add(0, germ);


        timer = new Timer(50, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                grid = rules.generationUP(grid, periodic, selectedIndex);

                stepUp();
                repaint();
            }
        });

        clearAll();

//        grid = new int[SIZE][]
    }

    private void stepUp() {
        checkInCycle = new int[SIZE][SIZE];

        doStep();


    }

    private void doStep() {
        int x, y;


        for (int i = 0; i < boardSize-1;) {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);


            if (checkInCycle[x][y] == 0) {
                checkInCycle[x][y] = 1;
//                if (notOnEdge[x][y] == 0) {
                    int energy = checkEnergy(x, y, grid[x][y]);

                    if (energy == 0) {
                        notOnEdge[x][y] = 1;
                    } else {
                        int newGridId = randomGrid(x, y);

                        int newEnergy = checkEnergy(x, y, newGridId);


                        if (newEnergy < energy) {
                            grid[x][y] = newGridId;
                            repaint();
                        }

                        notOnEdge[x][y] = 0;
                    }


//                }

                i++;
            }
        }


    }

    private int randomGrid(int x, int y) {

        while (true) {

            int x2 = rand.nextInt(3) + x - 1;
            int y2 = rand.nextInt(3) + y - 1;

            if (x2 < 0)
                x2=SIZE;
            if (x2 >= SIZE)
                continue;


            if (y2 < 0)
                continue;
            if (y2 >= SIZE)
                continue;

            if (y2 == x && y == y2)
                continue;

            if (grid[x][y] == grid[x2][y2])
                continue;

            return grid[x2][y2];

        }
    }

    private int checkEnergy(int x, int y, int gridId) {

        int energy = 0;

        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {


                if (i < 0)
                    continue;
                if (i >= SIZE)
                    continue;


                if (j < 0)
                    continue;
                if (j >= SIZE)
                    continue;

                if (i == x && y == j)
                    continue;


                if (gridId != grid[i][j])
                    energy++;


            }
        }

        return energy;
    }

    public void clearAll() {
        this.SIZE = this.width / CELLSIZE;
        this.grid = new int[SIZE][SIZE];
        this.notOnEdge = new int[SIZE][SIZE];
//        this.germList = new ArrayList<Germ>();
        repaint();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                addLife(i, j);
            }
        }

        boardSize = SIZE * SIZE;
    }

    public void addLife(int x, int y) {

        Color c = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Germ germ = new Germ(c);


        grid[x][y] = germ.getLocalID();
        germList.add(germ.getLocalID(), germ);

        repaint();
    }

    @Override
    @Transient
    public Dimension getPreferredSize() {
        return new Dimension(grid.length * CELLSIZE, grid[0].length * CELLSIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color gColor = g.getColor();


//        if (recrystalizationIsRunning) {
//            //recrystalization painting
//        } else {

//            g.drawString("Pokolenie: " + generationCounter, 0, 10);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) {
                    g.setColor(germList.get(grid[i][j]).getColor());
                    g.fillRect(j * CELLSIZE, i * CELLSIZE, CELLSIZE, CELLSIZE);
                }
            }
        }
//        }
        g.setColor(gColor);
    }


    public int getSIZE() {
        return SIZE;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
