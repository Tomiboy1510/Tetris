import javax.swing.*;
import java.util.Random;

/*
    Cosas que podría implementar luego:

    - Launcher con: keybindings, paletas de colores, resolución, FPS
    - Ventana resizable
    - Queue de eventos
*/

public class Main {

    public static void main (String[] args) {

        JFrame window = new JFrame("Tetris");
        long seed = new Random().nextLong();
        GamePanel gp = new GamePanel(1024, 768, seed, 120);

        window.add(gp);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.startGame();
    }
}