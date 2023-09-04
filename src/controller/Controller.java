package controller;

import graphic.*;
import algorithm.*;
import graphic.cells.*;
import algorithm.cells.*;
import algorithm.exceptions.*;


import java.io.IOException;

public class Controller {
    //MODEL
    private final KakuroSolver solver;
    private final KakuroLoader loader;
    //VIEWS
    private final Display game;
    private GraphicInputCell selectedCell;
    private NumberSelect numberPanel;
    //CONTROLLER
    private static Controller controller = null;

    private Controller() {
        loader = KakuroLoader.openLoader();
        solver = new KakuroSolver();
        game = new Display();
        game.display();
    }

    public static Controller getController() {
        if (controller == null)
            controller = new Controller();
        return controller;
    }

    /**
     * action triggered after clicking an input cell
     * opens the number select panel and updates selected cell
     */
    public void setSelectedCell(GraphicInputCell c) {
        selectedCell = c;
        numberPanel = NumberSelect.openNumberSelect();
        numberPanel.setVisible(true);
    }

    /**
     * action triggered after clicking a button on the number select panel
     * view and model update
     * @param n number clicked on the Number Panel
     */
    public void setSelectedNumber(int n) {
        selectedCell.updateText(String.valueOf(n));
        numberPanel.setVisible(false);

        //update value in model
        int row = selectedCell.getY()/selectedCell.getLength();
        int column = selectedCell.getX()/selectedCell.getLength();
        InputCell c = (InputCell)solver.getGrid()[row][column];
        c.setValue(n);

        //check if it's solved
        boolean isSolved = false;
        try {
            isSolved = solver.isSolved();
        } catch (NoSolutionException e) {
            game.setErrorText("This Grid Has No Solutions!");
        }

        //Check if person already won or there is a misplacement
        if (isSolved) {
            game.setWinText("You Won!");
        } else if (solver.checkMisplacement(row, column)) {
            game.setErrorText("I don't think that number goes there...");
        } else {
            game.setDefaultText();
        }

        selectedCell = null;
    }

    /**
     * action triggered after clicking reset on the number select panel
     * set's the selected cell's value to 0
     */
    public void resetValue() {
        selectedCell.updateText(" ");
        numberPanel.setVisible(false);

        //update value in model
        int row = selectedCell.getY()/selectedCell.getLength();
        int column = selectedCell.getX()/selectedCell.getLength();
        InputCell c = (InputCell)solver.getGrid()[row][column];
        c.setValue(0);

        selectedCell = null;
    }

    /**
     * action triggered after clicking cancel on the number select panel
     * closes the panel
     */
    public void cancelSelection() {
        selectedCell = null;
        numberPanel.setVisible(false);
    }

    /**
     * while constructing the controller set the initial grid to be the default one
     */
    public void setDefaultGraphicGrid() {
        game.setVisible(false);
        solver.setDefault();
        Cell[][] logicGrid = solver.getDefaultGrid();
        GraphicCell[][] graphicGrid = logicToGraphicGrid(logicGrid,solver.getRows(), solver.getColumns());
        game.updateGrid(graphicGrid,solver.getRows(),solver.getColumns());
        game.setVisible(true);
    }

    /**
     * action triggered after clicking on the Select Grid label or an element of the JComboBox of the different dimensions
     * thanks to the file loader loads either a random grid of the selected dimensions or the default grid
     * updates view and model
     * @param dimensions dimensions of the grid to load
     */
    public void loadRandomGrid(int dimensions) {
        try {
            Cell[][] loadedGrid;
            if (dimensions == 0)
                loadedGrid = loader.loadGrid(KakuroLoader.getDefaultGridFile());
            else
                loadedGrid = loader.loadRandomGrid(dimensions);
            solver.updateGrid(loadedGrid, loader.getRows(), loader.getColumns());
            updateGraphics();
            game.setWinText("Grid Loaded Successfully!");
        } catch (Exception e) {
            game.setErrorText("Error While Loading Grid");
        }
    }

