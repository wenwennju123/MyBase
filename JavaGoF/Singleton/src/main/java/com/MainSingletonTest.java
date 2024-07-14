package com;

/**
 * 单例模式测试程序
 * @author Leo
 */
public class MainSingletonTest {
    public static void main(String[] args) {
        System.out.println("Start");
        Singleton o1 = Singleton.getInstance();
        Singleton o2 = Singleton.getInstance();
        System.out.println(o1 == o2 ? "o1与o2是相同的实例" : "o1与o2是不同的实例");
    }
}
