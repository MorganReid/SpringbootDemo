package com.example.demo.codeExercise;


import java.util.ArrayList;
import java.util.List;

/**
 * @author: hujun
 * @date: 2022/04/23  15:39
 */
public class code {

    private void testPri() {
        System.out.println("private");
    }

    public void testPub(int a) {
        System.out.println("public");
    }

    public static void testSta() {
        System.out.println("static");
    }


    public static List<List<Integer>> applicationPairs1(int deviceCapacity, List<List<Integer>> foregroundAppList, List<List<Integer>> backgroundAppList) {
        List<List<Integer>> allList = new ArrayList<>();

        int maxValue = 0;

        for (int i = 0; i < foregroundAppList.size(); i++) {
            int forValue = foregroundAppList.get(i).get(1);

            for (int j = 0; j < backgroundAppList.size(); j++) {
                boolean flag = false;
                int backValue = backgroundAppList.get(j).get(1);
                int sum = forValue + backValue;
                if (sum > maxValue && sum < deviceCapacity) {
                    allList.clear();
                    flag = true;
                    maxValue = sum;
                } else if (sum == maxValue) {
                    flag = true;
                }

                if (flag) {
                    List<Integer> curList = new ArrayList<>();
                    curList.add(foregroundAppList.get(i).get(0));
                    curList.add(backgroundAppList.get(j).get(0));
                    allList.add(curList);
                }
            }

        }
        return allList;
    }


    static class Node {
        int index1;
        int index2;
        int sum;

        public Node(int i, int j, int k) {
            this.index1 = i;
            this.index2 = j;
            this.sum = k;
        }
    }

    public static void main(String args[]) {
        String str = "https://pic8.58cdn.com.cn//mobile/big/n_v2455feea08ba048ce8bd853d323fa3a65.jpg?t=1\n";
        int index = str.lastIndexOf('/');
        int index1 = str.indexOf('?');
        String substring = str.substring(index+1, index1);
        System.out.println(substring);
    }

}
