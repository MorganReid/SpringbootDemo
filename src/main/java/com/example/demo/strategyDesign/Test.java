package com.example.demo.strategyDesign;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: hujun
 * @date: 2021/04/30  12:08
 */
public class Test {

    @Autowired
    private SetupTemplateRouter router;

    public static void main(String[] args) {
        Test test = new Test01();
        test.handle(0);

    }

    private void handle(int type) {
        print(String.valueOf(type));
//        SetupTemplateNatureEnum natureEnum = SetupTemplateNatureEnum.valueOf(type);
//        SetupTemplate invoker = router.getInvoker(natureEnum);
//        invoker.getSelectData();

    }

    public void print(String str) {
        System.out.println(str + "1111");
    }


}
