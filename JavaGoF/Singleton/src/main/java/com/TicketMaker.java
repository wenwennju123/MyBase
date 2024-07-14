package com;

/**
 * 使用Singleton单例模式确保只生成一个对象，保证票号的合法性
 * 使用synchronized修饰方法，来让其在多线程环境下正常工作
 * @author Leo
 */
public class TicketMaker {
    private int ticket = 1000;
    private static TicketMaker singleton = new TicketMaker();
    private TicketMaker(){

    }
    public static TicketMaker getInstance(){
        return singleton;
    }
    public synchronized int getNextTicketNumber(){
        return ticket++;
    }
}
