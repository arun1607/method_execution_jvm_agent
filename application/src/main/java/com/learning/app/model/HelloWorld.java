package com.learning.app.model;

public class HelloWorld {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void doWork() {
        new Thread(new MyEmailChecker(message)).start();

    }
}
