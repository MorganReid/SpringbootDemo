package com.example.demo.codeExercise;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * @author: hujun
 * @date: 2022/04/22  21:44
 */
public class TreeCodeExercise {

    static ThreadLocal<String> threadLocal1 = new ThreadLocal<>();
    static ThreadLocal<Integer> threadLocal2 = new ThreadLocal<>();


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }

    }

    //路径和（任意节点，根节点到任意节点，根节点到叶子节点）
    static List<List<Integer>> res = new ArrayList<>();

    public static void main(String[] args) {

        TreeCodeExercise codeExercise = new TreeCodeExercise();
        TreeNode head = new TreeNode(1);
        head.left = new TreeNode(2);
        head.right = new TreeNode(7);
        head.left.left = new TreeNode(8);
//        head.left.right = new TreeNode(5);
//        head.right.left = new TreeNode(5);
//        head.right.right = new TreeNode(4);

//
//        height(head);
        int[] arr = new int[]{1, 1, 1, 2, 2, 3};
        int[] arr1 = new int[]{7, 6, 5};
        int[][] arr2 = new int[][]{{2, 1}, {1, 0}, {1, 3}, {3, 0}};
//        backtrack(arr, new LinkedList<>());

        String str = "aaa";
        System.out.println(str.substring(1, 2));
        Map<Integer, Integer> map = new HashMap<>();
        codeExercise.maxProfit(arr1);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        System.out.println(list.get(list.size()));


    }


    public boolean isValidBST(TreeNode root) {
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        return isValid(root, minValue, maxValue);

    }

    private boolean isValid(TreeNode root, int minValue, int maxValue) {
        if (root == null) {
            return true;
        }
        return handleValidBST(root, minValue, maxValue) && isValid(root.left, minValue, root.val) && isValid(root.right, root.val, maxValue);

    }

    public boolean handleValidBST(TreeNode root, int min, int max) {
        if (root == null) {
            return true;
        }
        if (root.left != null && (root.left.val >= max || root.left.val >= root.val)) {
            return false;
        }
        if (root.right != null && (root.right.val <= min || root.right.val <= root.val)) {
            return false;
        }
        return true;
    }

    public int numIslands(char[][] grid) {
        int count = 0;
        int row = grid.length;
        int col = grid[0].length;
        if (row <= 0 || col <= 0) {
            return count;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    public void dfs(char[][] grid, int row, int col) {
        //四个方向发散的范围
        if (!rangeHandle(grid, row, col)) {
            return;
        }
        //已经遍历过、水域直接返回
        if (grid[row][col] != '1') {
            return;
        }
        //已经遍历过置2
        grid[row][col] = '2';

        dfs(grid, row + 1, col);
        dfs(grid, row - 1, col);
        dfs(grid, row, col + 1);
        dfs(grid, row, col - 1);
    }

    public boolean rangeHandle(char[][] grid, int row, int col) {
        int r = grid.length;
        int c = grid[0].length;
        return row >= 0 && row < r && col >= 0 && col < c;
    }

    public int maxProfit(int[] prices) {
        //当前作为买入点，找到最大的卖出点
        int res = 0;
        int length = prices.length;
        for (int i = 0; i < length; i++) {
            int maxVal = 0;
            for (int j = i + 1; j < length; j++) {
                maxVal = Math.max(maxVal, prices[j]);
            }
            res = Math.max(res, maxVal - prices[i]);
        }
        return res <= 0 ? 0 : res;
    }

    public int[] findOrder(int numCourses, int[][] prerequisites) {

        //节点关联的入度
        List<List<Integer>> edges = new ArrayList<List<Integer>>();
        //统计入度个数
        int[] indeg = new int[numCourses];
        int[] res = new int[numCourses];
        int index = 0;

        for (int i = 0; i < numCourses; ++i) {
            edges.add(new ArrayList<Integer>());
        }
        for (int[] info : prerequisites) {
            edges.get(info[1]).add(info[0]);
            ++indeg[info[0]];
        }

        Queue<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < numCourses; ++i) {
            if (indeg[i] == 0) {
                queue.offer(i);
            }
        }

        int visited = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            res[index++] = u;
            for (int v : edges.get(u)) {
                --indeg[v];
                if (indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        return res;
    }

    public int[] topKFrequent(int[] nums, int k) {
        //map以值为key,次数为value
        int length = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>((index1, index2) -> {
            return map.get(index1) - map.get(index2);
        });
        for (Integer key : map.keySet()) {
            queue.add(key);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        int[] res = new int[queue.size()];
        int index = 0;
        while (!queue.isEmpty()) {
            res[index++] = queue.poll();
        }
        return res;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        //排序
        Arrays.sort(nums);
        //双指针
        int len = nums.length;
        for (int first = 0; first < len; first++) {
            //去重
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            int cur = nums[first];
            int second = first + 1;
            int third = len - 1;
            while (second < third) {
                int sum = cur + nums[second] + nums[third];
                if (sum > 0) {
                    third--;
                } else if (sum < 0) {
                    second++;
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(cur);
                    list.add(nums[second]);
                    list.add(nums[third]);
                    res.add(list);
                    while (second < third && nums[second + 1] == nums[second]) {
                        second++;
                    }
                    while (second < third && nums[third - 1] == nums[third]) {
                        third--;
                    }
                    second++;
                    third--;
                }
            }
        }
        return res;
    }


    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (int[] person1, int[] person2) -> {
            if (person1[0] != person2[0]) {
                return person2[0] - person1[0];
            } else {
                return person1[1] - person2[1];
            }
        });
        List<int[]> ans = new ArrayList<int[]>();
        for (int[] person : people) {
            //
            ans.add(person[1], person);
        }
        return ans.toArray(new int[ans.size()][]);
    }


    public int packageHandle(int[] weight, int[] value, int size) {
        //重量和价值的长度是一致的
        int wLength = weight.length;
        //在背包容量为j的情况下，前i个物品中选择能取到的最大价值
        int[][] dp = new int[wLength + 1][size + 1];
        //i=0表示一个物品不选取，j=0表示背包容量为0.这两种情况下的价值都是0
        for (int i = 1; i <= wLength; i++) {
            for (int j = 1; j <= size; j++) {
                if (j >= weight[i - 1]) {
                    //每个物品仅可选取一次
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight[i - 1]] + value[i - 1]);
                    //当物品可以重复选取时
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - weight[i - 1]] + value[i - 1]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[wLength][wLength];
    }

    int count = 0;

    public int findTargetSumWays(int[] nums, int target) {
        backtrack(nums, target, 0, 0);
        return count;
    }

    public void backtrack(int[] nums, int target, int index, int sum) {
        if (index == nums.length) {
            if (sum == target) {
                count++;
            }
        } else {
            backtrack(nums, target, index + 1, sum + nums[index]);
            backtrack(nums, target, index + 1, sum - nums[index]);
        }
    }

    public int subarraySum(int[] nums, int k) {
        //方法一：前缀和，然后遍历
        //方法二：辅助空间
        Map<Integer, Integer> map = new HashMap<>();
        int length = nums.length;
        //sum为key,次数为value
        map.put(0, 1);
        int sum = 0;
        int count = 0;
        for (int i = 0; i < length; i++) {
            sum += nums[i];
            int temp = sum - k;
            if (map.containsKey(temp)) {
                count += map.get(temp);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    public int findUnsortedSubarray(int[] nums) {
        //向左找第一个小于
        //向右找第一个大于
        int length = nums.length;
        int to = -1;
        int from = length;
        int maxVal = Integer.MIN_VALUE;
        int minVal = Integer.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            if (nums[i] < maxVal) {
                to = i;
            }
            maxVal = Math.max(maxVal, nums[i]);
        }
        for (int i = length - 1; i >= 0; i--) {
            if (nums[i] > minVal) {
                from = i;
            }
            minVal = Math.min(minVal, nums[i]);
        }
        if (to != -1) {
            return to - from + 1;
        }
        return 0;
    }


    public void handle(String s, List<List<Character>> res, List<Character> temp, int startIndex) {
        res.add(new ArrayList<>(temp));
        for (int i = startIndex; i < s.length(); i++) {
            temp.add(s.charAt(i));
            handle(s, res, temp, i + 1);
            temp.remove(temp.size() - 1);

        }
    }

    public int[] dailyTemperatures(int[] temperatures) {
        int length = temperatures.length;
        int[] res = new int[length];
        //单调栈
        Stack<Integer> stack = new Stack<>();
        for (int i = length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && temperatures[stack.peek()] <= temperatures[i]) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? 0 : stack.peek() - i;
            stack.push(i);
        }
        return res;
    }

    public int lengthOfLongestSubstring(String s) {
        int res = Integer.MIN_VALUE;
        int length = s.length();
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        if (length <= 0) {
            return 0;
        }
        int start = 0;
        for (int i = 0; i < length; i++) {
            if (map.containsKey(s.charAt(i))) {
                start = Math.max(start, map.get(s.charAt(i)));
            }
            res = Math.max(res, i - start);
            map.put(s.charAt(i), i);
        }
        return res;

    }


    public int[] levelOrder1(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        Queue<Integer> numQueue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                numQueue.offer(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        int[] res = new int[numQueue.size()];
        for (int i = 0; i < numQueue.size(); i++) {
            res[i] = numQueue.poll();
        }
        return res;
    }


    public List<List<Integer>> pathSum(TreeNode root, int target, boolean toleave, boolean fromRoot) {
        if (root == null) {
            return null;
        }
        //是否根节点出发
        if (fromRoot) {
            handle(root, target, toleave);
        } else {
            handle(root, target, toleave);
            pathSum(root.left, target, toleave, fromRoot);
            pathSum(root.right, target, toleave, fromRoot);
        }
        return res;
    }

    Deque<Integer> deque = new LinkedList<>();

    public void handle(TreeNode root, int target, boolean toLeave) {
        if (root == null) {
            return;
        }
        deque.addLast(root.val);
        //toLeave:是否到叶子节点
        if (toLeave) {
            if (root.val == target && root.left == null && root.right == null) {
                res.add(new LinkedList<>(deque));
            }
        } else {
            if (root.val == target) {
                res.add(new LinkedList<>(deque));
            }
        }
        handle(root.left, target - root.val, toLeave);
        handle(root.right, target - root.val, toLeave);
        deque.pollLast();
    }

    int sum = 0;

    public TreeNode convertBST(TreeNode root) {
        if (root != null) {
            convertBST(root.right);
            sum += root.val;
            root.val = sum;
            convertBST(root.left);
        }
        return root;
    }


    public static int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int res = Math.max(height(root.left), height(root.right)) + 1;
        System.out.println(res);
        return res;
    }


    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        TreeNode pre = null;
        while (root.val != p.val) {
            //右边
            if (p.val > root.val) {
                root = root.right;
            }
            //左边
            else {
                pre = root;
                root = root.left;
            }
        }
        //假如没有右子树
        if (root.right == null) {
            return pre;
        } else {
            root = root.right;
            while (root.left != null) {
                root = root.left;
            }
            return root;
        }
    }

    static List<LinkedList> res1 = Lists.newLinkedList();

    public static void backtrack(int[] nums, LinkedList<Integer> track) {
        if (track.size() == 2) {
            res1.add(new LinkedList(track));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (track.contains(nums[i])) {
                continue;
            }
            track.add(nums[i]);
            backtrack(Arrays.copyOfRange(nums, i + 1, nums.length), track);
            track.removeLast();
        }
    }

    //二叉有序树转链表，返回头节点

    static TreeNode pre = new TreeNode();

    public static TreeNode treeToList(TreeNode root) {
        if (root == null) {
            return root;
        }
        treeToList(root.right);
        root.right = pre;
        pre = root;
        treeToList(root.left);
        root.left = null;
        return pre;
    }

    public List<Integer> rightSideView(TreeNode root) {
        if (root == null) {
            return null;
        }
        //层级为key，该层级的最右节点值为value
        Map<Integer, Integer> levelValMap = new HashMap<>();
        //节点栈，用于获取最右节点
        Stack<TreeNode> nodeStack = new Stack<>();
        //层级栈，用于控制最右节点put到map
        Stack<Integer> levelStack = new Stack<>();
        nodeStack.push(root);
        levelStack.push(0);
        while (!nodeStack.isEmpty()) {
            TreeNode node = nodeStack.pop();
            int level = levelStack.pop();
            if (!levelValMap.containsKey(level)) {
                levelValMap.put(level, node.val);
            }
            if (node.left != null) {
                nodeStack.push(node.left);
                levelStack.push(level + 1);
            }
            if (node.right != null) {
                nodeStack.push(node.right);
                levelStack.push(level + 1);
            }
        }
        List<Integer> res = handle(levelValMap);
        int maxValue = Integer.MAX_VALUE;
        return res;
    }

    public List<Integer> handle(Map<Integer, Integer> map) {
        List<Integer> res = new ArrayList<>();
        int size = map.keySet().size();
        for (int i = 0; i < size; i++) {
            res.add(map.get(i));
        }
        return res;
    }

    public boolean isSubStructure(TreeNode A, TreeNode B) {
        return (A != null && B != null) && (recur(A, B) || isSubStructure(A.left, B) || isSubStructure(A.right, B));
    }

    boolean recur(TreeNode A, TreeNode B) {
        if (B == null) return true;
        if (A == null || A.val != B.val) return false;
        return recur(A.left, B.left) && recur(A.right, B.right);
    }

    //非递归的层次遍历
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (root == null) {
            return ret;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> levelList = new ArrayList<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                levelList.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            ret.add(levelList);
        }
        return ret;
    }


}

