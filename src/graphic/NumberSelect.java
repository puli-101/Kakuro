package graphic;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//pop up after clicking an input cell
public class NumberSelect extends JFrame {

    private static NumberSelect singleton = null;
    
    /**
     * Creates and opens a number selection panel
     */
    private NumberSelect() {
        JLabel label;
        JButton[] numbers;
        JButton closeButton;
        JButton resetValueButton;
        GridBagConstraints insertionConstraints;

        setSize(new Dimension(300,400));
        setLocationRelativeTo(null);
        setTitle("Select a number");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        setLayout(new GridBagLayout());

        insertionConstraints = new GridBagConstraints();
        numbers = new JButton[9];
        closeButton = new JButton("Cancel");
        resetValueButton = new JButton("Reset");

        label = new JLabel("Select a Number");
        label.setFont(new Font(label.getName(),Font.BOLD,16));

        for (int i = 0; i < 9; i++) {
            //position in grid
            numbers[i] = new JButton(Integer.toString(i+1));
            insertionConstraints.gridx = i%3;
            insertionConstraints.gridy = 1+(i/3);
            insertionConstraints.insets = new Insets(10,0,10,0);
            //style
            numbers[i].setSize(new Dimension(40,40));
            numbers[i].setPreferredSize(new Dimension(40,40));
            numbers[i].setBorder(BorderFactory.createLineBorder(new Color(26, 26, 26)));
            numbers[i].setBackground(new Color(26, 26, 26));
            numbers[i].setOpaque(true);
            numbers[i].setBorderPainted(true);
            numbers[i].setFocusable(false);
            numbers[i].setForeground(Color.white);
            //button listeners
            int value = i+1;
            numbers[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    Controller.getController().setSelectedNumber(value);
                }
            });
            add(numbers[i],insertionConstraints);
        }

        //LABEL
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 0;
        insertionConstraints.insets = new Insets(10,0,20,0);
        add(label,insertionConstraints);

        //---------RESET VALUE-----------
        //position
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 4;
        insertionConstraints.insets = new Insets(20,0,10,0);
        //style
        resetValueButton.setBackground(Color.gray);
        resetValueButton.setOpaque(true);
        resetValueButton.setFocusable(false);
        resetValueButton.setForeground(Color.white);
        //listener
        resetValueButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().resetValue();
            }
        });
        add(resetValueButton,insertionConstraints);

        //------CANCEL SELECTION-----
        //position
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 5;
        insertionConstraints.insets = new Insets(20,0,10,0);
        //style
        closeButton.setBackground(Color.gray);
        closeButton.setOpaque(true);
        closeButton.setFocusable(false);
        closeButton.setForeground(Color.white);
        //listener
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().cancelSelection();
            }
        });

        add(closeButton,insertionConstraints);

    }

    /**
     * To avoid opening a new panel if the user wants to reselect a different cell while the panel is already open
     * @return singleton
     */
    public static NumberSelect openNumberSelect() {
        if (singleton == null)
            singleton = new NumberSelect();
        return singleton;
    }
}
