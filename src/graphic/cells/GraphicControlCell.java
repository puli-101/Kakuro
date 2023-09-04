package graphic.cells;

import controller.CellType;
import graphic.cells.GraphicCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GraphicControlCell extends GraphicCell {
    private Integer vertical,horizontal;
    private JLabel verticalLabel, horizontalLabel, filler1, filler2;

    public GraphicControlCell() {
        this(0,0);
    }

    public GraphicControlCell(int x, int y) {
        this(x,y,null,null);
    }

    public GraphicControlCell(int x, int y, Integer vertical, Integer horizontal) {
        this(x,y,vertical,horizontal,defaultLength);
    }

    public GraphicControlCell(int x, int y, Integer vertical, Integer horizontal, int length) {
        super(x,y,length);
        this.vertical = vertical;
        this.horizontal = horizontal;

        verticalLabel = new JLabel(getString(vertical));
        verticalLabel.setFont(new Font(verticalLabel.getName(), Font.BOLD, length/3));

        horizontalLabel = new JLabel(getString(horizontal));
        horizontalLabel.setFont(new Font(horizontalLabel.getName(), Font.BOLD, length/3));

        filler1 = new JLabel("");
        filler2 = new JLabel("");

        setLayout(new GridLayout(2,2));
        add(filler1);
        add(horizontalLabel);
        add(verticalLabel);
        add(filler2);
    }

    @Override
    public CellType getType() {
        return CellType.control;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Square
        g.drawRect (0, 0, length, length);
        //Diagonal
        g.drawLine(0,0,length,length);
        //Lower triangle
        if (vertical == null)
            g.fillPolygon(new int[] {0,0,length}, new int[] {0,length,length}, 3);
        //Upper triangle
        if (horizontal == null)
            g.fillPolygon(new int[] {0,length,length}, new int[] {0,0,length}, 3);
    }

    private String getString(Integer n) {
        if (n == null)
            return "";
        if (n/10 == 0) {
            return " "+n;
        } else {
            return String.valueOf(n);
        }

    }
}
