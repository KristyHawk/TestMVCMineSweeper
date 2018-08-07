package Minesweeper.main;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Интерфейс контроллера. Определяет методы генерации игровых полей, и обработки игровых событий, а также проверки статуса игры
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public interface ControllerInterface {

    void createVisibleBoard(int width, int height);

    void createHiddenBoard(int width, int height, int mines, int exceptOfX, int exceptOfY);

    void checkUserAction(int x, int y, MouseEvent event);

    void checkActionPerformed(ActionEvent event);

    void checkGameStatus();
}
