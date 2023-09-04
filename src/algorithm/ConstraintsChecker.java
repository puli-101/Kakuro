package algorithm;

import algorithm.cells.Cell;
import algorithm.cells.ConstantCell;
import algorithm.cells.ControlCell;
import algorithm.cells.InputCell;
import controller.CellType;

public class ConstraintsChecker {
    //current grid scope
    private Cell[][] grid;

    //internal functioning variables
    //horizontalControlX : coordinates of the horizontally closest control cell to a certain position in the current context
    //verticalControlX : coordinates of the vertically closest control cell to a certain position in the current context
    private int horizontalControlColumn, horizontalControlRow;
    private int verticalControlColumn, verticalControlRow;

    //grid dimensions
    private int rows,columns;

    public ConstraintsChecker(Cell[][] grid, int rows, int columns) {
        this.grid = grid;
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Updates the coordinates of the closest horizontal control cell with respect to the current position
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return returns the reference of this cell if it exists
     */
    private ControlCell updateClosestControlHorizontal(int row, int column) {
        for (int i = column; i >= 0; i--) {
            if (grid[row][i].getType() == CellType.control) {
                horizontalControlColumn = i;
                horizontalControlRow = row;
                return (ControlCell) grid[row][i];
            }
        }
        return null;
    }

    /**
     * Tests if there aren't any repeated horizontal values or if the current horizontal sum surpasses the expected sum
     * @param newGrid grid being tested
     * @param row horizontal coordinate
     * @param column vertical coordinate
     */
    public boolean horizontalOK(Cell[][]newGrid, int row, int column) {
        grid = newGrid;
        if (updateClosestControlHorizontal(row,column) == null)
            return true;
        ControlCell control = ((ControlCell)grid[horizontalControlRow][horizontalControlColumn]);
        int[] possibleValues = new int[10];
        int maxSum = control.getHorizontal(), currentSum = 0;

        for (int i = horizontalControlColumn + 1; i < columns; i++) {
            Cell c = grid[horizontalControlRow][i];
            int val;

            switch (c.getType()) {
                //if we already checked the whole subsection then OK
                case control:
                case unreachable:
                    return true;
                case input:
                    val = ((InputCell)c).getValue();
                    currentSum += val;
                    possibleValues[val]++;
                    //check if value hasn't been used twice
                    //or if the accumulated sum is less than the maximum permitted sum
                    if (val != 0 && (possibleValues[val] > 1 || currentSum > maxSum)) {
                        return false;
                    }
                    break;
                case constant:
                    val = ((ConstantCell)c).getValue();
                    currentSum += val;
                    possibleValues[val]++;
                    //check if value hasn't been used twice
                    //or if the accumulated sum is less than the maximum permitted sum
                    if (val != 0 && (possibleValues[val] > 1 || currentSum > maxSum)) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    /**
     * Updates the values of the closest vertical control cell with respect to the current position
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return returns the reference towards this control cell
     */
    private ControlCell updateClosestControlVertical(int row, int column) {
        for (int i = row; i >= 0; i--) {
            if (grid[i][column].getType() == CellType.control) {
                verticalControlColumn = column;
                verticalControlRow = i;
                return (ControlCell) grid[i][column];
            }
        }
        return null;
    }

    /**
     * Checks if the current vertical sum doesn't surpass the expected sum and there aren't any repeated values
     * @param newGrid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return true if the sum doesn't surpass the expected sum and there aren't any repeated values
     */
    public boolean verticalOK(Cell[][]newGrid, int row, int column) {
        grid = newGrid;
        if (updateClosestControlVertical(row,column) == null)
            return true;
        ControlCell control = ((ControlCell)grid[verticalControlRow][verticalControlColumn]);
        int[] possibleValues = new int[10];
        int maxSum = control.getVertical(), currentSum = 0;

        for (int i = verticalControlRow + 1; i < rows; i++) {
            Cell c = grid[i][verticalControlColumn];
            int val;

            switch (c.getType()) {
                //if we already checked the whole subsection then OK
                case control:
                case unreachable:
                    return true;
                case input:
                    val = ((InputCell)c).getValue();
                    currentSum += val;
                    possibleValues[val]++;
                    //check if value hasn't been used twice
                    //or if the accumulated sum is less than the maximum permitted sum
                    if (val != 0 && (possibleValues[val] > 1 || currentSum > maxSum)) {
                        return false;
                    }
                    break;
                case constant:
                    val = ((ConstantCell)c).getValue();
                    currentSum += val;
                    possibleValues[val]++;
                    //check if value hasn't been used twice
                    //or if the accumulated sum is less than the maximum permitted sum
                    if (val != 0 && (possibleValues[val] > 1 || currentSum > maxSum)){
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    /**
     * checks both vertically and horizontally if there aren't any repeated values and if the sum doesn't surpass the
     * expected values with respect to the grid passed as parameter and the current row and column
     * @param grid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return boolean
     */
    public boolean isValid(Cell[][]grid, int row, int column) {
        return horizontalOK(grid,row,column) && verticalOK(grid,row,column);
    }

    /**
     * Checks if the current position is located at the end of a horizontal subsection
     * @param grid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return boolean
     */
    public boolean isEndOfRow(Cell[][]grid, int row, int column) {
        //it is the end of the row if there aren't any more columns
        if (column == columns-1)
            return true;
        //or if the next position is unreachable or a control cell
        //or if the next position is a constant cell but the following is the end of the row
        return grid[row][column+1].getType() == CellType.control
                || grid[row][column+1].getType() == CellType.unreachable
                || (grid[row][column+1].getType() == CellType.constant && isEndOfRow(grid,row,column+1));
    }

    /**
     * checks if the current position is located at the end of a vertical subsection
     * @param grid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return boolean
     */
    public boolean isEndOfColumn(Cell[][]grid, int row, int column) {
        if (row == rows-1)
            return true;
        return grid[row+1][column].getType() == CellType.control
                || grid[row+1][column].getType() == CellType.unreachable
                || (grid[row+1][column].getType() == CellType.constant && isEndOfColumn(grid,row+1,column));
    }

   
    /**
     * Iterates from the control cell from context to the end of the subsection
     * @param grid grid being tested
     * @param row starting point (starting row)
     * @return returns the sum of all iterated positions
     */
    public int getHorizontalSum(Cell[][]grid, int row) {
        int sum = 0;

        for (int i = horizontalControlColumn + 1; i < columns; i++) {
            Cell c = grid[row][i];
            if (c.getType() == CellType.control || c.getType() == CellType.unreachable)
                break;
            else if (c.getType() == CellType.constant)
                sum += ((ConstantCell)c).getValue();
            else if (c.getType() == CellType.input)
                sum += ((InputCell)c).getValue();
        }

        return sum;
    }

    /**
     * updates the location of the closest horizontal control cell
     * check if the sum of the subsection at (row,column) matches the desired horizontal sum
     * @param grid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return boolean
     */
    public boolean horizontalSumMatches(Cell[][] grid, int row, int column) {
        this.grid = grid;
        ControlCell control = updateClosestControlHorizontal(row, column);
        if (control == null)
            return true;
        return (control.getHorizontal() == getHorizontalSum(grid,row));
    }

    /**
     * Extracts the sum of the subsection related to the closest vertical control cell
     * @param grid grid being tested
     * @param column starting point (starting column)
     * @return returns the sum of all iterated positions
     */
    public int getVerticalSum(Cell[][]grid, int column) {
        int sum = 0;

        for (int i = verticalControlRow+1; i < rows; i++) {
            Cell c = grid[i][column];
            if (c.getType() == CellType.control || c.getType() == CellType.unreachable)
                break;
            else if (c.getType() == CellType.constant)
                sum += ((ConstantCell)c).getValue();
            else if (c.getType() == CellType.input)
                sum += ((InputCell)c).getValue();
        }

        return sum;
    }

    /**
     * updates the location of the closest vertical control cell
     * check if the sum of the subsection at (row,column) matches the desired vertical sum
     * @param grid grid being tested
     * @param row vertical coordinate
     * @param column horizontal coordinate
     * @return boolean
     */
    public boolean verticalSumMatches(Cell[][] grid, int row, int column) {
        this.grid = grid;
        ControlCell control = updateClosestControlVertical(row, column);
        if (control == null)
            return true;
        return (control.getVertical() == getVerticalSum(grid,column));
    }

    //double setter for the dimensions of the grid
    public void updateDimensions(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }
}
