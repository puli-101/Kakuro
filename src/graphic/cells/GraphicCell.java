package graphic.cells;
import controller.CellType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class GraphicCell extends JPanel {
    protected int x, y;
    protected int length;
    public static final int defaultLength = 100;

    public GraphicCell() {
        this(0,0);
    }

    public GraphicCell(int x, int y) {
        this(x,y,defaultLength);
    }

    public GraphicCell(int x, int y, int length) {
        this.x = x;
        this.y = y;
        this.length = length;
        setPreferredSize(new Dimension(length+1,length+1));
        setSize(new Dimension(length+1,length+1));


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public abstract CellType getType();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
