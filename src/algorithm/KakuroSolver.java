package algorithm;

import algorithm.cells.*;
import algorithm.exceptions.NoSolutionException;
import controller.CellType;

public class KakuroSolver {
    //dimensions
    private int rows,columns;
    private Cell[][] grid;
    private Cell[][] solution;
    private ConstraintsChecker constraints;
    private boolean solutionReady = false;

    
    /**
     * Generates default grid as seen in the pdf
     */
    public KakuroSolver() {
        setDefault();
        constraints = new ConstraintsChecker(grid,rows,columns);
    }

    /**
     * Hardcoding of the example kakuro displayed in the pdf
     */
    public void setDefault() {
        rows = 5;
        columns = 4;
        grid = new Cell[rows][columns];

        grid[0][0] = new UnreachableCell(0,0);
        grid[0][1] = new UnreachableCell(0,1);
        grid[0][2] = new ControlCell(0,2,25,null);
        grid[0][3] = new ControlCell(0,3,2,null);

        grid[1][0] = new UnreachableCell(1,0);
        grid[1][1] = new ControlCell(1,1,5,8);
        grid[1][2] = new InputCell(1,2);
        grid[1][3] = new InputCell(1,3);

        grid[2][0] = new ControlCell(2,0,null,11);
        grid[2][1] = new InputCell(2,1);
        grid[2][2] = new ConstantCell(2,2,8);
        grid[2][3] = new ControlCell(2,3,5,null);

        grid[3][0] = new ControlCell(3,0,null,15);
        grid[3][1] = new ConstantCell(3,1,2);
        grid[3][2] = new InputCell(3,2);
        grid[3][3] = new InputCell(3,3);

        grid[4][0] = new UnreachableCell(4,0);
        grid[4][1] = new ControlCell(4,1,null,3);
        grid[4][2] = new InputCell(4,2);
        grid[4][3] = new InputCell(4,3);

    }

