package com.shzq.screenshot.view;

import java.awt.*;

public class CaptureScreen {

    public static void main(String[] args) throws AWTException {
        CaptureScreen screen = new CaptureScreen();
        screen.screen();
    }

    public void screen() throws AWTException {
        EventQueue.invokeLater(() -> {
            try {
                new ScreenFrame();
            } catch (AWTException e) {
                e.printStackTrace();
            }

        });
//        ScreenFrame screenFrame = new ScreenFrame();
    }
}

