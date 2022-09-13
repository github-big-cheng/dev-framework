# framework

    基于springboot的底层框架，提供高效可用的相关工具类，以及统一配置，支持自定义配置的可插拔插件

## framework-core
    
    核心基础jar包，包含异常定义、工具类、插件基类、通用接口等

## framework-jdbc

    database（含关系型数据库、NoSql等）相关操作的工具类及插件封装

## framework-web

    基于spring-web提供的web相关插件及工具类jar包

## framework-converter

    消息处理转换器，提供输入检验、输出过滤/转换、Api接口说明文档的开发工具集

## framework-all

    本身并无意义，仅仅添加对framework-core、framework-jdbc、framework-web、framework-converter的依赖引用，方便项目引入


#版本

## 1.0.0-SNAPSHOT
  
    upgrade springboot version from 2.3.4.RELEASE to 2.6.5
  
    framework-core:
        ValidHelper:
            增加isNotNull方法
            增加compare方法，支持对象比较
            优化isBetween方法，支持对象比较
        DataHelper:
            支持数值对象（Number类型、数值字符串等）的加减乘除计算，当不为数字时转换为0
  
    framework-jdbc：
        redis：
            RedisPlugin:
                新增：集成基于lettuce操作工具，通过plugins.redis.client=lettuce开启
            MultiRedisPlugin:
                新增redis多数据源支持插件，通过plugins.multi-redis.enabled=true开启
        mybatis：
            升级pagehelper版本至：5.2.0
            升级配置以支持多数据源下pagehelper分页插件使用
        datasource：
            新增多数据源支持插件，通过plugins.multi-datasource.enabled=true开启
