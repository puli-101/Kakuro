package algorithm.cells;

import controller.CellType;

public class ConstantCell extends Cell {
    private final int value;

    public ConstantCell(int row, int column, int value) {
        super(row,column);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public CellType getType() {
        return CellType.constant;
    }
}
