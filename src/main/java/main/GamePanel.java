package main;

import gamestates.GameStateManager;
import tetris.ButtonPress;
import tetris.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements Runnable {

    private final GameStateManager manager;
    private final int fps;

    public GamePanel(GameSettings settings) {
        setPreferredSize(new Dimension(settings.width(), settings.height()));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        this.fps = settings.fps();
        manager = new GameStateManager(settings);

        setKeyBindings(settings.keys());
    }

    public void startGame() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        //  Game loop
        long lastTime = System.nanoTime();
        long now;
        double intervalNano = 1_000_000_000 / (double) fps;
        double delta = 0;

        while (true) {
            now = System.nanoTime();
            delta += (now - lastTime) / intervalNano;
            lastTime = now;

            while (delta >= 1) {
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

    private void setKeyBindings(String[] keys) {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(keys[0]), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(keys[1]), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(keys[2]), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(keys[3]), "rotateCounterClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[4]), "rotateClockwise");
        inputMap.put(KeyStroke.getKeyStroke(keys[5]), "pause");

        inputMap.put(KeyStroke.getKeyStroke("release " + keys[0]), "rel moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("release " + keys[1]), "rel moveDown");
        inputMap.put(KeyStroke.getKeyStroke("release " + keys[2]), "rel moveRight");

        actionMap.put("moveLeft", new ButtonAction(ButtonPress.MOVE_LEFT));
        actionMap.put("moveDown", new ButtonAction(ButtonPress.MOVE_DOWN));
        actionMap.put("moveRight", new ButtonAction(ButtonPress.MOVE_RIGHT));
        actionMap.put("rotateCounterClockwise", new ButtonAction(ButtonPress.ROTATE_COUNTER_CLOCKWISE));
        actionMap.put("rotateClockwise", new ButtonAction(ButtonPress.ROTATE_CLOCKWISE));
        actionMap.put("pause", new ButtonAction(ButtonPress.PAUSE));

        actionMap.put("rel moveLeft", new ButtonAction(ButtonPress.MOVE_LEFT));
        actionMap.put("rel moveDown", new ButtonAction(ButtonPress.MOVE_DOWN));
        actionMap.put("rel moveRight", new ButtonAction(ButtonPress.MOVE_RIGHT));
    }

    private class ButtonAction extends AbstractAction {

        private final ButtonPress p;

        public ButtonAction(ButtonPress p) {
            this.p = p;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().startsWith("rel ")) {
                // Soltar botón?

            }
            manager.buttonPressed(p);
        }
    }
}
