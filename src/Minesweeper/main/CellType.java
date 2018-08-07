package Minesweeper.main;

import Minesweeper.main.BoardMVC.BoardView;

import java.awt.*;

/**
 * Перечисление типов игровых клеток
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public enum CellType {

    EMPTY,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    CLOSED,
    MINE,
    MINE_BLOW,
    FLAG,
    QUESTION,
    WRONG_FLAG;

    /**
     * Каждое перечисление ассоциируется с одноименной иконком
     *
     * @see BoardView#initCellType()
     * @see /resources
     */
    public Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }
}
