package com.leowork.fasttest;

import com.leowork.examination.DynamicProgramming01.Solution01;
import com.leowork.examination.DynamicProgramming01.Solution02;
import com.leowork.examination.DynamicProgramming01.Solution03;
import com.leowork.examination.DynamicProgramming01.Solution04;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 测试动态规划题目1
 * @author Leo
 * @version 1.0
 * @className DynamicProgramming01Test
 * @since 1.0
 **/
public class DynamicProgramming01Test {
    @Test
    public void test() {
        //数组元素范围
        int bound = 100;
        //数组长度，数组长度每加1，暴力方法的耗时约等于翻倍
        int length = 50;
        //测试次数
        int testTimes = 5;
        System.out.println("测试开始");
        System.out.println("==================================================");
        for (int i = 0; i < testTimes; i++) {
            System.out.println("第" + (i + 1) + "次测试开始" );

            /**
             * 随机生成随机种子
             * 注意：
             * Math.random() 方法生成的随机数的范围是[0,1)。
             * 也就是说，它生成的随机数是一个大于等于0且小于1的double类型的值。
             * 具体来说，生成的随机数可以是0，但永远不会等于1
             */
            int ranBound = (int)(Math.random() * bound) + 1;
            int[] arr = DynamicProgramming01Test.randomArray(ranBound, length);

            System.out.print("ranBound=" + ranBound + ", ");
            System.out.println("length=" + length);
            System.out.print("随机数组为：");
            Arrays.stream(arr)
                    .forEach(num -> System.out.print(num + ", "));

            long nowTime = System.currentTimeMillis();
            //int ans1 = Solution01.max1(arr);
            long nowTime2 = System.currentTimeMillis();
            System.out.println();
            //System.out.print("方法一结果为：" + ans1 + "; ");
            System.out.println("运行时间为" + (nowTime2 - nowTime) + "ms");

            int ans2 = Solution02.max2(arr);
            long nowTime3 = System.currentTimeMillis();
            System.out.print("方法二结果为：" + ans2 + "; ");
            System.out.println("运行时间为" + (nowTime3 - nowTime2) + "ms");

            int ans3 = Solution03.recursionSolution(arr);
            long nowTime4 = System.currentTimeMillis();
            System.out.print("方法三结果为：" + ans3 + "; ");
            System.out.println("运行时间为" + (nowTime4 - nowTime3) + "ms");

            //int ans4 = Solution04.max3(arr);
            long nowTime5 = System.currentTimeMillis();
            //System.out.print("方法四结果为：" + ans4 + "; ");
            System.out.println("运行时间为" + (nowTime5 - nowTime4) + "ms");
            System.out.println("==================================================");
        }
        System.out.println("测试结束");
    }

    /**
     * 生成随机数组
     * @param bound 数组元素边界
     * @param length 数组长度
     * @return 随机数组
     */
    private static int[] randomArray(int bound, int length) {

        int[] arr = new int[length];

        Random random = new Random();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(bound);
        }

        return arr;
    }

}
