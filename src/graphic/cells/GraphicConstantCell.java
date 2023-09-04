package graphic.cells;

import controller.CellType;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GraphicConstantCell extends GraphicCell {
    private Integer value;
    private JLabel valueLabel;

    public GraphicConstantCell() {
        this(0,0,null);
    }

    public GraphicConstantCell(int x, int y) {
        this(x,y,null);
    }

    public GraphicConstantCell(int x, int y, Integer value) {
        super(x,y);

        this.value = value;
        valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font(valueLabel.getName(), Font.BOLD, length/3));

        setLayout(new GridBagLayout());
        add(valueLabel);
    }

    @Override
    public CellType getType() {
        return CellType.constant;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect (0, 0, length, length);
    }
}
