package testing;

import controller.Controller;

public class Test {
    public static void main(String[] args) {
        Controller kakuro = Controller.getController();
        kakuro.setDefaultGraphicGrid();
    }
}
