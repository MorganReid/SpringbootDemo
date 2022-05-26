package com.example.demo.codeExercise;

import com.example.demo.strategyDesign.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: hujun
 * @date: 2022/04/01  16:35
 * 链表变成练习
 */

public class ListCodeExeccise {

    private static volatile int num = 0;

    private static int min = Integer.MAX_VALUE;

    private static int max = Integer.MIN_VALUE;


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String a=new String("1");
        String b=new String("1");
        System.out.println(a.hashCode()+"------"+b.hashCode());
        System.out.println(a==b);
        System.out.println(a.equals(b));


        Singleton instance1 = Singleton.getUniqueInstance();
        Singleton instance2 = Singleton.getUniqueInstance();
        System.out.println(instance1.hashCode()+"------"+instance2.hashCode());
        Character.toUpperCase('a');


        ReentrantLock reentrantLock = new ReentrantLock();
        ReentrantLock reentrantLock1 = new ReentrantLock();
        System.out.println(reentrantLock.hashCode()+"------"+reentrantLock1.hashCode());

        reentrantLock.lock();
        reentrantLock.tryLock(1, TimeUnit.DAYS);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(0, 2);

        ListCodeExeccise h = new ListCodeExeccise();
        ListNode l1 = new ListNode(0);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(0);
        l1.next.next.next = new ListNode(2);
        l1.next.next.next.next = new ListNode(1);
        l1.next.next.next.next.next = new ListNode(0);

        ListNode l2 = new ListNode(1);
        ListNode l3 = new ListNode(9);
        l3.next = new ListNode(9);


