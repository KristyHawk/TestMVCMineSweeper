package Minesweeper.main.BoardMVC;

import Minesweeper.main.CellType;

import java.util.List;

/**
 * Класс-сервис, осуществляющий обработку событий поля, взаимодействует с сервисом генерации и обработки игрового поля
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class BoardListenerService {

    private BoardService service = new BoardService();

    /**
     * Метод обрабатывает события при нажатии левой кнопки мыши. Проверка осуществляется по всем возможным типам кнопок
     * для данного поля
     *
     * @param x,           @param y - координаты события на панели
     * @param hiddenBoard  - скрытое поле
     * @param visibleBoard - видимое поле
     */
    public void onLeftClick(int x, int y, Cell[][] hiddenBoard, Cell[][] visibleBoard) {
        if (visibleBoard[x][y].getCellType() != CellType.FLAG) {
            switch (hiddenBoard[x][y].getCellType()) {
                case EMPTY:
                    visibleBoard[x][y] = hiddenBoard[x][y];
                    openAroundEmptyCell(x, y, hiddenBoard, visibleBoard);
                    return;
                case MINE:
                    visibleBoard[x][y].setCellType(CellType.MINE_BLOW);
                    service.gameOver(hiddenBoard, visibleBoard);
                    service.checkWrongFlags(visibleBoard);
                    return;
                default:
            }
            visibleBoard[x][y] = hiddenBoard[x][y];
        }
    }

    /**
     * Метод обрабатывает события при нажатии правой кнопки мыши. Проверка осуществляется по всем возможным типам кнопок
     * для данного поля
     *
     * @param x,           @param y - координаты события на панели
     * @param visibleBoard - видимое поле
     */
    public void onRightClick(int x, int y, Cell[][] visibleBoard) {
        switch (visibleBoard[x][y].getCellType()) {
            case FLAG:
                visibleBoard[x][y].setCellType(CellType.QUESTION);
                return;
            case QUESTION:
                visibleBoard[x][y].setCellType(CellType.CLOSED);
                return;
            case CLOSED:
                visibleBoard[x][y].setCellType(CellType.FLAG);
        }
    }

    /**
     * Метод открывает пустые клетки вокруг выбранной пустой клетки, используется рекурсия
     *
     * @see BoardListenerService#onLeftClick
     */
    private void openAroundEmptyCell(int x, int y, Cell[][] hiddenBoard, Cell[][] visibleBoard) {
        List<Cell> list = service.lookAroundCell(new Cell(x, y));
        for (Cell c : list) {
            if (hiddenBoard[c.getX()][c.getY()].getCellType() != CellType.MINE &&
                    visibleBoard[c.getX()][c.getY()].getCellType() != CellType.EMPTY) {
                visibleBoard[c.getX()][c.getY()] = hiddenBoard[c.getX()][c.getY()];
                if (hiddenBoard[c.getX()][c.getY()].getCellType() == CellType.EMPTY) {
                    onLeftClick(c.getX(), c.getY(), hiddenBoard, visibleBoard);
                }
            }
        }
    }
}
