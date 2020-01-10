package com.shzq.screenshot.view;

import com.shzq.screenshot.bean.Result;
import com.shzq.screenshot.enums.ResultType;

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CaptureScreen {

    public static void main(String[] args) throws AWTException, InterruptedException {
        CaptureScreen screen = new CaptureScreen();

        System.out.println(Thread.currentThread().getName());

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<Result> r = new AtomicReference<>();
        screen.screen(result -> {
            ResultType resultType = result.getResultType();
            byte[] imageBytes = result.getImageBytes();
            boolean success = result.isSuccess();
            System.out.println("resultType = " + resultType);
            System.out.println("success = " + success);
            if (imageBytes != null) {
                System.out.println("imageBytes.length = " + imageBytes.length);
            }
            countDownLatch.countDown();
            r.set(result);
            System.out.println(Thread.currentThread().getName());
        });

        countDownLatch.await();

        System.out.println("screen = " + screen);
    }

    public void screen() throws AWTException {
        new ScreenFrame();
    }

    public void screen(Consumer<Result> consumer) throws AWTException {
        new ScreenFrame(consumer);
    }
}

