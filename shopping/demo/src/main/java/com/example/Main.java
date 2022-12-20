//package com.example;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//public class Main {
//    public static void main(String[] args) {
//        //创建对象
//        AutoGenerator autoGenerator = new AutoGenerator();
//        //数据源
//        DataSourceConfig dataSourceConfig = new DataSourceConfig();
//        dataSourceConfig.setDbType(DbType.MYSQL);
//        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
//        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/dormitory");
//        dataSourceConfig.setUsername("root");
//        dataSourceConfig.setPassword("sfzhm130928");
//        autoGenerator.setDataSource(dataSourceConfig);
//        //全局配置
//        GlobalConfig globalConfig = new GlobalConfig();
//        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
////      代码生成注释作者
//        globalConfig.setAuthor("admin");
////      不需要打开文件即可,直接在idea里面开发
//        globalConfig.setOpen(false);
//        //去掉Service的I
//        globalConfig.setServiceName("%sService");
//        autoGenerator.setGlobalConfig(globalConfig);
//        //包配置
//        PackageConfig packageConfig = new PackageConfig();
//        packageConfig.setParent("com.example");
//        packageConfig.setEntity("entity");
//        packageConfig.setMapper("mapper");
//        packageConfig.setService("service");
//        packageConfig.setServiceImpl("service.impl");
//        packageConfig.setController("controller");
//        autoGenerator.setPackageInfo(packageConfig);
//        //策略配置
//        StrategyConfig strategyConfig = new StrategyConfig();
//        strategyConfig.setInclude("dormitory_admin");
//        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
//        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategyConfig.setEntityLombokModel(true);
//        autoGenerator.setStrategy(strategyConfig);
//        //启动
//        autoGenerator.execute();
//
//    }
//}