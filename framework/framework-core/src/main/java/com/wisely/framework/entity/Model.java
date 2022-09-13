package com.wisely.framework.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.helper.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Model
 * 自定义model
 * 提供链式调用方法
 * 提供常用数据类型的转换获取
 * 提供数据校验
 */
public class Model<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -8501314022551529537L;

    protected String modelName;

    public Model() {
        // 为方便rpc等框架传输，提供默认无参构造方法，不建议直接new
        super();
    }

    private Model(String modelName) {
        this.modelName = modelName;
    }

    private Model(Map model) {
        super(model);
    }

    private Model(Model model) {
        super(model);
    }

    private Model(String moduleName, Model model) {
        this.modelName = moduleName;
        super.putAll(model);
    }

    public int hashCode() {
        if (isNotEmpty("id")) {
            return getString("id").hashCode();
        }

        AtomicInteger result = new AtomicInteger(0);
        entrySet().forEach(entry -> {
            if (!(null == entry.getKey() ||
                    null == entry.getValue() ||
                    entry.getValue() instanceof Model ||
                    entry.getValue() instanceof Collection)) {
                result.set(31 * result.get() + entry.getValue().hashCode());
            }
        });
        return result.get() == 0 ? super.hashCode() : result.get();
    }

    public String toString() {
        return JsonHelper.obj2JsonNode(this).toPrettyString();
    }


    public String getModelName() {
        return modelName;
    }

    public Model setModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }


    /**
     * 构造器
     *
     * @return
     */
    public static Model builder() {
        return new Model();
    }

    /**
     * 构造器
     *
     * @return
     */
    public static Model builder(String modelName) {
        return new Model(modelName);
    }

    /**
     * 构造器
     *
     * @param model
     * @return
     */
    public static Model builder(Map model) {
        return new Model(model);
    }

    /**
     * 构造器
     *
     * @param model
     * @return
     */
    public static Model builder(Model model) {
        return new Model(model);
    }

    /**
     * 构造器
     *
     * @param moduleName
     * @param model
     * @return
     */
    public static Model builder(String moduleName, Model model) {
        return new Model(moduleName, model);
    }

    /**
     * 获取最后一次 set 的键(Key)
     *
     * @return
     */
    public Object getLastKey() {
        Iterator iterator = this.keySet().iterator();
        Object key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
        }
        return key;
    }


    public Model set(Object key, Object value) {
        super.put((K) key, (V) value);
        return this;
    }


    public Model addAll(Map map) {
        this.putAll(map);
        return this;
    }

    public void putAll(Map map) {
        if (ValidHelper.isNotEmpty(map)) {
            super.putAll(map);
        }
    }


    /**
     * 指定key的键值对放入model
     *
     * @param map
     * @param fields
     */
    public void putByPickUp(Map map, Object... fields) {
        if (ValidHelper.isEmpty(fields)) {
            return;
        }

        Arrays.stream(fields).forEach(field -> this.set(field, map.get(field)));
    }

    /**
     * 除指定KEY外放入model
     *
     * @param map
     * @param fields
     */
    public void putByExcluded(Map map, Object... fields) {

        Set set = Sets.newHashSet(fields);
        map.keySet().forEach(key -> {
            if (set.contains(key)) {
                return;
            }
            this.set(key, map.get(key));
        });
    }


    /**
     * 清理特定关键字
     *
     * @param keyword
     * @param fields
     */
    public void clearKeyword(String keyword, String... fields) {
        if (ValidHelper.isEmpty(fields)) {
            return;
        }

        Arrays.stream(fields).forEach(field -> {
            String value = getString(field, "");
            this.set(field, value.replaceAll(keyword, ""));
        });
    }

    /**
     * 移除多个指定key
     *
     * @param keys
     */
    public void remove(Object... keys) {
        if (ValidHelper.isEmpty(keys)) {
            return;
        }

        Arrays.stream(keys).forEach(key -> this.remove(key));
    }

    /**
     * 按值移除
     *
     * @param key
     * @param value
     * @return
     */
    public Model removeEquals(Object key, String value) {
        if (equals(key, value)) {
            remove(key);
        }
        return this;
    }


    /* 对象转换 */


    /**
     * 将实体对象转为model
     *
     * @param object
     * @return
     */
    public static Model parseObject(Object object) {
        if (ValidHelper.isEmpty(object)) {
            return builder();
        }

        // String
        if (object instanceof String) {
            try {
                return JsonHelper.json2Obj((String) object, Model.class);
            } catch (Exception e) {
                // 非json格式字符串, 以null为key, 直接返回
                return builder().set(null, object);
            }
        }

        // Map
        if (object instanceof Map) {
            return builder((Map) object);
        }

        // Collection
        if (object instanceof Collection) {
            return builder().set(null, object);
        }

        // 先转换为json字符串再转为model
        return parseObject(JsonHelper.obj2Json(object));
    }


    /**
     * model转bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T convertTo(Class<T> clazz) {
        return JsonHelper.cast(this, clazz);
    }


    /* 数据结构处理 */

    /**
     * use Model instead of it
     *
     * @param key
     * @return
     */
    @Deprecated
    public Map getMap(Object key) {
        Object value = get(key);
        if (ValidHelper.isNull(value)) {
            return null;
        }
        return value instanceof Map ? (Map) value : null;
    }


    public Model getModel(Object key) {
        Object value = get(key);
        if (null == value) {
            return null;
        }
        if (value instanceof Model) {
            return (Model) value;
        }
        if (value instanceof Map) {
            Model model = Model.builder((Map) value);
            set(key, model);
            return model;
        }

        return Model.parseObject(value);
    }

    public Model getModel(Object key, boolean create) {
        Model model = getModel(key);
        if (null != model) {
            return model;
        }
        if (create) {
            set(key, model = new Model());
        }
        return model;
    }


    public Set getSet(Object key) {
        Object value = get(key);
        if (ValidHelper.isNull(value)) {
            return null;
        }
        if (value instanceof Set) {
            return ((Set) value);
        }
        return null;
    }

    public Set getSet(Object key, boolean create) {
        Set set = getSet(key);
        if (ValidHelper.isEmpty(set) && create) {
            this.set(key, set = Sets.newHashSet());
        }
        return set;
    }

    public LinkedHashSet getLinkedSet(Object key, boolean create) {
        LinkedHashSet set = (LinkedHashSet) getSet(key);
        if (ValidHelper.isEmpty(set) && create) {
            this.set(key, set = Sets.newLinkedHashSet());
        }
        return set;
    }

    public <T> List<T> getList(Object key) {
        Object value = get(key);
        return DataHelper.getList(value);
    }

    /**
     * 获取不到 list 就设置默认结果
     *
     * @param key
     * @param create
     * @return
     */
    public List getList(Object key, boolean create) {
        List result = getList(key);
        if (null == result && create) {
            set(key, result = Lists.newArrayList());
        }
        return result;
    }

    /**
     * 支持强制类型转换
     *
     * @param key
     * @param reference
     * @param <T>
     * @return
     */
    public <T> List<T> getCastList(Object key, TypeReference<List<T>> reference) {
        Object value = get(key);
        if (ValidHelper.isNull(value)) {
            return null;
        }

        List<T> values;
        if (value instanceof List) {
            values = (List<T>) value;
        } else if (value instanceof String) {
            return JsonHelper.json2Obj((String) value, reference);
        } else {
            values = Lists.newArrayList();
            values.add((T) value);
        }

        return JsonHelper.json2Obj(JsonHelper.obj2Json(values), reference);
    }


    public List<Model> getModelList(Object key) {
        return DataHelper.getModelList(get(key));
    }


    /* 数据转换处理 */

    public Integer getInt(Object key) {
        Object value = get(key);
        return DataHelper.getInt(value);
    }

    public Integer getInt(Object key, Integer defaultValue) {
        Object value = get(key);
        return DataHelper.getInt(value, defaultValue);
    }

    /**
     * 自增并获取
     *
     * @param key
     * @return
     */
    public synchronized Integer incrementAndGet(Object key) {
        return this.addAndGet(key, 1);
    }

    /**
     * 相加并获取
     *
     * @param key
     * @return
     */
    public synchronized Integer addAndGet(Object key, Integer delta) {
        Integer value = getInt(key) + DataHelper.getInt(delta);
        this.set(key, value);
        return value;
    }

    public Long getLong(Object key) {
        Object value = get(key);
        return DataHelper.getLong(value);
    }

    public Long getLong(Object key, Long defaultValue) {
        Object value = get(key);
        return DataHelper.getLong(value, defaultValue);
    }

    public boolean getBoolean(Object key) {
        Object value = get(key);
        return DataHelper.getBoolean(value);
    }


    public String getString(Object key) {
        Object value = get(key);
        return DataHelper.getString(value);
    }

    /**
     * key为空时返回 defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(Object key, String defaultValue) {
        Object value = get(key);
        return DataHelper.getString(value, defaultValue);
    }

    public String getStringByTrim(Object key) {
        String value = getString(key, "");
        return value.trim();
    }

    public BigDecimal getBigDecimal(Object key) {
        Object value = get(key);
        return DataHelper.getBigDecimal(value);
    }

    /**
     * 相加并获取
     *
     * @param key
     * @return
     */
    public synchronized BigDecimal addAndGet(Object key, BigDecimal delta) {
        BigDecimal value = DataHelper.add(getBigDecimal(key), delta);
        this.set(key, value);
        return value;
    }

    /**
     * use java.time.LocalDateTime instead of java.util.Date
     *
     * @param key
     * @return
     */
    @Deprecated
    public Date getDate(Object key) {
        Object value = get(key);
        return DateHelper.getDate(value);
    }

    public LocalDateTime getLocalDateTime(Object key) {
        Object value = get(key);
        return DateHelper.getLocalDateTime(value);
    }

    /**
     * 获取最大 BigDecimal
     *
     * @param key
     * @param value
     * @return
     */
    public BigDecimal getMax(Object key, BigDecimal value) {
        return getBigDecimal(key).max(value);
    }

    /**
     * 获取最小 BigDecimal
     *
     * @param key
     * @param value
     * @return
     */
    public BigDecimal getMin(Object key, BigDecimal value) {
        return getBigDecimal(key).min(value);
    }

    /**
     * 获取最大 long
     *
     * @param key
     * @param value
     * @return
     */
    public int getMax(Object key, int value) {
        return Math.max(getInt(key), value);
    }

    /**
     * 获取最小 long
     *
     * @param key
     * @param value
     * @return
     */
    public long getMin(Object key, long value) {
        return Math.min(getLong(key), value);
    }


    /**
     * 获取最大 int
     *
     * @param key
     * @param value
     * @return
     */
    public long getMax(Object key, long value) {
        return Math.max(getLong(key), value);
    }

    /**
     * 获取最小 int
     *
     * @param key
     * @param value
     * @return
     */
    public int getMin(Object key, int value) {
        return Math.min(getInt(key), value);
    }


    /**
     * 获取拆分集合
     *
     * @param key
     * @param splitFlag
     * @return
     */
    public List<String> getSpitList(Object key, String splitFlag) {
        String value = getString(key);
        if (ValidHelper.isNotEmpty(value)) {
            return StringHelper.splitToList(value, splitFlag);
        }
        return Lists.newArrayList();
    }


    /* 数据检查 */

    public boolean isEmpty(String field) {
        return ValidHelper.isEmpty(get(field));
    }

    public boolean isNotEmpty(String field) {
        return !isEmpty(field);
    }

    public boolean isBlank(String s) {
        return ValidHelper.isBlank(this.getString(s));
    }

    public boolean isNotBlank(String s) {
        return ValidHelper.isNotBlank(this.getString(s));
    }

    public boolean equals(Object key, Object compare) {
        Object value = get(key);
        return Objects.equals(value, compare);
    }

    public boolean notEquals(Object key, Object value) {
        return !equals(key, value);
    }

    public boolean isNumber(String field) {
        return ValidHelper.isNumber(getString(field));
    }

    public boolean isList(String field) {
        return get(field) instanceof List;
    }

    public boolean isMap(String field) {
        return get(field) instanceof Map;
    }

    public boolean instr(Object key, String eqValue) {
        String value = getString(key);
        return null != value && value.indexOf(eqValue) != -1;
    }

    /**
     * 是否小于0
     *
     * @return
     */
    public boolean isLesserThanZero(String field) {
        return getLong(field) < 0;
    }

    /**
     * 是否大于0
     *
     * @return
     */
    public boolean isGreaterThanZero(String field) {
        return getLong(field) > 0;
    }

    public boolean matches(Object key, String regex) {
        String value = getString(key);
        if (null == value) {
            return false;
        }
        return ValidHelper.matches(get(key), regex);
    }


    public boolean isEmail(Object key) {
        return ValidHelper.isEmail(getString(key));
    }

    public boolean isUrl(Object key) {
        return ValidHelper.isUrl(getString(key));
    }

    public boolean isDate(Object key) {
        return DateHelper.isDate(get(key));
    }
}
