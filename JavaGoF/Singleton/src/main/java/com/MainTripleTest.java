package com;

/**
 * @author Leo
 */
public class MainTripleTest {
    public static void main(String[] args) {
        System.out.println("Start");
        int count = 9;
        for (int i = 0; i < count; i++) {
            Triple triple = Triple.getInstance(i % 3);
            System.out.println(i + ":" + triple);
        }
        System.out.println("End");
    }
}
