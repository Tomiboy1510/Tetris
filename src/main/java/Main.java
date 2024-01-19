import javax.swing.*;

/*
    Cosas que podría implementar luego:

    - Poder cambiar key bindings
    - Generación de bloques en base a una semilla custom
    - Paletas de colores
    - Launcher con distintas resoluciones
    - Ventana resizable
*/

public class Main {

    public static void main (String[] args) {

        JFrame window = new JFrame("Tetris");
        GamePanel gp = new GamePanel(1024, 768);

        window.add(gp);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.startGame();
    }
}