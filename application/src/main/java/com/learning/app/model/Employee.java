package com.learning.app.model;

import com.learning.agent.Measured;

import java.util.Random;

public class Employee {
    private Random random = new Random();

    public Employee() {

    }

    @Measured
    public void doSleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
    }

    @Measured
    private void doTask() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
        }
    }

    @Measured
    public void doWork() {
        for(int i = 0 ; i < random.nextInt(10) ; i++) {
            doTask();
        }
    }
}
