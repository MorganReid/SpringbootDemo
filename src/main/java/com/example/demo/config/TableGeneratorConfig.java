package com.example.demo.config;


/**
 * @author: hujun
 * @date: 2021/03/08  14:13
 */

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class TableGeneratorConfig {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.example.demo");
        packageConfig.setMapper("dao.mapper");
        packageConfig.setEntity("domain");
        packageConfig.setXml("dao");
        mpg.setPackageInfo(packageConfig);
        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setAuthor("hujun");
        //生成文件的路径，根据自己电脑路径更改
        gc.setOutputDir("/Users/jian/IdeaProjects/SpringBootDemo/src/main/java");

        // 是否覆盖同名文件，默认是false
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columnList
        gc.setBaseColumnList(false);
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert());
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("hkmy");
        dsc.setPassword("5JwVKUqkNoTjBYQF_E43gbjT4");
        dsc.setUrl("jdbc:mysql://10.228.5.14:3306/hkshow?useUnicode=true&characterEncoding=utf8&useSSL=false");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);

        // 需要生成的表
        strategy.setInclude("tbl_op_log");

        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}

