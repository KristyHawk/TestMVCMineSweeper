package Minesweeper.main.BoardMVC;

import Minesweeper.main.BoardModelInterface;
import Minesweeper.main.CellType;
import Minesweeper.main.ControllerInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Класс представляет вид игрового поля - его интерфейс.
 *
 * @author Кристина Зеленько
 * @version 1.0
 */
public class BoardView {

    /**
     * Поле интерфейса игровой модели, далее инициализировано конкретной моделью
     */
    private BoardModelInterface model;

    /**
     * Поле интерфейса игрового контроллера, далее инициализировано конкретным контроллером
     */
    private ControllerInterface controller;

    /**
     * Поле рамки игры, содержит {@link BoardView#panel} и {@link BoardView#menuBar}
     */
    private JFrame frame;

    /**
     * Поле панели игры, на ней располагаются игровые поля: {@link BoardModel#visibleBoard}, {@link BoardModel#hiddenBoard}
     */
    public JPanel panel;
    private JMenuBar menuBar;

    /**
     * Поле представляет размер одной игровой клетки, в дальнейшем используется, как правило, для вычисления координат
     */
    private int iconSize = 25;

    /**
     * Конструктор - вызывается из метода {@link Minesweeper.main.Start#main(String[])}
     * Здесь происходит начало инициализации программы, инициализируется контроллер и модель игрового поля
     */
    BoardView(ControllerInterface controller, BoardModelInterface model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * Инициализирует интерфейс игры, также вызывается контроллером при определенных условиях для ее перезапуска
     * Первый вызов происходит в конструкторе при инициализации контроллера {@link BoardController#BoardController}
     */
    public void initGame() {
        if (frame != null) {
            frame.dispose();
        }
        initFrame();
        initMenu();
        initBoard();
        initPanel();
        initCellType();
        frame.add(panel);
        frame.pack();
        addMouseListener();
    }

    /**
     * Для удобства восприятия методы инициализации разных элементов интерфейса обособлены друг от друга.
     * Инициализация фрейма, установка настроек.
     */
    private void initFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("MineSweeper");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Инициализация панели вместе с переопределением метода paintComponent(). При изначальной отрисовке игрового поля
     * работает с верхней панелью, типы всех ячеек которой CellType.CLOSED. После каждого действия осуществляется
     * перерисовка панели методом repaint();
     * Размеры панели зависят от размеров текущего поля
     */
    private void initPanel() {
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Cell[][] cells = model.getVisibleBoard();
                for (Cell little[] : cells) {
                    for (Cell cell : little) {
                        ImageIcon imageIcon = new ImageIcon(cell.getCellType().getImage());
                        imageIcon.paintIcon(this, g, cell.getX() * iconSize, cell.getY() * iconSize);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(BoardModel.difficulty.width * iconSize
                , BoardModel.difficulty.height * iconSize));
    }

    /**
     * Инициализация меню и присвоение обработчика событий его элементам. Обработчик делигирует выполнение обработки
     * события контроллеру
     */
    private void initMenu() {
        menuBar = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New");
        JMenu difficulty = new JMenu("Difficulty");
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem normal = new JMenuItem("Normal");
        JMenuItem hard = new JMenuItem("Hard");
        JMenuItem exit = new JMenuItem("Exit");

        game.add(newGame);
        game.add(difficulty);
        game.add(exit);
        difficulty.add(easy);
        difficulty.add(normal);
        difficulty.add(hard);
        menuBar.add(game);
        frame.setJMenuBar(menuBar);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                controller.checkActionPerformed(event);
            }
        };
        newGame.addActionListener(listener);
        easy.addActionListener(listener);
        normal.addActionListener(listener);
        hard.addActionListener(listener);
        exit.addActionListener(listener);
    }

    /**
     * Присваивает каждому типу перечисления одноименную иконку из папки /resources.
     */
    private void initCellType() {
        BufferedImage image;
        for (CellType cellType : CellType.values()) {
            try {
                image = ImageIO.read(new File("resources/" + cellType.name() + ".png"));
                cellType.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Первое прямое обращение к контроллеру для начала генерации игрового поля на основе параметров сложности
     */
    private void initBoard() {
        controller.createVisibleBoard(BoardModel.difficulty.width, BoardModel.difficulty.height);
    }

    /**
     * Метод выходит из игры
     */
    public void exitGame() {
        System.exit(0);
    }

    /**
     * Инициализируем обработчик событий для игрового поля. Переопределенный метод делигирует выполнение обработки события
     * контроллеру, передавая координаты события и само событие, затем проверяет статус игры
     */
    private void addMouseListener() {
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent event) {
                int x = event.getX() / iconSize;
                int y = event.getY() / iconSize;
                controller.checkUserAction(x, y, event);
                controller.checkGameStatus();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
