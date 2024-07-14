package com;

/**
 * 演示在多线程环境下，降低程序处理速度的情况
 * if (singletonSlow == null)这一判断在多线程并发的环境下并不安全
 * 需要对此方法添加synchronized修饰，确保线程安全性
 *
 * 未添加synchronized输出：
 * Start
 * End
 * 生成了一个实例
 * 生成了一个实例
 * 生成了一个实例
 * A:obj=Singleton单例模式.SingletonSlow@4bd18f62
 * B:obj=Singleton单例模式.SingletonSlow@ed99c76
 * C:obj=Singleton单例模式.SingletonSlow@150a7ec8
 *
 *
 * 添加synchronized输出：
 * Start
 * End
 * 生成了一个实例
 * B:obj=Singleton单例模式.SingletonSlow@ed99c76
 * A:obj=Singleton单例模式.SingletonSlow@ed99c76
 * C:obj=Singleton单例模式.SingletonSlow@ed99c76
 *
 *
 * @author Leo
 */
public class SingletonSlow {
    private static SingletonSlow singletonSlow = null;
    private SingletonSlow(){
        System.out.println("生成了一个实例");
        slowdown();
    }
    public static synchronized SingletonSlow getInstance(){
        if (singletonSlow == null) {
            singletonSlow = new SingletonSlow();
        }
        return singletonSlow;
    }
    private void slowdown(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
