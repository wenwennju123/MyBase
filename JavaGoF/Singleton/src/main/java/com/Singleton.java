package com;

/**
 * 单例模式实例代码
 * Singleton类只会生成一个实例，初始化行为尽在该类被加载时执行一次
 * 并且构造方法私有化，private修饰，
 * 禁止从Singleton外部调用构造方法，确保任何情况下都只能生成一个实例
 * 可以类比封装的思想
 * Singleton单例模式封装了对象的构造方法
 * 然后对外提供getInstance方法，来允许外部程序获取Singleton类唯一的实例
 * 并且总是返回这一相同实例
 *
 * 单例模式的目的：当存在多个实例时，实例之间会相互影响，在多线程环境下可能会出现bug，
 * 需要保证有且只有一个实例
 * 在第一次调用getInstance方法时，Singleton类会被初始话，static字段singleton被初始化
 *
 * @author Leo
 */
public class Singleton {
    private static Singleton singleton = new Singleton();
    private Singleton(){
        System.out.println("生成了一个实例");
    }
    public static Singleton getInstance(){
        return singleton;
    }
}
