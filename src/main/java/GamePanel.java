import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements Runnable {

    private final int fps;
    private Thread gameThread;
    private final GameArea gameArea;

    public GamePanel(int WIDTH, int HEIGHT, long seed, int fps) {
        this.fps = fps;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setFocusable(true);

        setupInputHandling();

        // Inicializar área de juego (asegurando que la altura sea múltiplo de 20)
        // La altura es el doble de 10, que es el ancho en bloques del área de juego
        int pretendedHeight = (int)(HEIGHT * 0.9);
        int remainder = pretendedHeight % 20;
        int gameAreaHeight = remainder == 0 ?
                pretendedHeight :
                pretendedHeight - remainder;
        int gameAreaWidth = gameAreaHeight/2;

        this.gameArea = new GameArea(
                WIDTH/2 - gameAreaWidth/2,
                HEIGHT/2 - gameAreaHeight/2,
                gameAreaWidth,
                gameAreaHeight,
                seed,
                fps
        );
    }

    private void setupInputHandling() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.move(GameArea.Movements.LEFT);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.move(GameArea.Movements.RIGHT);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.move(GameArea.Movements.DOWN);
            }
        });
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.pause();
            }
        });
        actionMap.put("rotateClockWise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.move(GameArea.Movements.ROTATE_CLOCKWISE);
            }
        });
        actionMap.put("rotateCounterClockWise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameArea.move(GameArea.Movements.ROTATE_COUNTER_CLOCKWISE);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "rotateClockWise");
        inputMap.put(KeyStroke.getKeyStroke("Z"), "rotateCounterClockWise");
        inputMap.put(KeyStroke.getKeyStroke("P"), "pause");
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
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
                gameArea.update();
                repaint();
                delta--;
            }
        }
    }

    public void paintComponent(Graphics g) {
        // Dibujar área de juego
        super.paintComponent(g);
        gameArea.draw((Graphics2D) g);

        //Dibujar otros elementos de la interfaz

    }
}
