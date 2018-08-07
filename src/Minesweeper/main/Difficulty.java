package Minesweeper.main;

/**
 * Перечисление представляет уровни сложности игры, закрепляя параметры за каждым
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public enum Difficulty {

    EASY(9, 9, 10),
    NORMAL(16, 16, 40),
    HARD(30, 16, 99);

    public int width;
    public int height;
    public int mines;

    Difficulty(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;
    }
}
