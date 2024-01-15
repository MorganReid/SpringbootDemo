package com.example.demo.codeExercise;

import java.util.Arrays;

/**
 * leetcode.com热门150
 *
 * @author junhu
 * @date 2024/1/15
 */
public class TopInterview150 {
    public static void main(String[] args) {
        //nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int[] nums2 = new int[]{2, 5, 6};
        merge2(nums1, 3, nums2, 3);
    }

    /**
     * link:https://leetcode.com/problems/merge-sorted-array/?envType=study-plan-v2&envId=top-interview-150
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public static void merge1(int[] nums1, int m, int[] nums2, int n) {
        int start1 = 0;
        int start2 = 0;
        int index = 0;
        int[] res = new int[m + n];
        while (start1 < m && start2 < n) {
            if (nums1[start1] < nums2[start2]) {
                res[index++] = nums1[start1++];
            } else {
                res[index++] = nums2[start2++];
            }
        }
        if (start1 == m) {
            for (; start2 < n; start2++) {
                res[index++] = nums2[start2];
            }
        } else {
            for (; start1 < m; start1++) {
                res[index++] = nums1[start1];
            }
        }

        nums1 = Arrays.copyOfRange(res, 0, res.length);
        System.out.println(nums1);
    }

    public static void merge2(int[] nums1, int m, int[] nums2, int n) {
        int start1 = m - 1;
        int start2 = n - 1;
        int index = nums1.length - 1;
        while (start2 >= 0) {
            if (start1 >= 0 && nums1[start1] < nums2[start2]) {
                nums1[index--] = nums2[start2--];
            } else {
                nums1[index--] = nums1[start1--];
            }
        }
    }
}
