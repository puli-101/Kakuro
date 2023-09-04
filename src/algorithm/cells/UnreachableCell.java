package algorithm.cells;

import controller.CellType;

public class UnreachableCell extends Cell {
    public UnreachableCell(int row, int column) {
        super(row, column);
    }

    @Override
    public CellType getType() {
        return CellType.unreachable;
    }
}
