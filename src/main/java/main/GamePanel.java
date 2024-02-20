package main;

import gamestates.GameStateManager;
import tetris.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {

    private final GameStateManager manager;
    private final int fps;

    public GamePanel(GameSettings settings) {
        setPreferredSize(new Dimension(settings.WIDTH(), settings.HEIGHT()));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        this.fps = settings.fps();
        manager = new GameStateManager(settings);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                manager.keyPressed(e);
            }
        });
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
}
