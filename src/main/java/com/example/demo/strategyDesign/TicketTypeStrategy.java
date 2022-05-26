package com.example.demo.strategyDesign;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/30  12:03
 */
@Component
public class TicketTypeStrategy implements SetupTemplate {
    @Override
    public Map<Integer, String[]> getSelectData() {
        Map<Integer, String[]> map = new HashMap<>();
        String[] str = {"name1", "name2", "name3"};
        map.put(0, str);
        map.put(1, str);
        return map;
    }
}
