package com.example.demo.codeExercise;

import java.util.Arrays;

/**
 * @author: hujun
 * @date: 2022/04/22  14:43
 */
public class SortCodeExercise {

    public static void main(String[] args) {
        SortCodeExercise exercise = new SortCodeExercise();
        int[] arr = new int[]{2, 4, 1, 6, 7, 8, 3};
        int[] res = exercise.bubblie(arr);
        System.out.println("11");


    }

    public int[] merge(int[] arr) {
        if (arr.length < 2) {
            return arr;
        }
        int mid = arr.length / 2;
        int[] leftArr = Arrays.copyOfRange(arr, 0, mid);
        int[] rightArr = Arrays.copyOfRange(arr, mid, arr.length);
        int[] res = mergeHandler(merge(leftArr), merge(rightArr));
        return res;

    }

    private int[] mergeHandler(int[] leftArr, int[] rightArr) {
        int[] res = new int[leftArr.length + rightArr.length];
        int leftIndex = 0;
        int rightIndx = 0;
        int resIndex = 0;
        while (leftIndex < leftArr.length && rightIndx < rightArr.length) {
            if (leftArr[leftIndex] > rightArr[rightIndx]) {
                res[resIndex++] = rightArr[rightIndx];
                rightIndx++;
            } else {
                res[resIndex++] = leftArr[leftIndex];
                leftIndex++;
            }
        }
        while (leftIndex < leftArr.length) {
            res[resIndex++] = leftArr[leftIndex++];
        }
        while (rightIndx < rightArr.length) {
            res[resIndex++] = rightArr[rightIndx++];
        }
        return res;
    }

    public void quick(int[] arr, int left, int right) {
        int pos = quickHandler(arr, left, right);
        quick(arr, left, pos - 1);
        quick(arr, pos + 1, right);

    }

    private int quickHandler(int[] arr, int left, int right) {
        int i = left;
        int j = right;
        //基准值选择
        int target = arr[right];
        while (i < j) {
            while (i < j && arr[i] <= target) {
                i++;
            }
            while (i < j && arr[j] >= target) {
                j--;
            }
            swap(arr, i, j);
        }
        swap(arr, i, right);
        return i;
    }

    public int[] insert(int[] arr) {
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            int val = arr[i];
            int preIndex = i - 1;
            while (preIndex >= 0 && arr[preIndex] >= val) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = val;
        }
        return arr;
    }

    public int[] select(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (i != minIndex) {
                swap(arr, minIndex, i);
            }
        }
        return arr;
    }

    public int[] bubblie(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            boolean flag = false;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j + 1] < arr[j]) {
                    swap(arr, j + 1, j);
                }
                flag = true;
            }
            if (!flag) {
                break;
            }
        }
        return arr;
    }


    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


}
