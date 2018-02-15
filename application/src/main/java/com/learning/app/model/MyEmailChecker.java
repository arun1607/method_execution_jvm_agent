package com.learning.app.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyEmailChecker implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(MyEmailChecker.class);
    private String message;

    public MyEmailChecker(String message) {

        this.message = message;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(5000);
                logger.info("Doing my work with message: " + message);
            } catch (InterruptedException e) {
                logger.error("error occurred", e);
            }

        }
    }
}
