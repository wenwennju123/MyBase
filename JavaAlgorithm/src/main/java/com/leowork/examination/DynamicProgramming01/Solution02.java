package com.leowork.examination.DynamicProgramming01;

/**
 * @author Leo
 * @version 1.0
 * @className Solution02
 * @since 1.0
 **/
public class Solution02 {
    /**
     * 方法二
     *
     * 时间复杂度为O(N)，记忆化搜索
     * @param arr 目标数组
     * @return 最大累加和
     */
    public static int max2(int[] arr){
        if (arr.length == 0){
            return 0;
        }
        if (arr.length == 1){
            return arr[0];
        }
        int n = arr.length;
        /*
        创建动态规划表
        对用数组长度，每个点位（数组元素）对应四个情况
         */
        int[][] dp = new int[n][4];
        for (int i = 0; i < n; i++) {
            dp[i][0] = Integer.MIN_VALUE;
            dp[i][1] = Integer.MIN_VALUE;
            dp[i][2] = Integer.MIN_VALUE;
            dp[i][3] = Integer.MIN_VALUE;
        }
        /*
        可能性1，拿0位置的数
         */
        int ans = arr[0] + process2(arr, 1, 1, 1, dp);
        /*
        可能性2，不拿0位置的数
         */
        ans = Math.max(ans, process2(arr, 1, 0, 0, dp));
        return ans;
    }
    /**
     * arr[0...n-1] 注意0 和 n-1 位置是相邻的
     * @param arr 目标数组
     * @param i 当前来到的位置
     * @param pre i位置的前一个数，为1表示已经拿了，为0表示没拿过
     * @param end n-1位置的数，为1表示可以拿，为0表示不可以拿
     * @param dp 动态规划表
     * @return 最大累加和
     * 转换解释：
     *     i : 0~n-1
     *     pre: 0 1
     *     end: 0 1
     *     int[][][] dp = new int[n][2][2];
     *     int[][] dp = new int[n][4];
     *     四种情况
     *     pre == 0 ,end == 0 -> 0
     *     pre == 0 ,end == 1 -> 1
     *     pre == 1 ,end == 0 -> 2
     *     pre == 1 ,end == 1 -> 3
     *     通过位运算来完成，简化
     *     pre, end -> (pre << 1) | end
     */
    public static int process2(int[] arr, int i, int pre, int end, int[][]dp){
        /*
        如果到了最后一个数字
         */
        if (i == arr.length - 1){
            return (pre == 1 || end == 1) ? 0 : Math.max(0, arr[i]);
        }else {
            /*
            如果不是最后一个数字
            如果不是最小值，返回最小值
            是最小表示这个情况没触发过，继续
             */
            if (dp[i][(pre << 1) | end] != Integer.MIN_VALUE){
                return dp[i][(pre << 1) | end];
            }
            /*
            是建表时候全部赋给的最小值
            情况p1 不选当前位置，之后递归从下一位开始 pre为0
            递归运行时，把所有位都走一遍p1情况，最后一次，p1就是最后一位的数字
             */
            int p1 = process2(arr, i + 1, 0, end, dp);
            /*
            情况p2 先赋最小值保证安全
            情况p2 选当前位置，然后从下一位开始递归，pre为1
            递归运行时，会一直走p1，嵌套，直到走到末尾，开始回头走p2
            然后对每一种可能的路径的累加和进行比较
             */
            int p2 = Integer.MIN_VALUE;
            if (pre != 1){
                p2 = arr[i] + process2(arr, i + 1, 1, end, dp);
            }
            /*
            取最大的结果
             */
            int ans = Math.max(p1, p2);
            /*
            并将对应表中的数据更新为此结果
             */
            dp[i][(pre << 1) | end] = ans;
            return ans;
        }
    }
}
