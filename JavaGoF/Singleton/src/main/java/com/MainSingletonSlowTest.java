package com;

/**
 * @author Leo
 */
public class MainSingletonSlowTest implements Runnable{
    public static void main(String[] args) {
        System.out.println("Start");
        new Thread(new MainSingletonSlowTest("A")).start();
        new Thread(new MainSingletonSlowTest("B")).start();
        new Thread(new MainSingletonSlowTest("C")).start();
        System.out.println("End");
    }
    @Override
    public void run(){
        SingletonSlow obj = SingletonSlow.getInstance();
        System.out.println(getName() + ":obj=" + obj);
    }

    private String getName() {
        return this.name;
    }

    String name;
    public MainSingletonSlowTest(String name) {
        this.name = name;
    }
}
