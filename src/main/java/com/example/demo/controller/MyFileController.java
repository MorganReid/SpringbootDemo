package com.example.demo.controller;

import com.example.demo.config.ExcelLoadToSheetConfig;
import com.example.demo.config.SheetExportToExcelConfig;
import com.example.demo.dao.mapper.TblOpLogMapper;
import com.example.demo.domain.SetUpTemplateTicketType;
import com.example.demo.domain.TblOpLog;
import com.example.demo.exception.ErrorEnum;
import com.example.demo.exception.MyException;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: hujun
 * @date: 2021/03/05  17:56
 */
@RestController
@RequestMapping("/file")
public class MyFileController {


    @Autowired
    private TblOpLogMapper tblOpLogMapper;

    @Autowired
    private SheetExportToExcelConfig sheetExportToExcelConfig;

    /**
     * 文件上传，并指定目录存储
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "上传的文件不能为空！请重新上传";
        }
        if (file.getSize() <= 0) {
            return "上传的文件大小需要大于0kb";
        }
        //image/png
        System.out.println(file.getContentType());

        //获取文件原始的名称，并设置新文件名
        String originFileName = file.getOriginalFilename();
        String newFileName = System.currentTimeMillis() + originFileName;

        //存储的目录
        String filePath = System.getProperty("user.dir");
        String newFilePath = filePath + "/src/main/resources/static/images/";
        File file1 = new File(newFilePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        //将文件存储到指定目录的指定文件中
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(newFilePath + newFileName);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            //根据返回的地址，可以访问到已经存储的文件（需要在MyInterceptorConfig指定静态资源路径）
            return "localhost:9090/images/" + newFileName;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    /**
     * 将excel的数据导入，存储到DB的表中
     */


    @PostMapping("/excelLoadToSheet")
    public void loadToSheet(MultipartFile file) {
        if (file.isEmpty() || file.getSize() <= 0) {
            throw new MyException(ErrorEnum.NOT_FOUND.getErrorCode(), "上传的文件不能为空！请重新上传");
        }
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        try {
            ArrayList<TblOpLog> dataList = ExcelLoadToSheetConfig.getDataList(TblOpLog.class, file.getInputStream(), prefix);
            for (TblOpLog it : dataList) {
                System.out.println(it.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从DB的表中查询数据，导出为excel
     */
    @GetMapping("/sheetExportToExcel")
    public void exportToExcel(HttpServletResponse response) {
        String natureName1 = "ticket_type.xls";
        testMulSelect(response, natureName1);
//        QueryWrapper<TblOpLog> condition = new QueryWrapper();
//        condition.gt("id", 281452);
//        List<TblOpLog> tblOpLogList = tblOpLogMapper.selectList(condition);
//        Map<String, List<TblOpLog>> map = new HashMap<>();
//        map.put("日志列表哟", tblOpLogList);
//        sheetExportToExcelConfig.exportExcel(map, "日志明细表", response);

    }


    @Test
    public void testMulSelect(HttpServletResponse response, String natureCode) {
        //下拉框数据源列表
        List<SetUpTemplateTicketType> list = new ArrayList<>();
        list.add(new SetUpTemplateTicketType("AA", "1,2,3"));
        list.add(new SetUpTemplateTicketType("BB", "44,55,66"));
        list.add(new SetUpTemplateTicketType("CC", "77,88,99"));

        List<String> nameList = list.stream().map(SetUpTemplateTicketType::getTicketTypeCode).collect(Collectors.toList());
        String[] nameArr = nameList.toArray(new String[nameList.size()]);

        List<String> idList = list.stream().map(SetUpTemplateTicketType::getAscUserIds).collect(Collectors.toList());
        String[] idArr = idList.toArray(new String[nameList.size()]);
        //key：下拉框所在列号，value：对应列的下拉框数据源
        Map<Integer, String[]> map = new HashMap<>();
        map.put(0, nameArr);
        map.put(1, idArr);

        blankExcelExport(response, map, SetUpTemplateTicketType.class, natureCode);


    }


    private String excelContentType = "application/vnd.ms-excel";
    private String excelSuffix = ".xls";
    String utf = "UTF-8";

    /**
     * @param response
     * @param map
     * @param clazz
     * @param fileName
     * @param <T>
     * @throws IOException
     */
    public <T> void blankExcelExport(HttpServletResponse response, Map<Integer, String[]> map, Class<T> clazz, String fileName) {
        OutputStream os = null;
        setResponseHeader(response, fileName);
        try {
            os = response.getOutputStream();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            //创建一个sheet
            HSSFSheet sheet = hssfWorkbook.createSheet("sheet111");
            HSSFRow row = sheet.createRow(0);
            Field[] declaredFields = clazz.getDeclaredFields();
            //创建标题行
            for (int index = 0; index < declaredFields.length; index++) {
                row.createCell(index).setCellValue(declaredFields[index].getName());
            }
            //创建下拉列表
            for (Map.Entry entry : map.entrySet()) {
                int key = (int) entry.getKey();
                String[] selectArr = (String[]) entry.getValue();
                HSSFDataValidation dataValidation = createBox(hssfWorkbook, selectArr, 9999, key);
                if (dataValidation != null) {
                    sheet.addValidationData(dataValidation);
                }
            }
            //创建好的excel写入流
            hssfWorkbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setResponse(HttpServletResponse response, HSSFWorkbook hssfWorkbook, String fileName) throws IOException {
        response.setContentType(excelContentType);
        response.setCharacterEncoding(utf);
        response.setHeader("Content-Disposition",
                "attachment;filename=" + (URLEncoder.encode(fileName, "UTF8") + excelSuffix));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        hssfWorkbook.write(out);
        ByteArrayInputStream tempIn = new ByteArrayInputStream(out.toByteArray());
        response.setHeader("Content-Length", String.valueOf(tempIn.available()));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int a;
        while ((a = tempIn.read(buffer)) != -1) {
            outputStream.write(buffer, 0, a);
        }
        out.close();
        tempIn.close();
        outputStream.flush();
        outputStream.close();
    }

    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HSSFDataValidation createBox(HSSFWorkbook wb, String[] selectArray, int rows, int selectKey) {
        HSSFDataValidation dataValidation = null;
        //新建隐藏状态的sheet，用来存储码值。
        //创建sheet页
        HSSFSheet sheet = wb.createSheet("hidden" + selectKey);
        //向创建的sheet页添加码值数据。
        for (int i1 = 0; i1 < selectArray.length; i1++) {
            HSSFRow row = sheet.createRow(i1);
            HSSFCell cell = row.createCell((int) 0);
            cell.setCellValue(selectArray[i1]);
        }
        //将码值sheet页做成excel公式
        Name namedCell = wb.createName();
        namedCell.setNameName("hidden" + selectKey);
        namedCell.setRefersToFormula("hidden" + selectKey + "!$A$1:$A$" + selectArray.length);
        //确定要在哪些单元格生成下拉框
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint("hidden" + selectKey);
        CellRangeAddressList regions = new CellRangeAddressList(1, rows, selectKey, selectKey);
        dataValidation = new HSSFDataValidation(regions, dvConstraint);
        //隐藏码值sheet页
        int sheetNum = wb.getNumberOfSheets();
        for (int n = 1; n < sheetNum; n++) {
            wb.setSheetHidden(n, true);
        }
        return dataValidation;
    }


}