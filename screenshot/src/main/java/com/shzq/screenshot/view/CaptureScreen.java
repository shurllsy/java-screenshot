package com.shzq.screenshot.view;

import javax.swing.*;
import java.awt.*;

public class CaptureScreen extends JFrame {

    public CaptureScreen() {
        setTitle("截图");
        setSize(100, 80);
        setLocationRelativeTo(null);

        JButton jButton = new JButton("截图");
        jButton.addActionListener(e -> screen());
        add(jButton);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {
        CaptureScreen screen = new CaptureScreen();
//        screen.screen();
    }

    public void screen() {
        EventQueue.invokeLater(() -> {
            try {
                new ScreenFrame();
            } catch (AWTException e) {
                e.printStackTrace();
            }

        });
    }
}

