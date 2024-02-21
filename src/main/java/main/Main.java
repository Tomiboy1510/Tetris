package main;

import tetris.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
    Cosas que podría implementar luego:
    - Launcher con: keybindings, paletas de colores, resolución, FPS

    TODO:
        - Dibujar bloques en sus posiciones relativas al tetromino
        - Permitir a un tetromino cambiar de posición (afectando a los bloques) que lo componen,
          o bien, permitir renderizar un tetromino en cualquier posición.
          (Esto es para poder renderizar el siguiente tetromino fuera del área de juego)
        - Mudar el código de GameArea a PlayingState? (Quizá es mejor separar el área de juego de otros elementos de la GUI)
        - Gestionar los keyReleases
*/

public class Main {

    public static void main (String[] args) {

        JFrame window = new JFrame("Tetris");

        long seed = new Random().nextLong();

        Color[] palette = new Color[7];
        palette[0] = new Color(249, 65, 68);
        palette[1] = new Color(243, 114, 44);
        palette[2] = new Color(248, 150, 30);
        palette[3] = new Color(249, 199, 79);
        palette[4] = new Color(144, 190, 109);
        palette[5] = new Color(67, 170, 139);
        palette[6] = new Color(87, 117, 144);

        String[] keys = new String[6];
        keys[0] = "A";
        keys[1] = "S";
        keys[2] = "D";
        keys[3] = "Z";
        keys[4] = "SPACE";
        keys[5] = "P";

        GamePanel gp = new GamePanel(new GameSettings(
                1024,
                768,
                seed,
                60,
                palette,
                keys
        ));

        window.add(gp);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.startGame();
    }
}