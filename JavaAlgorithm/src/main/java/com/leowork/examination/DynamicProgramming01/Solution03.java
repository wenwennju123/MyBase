package com.leowork.examination.DynamicProgramming01;

/**
 * @author Leo
 * @version 1.0
 * @className Solution03
 * @since 1.0
 **/
public class Solution03 {
    /**
     * 方法三
     *
     * 先憋个递归recursion
     * 给定一个数组arr，假设首位相连
     * 不能选相邻数字，返回最大的累加和
     * @param arr 给定数组
     * @return 最大累加和
     */
    public static int recursionSolution(int[] arr){
        int n = arr.length;
        if (n == 0) {
            return 0;
        }
        if (n == 1){
            return arr[0];
        }
        /*
        n >= 2 的情况下
        分析可能性，0位置的数没有选，
        此时前一个数字没选，pre为0
        末尾数字可以考虑，end为1
        求得此可能性下的最大累加和
         */
        int p1 = sum(arr, 1, 0, 1);
        /*
        分析可能性，0位置的数选了，
        结果是arr[0] + 后面（位数 + 1）的和
        此时前一个数字选了，pre为1
        末尾数字不可以考虑，end为0
        求得此可能性下的最大累加和
         */
        int p2 = arr[0] + sum(arr, 1, 1, 0);
        /*
        返回两种可能性最大的结果
         */
        return Math.max(p1, p2);
    }
    /**
     * 递归
     * 三个可变参数的动态规划问题
     * arr[i......] 不吭选相邻数字，能得到最大多少的累加和
     * @param arr 给定数组
     * @param i 当前位置
     * @param pre 前一个数字[i-1] pre为0表示前一个数字没挑选，为1表示选择过
     * @param end 最后一个数字[n-1] end为0表示最后一个数字不可以选择，end为1表示最后一个数字可以选择
     * @return 最大累加和
     */
    public static int sum(int[] arr, int i, int pre, int end){
        /*
        到达末尾
         */
        if (i == arr.length - 1){
            /*
            baseCase
            如果前一个位置的数字挑选过，或者最后一位不可用，则无法选择，返回0
            */
            if (pre == 1 || end == 0){
                return 0;
            }else {
                return arr[i];
            }
        } else {
            /*
            i还没到末尾
            可能性1：不选择i位置的数字
             */
            int p1 = sum(arr, i + 1, 0, end);
            /*
            可能性2：选择i位置的数
            当前一个位置未被选择过时，可以选择i位置的数
            结果变为i位置 + 后续递归
             */
            int p2 = 0;
            if (pre == 0) {
                p2 = arr[i] + sum(arr, i + 1, 1, end);
            }
            /*
            返回两种可能性最大的结果
            */
            return Math.max(p1, p2);
        }
    }
}
