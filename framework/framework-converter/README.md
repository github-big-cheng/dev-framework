# framework-core

  基于springboot框架的消息转换器，提供参数的校验，过滤、转换及Api在线调试。

## 打开方式
 
   maven引入
   ```
    <dependency>
        <groupId>com.dounion</groupId>
        <artifactId>framework-converter</artifactId>
        <version>${framework.version}</version>
    </dependency>
   ```

## 启用
   ```properties
   plugins.converter.enable=true
   ```
   或者
   ```yaml
    plugins:
      converter:
        enable: true
   ```

## @Converter

   * path
   
     String，报文路径。`ConverterProperties.defineRootPath` + `File.separator` + `@Converter.path` 为报文定义路径
   
   * request
   
     boolean，是否开启输入校验
   
   * response

     boolean，是否开启输出过滤
     

## ConvertHandler

   `@Converter`注解的处理类。默认实现为`DefaultConverterHandler.java`， 基于around方式的Spring AOP实现。支持自定义实现了覆盖实现方式。
     

### validation

   提供参数校验

### filter

   返回参数过滤及转换

### api

   提供在线api文档

## 优缺点分析

   万事万物总是有两面性...

### 优点

1. 代码仅关心业务部分，基础的数据校验由外部完成
2. 代码与输入、输出分离，支持配置外置，动态刷新
3. 提供在线api文档，方便调试
4. 可扩展性良好，支持外部扩展实现
5. 国际化支持
6. 支持yml、xml格式报文配置定义，同时支持外部扩展

### 缺点

1. 培养了程序员的惰性，不能严格执行条件判断,不是对配置很熟悉,可能导致重复校验或漏校验
2. 有重复造轮子的嫌疑..
3. 暂不支持接口文档导出
4. 目前默认支持的功能较少
5. 暂不支持个性化的错误提示
6. 默认支持的报文配置较少，且配置繁琐
7. 基于framework-core、springboot,依赖度较高,不是一个合格的开源框架
