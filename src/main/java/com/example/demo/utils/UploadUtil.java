package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: hujun
 * @date: 2021/04/02  17:01
 */
public class UploadUtil {

    public static <T> List doUpload(Class<T> clazz, byte[] bytes, String prefix) throws IOException {
        if (!prefix.equals("csv")) {
            throw new IOException("当前文件不是CSV文件");
        }
        ArrayList dataList = new ArrayList();
        List<String> CSVList = getSource(bytes);
        int size = CSVList.size();
        if (size > 1000) {
            throw new IOException("导入CSV数据不能超过1000条");
        }
        if (size < 1) {
            throw new IOException("导入CSV数据不能为空");

        } else {
            Field[] fields = clazz.getDeclaredFields();
            outer:
            for (int rowIndex = 1; rowIndex < size; ++rowIndex) {
                List<String> row = Arrays.asList(CSVList.get(rowIndex).split(","));
                if (isRowEmpty(row)) {
                    break;
                }
                Object data;
                try {
                    data = clazz.newInstance();
                    for (int i = 0; i < 2; ++i) {
                        String cellValue = row.get(i);
                        if (StringUtils.isBlank(cellValue)) {
                            continue;
                        }
                        Field field = fields[i];
                        String name = field.getName();
                        String methodStr = "set" + name.toUpperCase().substring(0, 1) + name.substring(1);
                        Method method = clazz.getMethod(methodStr, field.getType());
                        Class type = field.getType();
                        if (type.isAssignableFrom(String.class)) {
                            method.invoke(data, cellValue);
                        } else if (type.isAssignableFrom(Long.class)) {
                            if (StringUtils.isBlank(cellValue)) {
                                continue outer;
                            } else {
                                method.invoke(data, Double.valueOf(cellValue).longValue());
                            }
                        } else if (type.isAssignableFrom(Integer.class)) {
                            method.invoke(data, StringUtils.isBlank(cellValue) ? 0 : Double.valueOf(cellValue).intValue());
                        } else if (type.isAssignableFrom(Date.class)) {
                            method.invoke(data, DateUtils.parseDate(cellValue, "yyyy-MM-dd"));
                        }
                    }
                } catch (Exception var21) {
                    throw new IllegalArgumentException(String.format("第%s行数据异常!%s", rowIndex + 1, var21.getMessage()));
                }
                dataList.add(data);
            }
            return dataList;
        }
    }

    private static boolean isRowEmpty(List<String> row) {
        return CollectionUtils.isEmpty(row);
    }


    private static List<String> getSource(byte[] bytes) throws IOException {
        BufferedReader br = null;
        ByteArrayInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = new ByteArrayInputStream(bytes);
            //指定以UTF-8编码读入
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line;
        String everyLine;
        List<String> allString = new ArrayList<>();
        try {
            //读取到的内容给line变量
            while ((line = br.readLine()) != null) {
                everyLine = line;
                allString.add(everyLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (isr != null) {
                isr.close();
            }
        }
        return allString;
    }

}
