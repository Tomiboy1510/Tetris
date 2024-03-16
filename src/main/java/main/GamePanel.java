package main;

import gamestates.GameStateManager;
import tetris.ButtonPress;
import tetris.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements Runnable {

    private final GameStateManager manager;
    private final GameController controller;
    private final int FPS;

    public GamePanel(GameSettings settings) {

        setPreferredSize(new Dimension(settings.width(), settings.height()));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        this.FPS = settings.fps();
        manager = new GameStateManager(settings);
        controller = new GameController(manager);

        setKeyBindings(settings.keyBindings());
    }

    public void startGame() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        //  Game loop
        long lastTime = System.nanoTime();
        long now;
        double intervalNano = 1_000_000_000 / (double) FPS;
        double delta = 0;

        while (true) {
            now = System.nanoTime();
            delta += (now - lastTime) / intervalNano;
            lastTime = now;

            while (delta >= 1) {
                controller.update();
                manager.update();
                repaint();
                delta--;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        manager.draw((Graphics2D) g);
    }

    private void setKeyBindings(int[] keys) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(keys[0], 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(keys[1], 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(keys[2], 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(keys[3], 0), "rotateCounterClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[4], 0), "rotateClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[5], 0), "pause");
        inputMap.put(KeyStroke.getKeyStroke(keys[6], 0), "reset");

        inputMap.put(KeyStroke.getKeyStroke(keys[0], 0, true), "rel moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(keys[1], 0, true), "rel moveDown");
        inputMap.put(KeyStroke.getKeyStroke(keys[2], 0, true), "rel moveRight");
        inputMap.put(KeyStroke.getKeyStroke(keys[3], 0, true), "rel rotateCounterClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[4], 0, true), "rel rotateClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[5], 0, true), "rel pause");
        inputMap.put(KeyStroke.getKeyStroke(keys[6], 0, true), "rel reset");

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.MOVE_LEFT);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.MOVE_DOWN);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.MOVE_RIGHT);
            }
        });
        actionMap.put("rotateCounterClockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.ROTATE_COUNTER_CLOCKWISE);
            }
        });
        actionMap.put("rotateClockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.ROTATE_CLOCKWISE);
            }
        });
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.PAUSE);
            }
        });
        actionMap.put("reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.press(ButtonPress.RESET);
            }
        });

        actionMap.put("rel moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.MOVE_LEFT);
            }
        });
        actionMap.put("rel moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.MOVE_DOWN);
            }
        });
        actionMap.put("rel moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.MOVE_RIGHT);
            }
        });
        actionMap.put("rel rotateCounterClockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.ROTATE_COUNTER_CLOCKWISE);
            }
        });
        actionMap.put("rel rotateClockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.ROTATE_CLOCKWISE);
            }
        });
        actionMap.put("rel pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.PAUSE);
            }
        });
        actionMap.put("rel reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.release(ButtonPress.RESET);
            }
        });
    }
}
