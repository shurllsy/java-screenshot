package com.shzq.screenshot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ScreenshotApplication implements CommandLineRunner {


    public static void main(String[] args) {
        new SpringApplicationBuilder(ScreenshotApplication.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) {
    }
}
