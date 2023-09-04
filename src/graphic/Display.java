package graphic;

import graphic.cells.GraphicCell;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {

    private final JLabel alertLabel;
    private final Options optionsPanel;
    private final GridBagConstraints insertionConstraints;

    private GameGrid gamePanel;

    /**
     * Creates the game frame by using the other view classes as objects
     */
    public Display() {
        setSize(800,800);
        setLocationRelativeTo(null);
        setTitle("POO2 - Kakuro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        optionsPanel = new Options();
        gamePanel = new GameGrid();
        alertLabel = new JLabel("Try Solving This!");
        alertLabel.setFont(new Font(alertLabel.getName(), Font.BOLD, 20));

        setLayout(new GridBagLayout());

        insertionConstraints = new GridBagConstraints();

        resetElements();
    }

    public void display() {
        setVisible(true);
    }

    public GameGrid grid() {
        return gamePanel;
    }

    public GraphicCell[][] getGraphicGrid() {
        return gamePanel.getGrid();
    }

    /**
     * Updates the current grid display by removing the grid panel and creating a new one
     * @param grid graphic grid to be displayed
     * @param rows vertical dimension
     * @param columns horizontal dimension
     */
    public void updateGrid(GraphicCell[][] grid, int rows, int columns) {
        remove(gamePanel);
        gamePanel = new GameGrid(grid,columns,rows);

        resetElements();

        update(getGraphics());
    }

    /**
     * Reinitialize the elements of the grid
     */
    public void resetElements() {
        insertionConstraints.fill = GridBagConstraints.VERTICAL;
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 0;
        add(alertLabel,insertionConstraints);
        insertionConstraints.fill = GridBagConstraints.VERTICAL;
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 1;
        insertionConstraints.insets = new Insets(20,0,10,0);
        add(gamePanel,insertionConstraints);
        insertionConstraints.fill = GridBagConstraints.VERTICAL;
        insertionConstraints.gridx = 0;
        insertionConstraints.gridy = 2;
        add(optionsPanel,insertionConstraints);
    }

    /**
     * Displays red error message on JLabel
     * @param msg text to display
     */
    public void setErrorText(String msg) {
        alertLabel.setText(msg);
        alertLabel.setForeground(new Color(255, 128, 44));
    }

    /**
     * Displays default text
     */
    public void setDefaultText() {
        alertLabel.setText("Try Solving This!");
        alertLabel.setForeground(new Color(1,1,1));
    }

    /**
     * Displays text on green
     * @param msg text to display
     */
    public void setWinText(String msg) {
        alertLabel.setText(msg);
        alertLabel.setForeground(new Color(0, 154, 20));
    }
}
