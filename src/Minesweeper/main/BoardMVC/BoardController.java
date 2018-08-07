package Minesweeper.main.BoardMVC;

import Minesweeper.main.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Данный класс представляет из себя контроллер панели для обработки любых событий/изменений, запрашиваемых из вида
 *
 * @author Кристина Зеленько
 * @version 1.0
 * @see BoardView
 */
public class BoardController implements ControllerInterface {

    /**
     * Модель игрового поля
     */
    private BoardModelInterface model;

    /**
     * Вид игрового поля
     */
    private BoardView view;

    /**
     * Сервис игрового поля
     */
    private BoardService service;

    /**
     * Сервис обработки событий игрового поля
     */
    private BoardListenerService listenerService;

    /**
     * Данная переменная используется для определения первого клика, после которого начинается генерация игрового поля.
     * Таким образом, игрок не начнет игру, нажав на мину
     */
    private int clickCount = 0;

    /**
     * Конструктор - инициализирует поля класса для дальнейшего взаимодействия с ними, начинает игру
     *
     * @param model- при создании принимает модель, в дальнейшем работа осуществляется с ней
     * @see BoardView#initGame();
     */
    public BoardController(BoardModelInterface model) {
        this.model = model;
        view = new BoardView(this, model);
        service = new BoardService();
        listenerService = new BoardListenerService();
        view.initGame();
    }

    /**
     * Метод генерации видимого поля. Параметры регулируются заданной сложностью игры {@link Difficulty}.
     * Все клетки поля типа CellType.CLOSED
     *
     * @param width  - ширина игрового поля
     * @param height - высота игрового поля
     */
    public void createVisibleBoard(int width, int height) {
        Cell[][] visibleBoard = service.fillBoard(width, height);
        service.setCellTypeToCellArray(visibleBoard, CellType.CLOSED);
        model.setVisibleBoard(visibleBoard);
    }

    /**
     * Метод генерации скрытого поля, именно на нем располагаются бомбы, цифры и пустые клетки.
     * Первые 3 параметра регулируются сложностью игры
     *
     * @param width      - ширина поля
     * @param height     - высота поля
     * @param mines      - количество мин на поле
     * @param exceptOfX, @param exceptOfY - с целью осуществления невозможности проигрыша (клик по мине)
     *                   с первого действия пользователя при генерации невидимого поля учитываются координаты  первого клика
     */
    public void createHiddenBoard(int width, int height, int mines, int exceptOfX, int exceptOfY) {
        Cell[][] hiddenBoard = service.fillBoard(width, height);
        service.putMinesAtBoard(hiddenBoard, mines, exceptOfX, exceptOfY);
        service.defineOtherCells(hiddenBoard);
        BoardModel.gameStatus = GameStatus.IN_PROCESS;
        model.setHiddenBoard(hiddenBoard);
    }

    /**
     * Метод используется для проверки первого клика и задания момента генерации невидимого поля.
     * Иначе вызывает метод обработки события
     *
     * @param x,    @param y - координаты клика
     * @param event - передает событие обработчику событий поля
     * @see BoardController#mouseEventHandler
     */
    public void checkUserAction(int x, int y, MouseEvent event) {
        if (clickCount == 0) {
            createHiddenBoard(BoardModel.difficulty.width, BoardModel.difficulty.height, BoardModel.difficulty.mines, x, y);
        }
        mouseEventHandler(x, y, event, model.getHiddenBoard(), model.getVisibleBoard());
        clickCount++;
        isGameWon();
    }

    /**
     * Метод для обработки событий мыши для поля. Распределяет событие и его параметры в методы сервиса обработчика событий
     * в зависимости от нажатой кнопки
     *
     * @param x            - координата клика x
     * @param y            - координата клика y
     * @param event        - событие клика
     * @param hiddenBoard  - скрытая доска
     * @param visibleBoard - видимая доска
     * @see BoardListenerService
     */
    private void mouseEventHandler(int x, int y, MouseEvent event, Cell[][] hiddenBoard, Cell[][] visibleBoard) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            listenerService.onLeftClick(x, y, hiddenBoard, visibleBoard);
            view.panel.repaint();
        }
        if (event.getButton() == MouseEvent.BUTTON3) {
            listenerService.onRightClick(x, y, visibleBoard);
            view.panel.repaint();
        }
    }

    /**
     * Метод вызывается после каждого клика для проверки статуса игры, проверяя все поле и сопостовляя количество
     * клеток со знаками вопроса, флагов и закрытых клеток с количеством мин, определенным для данной сложности игры
     */
    private void isGameWon() {
        int count = service.getSize(model.getVisibleBoard());
        for (Cell[] cells : model.getVisibleBoard()) {
            for (Cell cell : cells) {
                if (cell.getCellType() != CellType.FLAG && cell.getCellType() != CellType.CLOSED
                        && cell.getCellType() != CellType.QUESTION) {
                    count--;
                }
            }
        }
        if (count == BoardModel.difficulty.mines) {
            BoardModel.gameStatus = GameStatus.WIN;
        }
    }

    /**
     * Метод, проверяющий текущий статус игры и выводящий сообщения в случае проигрыша или выигрыша, которые
     * предоставляют игроку возможность начать игру снова. Если игрок согласен, сбрасывается счетчик кликов и
     * инициализация игры вызывается снова.
     *
     * @see BoardView#initGame()
     */
    public void checkGameStatus() {
        isGameWon();
        switch (BoardModel.gameStatus) {
            case WIN:
                String[] options = new String[]{"Yes", "No"};
                int response = JOptionPane.showOptionDialog(view.panel, "Congratulations! Start again?"
                        , "Winner!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                if (response == 0) {
                    clickCount = 0;
                    view.initGame();
                } else {
                    System.exit(0);
                }
                return;
            case LOSE:
                String[] options2 = new String[]{"Yes", "No"};
                int responses = JOptionPane.showOptionDialog(view.panel, "Game Over. Start again?"
                        , "GameOver", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options2, options2[0]);
                if (responses == 0) {
                    clickCount = 0;
                    view.initGame();
                } else {
                    System.exit(0);
                }
                return;
            case IN_PROCESS:
        }
    }

    /**
     * Метод обрабатывает следующие события меню:
     * New - начинает новую игру
     * Exit - закрывает программу
     * Easy, Normal, Hard - меняет сложность игры {@link Difficulty}
     * После каждого случая, кроме Exit, игра перезапускается {@link BoardView#initGame()}
     */
    public void checkActionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "New":
                view.initGame();
                clickCount = 0;
                return;
            case "Exit":
                view.exitGame();
                clickCount = 0;
            case "Easy":
                BoardModel.difficulty = Difficulty.EASY;
                view.initGame();
                clickCount = 0;
                return;
            case "Normal":
                BoardModel.difficulty = Difficulty.NORMAL;
                view.initGame();
                clickCount = 0;
                return;
            case "Hard":
                BoardModel.difficulty = Difficulty.HARD;
                view.initGame();
                clickCount = 0;
        }
    }
}
