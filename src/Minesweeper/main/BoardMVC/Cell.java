package Minesweeper.main.BoardMVC;

import Minesweeper.main.CellType;

/**
 * Класс, представляющий ячейку игрового поля
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class Cell {

    /**
     * Поле координаты ячейки X
     */
    private int x;

    /**
     * Поле координаты ячейки Y
     */
    private int y;

    /**
     * Поле, представляющее тип ячейки
     */
    private CellType cellType;

    /**
     * Конструктор - создание новой ячейки с передачей координат. По умолчанию ячейка создается с типом CLOSED
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        cellType = CellType.CLOSED;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
}
