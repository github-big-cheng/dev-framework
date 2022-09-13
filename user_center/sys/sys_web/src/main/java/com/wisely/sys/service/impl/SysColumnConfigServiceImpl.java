package com.wisely.sys.service.impl;

import com.wisely.sys.entity.SysColumnConfig;
import com.wisely.sys.mapper.SysColumnConfigMapper;
import com.wisely.sys.service.SysColumnConfigService;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.helper.UserHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义页面配置表(TSysColumnConfig)表服务实现类
 *
 * @author xintao.li
 * @since 2021-06-01 18:01:30
 */
@Service
public class SysColumnConfigServiceImpl implements SysColumnConfigService {

    @Resource
    SysColumnConfigMapper sysColumnConfigMapper;

    /**
     * 客户自定义页面配置查询
     * 未配置则返回系统默认配置
     *
     * @param query SysColumnConfig
     * @return List<SysColumnConfig>
     */
    @Override
    public List<SysColumnConfig> customConfigList(SysColumnConfig query) {
        query.setUserId(UserHelper.getUserId());
        query.setIsSelected(1);
        query.setOrderBy("orderNoAsc");
        List<SysColumnConfig> list = sysColumnConfigMapper.selectListBySelective(query);
        if (ValidHelper.isNotEmpty(list)) {
            return list;
        }
        // 系统默认配置
        query.setUserId(0);
        return sysColumnConfigMapper.selectListBySelective(query);
    }

    @Override
    public List<Model> fullList(SysColumnConfig query) {
        // 客户自定义
        List<SysColumnConfig> customList = this.customConfigList(query);

        // 系统默认
        query.setIsSelected(null);
        query.setUserId(0);
        List<SysColumnConfig> fullList = sysColumnConfigMapper.selectListBySelective(query);

        return fullList.stream().map(full -> {

            Model temp = Model.parseObject(full);

            temp.set("isSelected", "0")
                    .set("newName", full.getColName())
                    .set("newOrderNo", full.getOrderNo()); // 排序号

            // 获取是否选中
            customList.forEach(custom -> {
                if (StringHelper.equals(full.getCode(), custom.getCode())
                        && StringHelper.equals(full.getColKey(), custom.getColKey())) {
                    temp.set("isSelected", DataHelper.getInt(custom.getIsSelected()))
                            .set("newName", custom.getColName())
                            .set("newOrderNo", custom.getOrderNo())
                    ;
                    return;
                }
            });

            return temp;
        }).sorted(Comparator.comparing(x -> x.getInt("newOrderNo", 9999))).collect(Collectors.toList());
    }

    @Override
    public int save(List<SysColumnConfig> records) {
        AssertHelper.EX_VALIDATION.isNotEmpty(records, "common.parameter_required.records");

        SsoUser ssoUser = UserHelper.getUser();
        String now = DateHelper.formatNow();

        // 更新原数据未删除状态
        SysColumnConfig record = new SysColumnConfig();
        record.setCode(records.get(0).getCode());
        this.reset(record);

        // 插入新数据
        records.forEach(item -> {
            item.setIsSelected(1);
            item.setIsDeleted(0);
            item.setUserId(ssoUser.getId());
            item.setCreateBy(ssoUser.getId());
            item.setCreateTime(now);
            sysColumnConfigMapper.insertSelective(item);
        });

        return 0;
    }

    @Override
    public void reset(SysColumnConfig record) {
        AssertHelper.EX_VALIDATION.isNotBlank(record.getCode(), "common.parameter_required.code");
        AssertHelper.EX_VALIDATION.isNotEmpty(UserHelper.getUserId(), "common.parameter_required.userId");
        // 删除客户自定义配置
        record.setUserId(UserHelper.getUserId());
        sysColumnConfigMapper.deleteByUserIdAndCode(record);
    }
}
