package com.example.demo;

import com.example.demo.controller.StudentController;
import com.example.demo.domain.Student;
import com.example.demo.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: hujun
 * @date: 2021/03/15  15:32
 */
@RunWith(MockitoJUnitRunner.class)
public class test {

    @Mock
    private StudentService studentServiceMock;

    @InjectMocks
    private StudentController studentController;

    @Captor
    private ArgumentCaptor<Student> argumentCaptor;

    @Mock
    private LinkedList linkedListMock;

    @Spy
    private LinkedList linkedListSpy;


    @Test
    public void testStuMock() {


        Student student1 = new Student("hu01", 10L, 1);
        Student student2 = new Student("hu02", 10L, 1);

        //连续打桩
        when(studentServiceMock.selectOne(eq(1))).thenReturn(student1, student2);
        //自定义打桩
        when(studentServiceMock.selectOne(eq(2))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                Method method = invocationOnMock.getMethod();
                System.out.println("自定义answer arguments" + arguments.toString() + "----method" + method.getName());
                return student2;
            }
        });
        String name1 = studentController.selectOne(1);
        System.out.println(name1);

        String name2 = studentController.selectOne(2);
        System.out.println(name2);

        //spy
        //mock对象输出null
        System.out.println(linkedListMock.get(0));
        //spy对象越界
        // System.out.println(linkedListSpy.get(0));


        //void方法
        doNothing().when(studentServiceMock).print();
        studentController.testReturn();


        // 参数捕获
        studentController.testPrinStu(student1);
        verify(studentServiceMock).printStu(argumentCaptor.capture());
        assertEquals("hu01", argumentCaptor.getValue().getName());


        //执行顺序(验证的操作必须invoked且按照指定顺序invoked)
        InOrder inOrderSingle = inOrder(linkedListMock);
        inOrderSingle.verify(linkedListMock).add("mock1");
        inOrderSingle.verify(linkedListMock).add("mock2");

        InOrder inOrder = inOrder(linkedListMock, linkedListSpy);
        inOrder.verify(linkedListMock).add("mock1");
        inOrder.verify(linkedListSpy).add("spy1");
    }


}
