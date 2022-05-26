package com.example.demo.controller;


import com.example.demo.domain.Student;
import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


/**
 * @author: hujun
 * @date: 2021/03/06  12:40
 */

@RestController
public class MyMailController {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    /**
     * 简单邮件
     *
     * @return
     */
    @RequestMapping("/mail")
    public String sendMail() throws Exception {

        SimpleMailMessage message = new SimpleMailMessage();
        //邮件设置
        message.setSubject("邮件主题");
        message.setText("邮件内容");
        message.setTo("shshj0321@163.com");
        message.setFrom("shshj0321@163.com");
        javaMailSender.send(message);
        return "简单邮件发送成功！";

    }

    /**
     * 带有附件的邮件
     *
     * @return
     * @throws MessagingException
     */
    @RequestMapping("/mineMail")
    public String sendMineMail() throws MessagingException {
        //1、创建一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        //邮件主题
        helper.setSubject("这是一个邮件啊");
        //文本中添加图片
        // helper.addInline("image1", new FileSystemResource("/jian/Downloads/Pictures/wubai.jpg"));
        //邮件内容
        helper.setText("邮件正文" +
                " <a href=\"https://wiki.maoyan.com/pages/viewpage.action?pageId=136978882\">这是一个链接</a>", true);
        helper.setTo("shshj0321@163.com");
        helper.setFrom("shshj0321@163.com");
        //附件添加图片
        helper.addAttachment("1.jpg", new File("/users/jian/Downloads/Pictures/wubai.jpg"));
        //附件添加word文档
        helper.addAttachment("哈哈哈.docx", new File("/users/jian/Downloads/通用激活方法.docx"));

        javaMailSender.send(mimeMessage);
        return "复杂邮件发送！";
    }

    /**
     * thymeleaf模板作为邮件发送
     *
     * @return
     * @throws MessagingException
     */
    @RequestMapping("/thyMail")
    public String sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject("这是一个thymeleaf模板邮件");
        //邮件内容
        messageHelper.setTo("shshj0321@163.com");
        messageHelper.setFrom("shshj0321@163.com");
        Context context = new Context();
        context.setVariable("name", "这是一个新建的thymeleaf模板");
        context.setVariable("link", "https://www.cnblogs.com/swzx-1213/");
        context.setVariable("String", "12345678");
        // context.setVariable("image1", "/jian/Downloads/Pictures/wubai.jpg");
        TemplateEngine templateEngine = new TemplateEngine();
        String value = templateEngine.process("email.html", context);
        messageHelper.setText(value, true);
        javaMailSender.send(mimeMessage);
        return "模板邮件发送成功";
    }

    @Test
    public void createCompanyXml() throws Exception {
        Document doc = DocumentHelper.createDocument();
        String date = new SimpleDateFormat("yyyymmdd").format(new Date());

        Element batch = doc.addElement("batch");
        Element send_date = batch.addElement("SEND_DATE");
        send_date.setText(date);

        Student student1 = new Student("name1", 11L, 1);
        Student student2 = new Student("name2", 22L, 2);
        Student student3 = new Student("name3", 33L, 3);
        ArrayList<Student> students = Lists.newArrayList(student1, student2, student3);

        Element events = batch.addElement("STUDENTS");
        Element total = events.addElement("total");
        total.setText(String.valueOf(students.size()));
        handleEvents(events, students);

        String s = doc.asXML();
        System.out.println(s);

        //写入xml文档
        OutputFormat format = OutputFormat.createPrettyPrint();//写入xml文档
        format.setEncoding("UTF-8");// 设置编码格式
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File("/users/jian/Downloads/company.xml")), format);
        xmlWriter.write(doc);
        xmlWriter.close();

    }

    private void handleEvents(Element events, ArrayList<Student> students) {
        for (int i = 0; i < students.size(); i++) {
            Element student = events.addElement("Student");
            handleStudent(student, students.get(i));
        }
    }

    private void handleStudent(Element student, Student data) {
        Element name = student.addElement("name");
        Element age = student.addElement("age");
        Element id = student.addElement("id");
        name.setText(data.getName());
        age.setText(data.getAge().toString());
        id.setText(data.getId().toString());
    }

    private byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Test
    public void parserXml() throws MalformedURLException {
        String  fileName = "/users/jian/Downloads/company.xml";
        File inputXml = new File(fileName);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(inputXml);
            Element users = document.getRootElement();
            for (Iterator i = users.elementIterator(); i.hasNext(); ) {
                Element user = (Element) i.next();
                for (Iterator j = user.elementIterator(); j.hasNext(); ) {
                    Element node = (Element) j.next();
                    System.out.println(node.getName() + ":" + node.getText());
                    Iterator k = node.elementIterator();
                    boolean b = k.hasNext();
                }
                System.out.println();
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
    }

}