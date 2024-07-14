package com;

/**
 * 让Triple类的实例持有自己的编号id和一个静态Triple类型的数组，并实现在数组中保存三个实例
 * getInstance方法接收的参数时数组的下标，兵器而返回一个数组下标对应的Triple实例
 * 必须将triple字段定义为static字段，因为static定义的字段初始化只会在第一次生成时执行
 * 如果不，则会每次调用都会执行一次，new一次对象，进入无线循环，最后运行时导致报错堆栈溢出
 *
 * @author Leo
 */
public class Triple {
    private static Triple[] triple = new Triple[]{
            new Triple(0),
            new Triple(1),
            new Triple(2)
    };
    private int id;
    private Triple(int id) {
        System.out.println("The instance " + id + " is created");
        this.id = id;
    }
    public static Triple getInstance(int id){
        return triple[id];
    }
    @Override
    public String toString(){
        return "[Triple id =" + id + "]";
    }
}
