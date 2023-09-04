package graphic.cells;

import controller.CellType;
import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GraphicInputCell extends GraphicCell {
    private final JLabel valueLabel;
    private Integer value;

    public GraphicInputCell() {
        this(0,0);
    }

    public GraphicInputCell(int x, int y) {
        this(x,y,null);
    }

    public GraphicInputCell(int x, int y, Integer value) {
        super(x,y);
        this.value = value;

        if (value == null || value == 0)
            valueLabel = new JLabel("");
        else
            valueLabel = new JLabel(String.valueOf(value));

        valueLabel.setFont(new Font(valueLabel.getName(), Font.BOLD, length/3));

        setLayout(new GridBagLayout());
        add(valueLabel);

        GraphicInputCell thisInput = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().setSelectedCell(thisInput);
            }
        });
    }

    @Override
    public CellType getType() {
        return CellType.input;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect (0, 0, length, length);
    }

    public void updateText(String txt) {
        valueLabel.setText(txt);
    }
}
