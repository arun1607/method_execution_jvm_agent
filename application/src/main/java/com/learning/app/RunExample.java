package com.learning.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunExample {


    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("beans.xml");
    }
}
