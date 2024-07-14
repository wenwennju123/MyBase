package com;

/**
 * @author Leo
 */
public class MainTicketMakerTest {
    public static void main(String[] args) {
        System.out.println("Start");
        int count = 10;
        for (int i = 0; i < count; i++) {
            System.out.println(i + ":" + TicketMaker.getInstance().getNextTicketNumber());
        }
        System.out.println("End");
    }
}
