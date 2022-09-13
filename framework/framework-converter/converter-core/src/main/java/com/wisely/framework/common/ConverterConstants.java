package com.wisely.framework.common;

public interface ConverterConstants {

    // ===================== 数据类型 ========================
    String DATA_TYPE_STRING = "String"; // 字符串，默认类型
    String DATA_TYPE_BOOLEAN = "Boolean"; // 布尔类型
    String DATA_TYPE_INTEGER = "Integer"; // 整型
    String DATA_TYPE_LONG = "Long"; // 长整型
    String DATA_TYPE_DECIMAL = "Decimal"; // 精整型
    String DATA_TYPE_DECIMALSTRING = "DecimalString"; // 数值字符串
    String DATA_TYPE_DATESTRING = "DateString"; // 日期字符串
    String DATA_TYPE_LIST = "List"; // 列表
    String DATA_TYPE_JSONSTRING = "JsonString"; // JsonString
    String DATA_TYPE_MAP = "Map"; // Map
    String DATA_TYPE_TREE = "Tree"; // 递归
    String DATA_TYPE_FILE = "File"; // 单附件
    String DATA_TYPE_FILELIST = "FileList"; // 附件列表


    // ===================== 报文定义相关 ========================
    String XML_ROOT = "trans"; // 根节点
    String XML_SEND = "send"; // 发送节点
    String XML_RECV = "rcv"; // 接受节点

    String XML_ITEM = "item"; // 报文单节点
    String XML_ITEM_LIST = "item-list"; // 报文循环体节点
    String XML_ITEM_MAP = "item-map"; // MAP结构节点
    String XML_ITEM_TREE = "item-tree"; // 报文树结构节点
    String XML_ITEM_JSON = "item-json"; // 报文树结构节点
    // ===================== 属性相关相关 ========================
    String ATTR_INDEX = "index"; // index
    String ATTR_NAME = "name"; // 字段定义
    String ATTR_TYPE = "type"; // 字段类型
    String ATTR_DESC = "desc"; // 描述
    String ATTR_NOTES = "notes"; // 备注
    String ATTR_PARAM_TYPE = "paramType"; // 参数类型:path、query、header等 默认query

    // ===================== 数据结构操作 ========================
    String ATTR_TREE_KEY = "treeKey"; // 树结构字段（与item-tree搭配使用）
    String ATTR_IGNORE_KEY = "ignore-key"; // 直接返回、忽略当前key（与item-map、item-list搭配使用）
    String ATTR_PUT_ALL = "put-all"; // 直接放入所有数据，忽略处理（与item-map、item-list搭配使用）
    String ATTR_RANDOM_KEY = "random-key"; // 不确定的key（与item-map搭配使用）
    String ATTR_REAL_TYPE = "real-type"; // 实际类型（与item-tree搭配使用）

    // ===================== converter相关 =====================
    String CONVERTER_ATTR_DEF = "default"; // 默认值
    String CONVERTER_ATTR_TARGET = "target"; // 字段取值映射
    String CONVERTER_ATTR_SCALE = "scale"; // 精度,与Decimal一起出现
    String CONVERTER_ATTR_ZERO_FORMAT = "zero-format"; // 千分位，与DecimalString一起出现
    String CONVERTER_ATTR_THOUSANDTH = "thousandth"; // 千分位，与DecimalString一起出现
    String CONVERTER_ATTR_MAPPER = "mapper"; // 需转换值对应map中的key
    String CONVERTER_ATTR_SEPARATOR = "separator";//切割符，与Mapper一起出现
    String CONVERTER_ATTR_FROM_PATTERN = "fromPattern"; // 源数据格式 与DataEnum.DateString搭配使用
    String CONVERTER_ATTR_TO_PATTERN = "toPattern"; // 目标格式 与 DataEnum.DecimalString / DataEnumDateString搭配使用
    String CONVERTER_ATTR_L_SUB = "lsub"; // 从左侧截取指定长度
    String CONVERTER_ATTR_R_SUB = "rsub"; // 从右侧截取指定长度
    String CONVERTER_ATTR_LPAD = "lpad"; // 左补字符串
    String CONVERTER_ATTR_RPAD = "rpad"; // 右补字符串
    String CONVERTER_ATTR_PAD_LEN = "padLen"; // 左/右补字符串的长度

    // ===================== validation相关 ====================
    String VALIDA_ATTR_REQUIRED = "required"; // 是否必输
    String VALIDA_ATTR_LENGTH = "maxlength"; // 最大长度
    String VALIDA_ATTR_REGULAR = "regular"; // 正则
    String VALIDA_ATTR_REGULAR_TYPE = "regular-type"; // 正则
    String VALIDA_ATTR_MIN = "min"; // 最大值
    String VALIDA_ATTR_MAX = "max"; // 最小值
    String VALIDA_ATTR_BETWEEN = "between"; // 区间 包含首尾，支持多区间 A,C|C,D
    String VALIDA_ATTR_SCOPE = "scope"; // 范围 列表 A,B,C,D
    String VALIDA_ATTR_FROM_PATTERN = "fromPattern"; // 限定的日期格式

}
