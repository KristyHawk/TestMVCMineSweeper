package Minesweeper.main;

import Minesweeper.main.BoardMVC.Cell;

/**
 * Интерфейс модели. Гарантирует наличие геттеров и сеттеров у имеющихся игровых полей
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public interface BoardModelInterface {

    Cell[][] getVisibleBoard();

    Cell[][] getHiddenBoard();

    void setVisibleBoard(Cell[][] visibleBoard);

    void setHiddenBoard(Cell[][] hiddenBoard);
}
