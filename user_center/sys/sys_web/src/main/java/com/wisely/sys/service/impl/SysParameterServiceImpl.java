package com.wisely.sys.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.entity.SysParameter;
import com.wisely.sys.mapper.SysParameterMapper;
import com.wisely.sys.service.SysParameterService;
import com.wisely.sys.common.cache.ParameterCache;
import com.wisely.sys.vo.SysParameterVo;
import com.wisely.ucenter.client.eum.UcenterCacheEnum;
import com.wisely.ucenter.client.handler.UcenterDictionaryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;


@Service
@Slf4j
public class SysParameterServiceImpl implements SysParameterService, SysConstants {

    @Resource
    SysParameterMapper sysParameterMapper;

    @Resource
    ParameterCache parameterCache;

    @Override
    public PageInfo findPage(SysParameter sysParameter, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            sysParameter.setOrgIdQueryIn(UserHelper.getOrganizations());
        }
        sysParameter.setIsDeleted(0);
        return new PageInfo<>(sysParameterMapper.selectListBySelective(sysParameter));
    }

    @Override
    public List<SysParameter> list(SysParameter query) {
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            query.setOrgIdQueryIn(UserHelper.getOrganizations());
        }
        query.setIsDeleted(0);
        return sysParameterMapper.selectListBySelective(query);
    }

    @Override
    public Model load() {
        Model input = RequestHelper.getInput();
        AssertHelper.EX_VALIDATION.isNotBlank(input, "orgId", "common.parameter_required.orgId");
        AssertHelper.EX_VALIDATION.isNotBlank(input, "keies", "common.parameter_required.keies");
        byte[] param = RedisHelper.hgetBytes(PARAMETER_CACHE_KEY + input.getString("orgId"), input.getString("keies"));
        return ValidHelper.isNotEmpty(param) ?
                Model.parseObject(ProtoBufHelper.deserializer(param, SysParameterVo.class))
                : null;
    }


    @Transactional
    @Override
    public int save() {

        Model input = RequestHelper.getInput();

        // 要删除的数据
        if (input.isNotBlank("idDelIn")) {
            SysParameter delRecord = new SysParameter();
            delRecord.setIsDeleted(1);
            delRecord.setUpdateBy(UserHelper.getUserId());
            delRecord.setUpdateTime(DateHelper.formatNow());
            delRecord.setIdQueryIn(input.getString("idDelIn"));
            sysParameterMapper.updateBySelective(delRecord);
        }

        // 插入新数据
        Integer orgId = input.getInt("orgId");
        String orgName =
                UcenterDictionaryHelper.loadValue(UcenterCacheEnum.ORG_NAME.getMapper(), DataHelper.getString(orgId));
        List<SysParameter> parameters =
                JsonHelper.json2Obj(input.getString("parameters"), new TypeReference<List<SysParameter>>() {
                });
        if (ValidHelper.isNotEmpty(parameters)) {
            parameters.forEach(d -> {
                d.setOrgId(orgId);
                d.setOrgName(orgName);
                if (ValidHelper.isEmpty(d.getId())) {
                    d.setIsDeleted(0);
                    d.setCreateBy(UserHelper.getUserId());
                    d.setCreateTime(DateHelper.formatNow());
                    sysParameterMapper.insertSelective(d);
                } else {
                    d.setUpdateBy(UserHelper.getUserId());
                    d.setUpdateTime(DateHelper.formatNow());
                    sysParameterMapper.updateByPrimaryKeySelective(d);
                }
            });
        }

        this.refresh(orgId);
        return orgId;
    }


    public void refresh(Integer orgId) {
        parameterCache.refreshByOrgId(orgId);
    }
}
