package com.leowork.fasttest;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Leo
 * @version 1.0
 * @className Test01
 * @since 1.0
 **/
public class Test01 {
    public static void main(String[] args) {
        int[] arr = new int[100];

        Random random = new Random();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(101);
        }

        /*
        直接输出的是数组的引用内存地址
        */
        System.out.println(arr);

        /*
        forEach遍历
         */
        for (int num : arr){
            System.out.print(num + ", ");
        }
        System.out.println();
        /*
          流式编程的其他方法：
          数组求和
            Arrays.stream(array).sum()
          数组求最大值
            Arrays.stream(array).max().getAsInt()
          过滤数组中的偶数元素
            Arrays.stream(array).filter(num -> num % 2 == 0)
          对数组中每个元素进行平方操作
            使用map()方法可以对数组中的元素进行转换和映射，
            而使用forEach()方法可以对流中的每个元素执行一个操作。具体使用哪种方法取决于需求和操作的复杂性
            Arrays.stream(array).map(num -> num * num)
          对数组元素进行排序
            Arrays.stream(array).sorted()

          流式编程遍历数组并打印元素，添加逗号分隔符
          这种方法可以自动去除末尾的分隔符
         */
        String result = Arrays.stream(arr)
                              .mapToObj(String::valueOf)
                              .collect(Collectors.joining(", "));
        System.out.println(result);
        /*
        或者使用lambda + 流式编程的形式来完成
         */
        Arrays.stream(arr)
              .forEach(num -> System.out.print(num + ", "));

    }
}
