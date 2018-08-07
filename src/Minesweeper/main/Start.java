package Minesweeper.main;

import Minesweeper.main.BoardMVC.BoardController;
import Minesweeper.main.BoardMVC.BoardModel;

/**
 * Класс запускает инициализацию модели, отображения (далее - вида) и контроллера
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class Start {
    public static void main(String[] args) {
        BoardModelInterface model = new BoardModel();
        new BoardController(model);
    }
}
