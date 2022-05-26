package com.example.demo.strategyDesign;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/04/30  11:48
 */
public enum SetupTemplateNatureEnum {
    UNDEFINED(0, "undefined"),
    BLOCKTYPE(1, "block_type"),
    SUBEVENTTYPE(2, "sub_event_type"),
    PRICEZONE(3, "price_zone"),
    PRICETABLE(4, "price_table"),
    SEATTYPE(5, "seat_type"),
    TICKETTYPE(6, "ticket_type"),
    USERROLE(7, "user_role");

    private int type;
    private String desc;
    private static Map<Integer, SetupTemplateNatureEnum> valueMap = Maps.newHashMap();

    public static boolean isValid(Integer type) {
        if (type == null) {
            return false;
        } else {
            return valueMap.containsKey(type) && type != UNDEFINED.getType();
        }
    }

    public static SetupTemplateNatureEnum valueOf(Integer type) {
        return isValid(type) ? (SetupTemplateNatureEnum)valueMap.get(type) : UNDEFINED;
    }

    public int getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    private SetupTemplateNatureEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    static {
        valueMap = Maps.newHashMap();
        SetupTemplateNatureEnum[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            SetupTemplateNatureEnum item = var0[var2];
            valueMap.put(item.getType(), item);
        }

    }
}
