package com.wisely.framework.handler.sql;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
public class OrderByCache {


    private final static String filePath = "classpath*:/META-INF/metadata-orderby.json";

    private final static SqlDialect SQL_DIALECT =
            SpringHelper.hasBean("sqlDialect") ? SpringHelper.getBean(SqlDialect.class) : null;


    private final static ConcurrentHashMap<String, Model<String, String>> ENTITY_ORDER_BY_CACHE = new ConcurrentHashMap();

    static {
        try {
            Resource[] resources = ResourceHelper.getResources(filePath);
            if (ValidHelper.isNotEmpty(resources)) {
                for (int i = 0; i < resources.length; i++) {
                    // 解析文件为jsondata
                    BufferedInputStream bis = new BufferedInputStream(resources[i].getInputStream());
                    ByteArrayOutputStream buf = new ByteArrayOutputStream();
                    int result = bis.read();
                    while (result != -1) {
                        buf.write((byte) result);
                        result = bis.read();
                    }

                    //
                    Model<String, List> config = JsonHelper.json2Obj(buf.toString(), Model.class);
                    for (String key : config.keySet()) {
                        if (ENTITY_ORDER_BY_CACHE.containsKey(key)) {
                            throw SystemException.builder("orderBy.duplicate_key_found.{0}", key);
                        }

                        ENTITY_ORDER_BY_CACHE.put(key, buildSql(config.get(key)));
                    }
                }
            }

        } catch (Exception e) {
            log.warn("load order by metadata failed:{}", e);
            throw SystemException.builder(e, "orderBy.init_failed");
        }
    }

    private static Model buildSql(List<LinkedHashMap> list) {

        Model model = Model.builder();

        list.forEach(item -> {
            OrderBy orderBy = (OrderBy) Model.builder(item).convertTo(OrderBy.class);

            StringBuffer sql = new StringBuffer(" ");

            // 别名
            String alias = "";
            if (StringHelper.isNotBlank(orderBy.getAlias())) {
                alias = orderBy.getAlias() + "."; // T.
            }

            // 字段名称
            if (orderBy.isCheckNull() && SQL_DIALECT != null) {
                sql.append(SQL_DIALECT.getSqlIfNull(alias + orderBy.getColumn(), orderBy.getValueType(), orderBy.getNullValue()));
            } else {
                sql.append(alias + orderBy.getColumn());
            }

            if (!orderBy.isAsc()) {
                // 倒序
                sql.append(" ").append(SqlDialect.KEY_WORD_DESC);
            }

            model.set(orderBy.getKey().toLowerCase(), sql.toString());
        });

        return model;
    }


    /**
     * 获取对应的 orderBy 语句
     *
     * @param clazz
     * @param orderByKey
     * @return
     */
    public static String getOrderBySql(Class<? extends BaseEntity> clazz, String orderByKey) {

        Model model = ENTITY_ORDER_BY_CACHE.get(clazz.getName());
        if (ValidHelper.isEmpty(model)) {
            return null;
        }

        Model finalModel = model;
        return StringHelper.splitToStream(orderByKey, ",")
                .map(x -> finalModel.getString(x.toLowerCase(), ""))
                .collect(Collectors.joining(","));
    }
}
