package com.leowork.examination.DynamicProgramming01;

/**
 * @author Leo
 * @version 1.0
 * @className Solution04
 * @since 1.0
 **/
public class Solution04 {
    /**
     * ？？？？？
     * 有问题
     *
     *
     *
     * 正式方法，严格位置依赖的动态规划 + 空间压缩
     * 时间复杂度o(N)
     * @param arr 目标数组
     * @return 最大累加和
     *
     * 动态规划表
     * i : 0~n-1
     * pre: 0 1
     * end: 0 1
     * int[][][] dp = new int[n][2][2];
     * int[][] dp = new int[n][4];
     * pre == 0 ,end == 0 -> 0
     * pre == 0 ,end == 1 -> 1
     * pre == 1 ,end == 0 -> 2
     * pre == 1 ,end == 1 -> 3
     * pre, end -> (pre << 1) | end
     */
    public static int max3(int[] arr){
        if (arr.length == 1){
            return arr[0];
        }
        int n = arr.length;
        int[] next = new int[4];
        int[] cur = new int[4];
        next[0] = Math.max(0, arr[n - 1]);
        for (int i = n - 2; i >= 1 ; i--) {
            for (int pre = 0; pre < 2; pre++){
                for (int end = 0; end < 2; end++){
                    cur[(pre << 1) | end] = next[end];
                    if (pre != 1){
                        cur[(pre << 1) | end] = Math.max(cur[(pre << 1) | end], arr[i]);
                    }
                }
            }
            next[0] = cur[0];
            next[1] = cur[1];
            next[2] = cur[2];
            next[3] = cur[3];
        }
        return Math.max(arr[0] + next[3], next[0]);
    }

}
