package graphic;

import graphic.cells.*;
import javax.swing.*;
import java.awt.*;


public class GameGrid extends JPanel {
    private int rows,columns;
    private GraphicCell[][] grid;
    private GridBagConstraints insertionConstraints;
    private static int activeLength;

    public GameGrid() {
        this(new GraphicCell[1][1],1,1,GraphicCell.defaultLength);
    }

    /**
     * Creates and initializes the dimensions of the grid
     * @param size
     */
    public GameGrid(int size) {
        this(new GraphicCell[size][size],size,size,GraphicCell.defaultLength);
    }

    /**
     * Creates and initializes the rows and columns of the grid
     * @param columns
     * @param rows
     */
    public GameGrid(int columns, int rows) {
        this(new GraphicCell[columns][rows],columns,rows,GraphicCell.defaultLength);
    }

    public GameGrid(GraphicCell[][] grid, int columns, int rows) {
        this(grid,columns,rows,GraphicCell.defaultLength);
    }

    /**
     * Creates and sets the dimensions and grid matrix of the game grid
     * @param grid
     * @param columns
     * @param rows
     * @param length
     */
    public GameGrid(GraphicCell[][] grid, int columns, int rows, int length) {
        setSize( columns * activeLength + 5, rows * activeLength + 5);

        setPreferredSize(new Dimension( columns * activeLength + 5,
                rows * activeLength + 5));

        setLayout(new GridBagLayout());

        activeLength = length;
        this.rows = rows;
        this.columns = columns;
        this.grid = grid;

        if (isInit())
            updateDisplay();
    }

    /**
     * Updates the JPanel by resetting the grid if the controller was notified
     */
    public void updateDisplay() {
        removeAll();
        addCells();
        repaint();
    }

    /**
     * Adds all cells to the panel while respecting the active length
     */
    public void addCells() {
        insertionConstraints = new GridBagConstraints();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j].setX(j*activeLength);
                grid[i][j].setY(i*activeLength);

                insertionConstraints.fill = GridBagConstraints.VERTICAL;
                insertionConstraints.gridx = j;
                insertionConstraints.gridy = i;
                add(grid[i][j],insertionConstraints);
            }
        }
    }

    public GraphicCell[][] getGrid() {
        return grid;
    }

    private boolean isInit() {
        return grid[0][0] != null;
    }
}
