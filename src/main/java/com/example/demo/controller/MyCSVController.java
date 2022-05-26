package com.example.demo.controller;

import com.example.demo.domain.Student;
import com.example.demo.utils.ExportUtil;
import com.example.demo.utils.UploadUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/01  16:15
 */
@RestController
@Slf4j
public class MyCSVController {

    @GetMapping("/exportCSV")
    public void testExport(HttpServletResponse response) {
        List<Map<String, Object>> dataList = null;
        // 查询到要导出的信息
        List<Student> studentList = new ArrayList<>();
//        studentList.add(new Student("name1", 11, 1));
//        studentList.add(new Student("name2", 22, 2));
//        studentList.add(new Student("name3", 33, 3));

        if (studentList.size() == 0) {
            log.error("无数据导出");
        }
        String sTitle = "用户名,年龄,密码";
        String fName = "student表";
        String mapKey = "name,age";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (Student student : studentList) {
            map = new HashMap<>();
            map.put("name", student.getName());
            map.put("age", student.getAge());
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
        } catch (Exception e) {
            log.error("生成csv文件失败", e);
        }
        log.error("数据导出出错");
    }

    @PostMapping("/uploadCSV")
    public void testUpload(@RequestParam MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        String prefix = filename.substring(filename.lastIndexOf(".") + 1);
        List<Student> studentList = UploadUtil.doUpload(Student.class, bytes, prefix);
        for (Student student : studentList) {
            System.out.println(student.getName());
        }
    }


    //字符串s包含t的最小子串
    static String minWindow(String s, String t) {
        Map<Character, Integer> needMap = Maps.newHashMap();
        Map<Character, Integer> sourceMap = Maps.newHashMap();
        for (int i = 0; i < t.length(); i++) {
            needMap.put(t.charAt(i), needMap.getOrDefault(t.charAt(i), 0) + 1);
        }
        int count = 0;
        int left = 0;
        int right = 0;
        int size = Integer.MAX_VALUE;
        int start = 0;
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            if (needMap.containsKey(c)) {
                sourceMap.put(c, sourceMap.getOrDefault(c, 0) + 1);
                if (sourceMap.get(c).equals(needMap.get(c))) {
                    count++;
                }
            }
            while (count == needMap.size()) {

                if (right - left < size) {
                    size = right - left;
                    start = left;
                }
                char d = s.charAt(left);
                left++;
                if (needMap.containsKey(d)) {
                    if (sourceMap.get(d).equals(needMap.get(d))) {
                        count--;
                    }
                    sourceMap.put(d, sourceMap.getOrDefault(d, 0) - 1);
                }
            }
        }
        return size == Integer.MAX_VALUE ? "" : s.substring(start, start + size);
    }

    public static void main(String[] args) {

        int[] arr = new int[]{10, 9, 2, 5, 3, 7, 101, 18};
        String text1 = "psnw";
        String text2 = "zohs";
        int i = longestCommonSubsequence(text1, text2);
        System.out.println(i);
    }


    //最长公共子序列
    static int longestCommonSubsequence(String text1, String text2) {
        int length1 = text1.length();
        int length2 = text2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];
        for (int i = 1; i <= length1; i++) {
            for (int j = 1; j <= length2; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[length1][length2];
    }

    static int minUp(int[] arr) {
        int[] dp = new int[arr.length + 1];
        Arrays.fill(dp, 1);
        int res = Integer.MIN_VALUE;
        for (int i = 1; i < arr.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[i] > arr[j]) {
                    dp[i] = dp[j] + 1;
                    break;
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    int maxSubArray(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int dp_0 = nums[0];
        int dp_1 = 0, res = dp_0;
        for (int i = 1; i < n; i++) {
            dp_1 = Math.max(nums[i], nums[i] + dp_0);
            dp_0 = dp_1;
            res = Math.max(res, dp_1);
        }
        return res;
    }

    static void bucketSort(int[] arr) {
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            minVal = Math.min(minVal, arr[i]);
            maxVal = Math.max(maxVal, arr[i]);
        }
        int[] countArr = new int[maxVal + 1];
        for (int i = 0; i < arr.length; i++) {
            countArr[arr[i]]++;
        }
        int index = 0;
        for (int i = 0; i < countArr.length; i++) {
            for (int j = 0; j < countArr[i]; j++) {
                arr[index++] = i;
            }
        }
    }

    static void bubbleSort(int[] arr) {
        if (arr.length < 0) {
            return;
        }
        boolean flag = false;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    static void selectSort(int[] arr) {
        if (arr.length < 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int minValue = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = minValue;
            }
        }
    }

    static void insertSort(int[] arr) {
        if (arr.length < 0) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            int index = i - 1;
            int value = arr[i];
            //两个条件不可置换位置
            while (index >= 0 && arr[index] > value) {
                arr[index + 1] = arr[index];
                index--;
            }
            arr[index + 1] = value;
        }
    }

    public static void ShellSort(int[] array) {
        int len = array.length;
        if (len == 0)
            return;
        int gap = len / 2;
        while (gap > 0) {
            //直接插入排序
            for (int i = gap; i < len; i++) {
                int current = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && array[preIndex] > current) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = current;
            }
            gap /= 2;
        }
    }

    static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            int begin = quickHelp(arr, start, end);
            quickSort(arr, start, begin - 1);
            quickSort(arr, begin + 1, end);
        }
    }

    private static int quickHelp(int[] arr, int left, int right) {
        //固定坑
        int key = arr[right];
        while (left < right) {
            //left找大，找到后赋值，left作为新坑
            while (left < right && arr[left] <= key) {
                left++;
            }
            arr[right] = arr[left];
            //right找小，找到后赋值，right作为新坑
            while (left < right && arr[right] >= key) {
                right--;
            }
            arr[left] = arr[right];
        }
        //将坑元素赋值给相遇点
        arr[left] = key;
        return left;
    }

    static int count = 0;

    static void printIndent(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(" ");
        }
    }

    static int[] mergeSort(int[] arr) {
        printIndent(count++);
        if (arr.length < 2) {
            return arr;
        }
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static int[] merge(int[] left, int[] right) {
        int[] res = new int[left.length + right.length];
        int leftIndex = 0;
        int rightIndex = 0;
        int resIndex = 0;
        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex] <= right[rightIndex]) {
                res[resIndex++] = left[leftIndex++];
            } else {
                res[resIndex++] = right[rightIndex++];
            }
        }

        while (leftIndex < left.length) {
            res[resIndex++] = left[leftIndex++];
        }
        while (rightIndex < right.length) {
            res[resIndex++] = right[rightIndex++];
        }
        return res;
    }


    //目标和的最小张数
    static int money(int arr[], int count) {
        int[] dp = new int[arr.length + 1];
        Arrays.fill(dp, count + 1);
        dp[0] = 0;
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (i - j < 0) {
                    continue;
                }
                dp[i] = Math.min(dp[i], dp[i - j] + 1);
            }
        }
        return dp[count] < count + 1 ? dp[count] : -1;
    }
    //最长上升子序列


}
