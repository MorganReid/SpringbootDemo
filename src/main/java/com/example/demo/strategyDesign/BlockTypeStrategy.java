package com.example.demo.strategyDesign;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/30  12:03
 */
@Component
public class BlockTypeStrategy implements SetupTemplate {
    @Override
    public Map<Integer, String[]> getSelectData() {
        Map<Integer, String[]> map = new HashMap<>();
        String[] str = {"1", "2", "3"};
        map.put(0, str);
        return map;
    }
}
