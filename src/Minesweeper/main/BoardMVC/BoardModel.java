package Minesweeper.main.BoardMVC;

import Minesweeper.main.BoardModelInterface;
import Minesweeper.main.Difficulty;
import Minesweeper.main.GameStatus;

/**
 * Данный класс представляет из себя модель игрового поля
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class BoardModel implements BoardModelInterface {

    /**
     * Поле статуса игры
     *
     * @see GameStatus
     */
    public static GameStatus gameStatus;

    /**
     * Поле сложности игры. По умолчанию сложность легкая
     *
     * @see Difficulty
     */
    public static Difficulty difficulty = Difficulty.EASY;

    /**
     * Скрытое поле - расположение игровых элементов и их типизация происходит именно на нем
     *
     * @see BoardController#createHiddenBoard
     */
    private Cell[][] hiddenBoard;
    /**
     * Видимое игроку поле. По мере прогресса игры меняет тип клеток на тип нижнего игрового поля в зависимости от
     * координат клика и уже установленного типа поля
     *
     * @see BoardController#createVisibleBoard
     */
    private Cell[][] visibleBoard;

    public Cell[][] getHiddenBoard() {
        return hiddenBoard;
    }

    public void setHiddenBoard(Cell[][] hiddenBoard) {
        this.hiddenBoard = hiddenBoard;
    }

    public Cell[][] getVisibleBoard() {
        return visibleBoard;
    }

    public void setVisibleBoard(Cell[][] visibleBoard) {
        this.visibleBoard = visibleBoard;
    }
}
