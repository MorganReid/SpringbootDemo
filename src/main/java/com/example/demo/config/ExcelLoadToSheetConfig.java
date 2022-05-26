package com.example.demo.config;

/**
 * @author: hujun
 * @date: 2021/03/08  17:53
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ExcelLoadToSheetConfig {

    public static <T> ArrayList getDataList(Class<T> clazz, InputStream inputStream, String prefix) {
        ArrayList dataList = new ArrayList();
        Workbook workbook = null;
        //第一步：很根据后缀名，创建一个webbook，对应一个Excel文件对象，并绑定输入的表格数据
        try {
            if (("xls").equals(prefix)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (("xlsx").equals(prefix)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                throw new Exception("当前文件不是excel文件");
            }
        } catch (Exception var20) {
            throw new RuntimeException("获取Excel文件异常");
        }
        //第二步：通过webbook，获取里面的第一张表
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            throw new RuntimeException("Excel的sheet名称有误");
        } else {
            int rowCount = sheet.getLastRowNum() + 1;
            if (rowCount > 10000) {
                throw new IllegalArgumentException("导入Excel数据不能超过10000条");
            }
            if (rowCount < 1) {
                throw new IllegalArgumentException("导入Excel数据不能为空");
            } else {
                Field[] fields = clazz.getDeclaredFields();
                outer:
                for (int rowIndex = 1; rowIndex < rowCount; ++rowIndex) {
                    //第三步：获取表格中的每一行（0代表表头，从1开始）
                    Row row = sheet.getRow(rowIndex);
                    if (isRowEmpty(row)) {
                        break;
                    }
                    Object data;
                    try {
                        data = clazz.newInstance();
                        //第四步：获取每一行的数据（这里指定只获取10个字段）（每一行都相当于是一个对象）
                        for (int i = 0; i < 3; ++i) {
                            Cell cell = row.getCell(i);
                            String cellValue = "";
                            //第五步：获取每一列的值，相当于对象的一个字段
                            if (null != cell) {
                                cellValue = getCellValue(cell);
                            }
                            if (StringUtils.isEmpty(cellValue)) {
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
                            }
                        }
                    } catch (Exception var21) {
                        throw new IllegalArgumentException(String.format("第%s行数据异常!%s", rowIndex + 1, var21.getMessage()));
                    }
                    //第六步：将数据写入到db对应的对象列表，然后将列表数据插入db
                    dataList.add(data);
                }
                return dataList;
            }
        }
    }


    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        cellValue = simpleDateFormat.format(date);
                    } else {
                        cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
                    }
                    break;
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case BLANK:
                    cellValue = "";
                    break;
                case ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
            }
        }

        return cellValue.trim();
    }

    /**
     * 判断行是否为空
     *
     * @param row
     * @return
     */
    public static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && StringUtils.isNotEmpty(getCellValue(cell))) {
                return false;
            }
        }
        return true;
    }

}



