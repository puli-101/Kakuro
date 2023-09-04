package algorithm.cells;

import controller.CellType;

public abstract class Cell {
    protected int row = 0;
    protected int column = 0;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public abstract CellType getType();

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
