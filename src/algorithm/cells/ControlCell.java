package algorithm.cells;

import algorithm.cells.Cell;
import controller.CellType;

public class ControlCell extends Cell {
    private final Integer vertical, horizontal;

    public ControlCell(int row, int column, Integer vertical, Integer horizontal) {
        super(row,column);
        this.vertical = vertical;
        this.horizontal = horizontal;
    }

    @Override
    public CellType getType() {
        return CellType.control;
    }

    public Integer getVertical() {
        return vertical;
    }

    public Integer getHorizontal() {
        return horizontal;
    }

}
