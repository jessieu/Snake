package snake;

import javax.swing.*;
import java.awt.*;

public class Snake {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new SimpleFrame();
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            // add the panel component to the window
            frame.add(new Panel());
        });
    }
}

class SimpleFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 720; // include the menu bar in window

    public SimpleFrame() {
        setBounds(10, 10, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
