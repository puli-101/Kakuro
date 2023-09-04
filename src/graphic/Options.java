package graphic;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//options panel shown under the grid
public class Options extends JPanel {
    private final JButton solveButton;
    private final JButton resetButton;
    private final JLabel selectRandomButton;
    private final JButton saveSessionButton;
    private final JButton loadSession;
    private final JComboBox dimensionsList;
    private final String[] dimensionStrings = { "Default", "3x3", "4x4", "5x5"};

    private GridBagConstraints insertionConstraints;

    /**
     * Creates the required buttons that will be displayed under the grid
     */
    public Options() {
        setLayout(new GridBagLayout());

        insertionConstraints = new GridBagConstraints();
        insertionConstraints.fill = GridBagConstraints.HORIZONTAL;
        insertionConstraints.anchor = GridBagConstraints.WEST;
        insertionConstraints.insets = new Insets(0,10,10,10);

        //------SOLVE BUTTON---------
        //style
        solveButton = new JButton("Auto-Solve");
        solveButton.setBackground(Color.gray);
        solveButton.setOpaque(true);
        solveButton.setBorderPainted(true);
        solveButton.setFocusable(false);
        solveButton.setForeground(Color.white);
        //constraints
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 0;
        //listener
        solveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().setSolution();
            }
        });

        add(solveButton, insertionConstraints);

        //-------RESET BUTTON---------
        //style
        resetButton = new JButton("Reset Grid");
        resetButton.setBackground(Color.gray);
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(true);
        resetButton.setFocusable(false);
        resetButton.setForeground(Color.white);
        //constraints
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 0;
        //listener
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().resetValues();
            }
        });
        add(resetButton,insertionConstraints);

        //---- DIMENSIONS LIST --------
        //style
        dimensionsList = new JComboBox(dimensionStrings);
        dimensionsList.setBackground(Color.white);
        dimensionsList.setOpaque(true);
        dimensionsList.setFocusable(false);
        dimensionsList.setForeground(Color.black);
        dimensionsList.setSelectedIndex(0);
        //constraints
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 1;
        //listener
        dimensionsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Controller.getController().loadRandomGrid(getDimensions());
            }
        });

        add(dimensionsList, insertionConstraints);

        //-------SELECT RANDOM ------------
        //style
        selectRandomButton = new JLabel("Select Grid : ");
        //constraints
        insertionConstraints.anchor = GridBagConstraints.EAST;
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 1;
        //listener
        selectRandomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().loadRandomGrid(getDimensions());
            }
        });
        add(selectRandomButton,insertionConstraints);

        //------- SAVE SESSION --------
        //style
        saveSessionButton = new JButton("Save Session");
        saveSessionButton.setBackground(Color.gray);
        saveSessionButton.setOpaque(true);
        saveSessionButton.setBorderPainted(true);
        saveSessionButton.setFocusable(false);
        saveSessionButton.setForeground(Color.white);
        //constraints
        insertionConstraints.anchor = GridBagConstraints.WEST;
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 2;
        //listener
        saveSessionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().saveCurrentGrid();
            }
        });
        add(saveSessionButton,insertionConstraints);

        //--------- LOAD SESSION ---------
        //style
        loadSession = new JButton("Load Session");
        loadSession.setBackground(Color.gray);
        loadSession.setOpaque(true);
        loadSession.setBorderPainted(true);
        loadSession.setFocusable(false);
        loadSession.setForeground(Color.white);
        //constraints
        insertionConstraints.gridx = 1;
        insertionConstraints.gridy = 2;
        //listener
        loadSession.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Controller.getController().loadSavedGrid();
            }
        });
        add(loadSession,insertionConstraints);
    }

    private int getDimensions() {
        int n = dimensionsList.getSelectedIndex();
        //if n = default (0) then keep as default, else increment n so that we select an n x n grid
        n = n == 0 ? 0 : n + 2;
        return n;
    }
}
