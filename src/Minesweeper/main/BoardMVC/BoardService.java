package Minesweeper.main.BoardMVC;

import Minesweeper.main.CellType;
import Minesweeper.main.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Сервисный класс, отвечающий за обработку процессов с игровыми полями
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class BoardService {

    private static Random random = new Random();

    /**
     * Метод заполнения игрового поля пустыми ячейками
     *
     * @param width  - ширина поля
     * @param height - высота поля
     * @return возвращает игровое поле
     */
    public Cell[][] fillBoard(int width, int height) {
        Cell[][] cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
        return cells;
    }

    /**
     * Метод заполнения игрового поля минами
     *
     * @param cells     - игровое поле, которое необходимо заполнить минами
     * @param mines     - количество мин на поле
     * @param exceptOfX @param exceptOfY - с целью невозможности проигрыша (клик по мине) с первого действия пользователя
     *                  при генерации невидимого поля учитываются координаты клика
     */
    public void putMinesAtBoard(Cell[][] cells, int mines, int exceptOfX, int exceptOfY) {
        int mineCount = 0;
        while (mineCount < mines) {
            Cell random = generateRandomCell(cells.length, cells[0].length);
            if (cells[random.getX()][random.getY()].getCellType() != CellType.MINE
                    && random.getX() != exceptOfX
                    && random.getY() != exceptOfY) {
                cells[random.getX()][random.getY()].setCellType(CellType.MINE);
                mineCount++;
            }
        }
    }

    /**
     * Метод генерации числовых и пустых ячеек в зависимости от координат мин
     *
     * @param cells - игровое поле для заполнения
     */
    public void defineOtherCells(Cell[][] cells) {
        List<Cell> lookAroundCells;
        CellType[] cellTypes = CellType.values();
        int mineCount;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                mineCount = 0;
                if (cells[i][j].getCellType() == CellType.MINE) {
                    continue;
                }
                lookAroundCells = lookAroundCell(cells[i][j]);

                for (Cell cell : lookAroundCells) {
                    if (cells[cell.getX()][cell.getY()].getCellType() == CellType.MINE) {
                        mineCount++;
                    }
                }
                cells[i][j].setCellType(cellTypes[mineCount]);
            }
        }
    }

    /**
     * Метод установки типа ячеек (с привязанными иконками) всему полю. Используется для видимого поля
     *
     * @param cells    - игровое поле для заполнения
     * @param cellType - тип, к которому необходимо привести все ячейки
     */
    public void setCellTypeToCellArray(Cell[][] cells, CellType cellType) {
        for (Cell[] cell1 : cells) {
            for (Cell cell : cell1) {
                cell.setCellType(cellType);
            }
        }
    }

    /**
     * Служебный метод для генерации случайных координат. Используется для заполнения поля минами
     *
     * @param xLength, @param yLength - максимальные величины длины и ширины игрового поля
     */
    private Cell generateRandomCell(int xLength, int yLength) {
        return new Cell(random.nextInt((xLength - 1)), random.nextInt(yLength - 1));
    }

    /**
     * Служебный метод получения ячеек вокруг любой ячейки
     *
     * @param cell - ячейка, координаты вокруг которой необходимо получить
     * @return возвращает список ячеек
     */
    List<Cell> lookAroundCell(Cell cell) {
        List<Cell> aroundCellsList = new ArrayList<>();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                if (x == cell.getX() && y == cell.getY()) {
                    continue;
                }
                if (x >= 0 && y >= 0 && x <= BoardModel.difficulty.width - 1 && y <= BoardModel.difficulty.height - 1) {
                    aroundCellsList.add(new Cell(x, y));
                }
            }
        }
        return aroundCellsList;
    }

    /**
     * Метод, осуществляющий редактирование поля в случае проигрыша игрока (взрыв мин)
     *
     * @param hiddenBoard  - невидимое поле
     * @param visibleBoard - видимое поле, подвергается изменениям
     */
    void gameOver(Cell[][] hiddenBoard, Cell[][] visibleBoard) {
        BoardModel.gameStatus = GameStatus.LOSE;
        for (int i = 0; i < hiddenBoard.length; i++) {
            for (int b = 0; b < hiddenBoard[0].length; b++) {
                if (hiddenBoard[i][b].getCellType() == CellType.MINE) {
                    visibleBoard[i][b].setCellType(CellType.MINE_BLOW);
                }
            }
        }
    }


    /**
     * Метод, исполняющийся после окончания игры. Показывает неверно расставленные флаги в случае проигрыша
     *
     * @param visibleBoard - видимое поле, на котором отображаются флаги, подвергается изменениям
     */
    void checkWrongFlags(Cell[][] visibleBoard) {
        for (int x = 0; x < visibleBoard.length; x++) {
            for (int y = 0; y < visibleBoard[0].length; y++) {
                if (visibleBoard[x][y].getCellType() == CellType.FLAG) {
                    visibleBoard[x][y].setCellType(CellType.WRONG_FLAG);
                }
            }
        }
    }

    public int getSize(Cell[][] board) {
        return board.length * board[0].length;
    }
}
