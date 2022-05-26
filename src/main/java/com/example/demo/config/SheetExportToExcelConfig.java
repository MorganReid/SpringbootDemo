package com.example.demo.config;

import com.example.demo.domain.TblOpLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: hujun
 * @date: 2021/03/08  16:24
 */
@Service
public class SheetExportToExcelConfig {

    private String excelContentType = "application/vnd.ms-excel";
    private String excelSuffix = ".xlsx";

    public void exportExcel(Map<String, List<TblOpLog>> sheetMap, String fileName, HttpServletResponse response)
            throws IOException {
        XSSFWorkbook wb = null;
        // 第一步，创建一个webbook，对应一个Excel文件对象
        wb = new XSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet表
        for (Map.Entry entry : sheetMap.entrySet()) {
            String sheetName = (String) entry.getKey();
            List<TblOpLog> dataList = (List<TblOpLog>) entry.getValue();
            XSSFSheet sheet = wb.createSheet(sheetName);
            // 第三步，在sheet中添加表头第0行
            XSSFRow row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            XSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            List<String> headers = getHeaders(sheetName);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            //第五步，将数据写入到表中
            createExcelCell(sheet, dataList);
        }
        //第六部：将表中数据写入文件存储

        String filePath = System.getProperty("user.dir");
        String newFilePath = filePath + "/src/main/resources/static/";
        File file1 = new File(newFilePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(newFilePath + System.currentTimeMillis() + ".xlsx");
        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
//        response.setContentType(excelContentType);
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Disposition",
//                "attachment;filename=" + (URLEncoder.encode(fileName, "UTF8") + excelSuffix));
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        wb.write(out);
//        ByteArrayInputStream tempIn = new ByteArrayInputStream(out.toByteArray());
//        response.setHeader("Content-Length", String.valueOf(tempIn.available()));
//        ServletOutputStream outputStream = response.getOutputStream();
//        byte[] buffer = new byte[1024];
//        int a;
//        while ((a = tempIn.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, a);
//        }
//        out.close();
//        tempIn.close();
//        outputStream.flush();
//        outputStream.close();

    }

    private void createExcelCell(XSSFSheet sheet, List<TblOpLog> dataList) {
        int pos = 1;
        for (TblOpLog item : dataList) {
            XSSFRow row = sheet.createRow(pos++);
            int index = 0;
            row.createCell(index++).setCellValue(item.getId());
            row.createCell(index++).setCellValue(item.getAppName());
            row.createCell(index++).setCellValue(item.getActionType());
            row.createCell(index++).setCellValue(item.getSummary());
            row.createCell(index++).setCellValue(item.getContext());
        }

    }

    private List<String> getHeaders(String sheetName) {
        List<String> orderHeadList = new LinkedList<>();
        orderHeadList.add("ID");
        orderHeadList.add("应用名称");
        orderHeadList.add("操作类型");
        orderHeadList.add("摘要");
        orderHeadList.add("内容");
        return orderHeadList;
    }
}
