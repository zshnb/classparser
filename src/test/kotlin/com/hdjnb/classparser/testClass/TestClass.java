package com.hdjnb.classparser.testClass;

public class TestClass {
    private String name = "hello";

    private static int age = 5;

    public long inc(int a) {
        try {
            age += 5;
        } catch (Exception e) {
            age = 10;
            return age;
        } finally {
            return 5L;
        }
    }
}
