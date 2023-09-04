package graphic.cells;
import controller.CellType;
import java.awt.*;


public class GraphicUnreachableCell extends GraphicCell {
    public GraphicUnreachableCell() {
        super(0,0);
    }

    public GraphicUnreachableCell(int x, int y) {
        super(x,y);
    }
    @Override
    public CellType getType() {
        return CellType.unreachable;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect (0, 0, length, length);
        g.fillRect(0, 0, length, length);
    }
}