        h.splitListToParts(l1,3);
    }


    public ListNode[] splitListToParts(ListNode head, int k) {
        int n=0;
        ListNode temp=head;
        while(temp!=null){
            n++;
            temp=temp.next;
        }
        int parts=n/k;
        int remainder=n%k;
        ListNode[]res=new ListNode[k];
        ListNode cur=head;
        for(int i=0;i<k&&cur!=null;i++){
            res[i]=cur;
            int size=parts+(i<remainder?1:0);
            for(int j=0;j<size-1;j++){
                cur=cur.next;
            }
            ListNode next=cur.next;
            cur.next=null;
            cur=next;
        }
        return res;
    }

    public ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        ListNode dump = new ListNode(-1);
        ListNode pre = dump;
        ListNode temp1 = l1;
        ListNode temp2 = l2;
        int carry = 0;
        while (temp1 != null || temp2 != null || carry > 0) {
            int val1 = temp1 == null ? 0 : temp1.val;
            int val2 = temp2 == null ? 0 : temp2.val;
            int sum = val1 + val2 + carry;
            carry = sum / 10;
            pre.next = new ListNode(sum % 10);
            pre = pre.next;
            if (temp1 != null) {
                temp1 = temp1.next;
            }
            if (temp2 != null) {
                temp2 = temp2.next;
            }
        }
        return dump.next;
    }

    public ListNode removeDuplicateNodes(ListNode head) {
        ListNode ob = head;
        while (ob != null) {
            ListNode oc = ob;
            while (oc.next != null) {
                if (oc.next.val == ob.val) {
                    oc.next = oc.next.next;
                } else {
                    oc = oc.next;
                }
            }
            ob = ob.next;
        }
        return head;
    }

    public ListNode mergeNodes(ListNode head) {
        ListNode dump = new ListNode(-1);
        ListNode temp = dump;
        int sum = 0;
        ListNode next = head.next;
        while (next != null) {
            if (next.val == 0) {
                ListNode cur = new ListNode(sum);
                temp.next = cur;
                temp = temp.next;
                sum = 0;
            } else {
                sum += next.val;
            }
            next = next.next;
        }
        return dump.next;
    }

    public int pairSum(ListNode head) {
        ListNode midNode = findMidNode(head);
        ListNode reverseNode = reverse(midNode);
        int sum = Integer.MIN_VALUE;
        while (reverseNode != null) {
            sum = Math.max(sum, head.val + reverseNode.val);
            head = head.next;
            reverseNode = reverseNode.next;
        }
        return sum;
    }

    int k = 1;

    public ListNode reverseEvenLengthGroups(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode tail = head;
        for (int i = 0; i < k; i++) {
            tail = tail.next;
        }
        ListNode reverseNode = null;
        if (k % 2 == 0) {
            reverseNode = reverse(head, tail);
        }
        k++;
        head.next = reverseEvenLengthGroups(tail);
        return reverseNode;
    }


    public int[] nodesBetweenCriticalPoints(ListNode head) {
        List<Integer> list = new ArrayList<>();
        int index = 1;
        ListNode temp = head;
        while (temp.next != null && temp.next.next != null) {
            index++;
            ListNode mid = temp.next;
            ListNode next = temp.next.next;
            int midVal = mid.val;
            if (midVal > temp.val && midVal > next.val) {
                list.add(index);
            }
            if (midVal < temp.val && midVal < next.val) {
                list.add(index);
            }
            temp = temp.next;
        }
        int size = list.size();
        if (size < 2) {

            return new int[]{-1, -1};
        }
        int min = Integer.MAX_VALUE;
        int max = list.get(size - 1) - list.get(0);
        for (int i = 1; i < size; i++) {
            min = Math.min(min, list.get(i) - list.get(i - 1));
        }
        return new int[]{min, max};
    }


    private int[] handle(int[] arr) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                max = Integer.max(max, arr[i] - arr[j]);
                min = Integer.min(min, arr[i] - arr[j]);
            }
        }
        int[] res = new int[]{max, min};
        return res;
    }

    public boolean containVal(int[] arr, int value) {
        boolean res = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                res = true;
                break;
            }
        }
        return res;
    }

    public int[] nextLargerNodes(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> resStack = new Stack<>();
        ListNode reverseHead = reverse(head);
        while (reverseHead != null) {
            while (!stack.isEmpty() && stack.peek() <= reverseHead.val) {
                stack.pop();
            }
            resStack.push(stack.isEmpty() ? 0 : stack.peek());
            stack.push(reverseHead.val);
            reverseHead = reverseHead.next;
        }
        int[] arr = new int[resStack.size()];
        int index = 0;
        while (!resStack.isEmpty()) {
            arr[index++] = resStack.pop();
        }
        return arr;
    }

    //链表奇偶划分13524
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode oddNode = head;
        ListNode evenHead = head.next;
        ListNode evenNode = evenHead;
        while (evenNode != null && evenNode.next != null) {
            oddNode.next = evenNode.next;
            oddNode = oddNode.next;
            evenNode.next = oddNode.next;
            evenNode = evenNode.next;
        }
        oddNode.next = evenHead;
        return oddNode;
    }

    public boolean isPalindrome(ListNode head) {
        ListNode node = findMidNode(head);
        ListNode revereNode = reverse2(node.next);
        ListNode p1 = head;
        ListNode p2 = revereNode;
        boolean result = true;
        while (result && p2 != null) {
            if (p1.val != p2.val) {
                result = false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        node.next = reverse2(revereNode);
        return result;
    }

    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //寻找中间节点
        ListNode middleNode = findMidNode(head);
        ListNode rightNode = middleNode.next;
        middleNode.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(rightNode);
        return mergeSort(left, right);
    }

    private ListNode mergeSort(ListNode l1, ListNode l2) {
        ListNode sentry = new ListNode(-1);
        ListNode curr = sentry;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }

            curr = curr.next;
        }

        curr.next = l1 != null ? l1 : l2;
        return sentry.next;
    }

    public void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode midNode = findMidNode(head);
        ListNode l2 = midNode.next;
        midNode.next = null;
        ListNode reverseNode = reverse(l2);
        merge(head, reverseNode);
    }


    public void mergeList(ListNode l1, ListNode l2) {
        ListNode l1_tmp;
        ListNode l2_tmp;
        while (l1 != null && l2 != null) {
            l1_tmp = l1.next;
            l2_tmp = l2.next;

            l1.next = l2;
            l1 = l1_tmp;

            l2.next = l1;
            l2 = l2_tmp;
        }
    }


    private void merge(ListNode head, ListNode reverseNode) {
        ListNode headTemp;
        ListNode reverseTemp;
        while (head != null && reverseNode != null) {
            headTemp = head.next;
            reverseTemp = reverseNode.next;
            head.next = reverseNode;
            head = headTemp;

            reverseNode.next = head;
            reverseNode = reverseTemp;
        }
    }

    private ListNode findMidNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }


    //翻转区间节点
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode startDump = new ListNode(-1, head);
        ListNode endDump = new ListNode(-1, head);
        ListNode startNode = startDump;
        ListNode endNode = endDump;
        for (int i = 0; i < left - 1 && startNode.next != null; i++) {
            startNode = startNode.next;
        }
        for (int j = 0; j < right && endNode.next != null; j++) {
            endNode = endNode.next;
        }
        ListNode next = endNode.next;
        ListNode start = startNode.next;
        ListNode newHead = reverse(startNode.next, endNode.next);

        startNode.next = newHead;
        start.next = next;

        return startDump.next;
    }


    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        ListNode tail = head;
        //链表长度计数器
        int n = 1;
        while (tail.next != null) {
            tail = tail.next;
            n++;
        }
        //构成循环链表
        int move = n - k % n;
        if (move % n == 0) {
            return head;
        }
        tail.next = head;
        while (move-- > 0) {
            tail = tail.next;
        }
        ListNode newHead = tail.next;
        tail.next = null;
        return newHead;
    }

    //递归
    public ListNode reverse(ListNode head) {
        if (head.next == null) {
            return head;
        }
        System.out.println(head);
        ListNode last = reverse(head.next);
        head.next.next = head;
        head.next = null;
        System.out.println(head);
        return last;
    }

    //循环
    public ListNode reverse2(ListNode head) {
        ListNode pre = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = pre;
            pre = curr;
            curr = next;
        }
        return pre;
    }


    //循环
    private ListNode reverse(ListNode head, ListNode tail) {
        ListNode pre = null;
        ListNode next = null;
        while (head != tail) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = head;
        for (int i = 0; i < k; i++) {
            if (tail == null) {
                return head;
            }
            tail = tail.next;
        }
        ListNode newHead = reverse(head, tail);
        head.next = reverseKGroup(tail, k);
        return newHead;
    }

    public ListNode swapPairs(ListNode head) {
        ListNode dump = new ListNode(0);
        dump.next = head;
        ListNode temp = dump;
        while (temp.next != null && temp.next.next != null) {
            ListNode node1 = temp.next;
            ListNode node2 = temp.next.next;

            temp.next = node2;
            node1.next = node2.next;
            node2.next = node1;
            temp = node1;
        }
        return dump.next;
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dump = new ListNode(0);
        ListNode head1 = l1, head2 = l2, curHead = dump;
        int carry = 0;
        while (head1 != null || head2 != null) {
            int val1 = head1 == null ? 0 : head1.val;
            int val2 = head2 == null ? 0 : head2.val;
            int sum = val1 + val2 + carry;
            carry = sum / 10;
            curHead.next = new ListNode(sum % 10);
            curHead = curHead.next;
            if (head1 != null) {
                head1 = head1.next;
            }
            if (head2 != null) {
                head2 = head2.next;
            }
        }
        if (carry > 0) {
            curHead.next = new ListNode(carry);
        }
        return dump.next;
    }

    public int[] getLeastNumbers(int[] arr, int k) {

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(k, (o1, o2) -> {
            return o2 - o1;
        });
        for (int i = 0; i < arr.length; i++) {
            if (priorityQueue.size() < k) {
                priorityQueue.add(arr[i]);
            } else {
                if (arr[i] < priorityQueue.peek()) {
                    priorityQueue.poll();
                    priorityQueue.add(arr[i]);
                }
            }
        }
        int[] res = new int[priorityQueue.size()];
        int index = 0;
        while (!priorityQueue.isEmpty()) {
            res[index++] = priorityQueue.poll();
        }
        return res;
    }


}
