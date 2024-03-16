package main;

import tetris.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class GameLauncher extends JFrame {

    private final Map<String, Dimension> resolutions;
    private final JComboBox<String> resComboBox;
    private final JTextField seedText;
    private final int[] keyBindings;
    private final JButton changeKeyBindingsButton;
    private final JButton startButton;

    public GameLauncher() {
        setTitle("Tetris");
        setFocusable(true);
        setResizable(false);

        keyBindings = new int[] {
                KeyEvent.VK_A,
                KeyEvent.VK_S,
                KeyEvent.VK_D,
                KeyEvent.VK_Z,
                KeyEvent.VK_SPACE,
                KeyEvent.VK_P,
                KeyEvent.VK_R,
        };

        setLayout(new GridLayout(4,1));

        resolutions = new LinkedHashMap<>(); // Mantiene el orden
        resolutions.put("1920 x 1080 (16:9)", new Dimension(1920, 1080));
        resolutions.put("1600 x 900 (16:9)", new Dimension(1600, 900));
        resolutions.put("1280 x 720 (16:9)", new Dimension(1280, 720));
        resolutions.put("1024 x 768 (4:3)", new Dimension(1024, 768));
        resolutions.put("800 x 600 (4:3)", new Dimension(800, 600));
        resolutions.put("400 x 300 (4:3)", new Dimension(400, 300));

        resComboBox = new JComboBox<>(resolutions.keySet().toArray(new String[0]));
        resComboBox.setSelectedIndex(3);

        seedText = new JTextField(15);

        changeKeyBindingsButton = new JButton("Change key bindings");
        changeKeyBindingsButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //changeKeyBindings();
            }
        });

        startButton = new JButton("START GAME");
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchGame();
            }
        });

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.add(new JLabel("Resolution"));
        panel1.add(resComboBox);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2.add(new JLabel("Seed"));
        panel2.add(seedText);

        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.add(changeKeyBindingsButton);

        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(startButton);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        pack();
    }

    private void launchGame() {
        setVisible(false);

        Color[] palette = new Color[] {
                new Color(249, 65, 68),
                new Color(243, 114, 44),
                new Color(248, 150, 30),
                new Color(249, 199, 79),
                new Color(144, 190, 109),
                new Color(67, 170, 139),
                new Color(87, 117, 144)
        };

        long seed;
        String txt = seedText.getText();

        if (txt.isEmpty()) {
            seed = new Random().nextLong();
        } else {
            seed = new Random(txt.hashCode()).nextLong();
        }

        GamePanel gp = new GamePanel(new GameSettings(
                resolutions.get((String) resComboBox.getSelectedItem()).width,
                resolutions.get((String) resComboBox.getSelectedItem()).height,
                seed,
                60,
                palette,
                keyBindings
        ));

        JFrame gameWindow = new JFrame("Tetris");

        gameWindow.add(gp);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.pack();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);

        gp.startGame();
    }

    /*public void changeKeyBindings() {
        changeKeyBindingsButton.setEnabled(false);
        startButton.setEnabled(false);

        String[] messages = new String[] {
                "Press key for MOVE RIGHT",
                "Press key for MOVE LEFT",
                "Press key for MOVE DOWN",
                "Press key for ROTATE COUNTERCLOCKWISE",
                "Press key for ROTATE CLOCKWISE",
                "Press key for PAUSE",
                "Press key for RESET"
        };

        for (int i = 0; i < messages.length; i++) {
            keyBindings[i] = HACER ALGO;
        }

        changeKeyBindingsButton.setEnabled(true);
        startButton.setEnabled(true);
    }*/
}
