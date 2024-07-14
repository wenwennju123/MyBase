package com.leowork.examination.DynamicProgramming01;

/**
 * @author Leo
 * @version 1.0
 * @className Solution
 * @since 1.0
 *
 * 题目描述：来自华为OD
 * 给定一个数组 arr ，长度为n ，表示n 个格子的分数，并且这些格子首位相连
 * 孩子不能选择相邻的格子，不能回头选，不能选超过一圈。
 * 但是孩子可以决定从任何位置开始选，也可以什么都不选
 * 返回孩子能获得的最大分支
 * 1 <= n <= 10^6
 * 0 < arr[i] <= 10^6
 *
 * 题目分析，不可以选相邻，求最大累加和
 * 动态规划题目
 **/
public class Solution01 {
    /**
     * 方法一
     *
     * 暴力方法，测试用
     * @param arr 目标数组
     * @return 最大累加和
     */
    public static int max1(int[] arr){
        if (arr.length == 1){
            return arr[0];
        }
        return process(arr, 0, new boolean[arr.length]);
    }
    public static int process(int[] arr, int i, boolean[] path){
        if (i == arr.length){
            if (path[0] && path[arr.length - 1]){
                return Integer.MIN_VALUE;
            }
            for (int j = 1; j < arr.length; j++){
                if (path[j - 1] && path[j]){
                    return Integer.MIN_VALUE;
                }
            }
            int ans = 0;
            for (int j = 0; j < arr.length; j++){
                if (path[j]){
                    ans += arr[j];
                }
            }
            return ans;
        }else {
            path[i] = true;
            int ans1 = process(arr, i + 1, path);
            path[i] = false;
            int ans2 = process(arr, i + 1, path);
            return Math.max(ans1, ans2);
        }
    }
}
