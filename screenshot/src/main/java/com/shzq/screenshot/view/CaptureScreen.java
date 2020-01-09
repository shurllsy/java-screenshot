package com.shzq.screenshot.view;

import java.awt.*;
import java.util.function.Consumer;

public class CaptureScreen {

    public static void main(String[] args) throws AWTException {
        CaptureScreen screen = new CaptureScreen();
        screen.screen(o -> {
            System.out.println("screen = " + screen);
        });
    }

    public void screen() throws AWTException {
        new ScreenFrame();
    }

    public void screen(Consumer<Boolean> consumer) throws AWTException {
        new ScreenFrame(consumer);
    }
}

