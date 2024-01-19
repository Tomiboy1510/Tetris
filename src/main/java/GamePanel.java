import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private int WIDTH;
    private int HEIGHT;
    private Thread gameThread;
    private GameArea gameArea;
    private InputHandler inputHandler;

    public GamePanel(int WIDTH, int HEIGHT) {

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setFocusable(true);

        inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);

        // Inicializar área de juego (asegurando que la altura sea múltiplo de 20)
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
                gameAreaHeight
        );
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        //  Game loop
        while (gameThread != null) {
            update();
            repaint();
        }
    }

    private void update() {
        // Gestionar input

        // Actualizar estado del juego
        gameArea.update();
    }

    public void paintComponent(Graphics g) {

        // Dibujar área de juego
        super.paintComponent(g);
        gameArea.draw((Graphics2D) g);

        //Dibujar otros elementos de la interfaz

    }
}
