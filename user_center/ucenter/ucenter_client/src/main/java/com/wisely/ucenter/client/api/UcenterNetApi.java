package com.wisely.ucenter.client.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wisely.framework.api.NetApi;
import com.wisely.framework.api.NetTools;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.framework.helper.*;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class UcenterNetApi extends NetApi implements UcenterConstants {

    public UcenterNetApi(NetTools netTools) {
        super(netTools);
    }

    @Override
    protected String getName() {
        return "UcenterNetApi";
    }

    /**
     * 标准接口 - 检查返回状态
     * code == 0
     *
     * @param url
     * @param request
     * @param <T>
     * @return
     */
    private <T> T request(String url, Model request, TypeReference<T> typeReference) {
        Model modelResponse = this.doRequest(url, request);
        AssertHelper.EX_THIRD_PARTY.isEquals(modelResponse.getInt(ResponseBuilder.KEY_CODE, -1), 0,
                modelResponse.getString(ResponseBuilder.KEY_MESSAGE, "common.response_failed.{0}.{1}"),
                url,
                modelResponse);
        return JsonHelper.cast(modelResponse.get(ResponseBuilder.KEY_DATA), typeReference);
    }


    /************缓存操作方法开始************/
    /**
     * 获取ID获取人员信息
     *
     * @param personId 人员ID
     * @return UcenterPersonVo
     */
    public UcenterPersonVo loadPersonById(Integer personId) {
        return ValidHelper.isNotEmpty(personId) ?
                EntityCacheManager.deserializer(
                        PERSON_CACHE_KEY,
                        DataHelper.getString(personId),
                        UcenterPersonVo.class) : null;
    }


    /**
     * 根据ID获取机构/部门信息
     *
     * @param orgId 机构/部门id
     * @return UcenterOrgVo
     */
    public UcenterOrgVo loadOrgById(Integer orgId) {
        return ValidHelper.isNotEmpty(orgId) ? EntityCacheManager.deserializer(
                ORG_CACHE_KEY,
                DataHelper.getString(orgId),
                UcenterOrgVo.class) : null;
    }

    /************缓存操作方法结束************/


    /**
     * 通过personId获取SsoUser
     *
     * @return
     */
    public Model loadSsoUser(Integer personId) {
        return this.request(LOAD_SSO_USER, Model.builder().set("personId", personId), new TypeReference<Model>() {
        });
    }


    /**
     * 人员-列表查询
     *
     * @param model orgType org类型 部门-'12001-10'
     *              orgIdQueryIn 机构/部门ID
     *              roleCodeQueryIn 多角色code
     *              idQueryIn 多id查询
     * @return
     */
    public List<Model> loadPersonBySelective(Model model) {
        return this.request(PERSON_LIST, model, new TypeReference<List<Model>>() {
        });
    }


    /**
     * 人员-分页查询
     *
     * @param model
     * @return
     */
    public Model loadPersonByPage(Model model) {
        return this.request(PERSON_PAGE, model, new TypeReference<Model>() {
        });
    }

    /**
     * 机构/部门列表
     *
     * @param input
     * @return
     */
    public List<Model> orgList(Model input) {
        return this.request(ORG_LIST, input, new TypeReference<List<Model>>() {
        });
    }

    /**
     * 机构/部门分页
     *
     * @param model
     * @return
     */
    public Model loadOrgByPage(Model model) {
        return this.request(ORG_PAGE, model, new TypeReference<Model>() {
        });
    }
}