    /**
     * action triggered by Reset Values button on the Options panel
     * sets all input cells to 0
     */
    public void resetValues() {
        Cell[][] logicGrid = solver.getGrid();
        GraphicCell[][] graphicGrid = game.getGraphicGrid();

        for (int i = 0; i < solver.getRows(); i++) {
            for (int j = 0; j < solver.getColumns(); j++) {
                if (graphicGrid[i][j].getType() == CellType.input) {
                    ((GraphicInputCell)graphicGrid[i][j]).updateText(" ");
                    ((InputCell)logicGrid[i][j]).setValue(0);
                }
            }
        }
        game.setDefaultText();
    }

    /**
     * action triggered by Auto Solve button on Options panel
     * loads the solution given by the kakuro solver
     */
    public void setSolution() {
        try {
            Cell[][] solution = solver.getSolution();
            GraphicCell[][] graphicGrid = game.getGraphicGrid();

            for (int i = 0; i < solver.getRows(); i++) {
                for (int j = 0; j < solver.getColumns(); j++) {
                    if (graphicGrid[i][j].getType() == CellType.input) {
                        int value = ((InputCell) solution[i][j]).getValue();
                        ((GraphicInputCell) graphicGrid[i][j]).updateText(String.valueOf(value));
                    }
                }
            }
            solver.copyGrid(solution);
        } catch (NoSolutionException e) {
            game.setErrorText("This Grid Has No Solutions!");
        }
    }

    /**
     * Action triggered by the Save Session button on the Options panel
     * writes current grid into the savedKakuro.kakuro file
     */
    public void saveCurrentGrid() {
        try {
            loader.saveFile(solver.getGrid(), solver.getRows(), solver.getColumns());
            game.setWinText("Grid Saved Successfully!");
        } catch (IOException e) {
            game.setErrorText("Error While Saving Grid");
        }
    }

    /**
     * Action triggered by Load Session button on the Options panel
     * resets the model and view to load the grid saved in the kakuro file savedKakuro.kakuro
     */
    public void loadSavedGrid() {
        try {
            Cell[][] loadedGrid = loader.loadSavedGrid();
            solver.updateGrid(loadedGrid,loader.getRows(),loader.getColumns());
            updateGraphics();
            game.setWinText("Grid Loaded Successfully!");
        } catch (Exception e) {
            game.setErrorText("Error While Loading Grid");
        }
    }

    /**
     * Given a model-type Cell matrix, it translates it into a view (graphic cell matrix)
     * @param grid model grid
     * @param rows vertical dimension
     * @param columns horizontal dimension
     * @return grid of graphic cells
     */
    private GraphicCell[][] logicToGraphicGrid(Cell[][] grid, int rows, int columns) {
        GraphicCell[][] graphicGrid = new GraphicCell[rows][columns];

        Cell cell;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch(grid[i][j].getType()) {
                    case unreachable:
                        graphicGrid[i][j] =
                                new GraphicUnreachableCell(0,0);
                        break;
                    case control:
                        cell = grid[i][j];
                        graphicGrid[i][j] = new GraphicControlCell(0,0,
                                ((ControlCell)cell).getVertical(),
                                ((ControlCell)cell).getHorizontal());
                        break;
                    case input:
                        cell = grid[i][j];
                        graphicGrid[i][j] = new GraphicInputCell(0,0,((InputCell)cell).getValue());
                        break;
                    case constant:
                        cell = grid[i][j];
                        graphicGrid[i][j] = new GraphicConstantCell(0,0,((ConstantCell)cell).getValue());
                        break;
                }
            }
        }
        return graphicGrid;
    }

    /**
     * Updates the view with a new grid after loading a new grid
     */
    private void updateGraphics() {
        game.setVisible(false);
        Cell[][] logicGrid = solver.getGrid();
        GraphicCell[][] graphicGrid = logicToGraphicGrid(logicGrid,solver.getRows(), solver.getColumns());
        game.updateGrid(graphicGrid,solver.getRows(),solver.getColumns());
        game.setVisible(true);
    }
}