    /**
     * from the current position returns the reference to the next Input Cell going from left to right and up to down
     * @param grid current grid
     * @param row current vertical coordinate
     * @param column current horizontal coordinate
     * @return reference to the next Input Cell going from left to right and up to down
     */
    private InputCell getNextFreeCell(Cell[][]grid, int row, int column) {
        //first check line
        for (int i = column; i < columns; i++) {
            if (grid[row][i].getType() == CellType.input)
                return (InputCell) grid[row][i];
        }
        //then check the rest of the lower end of the grid
        for (int i = row+1; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j].getType() == CellType.input)
                    return (InputCell) grid[i][j];
            }
        }
        return null;
    }

    /**
     * Backtracking algorithm for kakuro solving
     * @param grid current grid distribution
     * @param row current row
     * @param column current column
     * @return true the solution has been found and saved into this.solution / false if there is no solution
     */
    public boolean solve(Cell[][] grid, int row, int column) {
        InputCell currentInput = getNextFreeCell(grid,row,column);
        //if there aren't any input cells left then the grid is solved
        if (currentInput == null)
            return true;

        //update values for current row/column
        row = currentInput.getRow();
        column = currentInput.getColumn();

        for (int num = 1; num < 10; num++) {
            //try number
            ((InputCell) grid[row][column]).setValue(num);
            // Check if the value isn't repeated and if the sum doesn't surpass the max value according to control cells
            if (constraints.isValid(grid, row, column)) {
                //if we're at a column's/row's end then check if the sum matches the expected value
                if (constraints.isEndOfRow(grid,row,column) && !constraints.horizontalSumMatches(grid,row,column)) {
                    currentInput.setValue(0);
                    continue;
                }
                if (constraints.isEndOfColumn(grid,row,column) && !constraints.verticalSumMatches(grid,row,column)) {
                    currentInput.setValue(0);
                    continue;
                }
                //all good -> try next position
                if (solve(grid, row, column + 1))
                    return true;
            }
            //value reset if guess was wrong
            currentInput.setValue(0);
        }
        return false;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell[][] getDefaultGrid() {
        setDefault();
        return grid;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    /**
     * checks if the value on the current position was misplaced
     * @param row current vertical position
     * @param column current horizontal position
     * @return a boolean that check if the number given is misplaced or not
     */
    public boolean checkMisplacement(int row, int column) {
        return !(constraints.horizontalOK(grid,row,column) && constraints.verticalOK(grid,row,column));
    }

    /**
     * Copies the current grid as a prototype (without taking into consideration the values of input cells)
     * @return returns a copy of the prototype of this.grid, setting all input values to 0
     */
    private Cell[][] copyGridStructure() {
        Cell[][] copy = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch(grid[i][j].getType()) {
                    case unreachable:
                        copy[i][j] = new UnreachableCell(i,j);
                        break;
                    case control:
                        Integer vertical = ((ControlCell)grid[i][j]).getVertical();
                        Integer horizontal = ((ControlCell)grid[i][j]).getHorizontal();
                        copy[i][j] = new ControlCell(i,j,vertical,horizontal);
                        break;
                    case input:
                        copy[i][j] = new InputCell(i,j);
                        break;
                    case constant:
                        copy[i][j] = new ConstantCell(i,j,((ConstantCell)grid[i][j]).getValue());
                        break;
                }
            }
        }
        return copy;
    }

    /**
     * Retrieves the solution of the current grid
     * @return solution to the current grid
     * @throws NoSolutionException if there is no solution
     */
    public Cell[][] getSolution() throws NoSolutionException {
        //to speed things up, it is only calculated once
        if (!solutionReady) {
            solution = copyGridStructure();

            constraints.updateDimensions(rows, columns);
            if (!solve(solution, 0, 0)) {
                throw (new NoSolutionException());
            }
            solutionReady = true;
        }
        return solution;
    }

    /**
     * Checks if current model grid matches solution
     * @return solved grid
     * @throws NoSolutionException if the current grid has no solutions
     */
    public boolean isSolved() throws NoSolutionException {
        solution = getSolution();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j].getType() == CellType.input) {
                    InputCell gridCell = (InputCell)grid[i][j];
                    InputCell solutionCell = (InputCell)solution[i][j];
                    //cell value comparison
                    if (gridCell.getValue() != solutionCell.getValue())
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * copies the prototype of the grid passed as a parameter into this.grid
     * @param grid grid to copy
     */
    public void copyGrid(Cell[][] grid) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch (grid[i][j].getType()) {
                    case control:
                        this.grid[i][j] = new ControlCell(i,j
                                ,((ControlCell)grid[i][j]).getVertical(),((ControlCell)grid[i][j]).getHorizontal());
                        break;
                    case input:
                        this.grid[i][j] = new InputCell(i,j,((InputCell)grid[i][j]).getValue());
                        break;
                    case unreachable:
                        this.grid[i][j] = new UnreachableCell(i,j);
                        break;
                    case constant:
                        this.grid[i][j] = new ConstantCell(i,j,((ConstantCell)grid[i][j]).getValue());
                        break;
                }
            }
        }
    }

    /**
     * reinitializes all fields
     * @param grid new grid
     * @param rows vertical dimension
     * @param columns horizontal dimension
     */
    public void updateGrid(Cell[][] grid, int rows, int columns) {
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
        solutionReady = false;
        constraints = new ConstraintsChecker(grid,rows,columns);
    }

    /**
     * prints in terminal the grid passed as parameter
     * the grid's dimensions have to match this.rows and this.columns
     * @param grid grid to print
     */
    public void printGrid(Cell[][]grid) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                switch(grid[i][j].getType()) {
                    case unreachable:
                        System.out.print('#');
                        System.out.print('#');
                        System.out.print("# ");
                        break;
                    case control:
                        Integer vertical = ((ControlCell)grid[i][j]).getVertical();
                        Integer horizontal = ((ControlCell)grid[i][j]).getHorizontal();
                        if (vertical != null)
                            System.out.print(vertical);
                        else
                            System.out.print('#');
                        System.out.print("\\");
                        if (horizontal != null)
                            System.out.print(horizontal);
                        else
                            System.out.print('#');
                        break;
                    case input:
                        System.out.print("  ");
                        System.out.print(((InputCell)grid[i][j]).getValue());
                        System.out.print("  ");
                        break;
                    case constant:
                        System.out.print("  ");
                        System.out.print(((ConstantCell)grid[i][j]).getValue());
                        System.out.print("  ");
                        break;
                }
                System.out.print("\t|");
            }
            System.out.println();
        }
    }
}