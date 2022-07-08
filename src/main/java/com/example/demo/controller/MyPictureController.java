package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: hujun
 * @date: 2022/07/08  16:03
 */
public class MyPictureController {


    public static void main(String[] args) throws Exception {
        URL url = new URL("https://s1.ax1x.com/2022/07/08/jBdSaQ.jpg");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10 * 1000);
        InputStream inputStream = connection.getInputStream();
        byte[] data = readInputStream(inputStream);
        //默认保存在当前工程根目录
        File file = new File("copy.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();


    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        ;
        return outputStream.toByteArray();

    }
}
