package com.dqy.englishstudyapi.generator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class UserGenerator {
    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("ciku");
        tables.add("cikuexample");
        tables.add("prounce");
        tables.add("word");
        tables.add("testuser");
        tables.add("cikutype");
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/englishstudy?serverTimezone=Asia/Shanghai","root","")
                .globalConfig(builder -> {
                    builder.author("邓亲优")               //作者
                            .outputDir(System.getProperty("user.dir")+"\\src\\main\\java")    //输出路径(写到java目录)
                            .enableSwagger()           //开启swagger
                            .commentDate("yyyy-MM-dd");
//                            .fileOverride();            //开启覆盖之前生成的文件

                })
                .packageConfig(builder -> {
                    builder.parent("com.dqy")
                            .moduleName("englishstudyapi")
                            .entity("tablebean")
                            .service("service")
                            .serviceImpl("service.imp")
                            .controller("controller")
                            .mapper("mapper")
                            .xml("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml,System.getProperty("user.dir")+"\\src\\main\\resources\\mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
//                            .addTablePrefix("p_")    //过滤表名前缀，如p_test 生成的表名就为test
                            .serviceBuilder()
                            .formatServiceFileName("%sService")  //service类名 %s适配 根据表名替换
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()
                            .enableLombok()//开启lombok
                            .logicDeleteColumnName("deleted")//说明逻辑删除是哪个字段
                            .enableTableFieldAnnotation()//属性加上说明注解
                            .controllerBuilder()
                            // 映射路径使用连字符格式，而不是驼峰
//                            .enableHyphenStyle()
                            .formatFileName("%sController")
                            .enableRestStyle()//启用restController
                            .mapperBuilder()
                            //生成通用的resultMap
                            .enableBaseResultMap()  
                            .superClass(BaseMapper.class)//继承哪个父类
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()//@mapper 注解
                            .formatXmlFileName("%sMapper");// xml 名
                })
                .templateConfig(new Consumer<TemplateConfig.Builder>() {
                    @Override
                    public void accept(TemplateConfig.Builder builder) {
                        // 使用我们自定义模板
                        builder.controller("templates/mycontroller.java");
                        builder.entity("templates/myentity.java");
                    }
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}