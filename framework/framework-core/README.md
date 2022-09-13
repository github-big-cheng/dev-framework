# framework-core

    核心基础jar包
    
   ### api
    
    模块间交互接口定义
    
   + NetApi
   
     提供模块间交互的基本规范及定义，其中具体通讯方式由NetTools的具体实现来完成，支持http、socket、websocket等任意可扩展的通讯方式。
   
   + NetTools
   
     |**方法签名**|**返回值**|**描述**|
     |:---|:---|:---|
     |buildUrl(String interfaceName)|String|请求地址的组装|
     |doRequest(String url, Model request, TypeReference<T> typeReference)|T|请求|
   
   ### entity
   
   + Model
   
     数据处理类，继承LinkedHashMap,支持有序存储，数据类型转换，规则校验等。
   
   ### exception
   
    异常定义及封装，支持异常链，可扩展。
   
   + Exception
   
       |**类名**|**错误代码**|**描述**|
       |:---|:---:|:---|
       |BaseException| |基础异常类，所有框架异常的超类，请勿直接抛出|
       |SystemException|40|系统异常，底层组件、工具类未正确执行|
       |ValidationException|20|数据验证异常，请求数据格式不正确、非法等|
       |BusinessException|30|业务异常，程序执行时，检查到不符合业务约定、要求|
       |ThirdPartyException|10|第三方异常，当前服务请求其他服务（第三方或其他模块）时，未正确获取返回结果|
   
   + Assert
   
         断言相关方法接口，对相关校验的断言封装。断言某一条件必定满足,否则抛出异常,具体使用详见 AssertHelper
           
         + 示例：   
           - isEmpty 对象必定为空，支持int、对象、数组、集合等
           - isNotEmpty 对象必定**不**为空，支持int、对象、数组、集合等
           - isBlank 字符串必定为空
           - isNotBlank 字符串必定不为空
           - isEquals 对象必定相等
           - isNotEquals 对象必定不相等
           - ...
   
   ### helper
   
    相关工具类
    
   + AssertHelper 
   
            断言工具类，封装了多种指定异常。
        
        使用示例： 
        ```
        // 指定字符串为空时，抛出验证异常
        AssertHelper.EX_VALIDATION.isNotBlank(str, "common.parameter_required.str");
     
        // 指定集合不为NULL且不为空时，抛出业务异常
        AssertHelper.EX_BUSINESS.isEmpty(list, "business_check.invalid_data_found");
        ```
     
   + ConfigHelper 
        
         配置文件属性获取工具类
   
   + DataHelper 
   
         数据转换工具类
   
   + DateHelper 
   
         基于common.lang3.DateUtils的日期处理工具类，扩展支持了JDK8的LocalDateTime
   
   + JsonHelper
   
         json转换工具类
   
   + ProtoBufHelper
   
         基于protobuf的序列化工具类
   
   + RandomHelper
   
         随机数工具类，支持int、long、集合等场景
   
   + RegexHelper
   
         常用正则工具类
   
   + ResourceHelper
   
         资源文件工具类
   
   + SpringHelper
   
         spring容器工具类，对spring容器中Bean对象的取出、判断是否存在等
   
   + StringHelper
   
          基于common.lang3.StringUtils的字符串处理工具类，扩展了对 java.util.Stream 的支持

   
   + ValidHelper
   
          校验工具类，支持int，对象，集合等
   
   + XmlHelper
   
           xml处理工具类
   
   ### plugins
   
   * AbstractPlugin
   
         简单封装的插件基类
         
       |方法|返回值|描述|
       |:---|:---:|:---|
       |getName|String|抽象方法，返回插件名称|
       |printLog|Void|用于启用打印|
