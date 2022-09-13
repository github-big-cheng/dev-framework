package com.wisely.framework.plugins.mybatis;

import java.util.Properties;

/**
 * pagehelper参数配置
 */
public class PageHelperProperties {

    private Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public String getOffsetAsPageNum() {
        return properties.getProperty("offsetAsPageNum");
    }

    public void setOffsetAsPageNum(String offsetAsPageNum) {
        properties.setProperty("offsetAsPageNum", offsetAsPageNum);
    }

    public String getRowBoundsWithCount() {
        return properties.getProperty("rowBoundsWithCount");
    }

    public void setRowBoundsWithCount(String rowBoundsWithCount) {
        properties.setProperty("rowBoundsWithCount", rowBoundsWithCount);
    }

    public String getPageSizeZero() {
        return properties.getProperty("pageSizeZero");
    }

    /**
     * 分页尺寸为0时查询所有纪录不再执行分页
     *
     * @param pageSizeZero
     */
    public void setPageSizeZero(String pageSizeZero) {
        properties.setProperty("pageSizeZero", pageSizeZero);
    }

    public String getReasonable() {
        return properties.getProperty("reasonable", "false");
    }

    /**
     * 页码<=0 查询第一页，页码>=总页数查询最后一页
     *
     * @param reasonable
     */
    public void setReasonable(String reasonable) {
        properties.setProperty("reasonable", reasonable);
    }

    public String getSupportMethodsArguments() {
        return properties.getProperty("supportMethodsArguments", "true");
    }

    /**
     * 支持通过 Mapper 接口参数来传递分页参数
     *
     * @param supportMethodsArguments
     */
    public void setSupportMethodsArguments(String supportMethodsArguments) {
        properties.setProperty("supportMethodsArguments", supportMethodsArguments);
    }

    public String getDialect() {
        return properties.getProperty("dialect");
    }

    public void setDialect(String dialect) {
        properties.setProperty("dialect", dialect);
    }

    public String getHelperDialect() {
        return properties.getProperty("helperDialect", "mysql");
    }

    public void setHelperDialect(String helperDialect) {
        properties.setProperty("helperDialect", helperDialect);
    }

    public String getAutoRuntimeDialect() {
        return properties.getProperty("autoRuntimeDialect");
    }

    public void setAutoRuntimeDialect(String autoRuntimeDialect) {
        properties.setProperty("autoRuntimeDialect", autoRuntimeDialect);
    }

    public String getAutoDialect() {
        return properties.getProperty("autoDialect");
    }

    public void setAutoDialect(String autoDialect) {
        properties.setProperty("autoDialect", autoDialect);
    }

    public String getCloseConn() {
        return properties.getProperty("closeConn");
    }

    public void setCloseConn(String closeConn) {
        properties.setProperty("closeConn", closeConn);
    }

    public String getParams() {
        return properties.getProperty("params", "count=countSql");
    }

    public void setParams(String params) {
        properties.setProperty("params", params);
    }
}
