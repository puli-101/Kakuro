package algorithm;

import algorithm.cells.*;
import algorithm.exceptions.InvalidGridFileException;

import java.io.*;
import java.util.Random;

public class KakuroLoader {
    private static final String saveFileName = "assets/grids/savedKakuro.kakuro";
    private static final String defaultGridFile = "assets/grids/defaultKakuro.kakuro";

    /**
     * paths to the different folders which contain the extractable grids
     */
    private static final String[] paths = {
            "assets/grids/3x3",
            "assets/grids/4x4",
            "assets/grids/5x5"
    };

    private static KakuroLoader loader = null;
    private int rows, columns;

    private KakuroLoader() {}

    public static String getDefaultGridFile() {
        return defaultGridFile;
    }

    public static KakuroLoader openLoader() {
        if (loader == null)
            loader = new KakuroLoader();
        return loader;
    }

    /**
     * takes the grid passed as parameter and saves it as a file using our custom kakuro file format
     * @param grid grid to save
     * @param rows horizontal dimension
     * @param columns vertical dimension
     * @throws IOException if error while writing on save file
     */
    public void saveFile(Cell[][] grid, int rows, int columns) throws IOException {
        FileWriter saveFile = new FileWriter(saveFileName);
        saveFile.write("KAKURO\n");
        saveFile.write(rows+" "+columns+"\n");

        Integer tmp1,tmp2;
        for (int i = 0; i < rows; i++) {
            for (int j =0; j < columns; j++) {
                switch (grid[i][j].getType()) {
                    case constant:
                        tmp1 = ((ConstantCell)grid[i][j]).getValue();
                        saveFile.write("k "+tmp1+" ");
                        break;
                    case unreachable:
                        saveFile.write("u ");
                        break;
                    case input:
                        tmp1 = ((InputCell)grid[i][j]).getValue();
                        saveFile.write("i "+tmp1+" ");
                        break;
                    case control:
                        tmp1 = ((ControlCell)grid[i][j]).getVertical();
                        tmp2 = ((ControlCell)grid[i][j]).getHorizontal();

                        tmp1 = tmp1 == null ? 0 : tmp1;
                        tmp2 = tmp2 == null ? 0 : tmp2;

                        saveFile.write("c "+tmp1+" "+tmp2+" ");
                        break;
                }
            }
            saveFile.write("\n");
        }
        saveFile.close();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cell[][] loadSavedGrid() throws Exception {
        return loadGrid(saveFileName);
    }

    public Cell[][] loadDefaultGrid() throws Exception {
        return loadGrid(defaultGridFile);
    }

    /**
     * given a dimension (e.g. if dimensions = 3, then we're talking about 3x3)
     * @param dimensions dimensions of the grid to load
     * @return a Cell matrix by reading a random kakuro file in our custom kakuro file format of the specified dimensions
     * @throws Exception if error while loading grid
     */
    public Cell[][] loadRandomGrid(int dimensions) throws Exception {
        Cell[][] grid;
        Random gen = new Random();
        String selectedFolder;
        String[] files;
        File folder;
        int index;

        //les dimensions des grilles commencent à 3
        if (dimensions < 3 || (dimensions-3) > paths.length)
            throw new InvalidGridFileException();
        selectedFolder = paths[dimensions - 3];

        folder = new File(selectedFolder);
        files = folder.list();

        if (files == null)
            throw new InvalidGridFileException();

        //le repertoire doit contenir des fichiers
        if (files.length == 0)
            throw new InvalidGridFileException();

        index = Math.abs(gen.nextInt()) % files.length;

        grid = loadGrid(selectedFolder+"/"+files[index]);
        return grid;
    }

    /**
     * given a path (name) returns the associated grid within the file if it respects our custom kakuro file format
     * @param name path of file
     * @return cell grid
     * @throws IOException if error while reading file
     * @throws InvalidGridFileException if invalid kakuro format
     */
    public Cell[][] loadGrid(String name) throws IOException, InvalidGridFileException {
        Cell[][] grid;
        BufferedReader br = new BufferedReader(new FileReader(name));

        String line;
        //check if header equals KAKURO
        line = br.readLine();
        if (line == null || "KAKURO".compareTo(line) != 0)
            throw new IOException();
        //check if header contains dimensions
        line = br.readLine();
        if (line == null)
            throw new IOException();
        //split to get the values
        String[] values = line.split("\\s+");
        //there are only 2 dimensions
        if (values.length != 2)
            throw new IOException();
        int rows, columns;
        try {
            rows = Integer.parseInt(values[0]);
            columns = Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            throw new InvalidGridFileException();
        }
        this.rows = rows;
        this.columns = columns;

        //create respective grid
        grid = new Cell[rows][columns];

        //iterate through the lines
        for (int i = 0; i < rows; i++) {
            line = br.readLine();
            values = line.split("\\s+");
            int value;
            Integer vertical, horizontal;
            for (int j = 0, currentColumn = 0; j < values.length; j++, currentColumn++) {
                if (currentColumn >= columns)
                    throw new InvalidGridFileException();
                switch (values[j].charAt(0)) {
                    case 'u':
                        grid[i][currentColumn] = new UnreachableCell(i,currentColumn);
                        break;
                    case 'k':
                        try {
                            value = Integer.parseInt(values[j+1]);
                            grid[i][currentColumn] = new ConstantCell(i,currentColumn,value);
                            j++;
                        } catch (Exception e) {
                            throw new InvalidGridFileException();
                        }
                        break;
                    case 'c':
                        try {
                            vertical = Integer.parseInt(values[j+1]);
                            horizontal = Integer.parseInt(values[j+2]);

                            vertical = vertical == 0 ? null : vertical;
                            horizontal = horizontal == 0 ? null : horizontal;

                            grid[i][currentColumn] = new ControlCell(i,currentColumn,vertical,horizontal);
                            j += 2;
                        } catch (Exception e) {
                            throw new InvalidGridFileException();
                        }
                        break;
                    case 'i':
                        try {
                            value = Integer.parseInt(values[j+1]);
                            grid[i][currentColumn] = new InputCell(i,currentColumn,value);
                            j++;
                        } catch (Exception e) {
                            throw new InvalidGridFileException();
                        }
                        break;
                    default:
                        throw new InvalidGridFileException();
                }
            }
        }

        br.close();
        return grid;
    }
}
