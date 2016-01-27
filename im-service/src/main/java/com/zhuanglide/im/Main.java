package com.zhuanglide.im;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by wwj on 16.1.26.
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext  context = new ClassPathXmlApplicationContext(new String[]{"classpath*:spring/*.xml", "classpath*:applicationContext*.xml"});
        context.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
