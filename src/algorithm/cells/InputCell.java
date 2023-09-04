package algorithm.cells;

import algorithm.cells.Cell;
import controller.CellType;

public class InputCell extends Cell {
    private int value;

    public InputCell(int row, int column) {
        super(row, column);
    }
    public InputCell(int row, int column, int value) {
        super(row, column);
        this.value = value;
    }
    @Override
    public CellType getType() { return CellType.input; }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        value = newValue;
    }
}
