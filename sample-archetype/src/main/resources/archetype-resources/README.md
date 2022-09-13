# Sample

    样例模块，请按需拷贝。
   
### client

    交互模块，包含不同模块间交互需使用的的常量、枚举类、接口申明、rpc方法等。

   * api rpc等方法实现
   * client 常量类、枚举类、工具类、vo等

### entity

    数据操作模块，包含实体类、dao层及sqlmap等数据库操作相关文件。
    
   * entity 实体类
   
         entity中仅维护数据有关字段，如：数据库字段、查询条件（like、le、lt、in等）。不得出现与返回结果有关字段，如：List<Attachment>等。
         
        + 查询语句字段："字段名称" + Query + "查询方式"
         
        |查询方式|描述|变量命名示例|sql示例|
        |----       |----      |----               |----        |
        |ne         |不等于     |typeQueryNe        |type <> #{typeQueryNe}|
        |gt         |大于       |ageQueryGt        |type > #{ageQueryGt}|
        |ge         |大于等于    |ageQueryGe        |type >= #{ageQueryGe}|
        |lt         |小于       |ageQueryLt        |type < #{ageQueryLt}|
        |le         |小于等于    |ageQueryLe        |type <= #{ageQueryLe}|
        |in         |多条件查询  |idQueryIn          |id in(1,2,3)|
        |notIn      |多条件查询  |idQueryNotIn       |id not in(1,2,3)|
        |like       |模糊查询    |nameQueryLike      |name like concat('%', #{nameQueryLike}, '%')|
        |notLike    |模糊查询    |nameQueryNotLike   |name not like concat('%', #{nameQueryNotLike}, '%')|
        |likeLeft   |左模糊查询  |nameQueryLikeLeft  |name like concat('%', #{nameQueryLikeLeft})|
        |likeRight  |右模糊查询  |nameQueryRightLike |name like concat(#{nameQueryRightLike}, '%')|
        |isNull     |为NULL     |statusQueryIsNull  |status is null|
        |isNotNull  |不为NULL   |statusQueryIsNotNull  |status is not null|
          
        + 更新语句有关："字段命名" + Update
        
        字段命名不要求与字段相同，但需满足“见文知意”的要求
         
   * mapper dao层
   * resources/mapper sqlmap文件，有数据库类型区分的，可添加级别，如：
   ```
    + resources
      + mapper
        + mysql
          - UcenterUserMapper.xml
        + oracle
          - UcenterUserMapper.xml
        + dm
          - UcenterUserMapper.xml
   ```

### web

    应用模块，包含service、controller等文件。
    
   * controller 实体类
   * service dao层
   * common 配置类、工具类、常量类、枚举等 
   * ~~vo~~ 应用层接收额外参数、请求参数校验等，通常为entity的扩展，如：attachments为附件json格式字符串，需应用层转换后调用dao层方法插入数据库。不推荐，建议`RequestHelper.getInput()`方式获取，数据校验由Converter实现。
