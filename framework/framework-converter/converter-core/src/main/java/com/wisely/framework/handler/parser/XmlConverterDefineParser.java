package com.wisely.framework.handler.parser;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.helper.XmlHelper;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Converter实体转换器
 */
@Slf4j
public class XmlConverterDefineParser implements ConverterDefineParser, ConverterConstants {


    private LoadingCache<String, Optional<ConverterEntity>> CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(512) // 最大存储数量
                    .expireAfterAccess(5, TimeUnit.MINUTES) // 设置过期时间 5分钟
                    .build(new CacheLoader<String, Optional<ConverterEntity>>() {
                        @Override
                        public Optional<ConverterEntity> load(String key) throws Exception {

                            Document doc = XmlHelper.readFile(loadPath(key + ".xml"), false);
                            if (doc == null) {
                                return Optional.absent();
                            }

                            Element root = doc.getRootElement();

                            ConverterEntity entity = new ConverterEntity();
                            entity.setName(getAttrVal(root, ATTR_NAME));
                            entity.setDesc(getAttrVal(root, ATTR_DESC));

                            // 保存所有属性
                            Iterator<Attribute> iterator = root.attributeIterator();
                            Attribute attribute;
                            while (iterator.hasNext()) {
                                attribute = iterator.next();
                                entity.setExtendField(attribute.getName(), attribute.getValue());
                            }

                            Element send = root.element(XML_SEND);
                            entity.setSend(itemConvert(send));
                            Element rcv = root.element(XML_RECV);
                            entity.setRcv(itemConvert(rcv));

                            return Optional.of(entity);
                        }
                    });


    /**
     * 获取报文定义
     *
     * @param key
     * @return
     */
    @Override
    public ConverterEntity loadConfig(String key) {
        try {
            return CACHE.get(key).orNull();
        } catch (Exception e) {
            log.error("loadConfig error key=>{} : {}", key, e);
        }
        return null;
    }

    @Override
    public void refreshConfig(String key) {
        Optional<ConverterEntity> ifPresent = CACHE.getIfPresent(key);
        if (ValidHelper.isNotEmpty(ifPresent) && ifPresent.isPresent()) {
            CACHE.refresh(key);
        }
    }

    private List<ConverterItemEntity> itemConvert(Element element) {

        List<ConverterItemEntity> rstList = new ArrayList<>();

        if (element == null) {
            return rstList;
        }

        List<Element> elements = element.elements();
        if (ValidHelper.isEmpty(elements)) {
            return rstList;
        }

        elements.forEach(el -> {

            ConverterItemEntity item = new ConverterItemEntity();

            item.setIndex(getAttrIntVal(el, ATTR_INDEX));
            item.setName(getAttrVal(el, ATTR_NAME));
            item.setDesc(getAttrVal(el, ATTR_DESC));
            item.setNotes(getAttrVal(el, ATTR_NOTES));
            item.setRequired(getAttrBooleVal(el, VALIDA_ATTR_REQUIRED));
            item.setTarget(getAttrVal(el, CONVERTER_ATTR_TARGET));

            // 保存其他属性
            el.attributes().forEach(x -> {
                Attribute attribute = (Attribute) x;
                item.setExtendField(attribute.getName(), attribute.getValue());
            });

            // item-map / item-list / item-tree / item / item-json
            String type = getAttrVal(el, ATTR_TYPE, DATA_TYPE_STRING); // 默认String
            if (StringHelper.equals(el.getName(), XML_ITEM_TREE)) {
                // item-tree
                item.setType(DATA_TYPE_TREE);
                item.setChildren(itemConvert(el));
                if (ValidHelper.isBlank(item.getExtendField(ATTR_TREE_KEY))) {
                    item.setExtendField(ATTR_TREE_KEY, item.getName());
                }
            } else if (DATA_TYPE_MAP.equals(type)
                    || StringHelper.equals(el.getName(), XML_ITEM_MAP)) {
                // item-map
                item.setType(DATA_TYPE_MAP);
                item.setMaxlength(getAttrIntVal(el, VALIDA_ATTR_LENGTH));
                item.setChildren(itemConvert(el));
            } else if (DATA_TYPE_LIST.equals(type)
                    || StringHelper.equals(el.getName(), XML_ITEM_LIST)) {
                // item-list
                item.setType(DATA_TYPE_LIST);
                item.setMaxlength(getAttrIntVal(el, VALIDA_ATTR_LENGTH));
                item.setChildren(itemConvert(el));
            } else if (StringHelper.equals(el.getName(), XML_ITEM_JSON)) {
                // item-json
                item.setType(DATA_TYPE_JSONSTRING);
                item.setChildren(itemConvert(el));
            } else {
                // item
                item.setType(type);
                item.setDefaultVal(getAttrVal(el, CONVERTER_ATTR_DEF));
                item.setMaxlength(getAttrIntVal(el, VALIDA_ATTR_LENGTH));
                item.setScale(getAttrIntVal(el, CONVERTER_ATTR_SCALE));
                item.setMapper(getAttrVal(el, CONVERTER_ATTR_MAPPER));
                item.setLsub(getAttrIntVal(el, CONVERTER_ATTR_L_SUB));
                item.setRsub(getAttrIntVal(el, CONVERTER_ATTR_R_SUB));
                item.setLpad(getAttrVal(el, CONVERTER_ATTR_LPAD));
                item.setRpad(getAttrVal(el, CONVERTER_ATTR_RPAD));
                item.setPadLen(getAttrIntVal(el, CONVERTER_ATTR_PAD_LEN));
                item.setFromPattern(getAttrVal(el, CONVERTER_ATTR_FROM_PATTERN));
                item.setToPattern(getAttrVal(el, CONVERTER_ATTR_TO_PATTERN));
            }

            rstList.add(item);
        });

        return rstList;
    }


    private String getAttrVal(Element element, String name) {
        return getAttrVal(element, name, "");
    }

    private String getAttrVal(Element element, String name, String defVal) {
        if (element == null || element.attribute(name) == null) {
            return defVal;
        }
        return DataHelper.getString(element.attribute(name).getValue());
    }

    private Integer getAttrIntVal(Element element, String name) {
        if (element == null || element.attribute(name) == null) {
            return null;
        }
        return DataHelper.getInt(element.attribute(name).getValue());
    }

    private Boolean getAttrBooleVal(Element element, String name) {
        if (element == null || element.attribute(name) == null) {
            return false;
        }
        return DataHelper.getBoolean(element.attribute(name).getValue());
    }

}
